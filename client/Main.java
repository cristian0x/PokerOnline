package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {


    private static Table table = new Table();
    private static StartScreen startScreen = new StartScreen();
    private static Client client = new Client(table, startScreen, startScreen.getLoginButton(), startScreen.getNewUserRadioButton());
    Scene tableScene = new Scene(table.drawScene());
    Button changingSceneButton = new Button("PLAY!");

    public Main(){}

    @Override
    public void start(Stage primaryStage) {
        Scene startScene = new Scene(startScreen.drawScene(changingSceneButton, primaryStage, tableScene));

        startScene.getRoot().requestFocus();
        primaryStage.setTitle("Poker online");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        client.start();
        launch(args);
    }
}
