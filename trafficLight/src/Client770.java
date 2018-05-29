//File name Client770.java
//Eiar 770
//Levian Yehonatan
import java.io.*;
import java.net.*;

class Client770 extends Thread		/// !! change to server IP name or address !! //
{
//    String SERVERHOST = "147.161.105.71";

//    String SERVERHOST = "147.161.52.150";

    String SERVERHOST = "";

    int DEFAULT_PORT = 770;
    Socket clientSocket = null;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    BufferedReader keyBoard;
    ClientWin770 myOutput;
    String line;
    Event64 evFreeze,ev_ToShabat,ev_Toweekday,evGroup1ToGreen,evGroup2ToGreen,evGroup3ToGreen,evCar_arrived,evCar_finish;

    public Client770(Event64 evFreeze, Event64 ev_ToShabat, Event64 ev_Toweekday, Event64 evGroup1ToGreen, Event64 evGroup2ToGreen, Event64 evGroup3ToGreen,Event64 evCar_arrived,Event64 evCar_finish)
    {
        this.evFreeze = evFreeze;
        this.ev_ToShabat = ev_ToShabat;
        this.ev_Toweekday = ev_Toweekday;
        this.evGroup1ToGreen = evGroup1ToGreen;
        this.evGroup2ToGreen = evGroup2ToGreen;
        this.evGroup3ToGreen = evGroup3ToGreen;
        this.evCar_arrived = evCar_arrived;
        this.evCar_finish = evCar_finish;
        start();
    }

    public void run()
    {
        try
        {
            // request to server
            clientSocket = new Socket(SERVERHOST, DEFAULT_PORT);

            // Init streams to read/write text in this socket
            bufferSocketIn = new BufferedReader(
                    new InputStreamReader(
                    clientSocket.getInputStream()));
            bufferSocketOut = new PrintWriter(
                    new BufferedWriter(
                    new OutputStreamWriter(
                    clientSocket.getOutputStream())), true);


//  	   Init streams to read text from the keyboard
//	   keyBoard = new BufferedReader(
//	   new InputStreamReader(System.in));


            myOutput = new ClientWin770("Client  ", this);

            // notice about the connection
            myOutput.printMe("Connected to " + clientSocket.getInetAddress() +
                    ":" + clientSocket.getPort());
            boolean end=false;

            while (!end)
            {
                if(evCar_finish.arrivedEvent())
                    bufferSocketOut.println((evCar_finish.waitEvent()).toString());

                line = bufferSocketIn.readLine(); // reads a line from the server
                if (line == null)  // connection is closed ?  exit
                {
                    myOutput.printMe("Connection closed by the Server.");
                    break;
                }

                myOutput.printOther(line); // shows it on the screen

                switch (line){
                    case "group1":
                        evGroup1ToGreen.sendEvent();
                        break;
                    case "group2":
                        evGroup2ToGreen.sendEvent();
                        break;
                    case "group3":
                        evGroup3ToGreen.sendEvent();
                        break;
                    case "freeze":
                        evFreeze.sendEvent();
                        break;
                    case "shabat":
                        ev_ToShabat.sendEvent();
                        break;
                    case "weekday":
                        ev_Toweekday.sendEvent();
                        break;
                    case "end":
                        end=true;
                        break;
                    default:
                        if(isRamzorAndCar(line))
                            evCar_arrived.sendEvent(line);
                        break;
                }

            }
        } catch (IOException e)
        {
            myOutput.printMe(e.toString());
            System.err.println(e);
        } finally
        {
            try
            {
                if (clientSocket != null)
                {
                    clientSocket.close();
                }
            } catch (IOException e2)
            {
            }
        }
        myOutput.printMe("end of client ");
        myOutput.send.setText("Close");

        System.out.println("end of client ");
    }

    private boolean isRamzorAndCar(String line) {

        return true;
    }

}
