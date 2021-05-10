package server;

import java.util.ArrayList;

public class CroupierStart implements Runnable{
    private ArrayList<ClientHandler> clientList;
    public CroupierStart(ArrayList<ClientHandler> clientList){
        this.clientList = clientList;
    }
    private int j = 0;

    public void run() {
        while (true) {
            j = 0;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(clientList.size() != 0) {
                for (int i = 0; i < clientList.size(); i++) {
                    if (clientList.get(i).isLoggedIn()) {
                        j++;
                    }
                }
            }

            if(j == 3){
                System.out.println("Everyone logged in, starting the game");
                break;
            }
        }
        Croupier cp = new Croupier(clientList);
        cp.game();
    }
}
