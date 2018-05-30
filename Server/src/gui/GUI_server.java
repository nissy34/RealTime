package gui;

import com.sun.deploy.util.StringUtils;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI_server
{


   // List<Socket> clientSockets;
    public ChoiceBox choiceBox;
    public Button stop;
    public TextField carnumText;
    public TextField ramzorText;
    public  ObservableList<String> clientNames;
    public  List<PrintWriter> clientBufferSocketOut;
    Random rand=new Random();

    public GUI_server()
    {
        List<String> list=new ArrayList<>();
        clientNames= FXCollections.observableArrayList(list);
        clientBufferSocketOut =new ArrayList<>();
    }

    public void g1Pressed(ActionEvent actionEvent)
    {
        sendCommand("group1");
    }

    public void g2Pressed(ActionEvent actionEvent)
    {
        sendCommand("group2");
    }

    public void weekdayPressed(ActionEvent actionEvent)
    {
        sendCommand("weekday");
    }

    public void shabatPressed(ActionEvent actionEvent)
    {
        sendCommand("shabat");
    }

    private void sendCommand(String command)
    {
        PrintWriter selectedBuffer;
        selectedBuffer=clientBufferSocketOut.get(choiceBox.getSelectionModel().getSelectedIndex());
        selectedBuffer.println(command);
    }

    public void g3Pressed(ActionEvent actionEvent)
    {
        sendCommand("group3");
    }

    public void stopPressed(ActionEvent actionEvent)
    {

        sendCommand("freeze");
    }

    public void addClient(Socket clientSocket)
    {
        try
        {
            // Init streams to read/write text in this socket
            /*bufferSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            */clientBufferSocketOut.add(new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    clientSocket.getOutputStream())), true));

        } catch (IOException e)
        {
            try
            {
                clientSocket.close();
            } catch (IOException e2)
            {
            }
            System.err.println("server:Exception when opening sockets: " + e);
            return;
        }
        clientNames.add("intersection: "+clientNames.size());
        choiceBox.setItems(clientNames);



    }

    public void sendCar()
    {
        int ramzor,carnum;

        try
        {
            ramzor=Integer.parseInt(ramzorText.getText())%4;


        }
        catch (Exception e)
        {
            ramzor=rand.nextInt(3);



        }

        try
        {

             carnum= Math.abs(Integer.parseInt(carnumText.getText()));

        }
        catch (Exception e)
        {
          
            carnum=rand.nextInt(2000);


        }

        ramzorText.setText(String.valueOf(ramzor));
        carnumText.setText(String.valueOf(carnum));

        if(!choiceBox.getSelectionModel().isEmpty())
            sendCommand(ramzor+","+carnum);


    }

}
