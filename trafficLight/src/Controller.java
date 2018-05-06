import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.swing.JPanel;


public class Controller extends Thread
{

    private boolean stop = true;
    InState inState;
    OutState outState;
    final int INTERVALATGREEN=10000;
    final int INTERVALTOGREEN=2000;

    final int[] GROUP1=new int[]{1,2,4,5,6,7,12,13};
    final int[] GROUP2=new int[]{2,3,4,5,8,11,14,15};
    final int[] GROUP3=new int[]{0,6,7,9,10,12,13};

    Event64  evPressedShabat,evPressedChol,evPressed,evTimerEnd;
    Event64[] evToShabat, evToWeekday, group1ToRed,group1AtRed,group1ToGreen,group2ToRed,group2AtRed,group2ToGreen,group3ToRed,group3AtRed,group3ToGreen;

    enum OutState
    {
        ON_SHABAT, ON_WEEKDAY
    }



    enum InState
    {
        G1_ON_RED, G1_ON_GREEN, G1_HOLDING,
        G2_ON_RED, G2_ON_GREEN, G2_HOLDING,
        G3_ON_RED, G3_ON_GREEN, G3_HOLDING,

    }




    public Controller(Event64[] evToShabat, Event64[] evToWeekday, Event64 evPressedChol,Event64 evPressedShabat, Event64 evPressed, Event64[] group1ToRed, Event64[] group1AtRed, Event64[] group1ToGreen, Event64[] group2ToRed, Event64[] group2AtRed, Event64[] group2ToGreen, Event64[] group3ToRed, Event64[] group3AtRed, Event64[] group3ToGreen)
    {
        super();
        this.evToShabat = evToShabat;
        this.evToWeekday = evToWeekday;
        this.evPressedShabat = evPressedShabat;
        this.evPressedChol = evPressedChol;
        this.evPressed = evPressed;
        this.group1ToRed = group1ToRed;
        this.group1AtRed = group1AtRed;
        this.group1ToGreen = group1ToGreen;
        this.group2ToRed = group2ToRed;
        this.group2AtRed = group2AtRed;
        this.group2ToGreen = group2ToGreen;
        this.group3ToRed = group3ToRed;
        this.group3AtRed = group3AtRed;
        this.group3ToGreen = group3ToGreen;
        evTimerEnd=new Event64();
        start();
    }



    public void run(){
        outState = OutState.ON_WEEKDAY;


        while(true){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(outState.toString()+" "+dtf.format(now)); //2016/11/16 12:08:43
            switch (outState){


                case ON_SHABAT:{
                    evPressedChol.waitEvent();
                    outState= OutState.ON_WEEKDAY;
                    for(Event64 ev : evToWeekday)
                        ev.sendEvent();
                    break;
                }
                case ON_WEEKDAY:{
                    inState=InState.G1_HOLDING;


                    while(outState==OutState.ON_WEEKDAY){

                         now = LocalDateTime.now();
                        System.out.println(inState.toString()+" "+dtf.format(now)); //2016/11/16 12:08:43
                        switch (inState){

                            case G1_ON_GREEN:{

                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALATGREEN, evTimerEnd);
                                while(true){
                                    if(evTimerEnd.arrivedEvent())
                                    {

                                        inState=InState.G1_ON_RED;
                                        setG1ToRed();
                                        break;
                                    }
                                    else if(evPressedShabat.arrivedEvent()){

                                        toShabat();
                                        break;

                                    }
                                    else {
                                        yield();
                                    }

                                }
                                break;
                            }
                            case G1_ON_RED:{

                                while(true){
                                    if(evPressedShabat.arrivedEvent()){
                                        toShabat();
                                        break;

                                    }
                                    else if(G1InRed()){
                                        inState=InState.G2_HOLDING;
                                        break;

                                    }
                                    else{
                                        yield();
                                    }
                                }

                                break;

                            }

                            case G2_HOLDING:{
                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALTOGREEN, evTimerEnd);
                                while(true){
                                    if(evTimerEnd.arrivedEvent())
                                    {

                                        inState=InState.G2_ON_GREEN;
                                        setG2ToGreen();
                                        break;
                                    }
                                    else if(evPressedShabat.arrivedEvent()){

                                        toShabat();
                                        break;

                                    }
                                    else if(evPressed.arrivedEvent()){
                                       int key=(int)evPressed.waitEvent();
                                       if(FromG2(key)){
                                           inState=InState.G2_ON_GREEN;
                                           setG2ToGreen();

                                       }
                                       else if(FromG3(key)){
                                           inState=InState.G3_ON_GREEN;
                                           setG3ToGreen();
                                        }
                                        else
                                       {
                                           inState = InState.G1_ON_GREEN;
                                           setG1ToGreen();
                                       }
                                        break;
                                    }
                                    else {
                                        yield();
                                    }

                                }
                                break;
                            }

                            case G2_ON_GREEN:{
                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALATGREEN, evTimerEnd);
                                while(true){
                                    if(evTimerEnd.arrivedEvent())
                                    {

                                        inState=InState.G2_ON_RED;
                                        setG2ToRed();
                                        break;
                                    }
                                    else if(evPressedShabat.arrivedEvent()){

                                        toShabat();
                                        break;

                                    }
                                    else {
                                        yield();
                                    }

                                }
                                break;
                            }
                            case G2_ON_RED:{
                                while(true){
                                    if(evPressedShabat.arrivedEvent()){
                                        toShabat();
                                        break;

                                    }
                                    else if(G2InRed()){
                                        inState=InState.G3_HOLDING;
                                        break;

                                    }
                                    else{
                                        yield();
                                    }
                                }
                                break;

                            }

                            case G3_HOLDING:{
                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALTOGREEN, evTimerEnd);
                                while(true){
                                    if(evTimerEnd.arrivedEvent())
                                    {

                                        inState=InState.G3_ON_GREEN;
                                        setG3ToGreen();
                                        break;
                                    }
                                    else if(evPressedShabat.arrivedEvent()){

                                        toShabat();
                                        break;

                                    }
                                    else if(evPressed.arrivedEvent()){
                                        int key=(int)evPressed.waitEvent();
                                        if(FromG3(key)){
                                            inState=InState.G3_ON_GREEN;
                                            setG3ToGreen();

                                        }
                                        else if(FromG1(key)){
                                            inState=InState.G1_ON_GREEN;
                                            setG1ToGreen();
                                        }
                                        else
                                        {
                                            inState = InState.G2_ON_GREEN;
                                            setG2ToGreen();
                                        }
                                        break;
                                    }
                                    else {
                                        yield();
                                    }

                                }
                                break;

                            }
                            case G3_ON_GREEN:{
                                evTimerEnd=new Event64();
                              new MyTimer(INTERVALATGREEN, evTimerEnd);

                                while(true){
                                    if(evTimerEnd.arrivedEvent())
                                    {

                                        inState=InState.G3_ON_RED;
                                        setG3ToRed();
                                        break;
                                    }
                                    else if(evPressedShabat.arrivedEvent()){

                                        toShabat();
                                        break;

                                    }
                                    else {
                                        yield();
                                    }

                                }
                                break;

                            }

                            case G3_ON_RED:{
                                while(true){
                                    if(evPressedShabat.arrivedEvent()){

                                        resetEvents(group3AtRed);
                                        toShabat();
                                        break;

                                    }
                                    else if(G3InRed()){
                                        inState=InState.G1_HOLDING;


                                        break;

                                    }
                                    else{
                                        yield();
                                    }
                                }
                                break;

                            }

                            case G1_HOLDING:{
                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALTOGREEN, evTimerEnd);
                                while(true){
                                    if(evTimerEnd.arrivedEvent())
                                    {

                                        inState=InState.G1_ON_GREEN;
                                        setG1ToGreen();
                                        break;
                                    }
                                    else if(evPressedShabat.arrivedEvent()){

                                        toShabat();
                                        break;

                                    }
                                    else if(evPressed.arrivedEvent()){

                                        int key=(int)evPressed.waitEvent();
                                        if(FromG1(key)){
                                            inState=InState.G1_ON_GREEN;
                                            setG1ToGreen();

                                        }
                                        else if(FromG2(key)){
                                            inState=InState.G2_ON_GREEN;
                                            setG2ToGreen();
                                        }
                                        else
                                        {
                                            inState = InState.G3_ON_GREEN;
                                            setG3ToGreen();
                                        }
                                        break;
                                    }
                                    else {
                                        yield();
                                    }

                                }
                                break;

                            }
                        }
                    }
                    break;
                }

            }
        }



    }

    private void toWeekday()
    {
        evPressedShabat.waitEvent();
        outState= OutState.ON_WEEKDAY;
        for(Event64 ev : evToWeekday)
            ev.sendEvent();
    }

    private void resetEvents(Event64[] events)
    {
        for (Event64 ev : events)
            ev.resetEvent();
    }

    private boolean G3InRed()
    {
        boolean[] isRed = new boolean[group3AtRed.length];

        for (int i = 0; i < group3AtRed.length; ++i)
        {
            isRed[i] = group3AtRed[i].arrivedEvent();
            if (!isRed[i])
                return false;
        }

        resetEvents(group3AtRed);
        return true;
    }

    private void setG3ToRed()
    {
        for (Event64 ev : group3ToRed)
            ev.sendEvent();
    }

    private void setG3ToGreen()
    {
        for (Event64 ev : group3ToGreen)
            ev.sendEvent();
    }

    private boolean G2InRed()
    {

        boolean[] isRed = new boolean[group2AtRed.length];

        for (int i = 0; i < group2AtRed.length; ++i)
        {
            isRed[i] = group2AtRed[i].arrivedEvent();
            if (!isRed[i])
                return false;
        }


        resetEvents(group2AtRed);
        return true;
    }

    boolean FromG1(int key){
        for(int x:GROUP1)
            if(x==key)
                return true;

        return false;

    }

    boolean FromG2(int key){
        for(int x:GROUP2)
            if(x==key)
                return true;

        return false;

    }

    boolean FromG3(int key){
        for(int x:GROUP3)
            if(x==key)
                return true;

        return false;

    }

/*
    private boolean AllInRed()
    {


           boolean[] isRed3 = new boolean[group3AtRed.length];

           for (int i = 0; i < group3AtRed.length; ++i)
           {
               isRed3[i] = group3AtRed[i].arrivedEvent();
               if (!isRed3[i])
                   return false;
           }

           boolean[] isRed2 = new boolean[group2AtRed.length];

           for (int i = 0; i < group2AtRed.length; ++i)
           {
               isRed2[i] = group2AtRed[i].arrivedEvent();
               if (!isRed2[i])
                   return false;
           }

           boolean[] isRed1 = new boolean[group1AtRed.length];

           for (int i = 0; i < group1AtRed.length; ++i)
           {
               isRed1[i] = group1AtRed[i].arrivedEvent();
               if (!isRed1[i])
                   return false;
           }

        resetEvents(group1AtRed);
        resetEvents(group2AtRed);
        resetEvents(group3AtRed);
        return true;
    }
*/


    private void setG2ToRed()
    {
        for (Event64 ev : group2ToRed)
            ev.sendEvent();
    }

    private void setG2ToGreen()
    {
        for (Event64 ev : group2ToGreen)
            ev.sendEvent();
    }

    private boolean G1InRed()
    {
        boolean[] isRed = new boolean[group1AtRed.length];

        for (int i = 0; i < group1AtRed.length; ++i)
        {
            isRed[i] = group1AtRed[i].arrivedEvent();
            if (!isRed[i])
                return false;
        }


        resetEvents(group1AtRed);
        return true;
    }

    private void toShabat()
    {
        evPressedShabat.waitEvent();
        outState= OutState.ON_SHABAT;
        for(Event64 ev : evToShabat)
            ev.sendEvent();
    }

    private void setG1ToRed()
    {
        for (Event64 ev : group1ToRed)
            ev.sendEvent();
    }

    private void setG1ToGreen()
    {
        for (Event64 ev : group1ToGreen)
            ev.sendEvent();
    }
}



