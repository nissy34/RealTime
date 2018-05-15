package BackEnd;//file name: BackEnd.Server770.java
//Iyar 5770
//Levian Yehonatan
import gui.GUI_server;

import java.io.*;
import java.net.*;

public class Server770 extends Thread 	   //the parallel server
{

    int DEFAULT_PORT = 770;
    ServerSocket listenSocket;
    Socket clientSockets;
    GUI_server server;

    public Server770(GUI_server gui_server)   // constructor of a TCP server
    {
        server=gui_server;
        try
        {
            listenSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e)    //error
        {
            System.out.println("Problem creating the server-socket");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Server starts on port " + DEFAULT_PORT);
        start();
    }

    public void run()
    {
        try
        {
            while (true)
            {
                clientSockets = listenSocket.accept();
                server.addClient(clientSockets);
            }

        } catch (IOException e)
        {
            System.out.println("Problem listening server-socket");
            System.exit(1);
        }

        System.out.println("end of server");
    }
}

