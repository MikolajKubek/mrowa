package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private TextField textFieldPattern;

    private CelluralAutomata automata;
    private int fps = 10;
    private Direction direction = Direction.UP;
    private Button temporaryButton;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event ->handleEventNext(null)));
    private Tooltip mousePosition = new Tooltip();
    private double prefferedWidth;
    private double prefferedHeight;

    @FXML
    private void initialize(){
        if (automata == null) {
            automata = new LangtonsAnt(400, 260);
        }
        automata.editCanvas(displayCanvas);

        slider.setMax(500);
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
                mousePosition.setText("x: " + (int)(event.getX()/displayCanvas.getWidth()*automata.getWidth()) + " y: " + (int)(event.getY()/displayCanvas.getHeight()*automata.getHeight()));
                mousePosition.show(displayCanvas, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });

        displayCanvas.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePosition.hide();
            }
        });

        prefferedWidth = displayCanvas.getWidth();
        prefferedHeight = displayCanvas.getHeight();
    }
    @FXML
    private void handleEventStart(ActionEvent event) {
        if (timeline.getStatus() == Animation.Status.STOPPED) {
            fps = (int)slider.getValue();
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            startButton.setStyle("-fx-background-color: red");
            startButton.setText("Stop");
        }
        else {
            timeline.stop();
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
        automata.editCanvas(displayCanvas);
        if(timeline.getStatus() != Animation.Status.STOPPED){
            handleEventStart(null);
        }
    }

    @FXML
    private void resizeCanvas(ActionEvent event)/*do zmiany*/{
        int width = Integer.parseInt(textFieldWidth.getText());
        int height = Integer.parseInt(textFieldHeight.getText());
        textFieldWidth.setStyle("-fx-border-color: white");
        textFieldHeight.setStyle("-fx-border-color: white");
        double newHeight = prefferedHeight;
        if(width < prefferedWidth && width > 0 && height < prefferedHeight && height > 0){
            automata = new LangtonsAnt(width, height);
            while(width/height*newHeight > prefferedWidth){
                while(newHeight%height != 0)
                    newHeight--;
            }

            displayCanvas.setHeight(newHeight);
            displayCanvas.setWidth(newHeight*width/height);
            automata.editCanvas(displayCanvas);
        }
        else{
            textFieldWidth.setStyle("-fx-border-color: red");
            textFieldHeight.setStyle("-fx-border-color: red");

        }
    }

}
