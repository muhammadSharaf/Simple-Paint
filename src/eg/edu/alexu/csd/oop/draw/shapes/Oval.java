package eg.edu.alexu.csd.oop.draw.shapes;

import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;

public class Oval extends MyShape {
    public Oval(Point currentPoint, Point endPoint, Color col, Color fillColor, Double thickness) {
        super(currentPoint, endPoint, col, fillColor, thickness);
        double majorAxis = endPoint.getX() - currentPoint.getX();
        double minorAxis =endPoint.getY() - currentPoint.getY();

        if (majorAxis < minorAxis){
            double temp = majorAxis;
            majorAxis = minorAxis;
            minorAxis = temp;
        }

        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.OVAL);
        props.put(Contact.Properties.MAJOR_AXIS, majorAxis);
        props.put(Contact.Properties.MINOR_AXIS, minorAxis);
        super.setProperties(props);

    }

    public Oval() {
        super(new Point(0,0), new Point(0,0),
                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault);
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.OVAL);
        props.put(Contact.Properties.MAJOR_AXIS, Contact.DefaultValues.OvalmajorAxis);
        props.put(Contact.Properties.MINOR_AXIS, Contact.DefaultValues.OvalminorAxis);
        super.setProperties(props);

    }

    @Override
    public void drawFx (GraphicsContext gc){
        double width = getEndPoint().getX() - getPosition().getX();
        double height = getEndPoint().getY() - getPosition().getY();

        gc.setLineWidth(getThickness());
        gc.setFill(toFxColor(getFillColor()));
        gc.fillOval(getPosition().getX(), getPosition().getY(),width, height);
        gc.setStroke(toFxColor(getColor()));
        gc.strokeOval(getPosition().getX(), getPosition().getY(),width, height);
    }

    @Override
    public Object clone (){
        return new Oval (
                this.getPosition(),
                this.getEndPoint(),
                this.getColor(),
                this.getFillColor(),
                this.getThickness()
        );
    }

    public Double getArea() {
        Double a = super.getProperties().get(Contact.Properties.MAJOR_AXIS) / 2;
        Double b = super.getProperties().get(Contact.Properties.MINOR_AXIS) / 2;
        Double area = Math.PI * a * b;

        return area;
    }
}
