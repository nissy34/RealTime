import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */



public class ShloshaAvot extends Thread
{
    Ramzor ramzor;
    JPanel panel;
    private int count;
    private int key;
    private boolean stop = true;
    final int BLINKINGINTERVAL=500;
    final int BLINKINGINTERVALYELLOW=1000;

    final int INTERVALYELLOWTORED=1000;
    final int INTERVALREDYOLLOWTOGREEN=1000;



    Event64 evToShabat, evToWeekday, evToGreen, evToRed, evAtRed, evTimerEnd;

    enum OutState
    {
        ON_SHABAT, ON_WEEKDAY
    }



    enum ShabatState
    {
        ON, OFF
    }



    enum BlinkingGreenState
    {
        ON, OFF
    }



    enum InState
    {
        ON_RED, ON_GREEN, ON_RED_YELLOW, ON_YELLOW, ON_BLINKING_GREEN
    }


    OutState outState;
    InState inState;
    ShabatState shabatState;
    BlinkingGreenState blinkingGreenState;

    public ShloshaAvot(Ramzor ramzor, JPanel panel, int key, Event64 evToShabat, Event64 evToWeekday, Event64 evToGreen, Event64 evToRed, Event64 evAtRed)
    {
        super();
        this.ramzor = ramzor;
        this.panel = panel;
        this.evToShabat = evToShabat;
        this.evToWeekday = evToWeekday;
        this.evToGreen = evToGreen;
        this.evToRed = evToRed;
        this.evAtRed = evAtRed;
        evTimerEnd = new Event64();
        this.key=key;




        start();
    }

    public void run()
    {
        outState = OutState.ON_WEEKDAY;

        while (true) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(key-1+" "+outState.toString()+" "+dtf.format(now)); //2016/11/16 12:08:43

            switch (outState) {

                case ON_SHABAT: {

                    shabatState = ShabatState.ON;
                    stop = true;
                    setToYellow();
                    while (outState == OutState.ON_SHABAT) {
                        switch (shabatState)
                        {
                            case ON:{
                                evTimerEnd=new Event64();
                                new MyTimer(BLINKINGINTERVALYELLOW, evTimerEnd);
                                while (true)
                                {

                                    if (evToWeekday.arrivedEvent())
                                    {
                                        evToWeekday.waitEvent();
                                        setToRed();
                                        outState = OutState.ON_WEEKDAY;
                                        break;
                                    } else if (evTimerEnd.arrivedEvent())
                                    {

                                        shabatState = ShabatState.OFF;
                                        setToGray();
                                        break;
                                    } else
                                        yield();
                                }
                                break;
                        }
                            case OFF: {

                                evTimerEnd=new Event64();
                                new MyTimer(BLINKINGINTERVALYELLOW, evTimerEnd);
                                while (true)
                                {

                                    if (evToWeekday.arrivedEvent())
                                    {
                                        evToWeekday.waitEvent();
                                        setToRed();
                                        outState = OutState.ON_WEEKDAY;
                                        break;
                                    }
                                    else if (evTimerEnd.arrivedEvent())
                                    {
                                        shabatState = ShabatState.ON;
                                        setToYellow();
                                        break;
                                    } else
                                        yield();
                                }
                                break;
                            }
                        }
                    }

                    break;
                }
                case ON_WEEKDAY:
                {
                    inState = InState.ON_RED;
                   // setToRed();
                    while (outState == OutState.ON_WEEKDAY)
                    {

                         now = LocalDateTime.now();
                        System.out.println(key-1+" "+ inState.toString()+" "+dtf.format(now)); //2016/11/16 12:08:43
                        switch (inState)
                        {
                            case ON_RED:
                            {
                                stop = true;
                                while (true)
                                {
                                    if (evToShabat.arrivedEvent())
                                    {
                                        evToShabat.waitEvent();
                                        outState = OutState.ON_SHABAT;
                                        break;
                                    }
                                    else if (evToGreen.arrivedEvent())
                                    {
                                        evToGreen.waitEvent();
                                        inState = InState.ON_RED_YELLOW;
                                        setToRedYellow();
                                        break;
                                    } else yield();
                                }
                                break;
                            }
                            case ON_RED_YELLOW:
                            {
                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALREDYOLLOWTOGREEN, evTimerEnd);
                                while (true)
                                {
                                    if (evToShabat.arrivedEvent())
                                    {

                                        evToShabat.waitEvent();
                                        outState = OutState.ON_SHABAT;
                                        break;
                                    }
                                    else if (evTimerEnd.arrivedEvent())
                                    {

                                        inState = InState.ON_GREEN;
                                        setToGreen();
                                        break;
                                    } else yield();
                                }
                                break;
                            }
                            case ON_GREEN:
                            {
                                stop = false;
                                while (true)
                                {
                                    if (evToShabat.arrivedEvent())
                                    {
                                        evToShabat.waitEvent();
                                        outState = OutState.ON_SHABAT;
                                        break;
                                    }
                                    else if (evToRed.arrivedEvent())
                                    {
                                        evToRed.waitEvent();
                                        inState = InState.ON_BLINKING_GREEN;
                                        break;
                                    } else
                                        yield();
                                }
                                break;
                            }
                            case ON_BLINKING_GREEN:
                            {

                                blinkingGreenState = BlinkingGreenState.ON;
                                count = 0;
                                while (inState == InState.ON_BLINKING_GREEN && outState == OutState.ON_WEEKDAY)
                                {
                                    switch (blinkingGreenState)
                                    {
                                        case ON:
                                        {
                                            if (count >= 3)
                                            {
                                                inState = InState.ON_YELLOW;
                                                setToYellow();
                                                stop = true;
                                                break;
                                            }
                                            evTimerEnd=new Event64();
                                            new MyTimer(BLINKINGINTERVAL, evTimerEnd);
                                            while (true)
                                            {
                                                if (evToShabat.arrivedEvent())
                                                {

                                                    evToShabat.waitEvent();
                                                    outState = OutState.ON_SHABAT;
                                                    break;
                                                } else if (evTimerEnd.arrivedEvent())
                                                {

                                                    blinkingGreenState = BlinkingGreenState.OFF;
                                                    count++;
                                                    setToGray();
                                                    break;
                                                } else yield();
                                            }
                                            break;
                                        }

                                        case OFF:
                                        {
                                            evTimerEnd=new Event64();
                                            new MyTimer(BLINKINGINTERVAL, evTimerEnd);
                                            while (true)
                                            {
                                                if (evToShabat.arrivedEvent())
                                                {

                                                    evToShabat.waitEvent();
                                                    outState = OutState.ON_SHABAT;
                                                    break;
                                                } else if (evTimerEnd.arrivedEvent())
                                                {

                                                    blinkingGreenState = BlinkingGreenState.ON;
                                                    setToGreen();
                                                    break;
                                                } else yield();
                                            }
                                            break;
                                        }

                                    }
                                }
                                break;
                            }
                            case ON_YELLOW:
                            {
                                evTimerEnd=new Event64();
                                new MyTimer(INTERVALYELLOWTORED, evTimerEnd);
                                while (true)
                                {
                                    if (evToShabat.arrivedEvent())
                                    {
                                        evToShabat.waitEvent();
                                        outState = OutState.ON_SHABAT;
                                        break;
                                    } else if (evTimerEnd.arrivedEvent())
                                    {

                                        inState = InState.ON_RED;
                                        setToRed();
                                        evAtRed.sendEvent();

                                        break;
                                    } else yield();
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

    private void setToGreen()
    {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.GREEN);
    }

    private void setToRed()
    {
        setLight(1, Color.RED);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
    }


    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    public boolean isStop()
    {
        return stop;
    }

    private void setToYellow()
    {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.YELLOW);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setToRedYellow()
    {
        setLight(1, Color.RED);
        setLight(2, Color.YELLOW);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setToGray()
    {

        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);

    }
}

