package eg.edu.alexu.csd.oop.draw.shapes;

import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;

import java.awt.*;
import java.util.HashMap;

public class Square extends NSide {
    public Square() {
        super(new Point(0,0), new Point(0,0),
                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault,4,Contact.DefaultValues.squareSideLen);
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.SQUARE);
        props.put(Contact.Properties.SIDE_LENGTH_SQUARE, Contact.DefaultValues.squareSideLen);
        super.setProperties(props);

    }

    public Square(Point currentPoint, Point endPoint,
                  Color col, Color fillColor, Double thickness, Double side) {
        super(currentPoint,endPoint,  col, fillColor, thickness, 4, side);
        HashMap<String, Double> props = new HashMap<>();
        props.put(Contact.SHAPE_TYPE,(double) Contact.SupportedShapes.SQUARE);
        props.put(Contact.Properties.SIDE_LENGTH_SQUARE, side);
        super.setProperties(props);
    }
}
