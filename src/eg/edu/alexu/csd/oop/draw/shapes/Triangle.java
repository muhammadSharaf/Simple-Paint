package eg.edu.alexu.csd.oop.draw.shapes;

import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;

public class Triangle extends MyShape {
    public Triangle(Point currentPoint, Point endPoint,
                    Color col, Color fillColor, Double thickness) {
        super(currentPoint, endPoint, col, fillColor, thickness);

        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.TRIANGLE);
        super.setProperties(props);
    }

    public Triangle() {
        super(new Point(0,0),new Point(0,0),
                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault);
    }

    public void drawFx (GraphicsContext gc){
        double[] xPoints = new double[]{
                (getEndPoint().getX() - getPosition().getX()) / 2 + getPosition().getX()
                , getPosition().getX(), getEndPoint().getX()
        };

        double[] yPoints = new double[]{
                getPosition().getY(), getEndPoint().getY(), getEndPoint().getY()
        };

        gc.setLineWidth(getThickness());
        gc.setFill(toFxColor(getFillColor()));
        gc.fillPolygon(xPoints, yPoints, 3);
        gc.setStroke(toFxColor(getColor()));
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    @Override
    public Object clone (){
        return new Triangle (
                this.getPosition(),
                this.getEndPoint(),
                this.getColor(),
                this.getFillColor(),
                this.getThickness()
        );
    }

}
