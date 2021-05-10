package server;

import java.awt.desktop.SystemEventListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import data.LoginMessage;
import data.Message;
import data.MyLog;

public class ClientHandler extends Thread{
    protected ObjectOutputStream outStream = null;
    protected ObjectInputStream inStream = null;
    protected Socket socket = null;
    private String name;
    private boolean loggedIn = false;
    private Message newMessage;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Started new ClientHandler");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            startReading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void send(Object obj) {
        try {
            outStream.writeObject(obj);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Object read() {
        Object obj;
        try {
            obj = inStream.readObject();
            if (obj != null) {
                return obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void startReading() {
        Object obj;
        while (!socket.isClosed()) {
            obj = read();
            if(obj != null){
                takeAction(obj);
            }
        }
    }
    public void takeAction(Object obj){
        switch (obj.getClass().getName()) {
            case "data.Message":
                takeAction((Message) obj);
                break;
            case "data.LoginMessage":
                takeAction((LoginMessage) obj);
                break;
        }
    }
    public void takeAction(LoginMessage loginMessage) {
        switch (loginMessage.getMsgType()){
            case "Login":     //sprawdz czy username i haslo sa w bazie i czy w uzyciu, jesli wszystko git to wyslij ze zalogowano
                login(loginMessage);
                break;
            case "Register":  //sprawdz czy username zajety, jesli nie to dodaj do bazy
                register(loginMessage);
                break;
        }
    }
    public void takeAction(Message message) {
        newMessage = message;
        System.out.println(newMessage.getMsgType() + newMessage.getAction());
    }
    public Message getNewMessage(){
        Message msg = new Message();
        for (int i = 0; i < 100; i++){
            try {
                Thread.sleep(500);
                if(newMessage != null){
                    msg = newMessage;
                    newMessage = null;
                    return msg;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message msg2 = new Message();
        msg2.setMsgType("timeOut");
        send(msg2);
        msg.setAction("fold");
        return msg;
    }
    public void login(LoginMessage loginMessage) {
        String username = loginMessage.getUsername();
        String password = loginMessage.getPassword();

        ArrayList arrayList = MyLog.readFromFile("registeredUsers.txt");

        String loginData = null;
        loginData = username + ":" + generateHash(password, "SHA-256");

        if (arrayList.contains(loginData)) {
            loginMessage.setMsgType("Login");
            loginMessage.setRequestRespone(true);
            loggedIn = true;
            name = username;
            send(loginMessage);
            System.out.println("Logged in");
        }
        else {
            loginMessage.setMsgType("Login");
            loginMessage.setRequestRespone(false);
            loggedIn = false;
            send(loginMessage);
        }

    }
    public void register(LoginMessage loginMessage) {
        String username = loginMessage.getUsername();
        String password = loginMessage.getPassword();

        ArrayList arrayList = MyLog.readFromFile("registeredUsers.txt");

        String loginData = null;
        loginData = username + ":" + generateHash(password, "SHA-256");

        if(registerCheck(loginMessage) && !arrayList.contains(loginData)) {
            MyLog.writeToFile(username, generateHash(password, "SHA-256" ), "registeredUsers.txt");
            MyLog.writeToFile(username, "10000" , "usersData.txt");
        }
        else {
            System.out.println("Invalid Data");
        }
    }

    public Boolean registerCheck(LoginMessage loginMessage) {
        boolean isDataValid = false;

        String username = loginMessage.getUsername();
        String password = loginMessage.getPassword();

        int usernameLength = username.length();

        if (usernameLength >= 3 && usernameLength <= 8 && username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            isDataValid = true;
        }

        return isDataValid;
    }

    private String generateHash(String password, String algorithm) {
        String hashValue = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            byte[] digest = messageDigest.digest(password.getBytes());
            hashValue = bytesToStringHex(digest);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error:hash: " + e.getStackTrace() + " " + e.getCause());
        }
        return hashValue;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private String bytesToStringHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    public String getPlayerName(){
        return name;
    }
}