package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;

import data.LoginMessage;
import data.Message;
import data.PlayerStatusInfo;
import data.TableActualization;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client extends Thread{
    protected ObjectOutputStream outStream = null;
    protected ObjectInputStream inStream = null;
    protected Socket socket = null;
    private Table table;
    private StartScreen startScreen;
    private Button loginButton;
    private RadioButton newUserRadioButton;

    public Client(Table table, StartScreen startScreen, Button loginButton, RadioButton newUserRadioButton){
        this.table = table;
        this.startScreen = startScreen;
        this.loginButton = loginButton;
        this.newUserRadioButton = newUserRadioButton;
        loginButton.setOnAction(actionEvent -> {
            loginButton();
        });
    }

    public void createSocket(){
        try {
            socket = new Socket("localhost", 5000);
            if(socket.isConnected()){
                System.out.println("Connected to server");
                table.disableMyMenu();
                startScreen.setServerInfo("Connected to server");
            }
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
        }catch (Exception e){
            System.out.println("Exc createSocket" + e.getCause());
            startScreen.setServerInfo("Unable to connect to server");
        }
    }
    public void run(){
        try {
            createSocket();
            startReading();
        }catch (Exception e){
            System.out.println("Exc run" + e.getCause());
        }
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
    public void send(Object msg) {
        try {
            outStream.writeObject(msg);
            outStream.flush();
        }catch (Exception e){
            System.out.println("Exc send" + e.getCause());
        }
    }
    public Object read() {
        Object obj;
        try {
            obj = inStream.readObject();
            if (obj != null) {
                return obj;
            }
        } catch (Exception e){
            System.out.println("Exc read" + e.getCause());
        }
        return null;
    }
    public void takeAction(Object obj){
        System.out.println("Obj get class: " + obj.getClass());
        System.out.println("Obj type: " + obj.getClass().getName());
        switch (obj.getClass().getName()) {
            case "data.Message":
                takeAction((Message) obj);
                break;
            case "data.TableActualization":
                takeAction((TableActualization) obj);
                break;
            case "data.LoginMessage":
                takeAction((LoginMessage) obj);
                break;
        }
    }
    public void takeAction(Message msg) {
        System.out.println(msg.getMsgType());
        if (msg.getMsgType() != null) {
            switch (msg.getMsgType()) {
                case "deal":
                    System.out.println(msg.getCards()[0] + msg.getCards()[1]);
                    table.resetCards();
                    table.displayHand(msg.getCards());
                    table.displayOppBackMenu();
                    break;
                case "croupierCards":
                    for (int i = 0; i < msg.getCards().length; i++) {
                        System.out.println(msg.getCards()[i]);
                    }
                    table.displayCroupierCards(msg.getCards());
                    break;
                case "move":
                    int minBet = msg.getActualBet();
                    table.actualizeMinBet(minBet);
                    table.enableMyMenu(msg);

                    while (true) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (table.isMoved() == true ) {
                            table.disableMyMenu();
                            Message msg2 = new Message();
                            msg2.setMsgType("move");
                            msg2.setAction(table.getMove());
                            if (table.getMove().equals("raise")) {
                                msg2.setRaiseCash(table.getCash());
                            }
                            System.out.println("Wysylam " + String.valueOf(msg2.getRaiseCash()+minBet) + " $");
                            send(msg2);
                            table.setMoved(false);
                            break;
                        }
                    }
                    break;
                case "timeOut":
                    table.disableMyMenu();
                    break;
            }
        }
    }
    public void takeAction(TableActualization tableActualization){
        ArrayList<PlayerStatusInfo> playerStatusInfos = tableActualization.getPlayerStatusInfos();
        table.actualizePoolMoney(tableActualization.getPoolMoney());
        if(tableActualization.getWinners() != null){
            table.showWinners(tableActualization.getWinners());
        }
        switch (tableActualization.getThisPlayerIndex()) {
            case 0:
                table.actualizeMe(playerStatusInfos.get(0));
                table.actualizeOppAfter(playerStatusInfos.get(1));
                table.actualizeOppBefore(playerStatusInfos.get(2));
                break;
            case 1:
                table.actualizeMe(playerStatusInfos.get(1));
                table.actualizeOppAfter(playerStatusInfos.get(2));
                table.actualizeOppBefore(playerStatusInfos.get(0));
                break;
            case 2:
                table.actualizeMe(playerStatusInfos.get(2));
                table.actualizeOppAfter(playerStatusInfos.get(0));
                table.actualizeOppBefore(playerStatusInfos.get(1));
                break;
        }
    }
    public void takeAction(LoginMessage loginMessage) {
        switch (loginMessage.getMsgType()){
            case "Login":
                if(loginMessage.isRequestRespone()){
                    System.out.println("Logged in successfully!");
                    startScreen.changeToHomeScreen((String) loginMessage.getUsername());
                }else {
                    System.out.println("Something is wrong!");
                }
                break;
            case "Register":
                if(loginMessage.isRequestRespone()){
                    System.out.println("Something is wrong!");
                }else {
                    System.out.println("Registered successfully!");
                }
                break;
        }
    }
    public void loginButton() {
        ArrayList arrayList = startScreen.login();
        LoginMessage loginMessage = new LoginMessage();

        if (newUserRadioButton.isSelected()) {
            loginMessage.setMsgType("Register");
        }
        else {
            loginMessage.setMsgType("Login");
        }

        loginMessage.setUsername((String) arrayList.get(0));
        loginMessage.setPassword((String) arrayList.get(1));

        send(loginMessage);
    }
}