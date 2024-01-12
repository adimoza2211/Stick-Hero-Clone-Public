package com.example.stickhero3;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.security.Key;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Random;

public class Scene1Controller  {


    private Stick Stick;
    private Avatar Avatar;
    private Game game = new Game(0,0);
    private int totalScore=0;
    private int totalHighScore=0;
    @FXML private Label score;
    @FXML private Label highscore;
    @FXML private Label rewards;
    @FXML
    private Button start;
    private Stage stage;
    private Scene scene;
    @FXML
    private Rectangle stick;
    @FXML
    AnchorPane anchorPane2;
    @FXML
    private ImageView avatar;
    @FXML private Rectangle pillar1;
    @FXML private Rectangle pillar2;

    private ActionEvent event;

    public ImageView getAvatar() {
        return avatar;
    }

    public Rectangle getPillar1() {
        return pillar1;
    }

    public Rectangle getPillar2() {
        return pillar2;
    }

    private void setup(){
//        Stick = com.example.stickhero3.Stick.getInstance(stick);
//        Avatar = com.example.stickhero3.Avatar.getInstance(avatar);
//        Stick.setup();
//        Avatar.setup();
    }

    public void startGame(ActionEvent event) throws IOException {

        FXMLLoader file = new FXMLLoader(getClass().getResource("scene2.fxml"));
        Parent root = file.load();
        Scene1Controller scene1Controller = file.getController();
        scene1Controller.setup();
        this.event=event;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void setScore(int sc){
        totalScore=sc;
        score.setText((String.valueOf(sc)));
        game.setPlayerScore(sc);
    }

    public void setRewards(int rew){
        rewards.setText((String.valueOf(rew)));
        game.setPlayerRewards(rew);
    }



    public int flag=0;
    private boolean isResizing = false;
    private Timeline timeline;


    @FXML
    public void stopStick() {
        flag=1;
        timeline.stop();
        rotateStick();
        isResizing = false;
        flag=0;
    }

    @FXML
    public void extendStick() {
        if(flag==0) {
            isResizing = true;
            // Set the duration for the keyframe (e.g., 50 milliseconds)
            Duration keyFrameDuration = Duration.millis(50);
            timeline = new Timeline(
                    new KeyFrame(keyFrameDuration, event -> extend())
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    private void rotateStick(){
        Rotate rotate=new Rotate();
        rotate.setPivotX(stick.getX());
        rotate.setPivotY(stick.getY()+stick.getHeight());
        stick.getTransforms().add(rotate);
        rotate.setAngle(90);
        moveAvatar();
    }


    public void extend() {
        if(flag==0) {
//            if (isResizing) {
                double newY = stick.getY() - 5;
                double newHeight = stick.getHeight() + 5;
                stick.setY(newY);
//                Stick.setHeight(newHeight);
                stick.setHeight(newHeight);
//            }
        }

    }
    void displayGameOverScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene5.fxml"));
        Parent root = loader.load();
        Scene5Controller scene5Controller = loader.getController();
        scene5Controller.setTotalScore(totalScore);
        if (totalScore > HelloApplication.highscore){
            HelloApplication.highscore=totalScore;
        }
        scene5Controller.setHighScore(HelloApplication.highscore);
        stage = new Stage();
        stage.setTitle("GameOver");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private  void serializeGame(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\Users\\aarya\\OneDrive\\Documents\\stick-hero\\Stick-Hero\\stickhero3\\src\\main\\resources\\com\\example\\stickhero3\\gameState.ser"))) {
            oos.writeObject(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserializeSavedGame(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Users\\aarya\\OneDrive\\Documents\\stick-hero\\Stick-Hero\\stickhero3\\src\\main\\resources\\com\\example\\stickhero3\\savedGameState.ser"))) {
            game = (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void savedGame(ActionEvent event) throws IOException{
        deserializeSavedGame();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene2.fxml"));
        Parent root = loader.load();

        Scene1Controller scene1Controller = loader.getController();
        scene1Controller.setup();

        scene1Controller.setScore(game.getPlayerScore());
        scene1Controller.setRewards(game.getPlayerRewards());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void help(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("scene4.fxml"));
        this.event = event;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void backPage(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        this.event = event;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pauseScreen(ActionEvent event) throws IOException{
        serializeGame();
        FXMLLoader parent =  new FXMLLoader(getClass().getResource("scene3.fxml"));
        Parent root= parent.load();
        Scene3Controller scene3Controller = parent.getController();
        scene3Controller.setTotalScore(totalScore);

        this.event = event;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void makeAvatarCrash(){
        boolean ifFalling = false;

        double avatarPosition = pillar1.getLayoutX() +pillar1.getWidth()+ stick.getHeight()-10;


        if (avatarPosition< pillar2.getLayoutX() || avatarPosition > (pillar2.getLayoutX()+pillar2.getWidth())){
            ifFalling=true;
        }
        Timeline timelineCrashAvatar =new Timeline(
                new KeyFrame(Duration.ZERO,new KeyValue(avatar.layoutYProperty(), avatar.getLayoutY())),
                new KeyFrame(Duration.millis(500),new KeyValue(avatar.layoutYProperty(),600))
        );
        if(ifFalling){
            timelineCrashAvatar.play();
            timelineCrashAvatar.setOnFinished(
                    actionEvent -> {
                        try {
                            displayGameOverScreen();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }else{
            totalScore++;
            setScore(totalScore);
            shiftPillars();
        }


    }

    public void invertAvatar(KeyEvent event){
        if (event.getCode() == KeyCode.I){
            avatar.setScaleY(-1);
        }
    }

    private void moveAvatar()
    {

        double toMove = pillar1.getLayoutX() +pillar1.getWidth()+ stick.getHeight() - avatar.getFitWidth();

        Timeline timelineMoveAvatar=new Timeline(
                new KeyFrame(Duration.ZERO,new KeyValue(avatar.layoutXProperty(),avatar.getLayoutX())),
                new KeyFrame(Duration.millis(500),new KeyValue(avatar.layoutXProperty(),toMove ))
        );
        timelineMoveAvatar.play();
        timelineMoveAvatar.setOnFinished(actionEvent -> {
            makeAvatarCrash();
        });
    }
    private int RectangleNumberidx = 0;
    private double stickLength = 0;
//    public void setup()
//    {
//        Rectangle r1 = new Rectangle();
//        r1.setHeight(208.0);
//        r1.setLayoutY(327);
//        r1.setWidth(80);
//        r1.setLayoutX(360);
//        this.rectangleArrayList.add(new Pillar(r1));
//        r1 = new Rectangle();
//        r1.setHeight(208.0);
//        r1.setLayoutY(327);
//        r1.setWidth(40);
//        r1.setLayoutX(300);
//        this.rectangleArrayList.add(new Pillar(r1));
//        r1 = new Rectangle();
//        r1.setHeight(208.0);
//        r1.setLayoutY(327);
//        r1.setWidth(130);
//        r1.setLayoutX(150);
//        this.rectangleArrayList.add(new Pillar(r1));
//
//    }
    void shiftPillars(){
//        this.rectangleArrayList = new ArrayList<Pillar>();
//        setup();
        Timeline timelineMovePillar=new Timeline(
                new KeyFrame(Duration.ZERO,new KeyValue(pillar2.layoutXProperty(),pillar2.getLayoutX())),
                new KeyFrame(Duration.millis(200),new KeyValue(pillar2.layoutXProperty(),pillar1.getLayoutX()))
        );

        timelineMovePillar.setOnFinished(ActionEvent ->{
            stickLength  += stick.getHeight();
            Timeline timelineMoveAvatar=new Timeline(
                    new KeyFrame(Duration.ZERO,new KeyValue(avatar.layoutXProperty(),avatar.getLayoutX())),
                    new KeyFrame(Duration.millis(200),new KeyValue(avatar.layoutXProperty(),pillar2.getLayoutX() + pillar2.getWidth() - avatar.getFitWidth()))
            );

            timelineMoveAvatar.setOnFinished(ActionEvent1 -> {
                Timeline timlineMoveStick = new Timeline(
//                        new KeyFrame(Duration.ZERO,new KeyValue(stick.layoutXProperty(),stick.getLayoutX())),
                        new KeyFrame(Duration.millis(1),event1->{

                            stick.setY(35);
                        })
                );



                timlineMoveStick.setOnFinished(ActionEvent2 -> {
                    stick.setHeight(30);
                    stick.setWidth(7);

                    Rotate rotate=new Rotate();
                    rotate.setPivotX(stick.getX());
                    rotate.setPivotY(stick.getY()+stick.getHeight());
                    stick.getTransforms().add(rotate);
                    rotate.setAngle(-90);
                    RotateTransition rotation = new RotateTransition(Duration.millis(100),stick);
                    rotation.play();
//                    stick.setHeight(30);
//                    stick.setWidth(7);
                    isResizing = false;
                    flag = 0;



                    Random random1 = new Random();
                    int min = 50;
                    int max = 200;
                    int randomWidth = random1.nextInt(max-min+1)+min;

                    Random random2 = new Random();
                    int min1 = 100;
                    int max1 = 250;
                    int randomDistance = random2.nextInt(max1-min1+1)+min1;

                    Rectangle newPillar = new Rectangle();
                    newPillar.setHeight(pillar1.getHeight());
                    newPillar.setLayoutY(pillar1.getLayoutY());
                    newPillar.setWidth(randomWidth);
                    newPillar.setLayoutX(pillar1.getLayoutX() + pillar1.getWidth()+ randomDistance);
                    anchorPane2.getChildren().add(newPillar);

                    pillar1=pillar2;
                    pillar2=newPillar;


                });
                timlineMoveStick.play();
            });
            timelineMoveAvatar.play();
        });
        timelineMovePillar.play();

    }






 }


