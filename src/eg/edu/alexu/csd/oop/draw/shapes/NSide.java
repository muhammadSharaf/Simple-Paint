package eg.edu.alexu.csd.oop.draw.shapes;

import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
import javafx.scene.canvas.GraphicsContext;
import sample.Draw;

import java.awt.*;
import java.util.HashMap;

public class NSide extends MyShape {
    public int n;
    public NSide(Point currentPoint, Point endPoint,
                 Color col, Color fillColor, Double thickness, int n, Double side) {
        super(currentPoint, endPoint,  col, fillColor, thickness);

        this.n = n;
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.NSIDE);
        props.put(Contact.Properties.SIDE_LENGTH, side);
        props.put(Contact.Properties.SIDE_COUNT, (double)n);
        props.put(Contact.Properties.ANGLE, (double)(360 / n));
        super.setProperties(props);
    }

    public NSide() {
        super(new Point(0,0), new Point(0,0),
                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault);
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.NSIDE);
        props.put(Contact.Properties.SIDE_LENGTH, Contact.DefaultValues.nSideDefaultLen);
        props.put(Contact.Properties.SIDE_COUNT, (double)Contact.DefaultValues.nforNside);
        props.put(Contact.Properties.ANGLE, (double)(360 / Contact.DefaultValues.nforNside));
        super.setProperties(props);
    }

    public void drawFx (GraphicsContext gc){
        double orientation = Math.atan2(getEndPoint().getY() - getPosition().getY(),
                getEndPoint().getX() - getPosition().getX());
        double polygonRadius = Math.sqrt(Math.pow(getEndPoint().getX() - getPosition().getX(), 2)
                + Math.pow(getEndPoint().getY() - getPosition().getY(), 2));

        double[] xPoints = new double[n];
        double[] yPoints = new double[n];

        for (int i = 0; i < n; i++) {
            xPoints[i] = polygonRadius * Math.cos(2 * Math.PI * i / n + orientation) + getPosition().getX();
            yPoints[i] = polygonRadius * Math.sin(2 * Math.PI * i / n + orientation) + getPosition().getY();
        }

        gc.setLineWidth(getThickness());
        gc.setFill(toFxColor(getFillColor()));
        gc.fillPolygon(xPoints, yPoints, n);
        gc.setStroke(toFxColor(getColor()));
        gc.strokePolygon(xPoints, yPoints, n);
    }

    @Override
    public Object clone (){
        return new NSide (
                this.getPosition(),
                this.getEndPoint(),
                this.getColor(),
                this.getFillColor(),
                this.getThickness(),
                this.n,
                this.getProperties().get(Contact.Properties.SIDE_LENGTH)
        );
    }

    public Double getArea() {
        Double side = super.getProperties().get(Contact.Properties.SIDE_LENGTH);
        Double n = super.getProperties().get(Contact.Properties.SIDE_COUNT);
        Double area = (Math.sqrt(side) * n) / (4 * Math.tan(180 / n));
        return area;
    }
}
