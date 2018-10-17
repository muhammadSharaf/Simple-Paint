package sample;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyDrawingEngine;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;


public class DialogController {
    private Pane scene = Controller.sceneForDialog;
    private Canvas canvas = Controller.canvasForDialog;
    private Canvas previewCanvas = Controller.previewCanvasForDialog;
    public Slider sceneWidthSlider, sceneHeightSlider;
    public TextField sceneWidthText, sceneHeightText;
    public ColorPicker sceneColor;
    private double previousWidth, previousHeight;
    private MyDrawingEngine drawingEngine = MyDrawingEngine.getInstance();

    public void initialize (){
        initListeners();

        previousWidth = scene.getPrefWidth();
        previousHeight = scene.getPrefHeight();


        sceneWidthSlider.setValue(scene.getPrefWidth());
        sceneWidthText.setText(String.valueOf(scene.getPrefWidth()));

        sceneHeightSlider.setValue(scene.getPrefHeight());
        sceneHeightText.setText(String.valueOf(scene.getPrefHeight()));

        Controller.dialog.setOnCloseRequest(event -> {
            scene.setPrefWidth(previousWidth);
            scene.setPrefHeight(previousHeight);
        });
    }

    public void initListeners (){
        sceneWidthSlider.setOnMouseDragged(event -> {
            Controller.dialog.setOpacity(.4f);
            sceneWidthText.setText(String.valueOf(sceneWidthSlider.getValue()));
            scene.setPrefWidth(sceneWidthSlider.getValue());
            canvas.setWidth(sceneWidthSlider.getValue());
            previewCanvas.setWidth(sceneWidthSlider.getValue());
        });

        sceneHeightSlider.setOnMouseDragged(event -> {
            Controller.dialog.setOpacity(.4f);
            sceneHeightText.setText(String.valueOf(sceneHeightSlider.getValue()));
            scene.setPrefHeight(sceneHeightSlider.getValue());
            canvas.setHeight(sceneHeightSlider.getValue());
            previewCanvas.setHeight(sceneHeightSlider.getValue());
        });

        sceneWidthSlider.setOnMouseReleased(event -> {
            Controller.dialog.setOpacity(1);
        });

        sceneHeightSlider.setOnMouseReleased(event -> {
            Controller.dialog.setOpacity(1);
        });

        sceneWidthText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")){
                newValue = oldValue;
            }
            sceneWidthSlider.setValue(Double.valueOf(newValue));
            scene.setPrefWidth(Double.valueOf(newValue));
            canvas.setWidth(sceneWidthSlider.getValue());
            previewCanvas.setWidth(sceneWidthSlider.getValue());
            drawingEngine.refresh(null);
        });

        sceneHeightText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")){
                newValue = oldValue;
            }
            sceneHeightSlider.setValue(Double.valueOf(newValue));
            scene.setPrefHeight(Double.valueOf(newValue));
            canvas.setHeight(sceneHeightSlider.getValue());
            previewCanvas.setHeight(sceneHeightSlider.getValue());
            drawingEngine.refresh(null);
        });

    }

    public void onSettingsApply(){
        String color = "#" + Integer.toHexString(sceneColor.getValue().hashCode());
        String style = "-fx-background-color: " + color;
        scene.setStyle(style);
        Controller.dialog.close();
    }

}
