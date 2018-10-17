package sample;

import eg.edu.alexu.csd.oop.draw.cs37_54.MyDrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Map;

public class Controller {
    private MyDrawingEngine drawingEngine = MyDrawingEngine.getInstance();
    public ToolBar shapeBar, toolBar;
    private String drawShape;
    public Pane scene;
    protected static Pane sceneForDialog;
    public Canvas canvas, previewCanvas;
    protected static Canvas canvasForDialog, previewCanvasForDialog;
    private GraphicsContext gcPreviewCanvas;
    public static GraphicsContext gcCanvas;
    public ScrollPane workingArea;
    private Point mouseStartPoint = new Point();
    private Point mouseEndPoint = new Point();
    public Label strokeLabel, fillLabel;
    public ColorPicker fillColor, strokeColor;
    public Line strokeLine, fillLine;
    private LinkedList<String> supportedShapes = new LinkedList<>();
    public TextField thicknessField, nSidesField;
    public HBox nSideGroup;
    public Button undoBtn, redoBtn, gridBtn;
    private boolean erased = false;
    private Stage primaryStage;
    public VBox layerVBox, propVBox;
    private LinkedList<Shape> selectedShapes = new LinkedList<>();
    private LinkedList<Shape> clonedShapes = new LinkedList<>();
    private boolean load, copy;
    private static ToggleGroup controls;
    public RadioButton move, scale;
    private boolean controlBtnIsSelected, grid;
    public static Stage dialog;

    public void stageSetter(Stage stage) {
        this.primaryStage = stage;
    }

    public void initialize() throws IOException {
        toolBar.getItems().remove(nSideGroup);
        thicknessField.setFocusTraversable(false);
        initToolBar();
        initListeners();
        workingArea.setContent(scene);
        strokeColor.setValue(Color.BLACK);
        gcCanvas = canvas.getGraphicsContext2D();
        gcPreviewCanvas = previewCanvas.getGraphicsContext2D();
        drawingEngine.gcSetter(gcCanvas);
        fillColor.setValue(Color.BLUE);

        controls = new ToggleGroup();
        move.setToggleGroup(controls);
        move.setUserData("move");
        scale.setToggleGroup(controls);
        scale.setUserData("scale");

        sceneForDialog = scene;
        canvasForDialog = canvas;
        previewCanvasForDialog = previewCanvas;

        dialog = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("scene_dialog.fxml"));
        dialog.setTitle("Drawing Area Settings");
        String css = this.getClass().getResource("dialog.css").toExternalForm();
        root.getStylesheets().add(css);
        dialog.setScene(new Scene(root));
        dialog.setResizable(false);
    }

    private void initToolBar() {
        for (int i = 0; i < drawingEngine.getSupportedShapes().size(); i++) {
            Button shapeBtn = new Button();
            int toolBarBtn = 40;
            shapeBtn.setMaxSize(toolBarBtn, toolBarBtn);
            shapeBtn.setMinSize(toolBarBtn, toolBarBtn);
            shapeBtn.getStyleClass().add("tools");

            String[] classPackage = drawingEngine.getSupportedShapes().get(i).getName().split("\\.");
            String className = classPackage[classPackage.length - 1];
            supportedShapes.add(className);

            shapeBtn.setOnAction(event -> {
                controlBtnIsSelected = false;
                move.setSelected(false);
                scale.setSelected(false);

                int previousSelectedShapeIndex = supportedShapes.indexOf(drawShape) + 6;
                shapeBar.getItems().get(previousSelectedShapeIndex).getStyleClass().remove("tools-focused");
                drawShape = className;
                int currentSelectedShapeIndex = supportedShapes.indexOf(drawShape) + 6;
                shapeBar.getItems().get(currentSelectedShapeIndex).getStyleClass().add("tools-focused");

                toolBar.getItems().remove(nSideGroup);
                if (drawShape.equals("Square")) {
                    Draw.nSideSetter(4);
                } else if (drawShape.equals("NSide")) {
                    Draw.nSideSetter(Integer.valueOf(nSidesField.getText()));
                    toolBar.getItems().add(nSideGroup);
                }
            });

            StringBuilder image = new StringBuilder("-fx-background-image: url('/images/");
            image.append(className);
            image.append(".png')");
            shapeBtn.setStyle(image.toString());
            shapeBar.getItems().add(shapeBtn);
        }
        shapeBar.getItems().get(6).getStyleClass().add("tools-focused");
        drawShape = supportedShapes.get(0);
    }

    private void initListeners() {
        fillColor.setOnAction(event -> {
            if (!selectedShapes.isEmpty()) {
                for (Shape shape : selectedShapes) {
                    shape.setFillColor(Draw.toAwtColor(fillColor.getValue()));
                }
                drawingEngine.refresh(null);
            }
        });

        strokeColor.setOnAction(event -> {
            if (!selectedShapes.isEmpty()) {
                for (Shape shape : selectedShapes) {
                    shape.setColor(Draw.toAwtColor(strokeColor.getValue()));
                }
                drawingEngine.refresh(null);
            }
        });

        scene.setOnMousePressed(event -> {
            mouseStartPoint.setX(event.getX());
            mouseStartPoint.setY(event.getY());
            if (controlBtnIsSelected) {
                clonedShapes.clear();
                for (Shape shape : selectedShapes) {
                    try {
                        clonedShapes.add((Shape) shape.clone());
                        shape.gcSetter(gcPreviewCanvas);
                        shape.setFillColor(
                                new java.awt.Color(1, 1, 0, .7f));
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            mouseEndPoint.setX(-1);
            mouseEndPoint.setY(-1);
        });

        scene.setOnMouseDragged(event -> {
            mouseEndPoint.setX(event.getX());
            mouseEndPoint.setY(event.getY());

            undoBtn.setDisable(false);
            undoBtn.setStyle("-fx-background-image: url('/images/undoActive.png')");

            if (controlBtnIsSelected) {
                if (selectedShapes.isEmpty()) {
                    //TODO massage no selected shapes
                } else {
                    gcPreviewCanvas.clearRect(0, 0,
                            gcPreviewCanvas.getCanvas().getWidth(), gcPreviewCanvas.getCanvas().getHeight());
                    String operation = controls.getSelectedToggle().getUserData().toString();
                    Draw.updatePreview(gcPreviewCanvas, gcCanvas,
                            operation, selectedShapes, clonedShapes,
                            new Point(
                                    mouseStartPoint.getX(),
                                    mouseStartPoint.getY(),
                                    0, null
                            ), new Point(
                                    mouseEndPoint.getX(),
                                    mouseEndPoint.getY(),
                                    0, null
                            ));
                }
                return;
            }

            try {
                Draw.drawPreview(gcPreviewCanvas, drawShape, new Point(
                                mouseStartPoint.getX(),
                                mouseStartPoint.getY(),
                                0, null
                        ),
                        new Point(
                                mouseEndPoint.getX(),
                                mouseEndPoint.getY(),
                                0, null
                        ),
                        strokeColor.getValue(), fillColor.getValue(),
                        Double.valueOf(thicknessField.getText()), supportedShapes.indexOf(drawShape));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        scene.setOnMouseReleased(event -> {
            if (!controlBtnIsSelected) {
                if (mouseEndPoint.getX() == -1 && mouseEndPoint.getY() == -1) {
                    return;
                }
                try {
                    makeLayer(drawShape, 1);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < selectedShapes.size(); i++) {
                    selectedShapes.get(i).gcSetter(gcCanvas);
                    selectedShapes.get(i).setFillColor(clonedShapes.get(i).getFillColor());
                    Shape newShape = null;
                    try {
                        newShape = (Shape) selectedShapes.get(i).clone();
                        newShape.setLayer(selectedShapes.get(i).getLayer());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    selectedShapes.get(i).setPosition(clonedShapes.get(i).getPosition());
                    selectedShapes.get(i).setEndPoint(clonedShapes.get(i).getEndPoint());
                    drawingEngine.updateShape(
                            selectedShapes.get(i), newShape
                    );
                    int oldShapeIndex = selectedShapes.indexOf(selectedShapes.get(i));
                    selectedShapes.set(oldShapeIndex, newShape);
                    gcPreviewCanvas.clearRect(0, 0,
                            gcPreviewCanvas.getCanvas().getWidth(), gcPreviewCanvas.getCanvas().getHeight());
                    drawingEngine.refresh(null);
                }
            }
            clonedShapes.clear();
            mouseEndPoint.setX(-1);
            mouseEndPoint.setY(-1);
        });

        nSidesField.textProperty().addListener((observable, oldValue, newValue) -> {
            Draw.nSideSetter(Integer.valueOf(newValue));
        });

        thicknessField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                if (!selectedShapes.isEmpty()) {
                    for (Shape shape : selectedShapes) {
                        shape.setThickness(Double.valueOf(newValue));
                    }
                    drawingEngine.refresh(null);
                }
            } else {
                newValue = oldValue;
            }
        });
    }

    private void makeLayer(String drawShape, int index) throws InstantiationException, IllegalAccessException {
        CheckBox visible = new CheckBox("");
        visible.setPadding(new Insets(0, 10, 0, 0));
        visible.setSelected(true);
        visible.getStyleClass().add("visible");

        CheckBox selectShape = new CheckBox("");
        selectShape.getStyleClass().add("select");
        selectShape.setPadding(new Insets(0, 10, 0, 0));

        Image image = previewCanvas.snapshot(null, null);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(30);

        Label label = new Label(drawShape);
        label.getStyleClass().add("layer-label");
        label.setPadding(new Insets(0, 40, 0, 10));


        HBox hbox = new HBox(visible, selectShape, imageView, label);
        hbox.setPrefWidth(286);
        hbox.setPrefHeight(40);
        hbox.setPadding(new Insets(0, 0, 0, 10));
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("layer");

        VBox.setMargin(hbox, new Insets(2, 5, 0, 5));
        layerVBox.getChildren().add(0, hbox);

        if (!load && !copy) {
            Draw.drawShape(hbox, gcCanvas, drawShape, mouseStartPoint, mouseEndPoint,
                    strokeColor.getValue(), fillColor.getValue(),
                    Double.valueOf(thicknessField.getText()), supportedShapes.indexOf(drawShape));
        } else {
            load = false;
            copy = false;
        }

        gcPreviewCanvas.clearRect(0, 0,
                gcPreviewCanvas.getCanvas().getWidth(), gcPreviewCanvas.getCanvas().getHeight());

        int shapeIndex = drawingEngine.getShapes().length - index - layerVBox.getChildren().indexOf(hbox);

        if (drawingEngine.getShapes()[shapeIndex].getLayer() == null) {
            drawingEngine.getShapes()[shapeIndex].setLayer(hbox);
        }

        java.awt.Color stroke = drawingEngine.getShapes()[shapeIndex].getColor();
        java.awt.Color fill = drawingEngine.getShapes()[shapeIndex].getFillColor();

        hbox.setOnMouseEntered(event1 -> {
            if (visible.isSelected()) {
                if (drawingEngine.getShapes()[shapeIndex].getColor() == stroke) {
                    drawingEngine.getShapes()[shapeIndex].setColor(java.awt.Color.RED);
                }

                if (drawingEngine.getShapes()[shapeIndex].getFillColor() == fill) {
                    drawingEngine.getShapes()[shapeIndex].setFillColor(java.awt.Color.YELLOW);
                }
                drawingEngine.refresh(null);
            }
        });

        hbox.setOnMouseExited(event1 -> {
            if (!selectShape.isSelected() && visible.isSelected()) {
                if (drawingEngine.getShapes()[shapeIndex].getColor() == java.awt.Color.RED) {
                    drawingEngine.getShapes()[shapeIndex].setColor(stroke);
                }

                if (drawingEngine.getShapes()[shapeIndex].getFillColor() == java.awt.Color.YELLOW) {
                    drawingEngine.getShapes()[shapeIndex].setFillColor(fill);
                }
                drawingEngine.refresh(null);
            }
        });

        visible.setOnAction(event1 -> {
            if (visible.isSelected()) {
                if (!selectShape.isSelected()) {
                    drawingEngine.getShapes()[shapeIndex].setColor(stroke);
                    drawingEngine.getShapes()[shapeIndex].setFillColor(fill);
                    drawingEngine.refresh(null);
                } else {
                    drawingEngine.getShapes()[shapeIndex].setColor(java.awt.Color.RED);
                    drawingEngine.getShapes()[shapeIndex].setFillColor(java.awt.Color.YELLOW);
                    drawingEngine.refresh(null);
                }
            } else {
                drawingEngine.getShapes()[shapeIndex].setColor(
                        new java.awt.Color(0, 0, 0, 1));
                drawingEngine.getShapes()[shapeIndex].setFillColor(
                        new java.awt.Color(0, 0, 0, 1));
                drawingEngine.refresh(null);
            }
        });

        selectShape.setOnAction(event1 -> {
            if (!visible.isSelected()) {
                return;
            }
            if (selectShape.isSelected()) {
                selectedShapes.add(drawingEngine.getShapes()[shapeIndex]);
                drawingEngine.getShapes()[shapeIndex].setColor(java.awt.Color.RED);
                drawingEngine.getShapes()[shapeIndex].setFillColor(java.awt.Color.YELLOW);
                drawingEngine.refresh(null);
            } else {
                clonedShapes.remove(drawingEngine.getShapes()[shapeIndex]);
                selectedShapes.remove(drawingEngine.getShapes()[shapeIndex]);

                if (drawingEngine.getShapes()[shapeIndex].getColor() == java.awt.Color.RED) {
                    drawingEngine.getShapes()[shapeIndex].setColor(stroke);
                }

                if (drawingEngine.getShapes()[shapeIndex].getFillColor() == java.awt.Color.YELLOW) {
                    drawingEngine.getShapes()[shapeIndex].setFillColor(fill);
                }
                drawingEngine.refresh(null);
            }
            //selectoed only one object >> show its properties
            if (selectedShapes.size() == 1) {
                for (Map.Entry<String, Double> entry : selectedShapes.get(0).getProperties().entrySet()) {
                    if (!entry.getKey().equals("type")) {
                        Label property = new Label(entry.getKey());
                        property.getStyleClass().add("layer-label");
                        property.setPadding(new Insets(0, 20, 0, 10));

                        Label value = new Label(entry.getValue().toString());
                        value.getStyleClass().add("layer-label");

                        HBox element = new HBox(property, value);
                        element.setPrefWidth(286);
                        element.setPrefHeight(40);
                        element.setPadding(new Insets(0, 0, 0, 10));
                        element.setAlignment(Pos.CENTER_LEFT);
                        element.getStyleClass().add("layer");

                        VBox.setMargin(element, new Insets(2, 5, 0, 5));
                        propVBox.getChildren().add(element);
                    }
                }
            } else {
                propVBox.getChildren().clear();
            }
        });
    }

    public void onSettingsClicked() {
        dialog.showAndWait();
    }

    public void onUndoPressed() {
        if (!erased) {
            drawingEngine.undo();
        }
        erased = false;
        drawingEngine.refresh(null);
        redoBtn.setDisable(false);
        redoBtn.setStyle("-fx-background-image: url('/images/redoActive.png')");
        if (drawingEngine.getHistoryState() == -1) {
            undoBtn.setDisable(true);
            undoBtn.setStyle("-fx-background-image: url('/images/undoInActive.png')");
        }
        layerVBox.getChildren().clear();
        for (int i = drawingEngine.getShapes().length - 1; i >= 0; i--) {
            layerVBox.getChildren().add(drawingEngine.getShapes()[i].getLayer());
        }
    }

    public void onRedoPressed() {
        drawingEngine.redo();
        drawingEngine.refresh(null);
        undoBtn.setDisable(false);
        undoBtn.setStyle("-fx-background-image: url('/images/undoActive.png')");
        if (drawingEngine.getHistoryState() == 1) {
            redoBtn.setDisable(true);
            redoBtn.setStyle("-fx-background-image: url('/images/redoInActive.png')");
        }
        layerVBox.getChildren().clear();
        for (int i = drawingEngine.getShapes().length - 1; i >= 0; i--) {
            layerVBox.getChildren().add(drawingEngine.getShapes()[i].getLayer());
        }
    }

    public void onGridChecked() {
        if (!grid) {
            grid = true;
            scene.getStyleClass().add("scene-background");
            gridBtn.getStyleClass().remove("grid");
            gridBtn.getStyleClass().add("grid-clicked");
        } else {
            grid = false;
            scene.getStyleClass().remove("scene-background");
            gridBtn.getStyleClass().remove("grid-clicked");
            gridBtn.getStyleClass().add("grid");
        }
    }

    public void onErase() {
        erased = true;
        layerVBox.getChildren().clear();
        gcCanvas.clearRect(0, 0,
                gcCanvas.getCanvas().getWidth(), gcCanvas.getCanvas().getHeight());
    }

    public void onSaveImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        WritableImage image = scene.snapshot(new SnapshotParameters(), null);
        File file = fileChooser.showSaveDialog(primaryStage);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {

        }
    }

    public void onSave() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter1 =
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter1);
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);

        String path = file.getPath();
        drawingEngine.save(path);
    }

    public void onLoad() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        redoBtn.setDisable(true);
        undoBtn.setDisable(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(primaryStage);
        String path = file.getAbsolutePath();

        drawingEngine.load(path);
        drawingEngine.gcSetter(gcCanvas);
        layerVBox.getChildren().clear();
        String[] classPackage;
        String className;
        Shape shape;

        Draw.graphicsContext = gcCanvas;
        for (String x: supportedShapes){
            System.out.println(x);
        }
        for (int i = 0; i < drawingEngine.getShapes().length; i++) {
            load = true;
            shape = drawingEngine.getShapes()[i];
            classPackage = (shape.getClass().getName().split("\\."));
            className = classPackage[classPackage.length - 1];
            Draw.pluginShapeIndex = i;

            if (!drawingEngine.getSupportedShapes().contains(shape.getClass())){
                className = supportedShapes.getLast();
            }

            Draw.drawLoadImage(gcPreviewCanvas, className, shape.getPosition(),
                    shape.getEndPoint(), shape.getColor(), shape.getFillColor(),
                    Double.valueOf(thicknessField.getText()),
                    supportedShapes.indexOf(className));
            makeLayer(className, drawingEngine.getShapes().length - i);
        }
        drawingEngine.refresh(null);
    }

    public void onImport() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(primaryStage);
        String path = file.getAbsolutePath();
        drawingEngine.plugin(path, this.getClass());

        shapeBar.getItems().clear();
        shapeBar.getItems().addAll(strokeLabel, strokeColor, strokeLine,
                fillLabel, fillColor, fillLine);
        supportedShapes.clear();
        initToolBar();
    }

    public void onDeleteShape() {
        for (Shape shape : selectedShapes) {
            layerVBox.getChildren().remove(shape.getLayer());
            drawingEngine.removeShape(shape);
        }
        drawingEngine.refresh(null);
        selectedShapes.clear();
    }

    public void onControlBtnPressed() {
        controlBtnIsSelected = true;
        int previousSelectedShapeIndex = supportedShapes.indexOf(drawShape) + 6;
        shapeBar.getItems().get(previousSelectedShapeIndex).getStyleClass().remove("tools-focused");
    }

    public void onCopy() throws CloneNotSupportedException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (selectedShapes.isEmpty()) {
            return;
        }

        for (int i = 0; i < selectedShapes.size(); i++) {
            Shape shape = (Shape) selectedShapes.get(i).clone();
            java.awt.Point newStartPosition = new java.awt.Point();
            newStartPosition.setLocation(shape.getPosition().getX() + 10, shape.getPosition().getY() + 10);

            java.awt.Point newEndPoint = new java.awt.Point();
            newEndPoint.setLocation(shape.getEndPoint().getX() + 10, shape.getEndPoint().getY() + 10);

            shape.setPosition(newStartPosition);
            shape.setEndPoint(newEndPoint);

            shape.setColor(Draw.toAwtColor(strokeColor.getValue()));
            shape.setFillColor(Draw.toAwtColor(fillColor.getValue()));

            drawingEngine.addShape(shape);
        }
        layerVBox.getChildren().clear();
        for (int i = 0; i < drawingEngine.getShapes().length; i++) {
            copy = true;
            Shape shape = drawingEngine.getShapes()[i];
            if (shape.getLayer() == null) {

                String[] classPackage = (shape.getClass().getName().split("\\."));
                String className = classPackage[classPackage.length - 1];
                Draw.drawLoadImage(gcPreviewCanvas, className, shape.getPosition(),
                        shape.getEndPoint(), shape.getColor(), shape.getFillColor(),
                        Double.valueOf(thicknessField.getText()),
                        supportedShapes.indexOf(className));
                makeLayer(className, drawingEngine.getShapes().length - i);
            } else {
                layerVBox.getChildren().add(0, shape.getLayer());
            }
        }
        drawingEngine.refresh(null);
    }
}
