package BackEnd;

import java.io.*;
import java.net.Socket;

/**
 * Created by USER on 29/05/2018.
 */
public class IntersectionClient extends Thread {
    static int intersectionNum =0;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    String name;
    Event64 ev_startNextIntersection,ev_startIntersection;



    public IntersectionClient(Socket clientSocket) {
        try {
            // Init streams to read/write text in this socket
            bufferSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            bufferSocketOut = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    clientSocket.getOutputStream())), true);
            name="Intersection"+intersectionNum++;



        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e2) {
            }
            System.err.println("server:Exception when opening sockets: " + e);
            return;
        }

    }

    @Override
    public void run() {


       Thread t=new Thread() {
            @Override
            public void run() {
                String line="";
                while (true) {
                    try {
                        line = bufferSocketIn.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(line != "" && ev_startNextIntersection != null){
                        ev_startNextIntersection.sendEvent(line);
                        line="";
                    }
                    yield();
                }
            }

        };
        t.start();
        while (true){

            if(ev_startIntersection !=null && ev_startIntersection.arrivedEvent()){

                String data =(ev_startIntersection.waitEvent()).toString();
                int ramzor=Integer.parseInt(data.split(",")[0]);
                int carNum=Integer.parseInt(data.split(",")[1]);
                switch (ramzor){
                    case 3:
                        bufferSocketOut.println("3,"+carNum);
                        break;
                    case 0:
                    case 1:
                        bufferSocketOut.println("0,"+carNum);
                        break;
                    case 2:
                        bufferSocketOut.println("3,"+carNum);
                }
            }

            yield();
        }



    }

    public BufferedReader getBufferSocketIn() {
        return bufferSocketIn;
    }

    public PrintWriter getBufferSocketOut() {
        return bufferSocketOut;
    }

    public void setEv_startNextIntersection(Event64 ev_startNextIntersection) {
        this.ev_startNextIntersection = ev_startNextIntersection;
    }

    public void setEv_startIntersection(Event64 ev_startIntersection) {
        this.ev_startIntersection = ev_startIntersection;
    }
}

