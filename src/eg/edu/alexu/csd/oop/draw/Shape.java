package eg.edu.alexu.csd.oop.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.*;

public interface Shape extends Fx {
    public void setPosition(java.awt.Point position);

    public java.awt.Point getPosition();

    /* update shape specific properties (e.g., radius) */
    public void setProperties(java.util.Map<String, Double> properties);

    public java.util.Map<String, Double> getProperties();

    public void setColor(java.awt.Color color);

    public java.awt.Color getColor();

    public void setFillColor(java.awt.Color color);

    public java.awt.Color getFillColor();

    /* redraw the shape on the canvas */
    public void draw(java.awt.Graphics canvas);

    /* create a deep clone of the shape */
    public Object clone() throws CloneNotSupportedException;

    public void setThickness(Double thickness);

}