package eg.edu.alexu.csd.oop.draw.shapes;

import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
import javafx.scene.canvas.GraphicsContext;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;


public class Line extends MyShape {
    public Line() {
        super(new Point(0, 0), new Point(0, 0),
                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault);
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE, (double) Contact.SupportedShapes.LINE);
        props.put(Contact.Properties.LENGTH, Contact.DefaultValues.lineLengthDefault);
        props.put(Contact.Properties.LINE_ANGLE, Contact.DefaultValues.lineAngleDefault);

        super.setProperties(props);
    }

    /**
     * a public constructor to call in order to make a line
     *
     * @param currentPoint
     * @param length
     * @param col
     * @param thickness
     */
    public Line(Point currentPoint, Point endPoint, Color col, Color fillColor, Double thickness, Double length) {
        super(currentPoint, endPoint, col, fillColor, thickness);
        double angle = Math.atan2(endPoint.getY() - currentPoint.getY(),
                endPoint.getX() - currentPoint.getX());
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE, (double) Contact.SupportedShapes.LINE);
        props.put(Contact.Properties.LENGTH, length);
        props.put(Contact.Properties.LINE_ANGLE, angle);

        super.setProperties(props);
    }

    public void drawFx(GraphicsContext gc) {
        gc.setLineWidth(getThickness());
        gc.setStroke(toFxColor(getFillColor()));
        gc.strokeLine(getPosition().getX(), getPosition().getY(), getEndPoint().getX(), getEndPoint().getY());
    }

    @Override
    public Object clone (){
        return new Line (
                this.getPosition(),
                this.getEndPoint(),
                this.getColor(),
                this.getFillColor(),
                this.getThickness(),
                this.getProperties().get(Contact.Properties.LENGTH)
        );
    }

    public Double getArea() {
        return 0.0;
    }
}
