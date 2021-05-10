package client;

import data.Message;
import data.PlayerStatusInfo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;

public class Table {
    private int width = 1000;
    private int height = 600;
    private int cardWidth = 500;
    private int cardHeight = 726;
    private Label pool = new Label("0 $");

    private Label turn = new Label("turn");
    private Label myMoney = new Label();
    private Label myBet = new Label();

    private Label myNick = new Label();
    private Label raiseValue = new Label("Raise 200 $");

    private Slider raiseSlider = new Slider();
    private Button raise = new Button("Raise");
    private Button call = new Button("Call");
    private Button check = new Button("Check");
    private Button fold = new Button("Fold");
    private boolean moved = false;
    private String move;
    private int cash;

    public int getCash() {
        return cash;
    }
    public String getMove(){
        return move;
    }
    public boolean isMoved() {
        return moved;
    }
    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    public HBox getMyMenu() {
        return myMenu;
    }

    private VBox raiseSliderMenu = new VBox(raiseValue,raiseSlider);
    private HBox buttons = new HBox(call,check,fold,raise);
    private HBox myMenu = new HBox(4, buttons,raiseSliderMenu);;
    private VBox userData = new VBox(myNick, myMoney, myBet);
    private HBox myHand = new HBox();
    private HBox crHand = new HBox();
    private Image image = new Image("poker_table.jpg");
    private BorderPane root = new BorderPane();
    private Label opp1Money = new Label();
    private Label opp1Bet = new Label();
    private Label opp2Money = new Label();
    private Label opp2Bet = new Label();
    private Label opponentNick1 = new Label();
    private Label opponentNick2 = new Label();
    private VBox opponentData1 = new VBox(opponentNick1, opp1Money, opp1Bet);
    private VBox opponentData2 = new VBox(opponentNick2, opp2Money, opp2Bet);
    private HBox oppHand1 = new HBox(opponentData1);
    private HBox oppHand2 = new HBox(opponentData2);

    private GridPane stolGrid =new GridPane();
    private BorderPane croupierPane = new BorderPane();

    public Pane drawScene() {
        root.setMinSize(width,height);

        ImageView mv = new ImageView();
        mv.setImage(image);
        mv.fitWidthProperty().bind(root.widthProperty());
        mv.fitHeightProperty().bind(root.heightProperty());
        root.getChildren().add(mv);

        stolGrid.setMinSize(width,height);
        root.setCenter(stolGrid);
        stolGrid.setAlignment(Pos.CENTER);

        stolGrid.setStyle("-fx-padding: 50 0 0 0");

        croupierPane.setCenter(pool);
        croupierPane.setStyle("-fx-padding: 30;");

        crHand.setSpacing(10);
        myHand.setSpacing(2);
        myHand.setStyle("-fx-alignment: center;");

        myHand.setAlignment(Pos.CENTER);
        raiseValue.setAlignment(Pos.CENTER);
        raiseValue.setTextFill(Color.WHITE);
        buttons.setSpacing(2);

        HBox userMenu = new HBox(userData, myHand);
        userData.setAlignment(Pos.CENTER_LEFT);
        userData.setStyle("-fx-padding: 0 0 0 60;");
        userMenu.setSpacing(40);

        pool.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 150;" +
                "-fx-font-size: 35;" +
                "-fx-font-family: 'Baskerville Old Face';");


        crHand.setStyle("-fx-border-color: black;"+
                "-fx-border-insets: 5;" +
                "-fx-border-width: 3;" +
                "-fx-border-style: dashed;" +
                "-fx-border-radius: 2;");

        call.setStyle("-fx-background-color: rgba(0,0,0,0.08),\n" +
                "            linear-gradient(#9a9a9a, #909090),\n" +
                "            linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);" +
                "-fx-background-insets: 0 0 -1 0,0,1;" +
                "-fx-background-radius: 5,5,4;" +
                "-fx-padding: 2 20 2 20;" +
                "-fx-text-fill: #242d35;" +
                "-fx-font-size: 13px;");
        check.setStyle("-fx-background-color: rgba(0,0,0,0.08),\n" +
                "            linear-gradient(#9a9a9a, #909090),\n" +
                "            linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);" +
                "-fx-background-insets: 0 0 -1 0,0,1;" +
                "-fx-background-radius: 5,5,4;" +
                "-fx-padding: 2 20 2 20;" +
                "-fx-text-fill: #242d35;" +
                "-fx-font-size: 13px;");
        raise.setStyle("-fx-background-color: rgba(0,0,0,0.08),\n" +
                "            linear-gradient(#9a9a9a, #909090),\n" +
                "            linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);" +
                "-fx-background-insets: 0 0 -1 0,0,1;" +
                "-fx-background-radius: 5,5,4;" +
                "-fx-padding: 2 20 2 20;" +
                "-fx-text-fill: #242d35;" +
                "-fx-font-size: 13px;");
        fold.setStyle("-fx-background-color: rgba(0,0,0,0.08),\n" +
                "            linear-gradient(#9a9a9a, #909090),\n" +
                "            linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);" +
                "-fx-background-insets: 0 0 -1 0,0,1;" +
                "-fx-background-radius: 5,5,4;" +
                "-fx-padding: 2 20 2 20;" +
                "-fx-text-fill: #242d35;" +
                "-fx-font-size: 13px;");

        myNick.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");

        myNick.setFont(new Font("Arial", 32));

        myMoney.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        myMoney.setTextFill(Color.BLACK);

        myBet.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        myBet.setTextFill(Color.BLACK);

        opponentNick1.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        opponentNick1.setTextFill(Color.BLACK);

        opponentNick2.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        opponentNick2.setTextFill(Color.BLACK);

        opp1Bet.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        opp1Bet.setTextFill(Color.BLACK);

        opp1Money.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        opp1Money.setTextFill(Color.BLACK);

        opp2Bet.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        opp2Bet.setTextFill(Color.BLACK);

        opp2Money.setStyle("-fx-text-fill: black;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 100;" +
                "-fx-font-size: 20;" +
                "-fx-font-family: 'Baskerville Old Face';");
        opp2Money.setTextFill(Color.BLACK);

        oppHand1.setAlignment(Pos.CENTER);
        oppHand2.setAlignment(Pos.CENTER);
        opponentData1.setAlignment(Pos.CENTER_RIGHT);
        opponentData2.setAlignment(Pos.CENTER_LEFT);

        stolGrid.add(croupierPane,1,0);
        stolGrid.add(crHand,1,1);
        stolGrid.add(userMenu,1,2);
        stolGrid.add(oppHand1,0,1);
        stolGrid.add(oppHand2,2,1);

        GridPane.setHalignment(myHand, HPos.CENTER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2= new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHalignment(HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        col3.setHalignment(HPos.CENTER);
        stolGrid.getColumnConstraints().addAll(col1,col2,col3);

        myMenu.setAlignment(Pos.CENTER);
        root.setBottom(myMenu);

        raiseSlider.setMin(200);
        raiseSlider.setMajorTickUnit(200);
        raiseSlider.setMinorTickCount(0);
        raiseSlider.setSnapToTicks(true);

        raiseSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                raiseValue.textProperty().setValue("Raise " + String.valueOf(newValue.intValue()) + " $");
                raiseValue.setTextFill(Color.web("#ffffff"));
            }
        });
        call.setOnAction(actionEvent -> {
            move = "call";
            setMoved(true);
            System.out.println("Call");
            System.out.println(isMoved());
        });
        raise.setOnAction(actionEvent -> {
            move = "raise";
            cash = (int) raiseSlider.getValue();
            setMoved(true);
            System.out.println("Raise");System.out.println(isMoved());
        });
        check.setOnAction(actionEvent -> {
            move = "check";
            setMoved(true);
            System.out.println("Check");System.out.println(isMoved());
        });
        fold.setOnAction(actionEvent -> {
            move = "fold";
            setMoved(true);
            System.out.println("Fold");System.out.println(isMoved());
        });
        generateEmptyCards();
        return root;
    }
    public void disableMyMenu(){
        myMenu.setDisable(true);
    }
    public void enableMyMenu(Message msg){
        myMenu.setDisable(false);
        if(msg.isCanRaise()){
            raiseSliderMenu.setDisable(false);
            raise.setDisable(false);
        }else {
            raiseSliderMenu.setDisable(true);
            raise.setDisable(true);
        }
        if(msg.isCanCall()){
            call.setDisable(false);
        }else {
            call.setDisable(true);
        }
        if(msg.isCanCheck()){
            check.setDisable(false);
        }else {
            check.setDisable(true);
        }
    }
    public void actualizeMe(PlayerStatusInfo psi){
        Platform.runLater(() -> {
            myMoney.setText(psi.getPlayerMoney() + " $");
            if(psi.getPlayerAction() != null) {
                if (!psi.getPlayerAction().equals("raise")) {
                    myBet.setText(psi.getPlayerAction());
                } else {
                    myBet.setText(psi.getPlayerBet() + " $");
                }
            } else {
                myBet.setText(psi.getPlayerBet() + " $");
            }
            myNick.setText(psi.getName());
        });
    }
    public void actualizeOppAfter(PlayerStatusInfo psi){
        Platform.runLater(() -> {
            opp1Money.setText(psi.getPlayerMoney() + " $");
            if(psi.getPlayerAction() != null) {
                if (!psi.getPlayerAction().equals("raise")) {
                    opp1Bet.setText(psi.getPlayerAction());
                } else {
                    opp1Bet.setText(psi.getPlayerBet() + " $");
                }
            } else {
                opp1Bet.setText(psi.getPlayerBet() + " $");
            }
            opponentNick1.setText(psi.getName());
            if(psi.getHand() != null){
                for(int i = 0; i < 2; i++) {
                    Node handChange1 = oppHand1.getChildren().get(i + 1);
                    if (handChange1 instanceof ImageView) {
                        ((ImageView) handChange1).setFitHeight(cardHeight/8+30);
                        ((ImageView) handChange1).setFitWidth(cardWidth/8+30);
                        ((ImageView) handChange1).setImage(new Image("images/" + psi.getHand()[i]  +".png"));
                    }
                }
            }
        });
    }
    public void actualizeOppBefore(PlayerStatusInfo psi){
        Platform.runLater(() -> {
            opp2Money.setText(psi.getPlayerMoney() + " $");
            if(psi.getPlayerAction() != null){
                if(!psi.getPlayerAction().equals("raise")){
                    opp2Bet.setText(psi.getPlayerAction());
                }else {
                    opp2Bet.setText(psi.getPlayerBet() + " $");
                }
            } else {
                opp2Bet.setText(psi.getPlayerBet() + " $");
            }
            opponentNick2.setText(psi.getName());
            if(psi.getHand() != null){
                for(int i = 0; i < 2; i++) {
                    Node handChange1 = oppHand2.getChildren().get(i + 1);
                    if (handChange1 instanceof ImageView) {
                        ((ImageView) handChange1).setFitHeight(cardHeight/8+30);
                        ((ImageView) handChange1).setFitWidth(cardWidth/8+30);
                        ((ImageView) handChange1).setImage(new Image("images/" + psi.getHand()[i]  +".png"));
                    }
                }
            }
        });
    }

    public void actualizePoolMoney(int i){
        Platform.runLater(() -> {
            String money = String.valueOf(i);
            pool.setText("Pool: " + money + " $");
        });
    }

    public void actualizeMinBet(int i){
        Platform.runLater(() -> {
            String money = String.valueOf(i);
            raiseSlider.setMin(200);
            raiseSlider.setMax(Integer.parseInt(myMoney.getText().replaceAll("[\\D]", "")) - i);
        });
    }
    public void showWinners(ArrayList<String> winners){
        Platform.runLater(() -> {
            String str = "";
            for(int i = 0; i < winners.size(); i++){
                str = str + winners.get(i) + " ";
            }
            pool.setText(str);
        });
    }

    public void generateEmptyCards(){
        for(int i = 0; i < 5; i++){     //tworzy puste pola na karty ktore pozniej metodami sie podmieni
            ImageView card = new ImageView();
            card.setImage(null);
            card.setFitHeight(cardHeight/5);
            card.setFitWidth(cardWidth/5);
            crHand.getChildren().add(card);
        }
        for(int i = 0; i < 2; i++){     //tworzy puste pola na karty ktore pozniej metodami sie podmieni
            ImageView card = new ImageView();
            card.setImage(null);
            card.setFitHeight(cardHeight/5);
            card.setFitWidth(cardWidth/5);
            myHand.getChildren().add(card);
        }
        for(int i = 0; i < 2; i++){     //tworzy puste pola na karty ktore pozniej metodami sie podmieni
            ImageView card1 = new ImageView();
            ImageView card2 = new ImageView();
            card1.setImage(null);
            card2.setImage(null);
            card1.setFitHeight(cardHeight/5);
            card2.setFitHeight(cardHeight/5);
            if(i == 1){
                card1.setFitWidth(0);
                card2.setFitWidth(0);
            } else {
                card1.setFitWidth(cardWidth/5+30);
                card2.setFitWidth(cardWidth/5+30);
            }
            oppHand1.getChildren().add(card1);
            oppHand2.getChildren().add(card2);
        }
    }
    public void displayHand(String[] handCards){
        Platform.runLater(() -> {
            for(int i = 0; i < handCards.length; i++){
                Node handChange = myHand.getChildren().get(i);
                if (handChange instanceof ImageView){
                    ((ImageView) handChange).setImage(new Image("images/"+handCards[i]+".png"));
                }
            }
            GridPane.setHalignment(myHand, HPos.CENTER);
        });
    }
    public void displayOppBackMenu(){
        Platform.runLater(() -> {
            for(int i = 0; i < 2; i++){
                Node handChange1 = oppHand1.getChildren().get(i+1);
                Node handChange2 = oppHand2.getChildren().get(i+1);
                if(i == 0) {
                    if (handChange1 instanceof ImageView){
                        ((ImageView) handChange1).setFitHeight(cardHeight/5);
                        ((ImageView) handChange1).setFitWidth(cardWidth/5);
                        ((ImageView) handChange1).setImage(new Image("images/back2.png"));
                    }
                    if (handChange2 instanceof ImageView){
                        ((ImageView) handChange2).setFitHeight(cardHeight/5);
                        ((ImageView) handChange2).setFitWidth(cardWidth/5);
                        ((ImageView) handChange2).setImage(new Image("images/back2.png"));
                    }
                } else {
                    if (handChange1 instanceof ImageView){
                        ((ImageView) handChange1).setImage(null);
                        ((ImageView) handChange1).setFitWidth(0);
                    }
                    if (handChange2 instanceof ImageView){
                        ((ImageView) handChange2).setImage(null);
                        ((ImageView) handChange2).setFitWidth(0);

                    }
                }
            }
            //nie wiem jak to wysrodkowac
            oppHand1.setAlignment(Pos.CENTER);
            oppHand2.setAlignment(Pos.CENTER);
            opponentData1.setAlignment(Pos.CENTER_RIGHT);
            opponentData2.setAlignment(Pos.CENTER_LEFT);
        });
    }
    int startCard;
    int startCard2;
    public void displayCroupierCards(String[] crCards){
        if(crCards.length == 3){
            startCard = 0;
            startCard2 = 0;
        }else {
            startCard = 3 + startCard2;
            startCard2++;
        }
        Platform.runLater(() -> {
            for(int i=0; i<crCards.length; i++){
                Node handChange = crHand.getChildren().get(i+startCard);
                if (handChange instanceof ImageView){
                    ((ImageView) handChange).setImage(new Image("images/"+crCards[i]+".png"));
                }
            }
        });
    }

    public void resetCards(){
        Platform.runLater(() -> {
            for(int i = 0; i < 5; i++){
                Node cardReset = crHand.getChildren().get(i);
                if (cardReset instanceof ImageView){
                    ((ImageView) cardReset).setImage(null);
                }
            }
            for(int i = 0; i < 2; i++){
                Node cardReset = myHand.getChildren().get(i);
                if (cardReset instanceof ImageView){
                    ((ImageView) cardReset).setImage(null);
                }
            }
        });
    }
}