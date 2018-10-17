package eg.edu.alexu.csd.oop.draw.shapes;

import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
import javafx.scene.canvas.GraphicsContext;

import java.awt.Point;
import java.awt.Color;
import java.util.HashMap;

public class Circle extends MyShape {

    public Circle () {
        super(new Point(0,0), new Point(0,0),
                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault);
    	  HashMap<String, Double> props = new HashMap<>();
          props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.CIRCLE);
          props.put(Contact.Properties.RADIUS, Contact.DefaultValues.radiusDefault);
          super.setProperties(props);
    }

    public Circle(Point currentPoint, Point endPoint, Color col, Color fillColor, Double thickness, Double radius) {
        super(currentPoint, endPoint, col, fillColor, thickness);

        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.CIRCLE);
        props.put(Contact.Properties.RADIUS, radius);
        props.put(Contact.THICKNESS, thickness);
        super.setProperties(props);
    }

    @Override
    public void drawFx (GraphicsContext gc){
        double value = Math.pow(getPosition().getX() - getEndPoint().getX(), 2)
                + Math.pow(getPosition().getY() - getEndPoint().getY(), 2);

        double radius = Math.sqrt(value / 2) / 2;
        this.getProperties().put(Contact.Properties.RADIUS, radius);
        this.getProperties().put(Contact.THICKNESS, getThickness());

        gc.setLineWidth(getProperties().get(Contact.THICKNESS));
        gc.setFill(toFxColor(getFillColor()));
        gc.fillOval(getPosition().getX(), getPosition().getY(), radius * 2, radius * 2);
        gc.setStroke(toFxColor(getColor()));
        gc.strokeOval(getPosition().getX(), getPosition().getY(), radius * 2, radius * 2);
    }

    @Override
    public Object clone (){
        return new Circle (
                this.getPosition(),
                this.getEndPoint(),
                this.getColor(),
                this.getFillColor(),
                this.getThickness(),
                this.getProperties().get(Contact.Properties.RADIUS)
        );
    }

    public Double getArea() {
        Double radius = super.getProperties().get(Contact.Properties.RADIUS);
        Double area = Math.PI * Math.sqrt(radius);
        return area;
    }
}
