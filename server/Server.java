package server;

import java.io.IOException;
import java.util.*;
import java.net.*;

public class Server {
    protected ServerSocket serverSocket = null;
    protected Socket socket = null;
    private int port = 5000;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public void createServerSocket(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Starting server socket at port " + port);
            Thread croupierStart = new Thread(new CroupierStart(clientHandlers));
            croupierStart.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void acceptClients(){
        try {
            while(true) {
                System.out.println("Listening at port " + port);

                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("Client accepted");

                if(clientHandlers.size() == 3){
                    System.out.println("3 people connected");
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.createServerSocket();
        server.acceptClients();
    }
}
