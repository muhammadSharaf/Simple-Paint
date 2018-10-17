//package eg.edu.alexu.csd.oop.draw.shapes;
//
//import eg.edu.alexu.csd.oop.draw.cs37_54.Contact;
//import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
//import javafx.scene.canvas.GraphicsContext;
//
//import java.awt.Color;
//import java.awt.Point;
//import java.util.HashMap;
//
//public class Rectangle extends MyShape {
//    private double width;
//    private double height;
//
//    public Rectangle() {
//        super(new Point(0, 0), new Point(0, 0),
//                Color.BLACK, Color.WHITE, Contact.DefaultValues.thicknessDefault);
//        HashMap<String, Double> props = new HashMap<>();
//        props.put(Contact.SHAPE_TYPE, (double) Contact.SupportedShapes.RECTANGLE);
//        props.put(Contact.Properties.HEIGHT, Contact.DefaultValues.rectheight);
//        props.put(Contact.Properties.WIDTH, Contact.DefaultValues.rectwidth);
//        super.setProperties(props);
//    }
//
//    public Rectangle(Point currentPoint, Point endPoint,
//                     Color col, Color fillColor, Double thickness) {
//        super(currentPoint, endPoint, col, fillColor, thickness);
//        width = endPoint.getX() - currentPoint.getX();
//        height = endPoint.getY() - currentPoint.getY();
//
//        HashMap<String, Double> props = new HashMap<>();
//        props.put(Contact.SHAPE_TYPE, (double) Contact.SupportedShapes.RECTANGLE);
//        props.put(Contact.Properties.HEIGHT, height);
//        props.put(Contact.Properties.WIDTH, width);
//        super.setProperties(props);
//    }
//
//    @Override
//    public void drawFx(GraphicsContext gc) {
//        width = getEndPoint().getX() - getPosition().getX();
//        height = getEndPoint().getY() - getPosition().getY();
//        this.getProperties().put(Contact.Properties.HEIGHT, height);
//        this.getProperties().put(Contact.Properties.WIDTH, width);
//
//        gc.setLineWidth(getThickness());
//        gc.setFill(toFxColor(getFillColor()));
//        gc.fillRect(getPosition().getX(), getPosition().getY(), width, height);
//        gc.setStroke(toFxColor(getColor()));
//        gc.strokeRect(getPosition().getX(), getPosition().getY(), width, height);
//    }
//
//    @Override
//    public Object clone() {
//        return new Rectangle(
//                this.getPosition(),
//                this.getEndPoint(),
//                this.getColor(),
//                this.getFillColor(),
//                this.getThickness()
//        );
//    }
//
//    public Double getArea() {
//        Double height = super.getProperties().get(Contact.Properties.HEIGHT);
//        Double width = super.getProperties().get(Contact.Properties.WIDTH);
//        return height * width;
//    }
//}
//
//
//
