package org.lara.rnn;

import java.net.*; 
import java.io.*; 
import java.util.concurrent.TimeUnit;

import org.lara.rnn.Message.Command;

public class Server {

    final private int PORT = 9987;
    private Socket socket;
    private OutputStream os;
    private InputStream is;

    public Command makeQuestion(String q) {
        return Command.newBuilder()
                      .setType(Command.CommandType.QUESTION)
                      .setName("Question")
                      .setData(q)
                      .build();
    }

    public Command makeShutdown() {
        return Command.newBuilder()
                      .setType(Command.CommandType.SHUTDOWN)
                      .setName("Shuting down")
                      .setData("Shutdown server.")
                      .build();
    }

    public Command makeSwitchPerson(int personn) {
        return Command.newBuilder()
                      .setType(Command.CommandType.SWITCH_PERSON)
                      .setName("Switching to personn " + personn )
                      .setData(Integer.toString(personn))
                      .build();
    }

    public void send_without_answer(Command cmd) {
        try {
            openSock();
            cmd.writeTo(os);
            closeSock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Command send(Command cmd) {
        try {
            openSock();
            cmd.writeTo(os);
            Command ret = Command.parseFrom(is);
            closeSock();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            //return Command.parseFrom(is);
            return Command.newBuilder().build();
        }
    }

    public String sendQuestion(String q) {
        return send(makeQuestion(q)).getData();
    }

    public void shutdownServer() {
        send_without_answer(makeShutdown());
        closeSock();
    }

    public void switchPersonn(int personn) {
        send_without_answer(makeSwitchPerson(personn));
    }

    public void openSock() {
        try {
            socket = new Socket("localhost", PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void closeSock() {
        try {
            os.close();
            is.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
