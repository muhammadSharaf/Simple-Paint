package eg.edu.alexu.csd.oop.draw.cs37_54;


import eg.edu.alexu.csd.oop.draw.Shape;
import javafx.scene.canvas.*;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import sample.Controller;
import java.awt.*;
import java.awt.Color;
import java.io.*;
import java.util.Map;

public  class MyShape implements Shape {
    private Point position;
    private Point endPoint;
    private Map<String, Double> properties;
    /**
     * the color of the lines
     */
    private Color color;
    /**
     * the color inside the shape
     */
    private Color fillColor;

    /**
     * public constructor
     * must set default values
     */

    private Double thickness;


    public MyShape () {

    }

    //TODO look! this is the new constructor
    //TODO can make enum for drawing line types (straight, zigzag ,..)
    public MyShape(Point currentPoint, Point endPoint, Color col, Color fillColor, Double thickness) {
        this.position = currentPoint;
        this.endPoint = endPoint;
        this.color = col;
        this.fillColor = fillColor;
        this.thickness = thickness;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public Point getEndPoint() {return endPoint;}

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Double> getProperties() {
        return properties;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setFillColor(Color color) {
        /* in case of line*/
        if (color != null)
            this.fillColor = color;
    }

    @Override
    public Color getFillColor() {
        return this.fillColor;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    @Override
    public void draw(Graphics canvas) {
        drawFx(this.gc);
    }

    private GraphicsContext gc = Controller.gcCanvas;

    public void gcSetter (GraphicsContext gc){
        this.gc = gc;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void drawFx (GraphicsContext canvas){

    }

    protected static javafx.scene.paint.Color toFxColor(java.awt.Color color) {
        javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha() / 255.0
        );
        return fxColor;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        //overidding with calling constructors cause this returned null !
        return null;
    }


    public HBox layer;

    @Override
    public HBox getLayer() {
        return layer;
    }

    @Override
    public void setLayer(HBox layer) {
        this.layer = layer;

    }
}

