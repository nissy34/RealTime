//File name Client770.java
//Eiar 770
//Levian Yehonatan
import java.io.*;
import java.net.*;

class Client770		/// !! change to server IP name or address !! //
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

    public void doit()
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
            while (true)
            {
                line = bufferSocketIn.readLine(); // reads a line from the server
                if (line == null)  // connection is closed ?  exit
                {
                    myOutput.printMe("Connection closed by the Server.");
                    break;
                }
                myOutput.printOther(line); // shows it on the screen
                if (line.equals("end"))
                {
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

    public static void main(String[] args)
    {
        Client770 client = new Client770();
        client.doit();
    }
}
