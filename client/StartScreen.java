package client;

import data.MyLog;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartScreen {

    private int width = 1000;
    private int height = 600;
    private BorderPane root = new BorderPane();
    private Image image = new Image("background4.jpg");

    TextField usernameForm = new TextField();
    PasswordField passwordForm = new PasswordField();

    Button loginButton = new Button("Login/Register");
    RadioButton newUserRadioButton = new RadioButton("new user");
    VBox form = new VBox();
    VBox homeScreen = new VBox();
    Button playButton;
    String serverInfo;

    public Pane drawScene(Button changingSceneButton, Stage primaryStage, Scene tableScene) {
        root.setMinSize(width, height);
        changingSceneButton.setOnAction(e -> primaryStage.setScene(tableScene));
        root.setPadding(new Insets(10, 230, 10, 200));

        changingSceneButton.setStyle("-fx-font-size: 50px;");

        playButton = changingSceneButton;

        ImageView mv = new ImageView();
        mv.setImage(image);
        mv.fitWidthProperty().bind(root.widthProperty());
        mv.fitHeightProperty().bind(root.heightProperty());
        root.getChildren().add(mv);

        root.setCenter(loginForm());

        return root;
    }

    public VBox loginForm() {

        form.setPadding(new Insets(140));
        form.setSpacing(12);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Welcome to Poker!");
        welcomeTxt.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 40));


        root.getStylesheets().add(getClass().getResource("radio.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("login.css").toExternalForm());

        form.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BLACK, 12));

        usernameForm.setPromptText("username");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BLACK, 12));

        passwordForm.setPromptText("password");

        HBox signButtons = new HBox();
        signButtons.getChildren().add(loginButton);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(column1, column2);

        newUserRadioButton.setStyle("-fx-padding: 0 240 0 0;" +
                "-fx-font-family: 'Baskerville Old Face';" +
                "-fx-font-size: 18;");

        Label serverStatus = new Label();
        serverStatus = serverStatus();

        form.getChildren().addAll(welcomeTxt, usernameForm, passwordForm, newUserRadioButton, loginButton, serverStatus);

        usernameLabel.setTextFill(Color.WHITE);
        passwordLabel.setTextFill(Color.WHITE);

        form.setStyle("-fx-border-color: black;"+
                "-fx-border-insets: 5;" +
                "-fx-border-width: 3;" +
                "-fx-border-style: dashed;" +
                "-fx-border-radius: 80;");

        return form;
    }

    public VBox homeScreen(String username) {
        homeScreen.setStyle("-fx-border-color: black;"+
                "-fx-border-insets: 5;" +
                "-fx-border-width: 3;" +
                "-fx-border-style: dashed;" +
                "-fx-border-radius: 80;");

        Text homeScreenTxt = new Text("Welcome " + username + "!");
        homeScreenTxt.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 40));

        homeScreen.setSpacing(10);
        homeScreen.getChildren().add(homeScreenTxt);
        homeScreen.getChildren().add(playButton);
        homeScreen.setAlignment(Pos.CENTER);

        return homeScreen;
    }

    public Label serverStatus() {
        Label serverStatus = new Label(serverInfo);
        serverStatus.setFont(Font.font("Arial", FontWeight.BLACK, 20));

        return serverStatus;
    }

    public ArrayList login() {
        ArrayList<String> arrayList = new ArrayList<>();
        String username = usernameForm.getText();
        String password = passwordForm.getText();
        arrayList.add(username);
        arrayList.add(password);

        return arrayList;
    }

    public Text loginInfo(String loginInfo) {
        Text loginInformation = new Text();

        loginInformation.setText(actualizeLoginInfo(loginInfo));

        return loginInformation;
    }

    public String actualizeLoginInfo(String loginInfo) {
        String text = loginInfo;

        return text;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public RadioButton getNewUserRadioButton() {
        return newUserRadioButton;
    }

    public BorderPane changeToHomeScreen(String username) {
        Platform.runLater(() -> {
            root.setCenter(homeScreen(username));
        });
        return root;
    }

    public void setServerInfo(String info) {
        serverInfo = info;
    }
}
