package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;



public class Controller {

    @FXML
    private Slider slider;
    @FXML
    private Button startButton;
    @FXML
    private Canvas displayCanvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField textFieldWidth;
    @FXML
    private TextField textFieldHeight;
    @FXML
    private AnchorPane tuJestObrazek;
    @FXML
    private TextField textFieldPattern;

    private CelluralAutomata automata;
    private int fps = 10, prefferedWidth = 800, prefferedHeight = 520;
    private Direction direction = Direction.UP;
    private Button temporaryButton;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event ->handleEventNext(null)));/*Animation timer*/
    private AnimationTimer animationTimer =new AnimationTimer() {
        @Override
        public void handle(long now) {
            handleEventNext(null);
        }
    };
    private Tooltip mousePositionToolTip = new Tooltip();


    @FXML
    private void initialize(){
        if (automata == null) {
            automata = new LangtonsAnt(800, 520);
        }
        displayCanvas.setWidth(automata.getWidth()*2);
        displayCanvas.setHeight(automata.getHeight()*2);
        tuJestObrazek.setPrefWidth(automata.getWidth()*2);
        tuJestObrazek.setPrefHeight(automata.getHeight()*2);
        automata.editCanvas(displayCanvas);

        slider.setMax(1000);
        slider.setMin(1);
        slider.setValue(10);

        displayCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                automata.addElement((int)(event.getX()/displayCanvas.getWidth()*automata.getWidth()), (int)(event.getY()/displayCanvas.getHeight()*automata.getHeight()), colorPicker.getValue(), direction);
                automata.editCanvas(displayCanvas);
            }
        });

        displayCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePositionToolTip.setText("x: " + (int)(event.getX()/displayCanvas.getWidth()*automata.getWidth()) + " y: " + (int)(event.getY()/displayCanvas.getHeight()*automata.getHeight()));
                mousePositionToolTip.show(displayCanvas, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });

        displayCanvas.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePositionToolTip.hide();
            }
        });
    }
    @FXML
    private void handleEventStart(ActionEvent event) {
        if (startButton.getText().equals("Start")) {
            fps = (int)slider.getValue();
            animationTimer.start();
            startButton.setStyle("-fx-background-color: red");
            startButton.setText("Stop");
        }
        else {
            animationTimer.stop();
            startButton.setStyle("-fx-background-color: green");
            startButton.setTextFill(Color.WHITE);
            startButton.setText("Start");
        }
    }

    @FXML
    private void handleEventNext(ActionEvent event){
            if (automata != null) {
                for (int i = 0; i < fps; i++) {
                    automata.process();
                }
                automata.editCanvas(displayCanvas);
            }

            fps = (int)slider.getValue();
    }

    @FXML
    private void directionButtonEvent(ActionEvent event){
        switch (event.getSource().toString())
        {
            case "Button[id=buttonUp, styleClass=button]'^'":
                direction =Direction.UP;
                break;
            case "Button[id=buttonRight, styleClass=button]'>'":
                direction =Direction.RIGHT;
                break;
            case "Button[id=buttonDown, styleClass=button]'V'":
                direction =Direction.DOWN;
                break;
            case "Button[id=buttonLeft, styleClass=button]'<'":
                direction =Direction.LEFT;
                break;
        }
        if(temporaryButton != null)
            temporaryButton.setDisable(false);
        temporaryButton = (Button)event.getSource();
        temporaryButton.setDisable(true);
    }

    @FXML
    private void restart(){
        automata = new LangtonsAnt((int)prefferedWidth/2, (int)prefferedHeight/2);
        displayCanvas.setWidth(prefferedWidth);
        displayCanvas.setHeight(prefferedHeight);
        tuJestObrazek.setPrefWidth(prefferedWidth);
        tuJestObrazek.setPrefHeight(prefferedHeight);
        automata.editCanvas(displayCanvas);
        if(timeline.getStatus() != Animation.Status.STOPPED){
            handleEventStart(null);
        }
    }

    @FXML
    private void resizeCanvas(ActionEvent event)/*do zmiany*/{

        int width;
        int height;
        try {
            width = Integer.parseInt(textFieldWidth.getText());
            height = Integer.parseInt(textFieldHeight.getText());
        }
        catch(NumberFormatException exception){width = (int)prefferedWidth; height = (int)prefferedHeight;}

        textFieldWidth.setStyle("-fx-border-color: white");
        textFieldHeight.setStyle("-fx-border-color: white");

        if(width > prefferedWidth || height > prefferedHeight){
            automata = new LangtonsAnt(width, height);
            displayCanvas.setWidth(width * 2);
            displayCanvas.setHeight(height * 2);
            tuJestObrazek.setPrefWidth(width * 2);
            tuJestObrazek.setPrefHeight(height * 2);
            automata.editCanvas(displayCanvas);
        }
        else{
            automata = new LangtonsAnt(width, height);
            int scale = 1;
            while(width*scale < prefferedWidth)
                scale++;
            displayCanvas.setWidth(scale*width);
            displayCanvas.setHeight(scale*height);
            tuJestObrazek.setPrefWidth(scale*width);
            tuJestObrazek.setPrefHeight(scale*height);
            automata.editCanvas(displayCanvas);
        }
    }
}
