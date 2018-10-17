package sample;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyDrawingEngine;
import eg.edu.alexu.csd.oop.draw.cs37_54.MyShape;
import eg.edu.alexu.csd.oop.draw.shapes.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sun.security.provider.SHA;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class Draw {
    //drawing engine to add shapes
    private static MyDrawingEngine drawingEngine = MyDrawingEngine.getInstance();
    private static double[] xPoints;
    private static double[] yPoints;
    private static int nSides = 5;
    private static java.awt.Point position;
    private static java.awt.Point endPoint;
    public static int pluginShapeIndex;
    public static GraphicsContext graphicsContext;

    public static void nSideSetter(int nSides) {
        Draw.nSides = nSides;
    }

    protected static java.awt.Color toAwtColor(Color color) {
        java.awt.Color awtColor = new java.awt.Color(
                (float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue(),
                (float) color.getOpacity()
        );
        return awtColor;
    }

    protected static Color toFxColor(java.awt.Color color) {
        Color fxColor = Color.rgb(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha() / 255.0
        );
        return fxColor;
    }

    private static void validatePoints(Point upperLeft, Point lowerRight) {
        double temp;
        if (upperLeft.getX() > lowerRight.getX()) {
            temp = upperLeft.getX();
            upperLeft.setX(lowerRight.getX());
            lowerRight.setX(temp);
        }
        if (upperLeft.getY() > lowerRight.getY()) {
            temp = upperLeft.getY();
            upperLeft.setY(lowerRight.getY());
            lowerRight.setY(temp);
        }
    }

    private static double distance(Point point1, Point point2) {
        double squareDiameter = Math.pow(point1.getX() - point2.getX(), 2)
                + Math.pow(point1.getY() - point2.getY(), 2);

        return Math.sqrt(squareDiameter);
    }

    public static void drawLoadImage(GraphicsContext gcPreview, String shape, java.awt.Point position,
                                     java.awt.Point endPoint, java.awt.Color strokeColor,
                                     java.awt.Color fillColor, Double thickness, int classIndex) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Point upperLeft = new Point(position.getX(), position.getY(), 0, null);
        Point lowerRight = new Point(endPoint.getX(), endPoint.getY(), 0, null);

        drawPreview(gcPreview, shape, upperLeft, lowerRight, toFxColor(strokeColor),
                toFxColor(fillColor), thickness, classIndex);

    }

    //TODO use static names for classes from contract
    public static void drawPreview(GraphicsContext gcPreview, String shape, Point upperLeft, Point lowerRight,
                                   Color strokeColor, Color fillColor, Double thickness, int classIndex)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        gcPreview.setLineWidth(thickness);
        if (!shape.equals("Line") && !shape.equals("Triangle") && !shape.equals("NSide") && !shape.equals("Square")) {
            validatePoints(upperLeft, lowerRight);
        }
        gcPreview.clearRect(0, 0, gcPreview.getCanvas().getWidth(), gcPreview.getCanvas().getHeight());
        switch (shape) {
            case "Circle":
                double diameter = distance(upperLeft, lowerRight) / Math.sqrt(2);
                gcPreview.setFill(fillColor);
                gcPreview.fillOval(upperLeft.getX(), upperLeft.getY(),
                        diameter, diameter
                );
                gcPreview.setStroke(strokeColor);
                gcPreview.strokeOval(upperLeft.getX(), upperLeft.getY(),
                        diameter, diameter
                );
                break;
            case "Diamond":
                xPoints = new double[]{
                        (lowerRight.getX() - upperLeft.getX()) / 2 + upperLeft.getX(), lowerRight.getX(),
                        (lowerRight.getX() - upperLeft.getX()) / 2 + upperLeft.getX(), upperLeft.getX()
                };

                yPoints = new double[]{
                        upperLeft.getY(), (lowerRight.getY() - upperLeft.getY()) / 2 + upperLeft.getY(),
                        lowerRight.getY(), (lowerRight.getY() - upperLeft.getY()) / 2 + upperLeft.getY()
                };

                gcPreview.setFill(fillColor);
                gcPreview.fillPolygon(xPoints, yPoints, 4);
                gcPreview.setStroke(strokeColor);
                gcPreview.strokePolygon(xPoints, yPoints, 4);
                break;
            case "Line":
                gcPreview.setStroke(fillColor);
                gcPreview.strokeLine(upperLeft.getX(), upperLeft.getY(),
                        lowerRight.getX(), lowerRight.getY());
                break;
            case "NSide":
            case "Square":
                double orientation = Math.atan2(lowerRight.getY() - upperLeft.getY(),
                        lowerRight.getX() - upperLeft.getX());
                double polygonRadius = distance(upperLeft, lowerRight);

                xPoints = new double[nSides];
                yPoints = new double[nSides];

                for (int i = 0; i < nSides; i++) {
                    xPoints[i] = polygonRadius * Math.cos(2 * Math.PI * i / nSides + orientation) + upperLeft.getX();
                    yPoints[i] = polygonRadius * Math.sin(2 * Math.PI * i / nSides + orientation) + upperLeft.getY();
                }

                gcPreview.setFill(fillColor);
                gcPreview.fillPolygon(xPoints, yPoints, nSides);
                gcPreview.setStroke(strokeColor);
                gcPreview.strokePolygon(xPoints, yPoints, nSides);

                break;
            case "Triangle":
                xPoints = new double[]{
                        (lowerRight.getX() - upperLeft.getX()) / 2 + upperLeft.getX()
                        , upperLeft.getX(), lowerRight.getX()
                };

                yPoints = new double[]{
                        upperLeft.getY(), lowerRight.getY(), lowerRight.getY()
                };

                gcPreview.setFill(fillColor);
                gcPreview.fillPolygon(xPoints, yPoints, 3);
                gcPreview.setStroke(strokeColor);
                gcPreview.strokePolygon(xPoints, yPoints, 3);
                break;
            case "Oval":
                gcPreview.setFill(fillColor);
                gcPreview.fillOval(upperLeft.getX(), upperLeft.getY(),
                        lowerRight.getX() - upperLeft.getX(),
                        lowerRight.getY() - upperLeft.getY()
                );
                gcPreview.setStroke(strokeColor);
                gcPreview.strokeOval(upperLeft.getX(), upperLeft.getY(),
                        lowerRight.getX() - upperLeft.getX(),
                        lowerRight.getY() - upperLeft.getY()
                );
                break;
//            case "Rectangle":
//                gcPreview.setFill(fillColor);
//                gcPreview.fillRect(upperLeft.getX(), upperLeft.getY(),
//                        lowerRight.getX() - upperLeft.getX(),
//                        lowerRight.getY() - upperLeft.getY()
//                );
//                gcPreview.setStroke(strokeColor);
//                gcPreview.strokeRect(upperLeft.getX(), upperLeft.getY(),
//                        lowerRight.getX() - upperLeft.getX(),
//                        lowerRight.getY() - upperLeft.getY()
//                );
//                break;
            default:
                position = new java.awt.Point();
                position.setLocation(upperLeft.getX(), upperLeft.getY());
                endPoint = new java.awt.Point();
                endPoint.setLocation(lowerRight.getX(), lowerRight.getY());
                Shape pluginShape = drawingEngine.getSupportedShapes().get(classIndex).getDeclaredConstructor(
                        java.awt.Point.class, java.awt.Point.class, java.awt.Color.class,
                        java.awt.Color.class, Double.class).newInstance(
                        position, endPoint, toAwtColor(strokeColor),
                        toAwtColor(fillColor), thickness
                );
                pluginShape.gcSetter(gcPreview);
                pluginShape.draw(null);
                pluginShape.gcSetter(graphicsContext);
                drawingEngine.removeShape(drawingEngine.getShapes()[pluginShapeIndex]);
                drawingEngine.addShape(pluginShape);
        }
    }

    public static void drawShape(HBox layer, GraphicsContext gc, String shape, Point upperLeft, Point lowerRight,
                                 Color strokeColor, Color fillColor, Double thickness, int classIndex) throws IllegalAccessException, InstantiationException {

        if (!shape.equals("Line") && !shape.equals("Triangle") && !shape.equals("NSide") && !shape.equals("Square")) {
            validatePoints(upperLeft, lowerRight);
        }

        position = new java.awt.Point();
        position.setLocation(upperLeft.getX(), upperLeft.getY());
        endPoint = new java.awt.Point();
        endPoint.setLocation(lowerRight.getX(), lowerRight.getY());

        switch (shape) {
            case "Circle":
                Circle circle = new Circle(position, endPoint, toAwtColor(strokeColor), toAwtColor(fillColor),
                        thickness, distance(upperLeft, lowerRight) / Math.sqrt(2) / 2d);
                circle.draw(null);
                circle.setLayer(layer);
                drawingEngine.addShape(circle);
                break;
            case "Diamond":
                double side = Math.sqrt(Math.pow((position.getX() - endPoint.getX()) / 2, 2)
                        + Math.pow((position.getY() - endPoint.getY()) / 2, 2));
                double angle = Math.atan2((position.getX() - endPoint.getX()) / 2
                        , (position.getY() - endPoint.getY()) / 2) * 2;

                Diamond diamond = new Diamond(position, endPoint, toAwtColor(strokeColor)
                        , toAwtColor(fillColor), thickness, side, angle);
                diamond.draw(null);
                diamond.setLayer(layer);
                drawingEngine.addShape(diamond);
                break;
            case "Line":
                Line line = new Line(position, endPoint, toAwtColor(strokeColor), toAwtColor(fillColor),
                        thickness, distance(lowerRight, upperLeft));
                line.draw(null);
                line.setLayer(layer);
                drawingEngine.addShape(line);
                break;
            case "NSide":
                double sideLen = 2 * distance(upperLeft, lowerRight) * Math.sin(Math.PI / nSides);
                NSide nSide = new NSide(position, endPoint, toAwtColor(strokeColor), toAwtColor(fillColor),
                        thickness, nSides, sideLen);
                nSide.draw(null);
                nSide.setLayer(layer);
                drawingEngine.addShape(nSide);
                break;
            case "Oval":
                Oval oval = new Oval(position, endPoint, toAwtColor(strokeColor), toAwtColor(fillColor),
                        thickness);
                oval.draw(null);
                oval.setLayer(layer);
                drawingEngine.addShape(oval);
                break;
//            case "Rectangle":
//                Rectangle rectangle = new Rectangle(position, endPoint, toAwtColor(strokeColor),
//                        toAwtColor(fillColor), thickness);
//                rectangle.draw(null);
//                rectangle.setLayer(layer);
//                drawingEngine.addShape(rectangle);
//                break;
            case "Square":
                double squareLen = 2 * distance(upperLeft, lowerRight) * Math.sin(Math.PI / nSides);
                Square square = new Square(position, endPoint, toAwtColor(strokeColor), toAwtColor(fillColor),
                        thickness, squareLen);
                square.draw(null);
                square.setLayer(layer);
                drawingEngine.addShape(square);
                break;
            case "Triangle":
                Triangle triangle = new Triangle(position, endPoint, toAwtColor(strokeColor),
                        toAwtColor(fillColor), thickness);
                triangle.draw(null);
                triangle.setLayer(layer);
                drawingEngine.addShape(triangle);
                break;
            default:
                try {
                    Shape pluginShape = drawingEngine.getSupportedShapes().get(classIndex).getDeclaredConstructor(
                            java.awt.Point.class, java.awt.Point.class, java.awt.Color.class,
                            java.awt.Color.class, Double.class).newInstance(
                            position, endPoint, toAwtColor(strokeColor),
                            toAwtColor(fillColor), thickness
                    );
                    pluginShape.setLayer(layer);
                    pluginShape.gcSetter(gc);
                    pluginShape.draw(null);
                    drawingEngine.addShape(pluginShape);
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

        }
    }

    public static void updatePreview(GraphicsContext gcPreview, GraphicsContext gcCanvas,
                                     String operation, LinkedList<Shape> shapes,
                                     LinkedList<Shape> cloned,
                                     Point upperLeft, Point lowerRight) {


        java.awt.Point distancePoint = new java.awt.Point();

        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).setPosition(cloned.get(i).getPosition());
            shapes.get(i).setEndPoint(cloned.get(i).getEndPoint());
        }
        switch (operation) {
            case "move":
                java.awt.Point startPoint = new java.awt.Point();
                startPoint.setLocation(upperLeft.getX(), upperLeft.getY());
                java.awt.Point endPoint = new java.awt.Point();
                endPoint.setLocation(lowerRight.getX(), lowerRight.getY());

                for (int i = 0; i < shapes.size(); i++) {
                    java.awt.Point currentPoint = shapes.get(i).getPosition();
                    java.awt.Point end = shapes.get(i).getEndPoint();

                    distancePoint.setLocation((endPoint.getX() - startPoint.getX()),
                            (endPoint.getY() - startPoint.getY()));


                    java.awt.Point newPoint = new java.awt.Point();
                    newPoint.setLocation((currentPoint.getX() + distancePoint.getX()),
                            (currentPoint.getY() + distancePoint.getY()));
                    shapes.get(i).setPosition(newPoint);

                    java.awt.Point newPoint1 = new java.awt.Point();
                    newPoint1.setLocation(end.getX() + distancePoint.getX(),
                            end.getY() + distancePoint.getY());
                    shapes.get(i).setEndPoint(newPoint1);
                    shapes.get(i).gcSetter(gcPreview);
                    shapes.get(i).draw(null);
                }
                break;

            case "scale":
                java.awt.Point newPoint = new java.awt.Point();
                java.awt.Point newPoint1 = new java.awt.Point();
                java.awt.Point currentPoint;
                java.awt.Point end;
                double scaleFactor = Draw.distance(upperLeft, lowerRight);

                if ((upperLeft.getX() <= lowerRight.getX() && upperLeft.getY() >= lowerRight.getY())
                        || (upperLeft.getX() >= lowerRight.getX() && upperLeft.getY() >= lowerRight.getY())) {

                    for (int i = 0; i < shapes.size(); i++) {

                        currentPoint = shapes.get(i).getPosition();
                        end = shapes.get(i).getEndPoint();

                        newPoint.setLocation(currentPoint.getX() - scaleFactor,
                                currentPoint.getY() - scaleFactor);

                        newPoint1.setLocation(end.getX() + scaleFactor,
                                end.getY() + scaleFactor);

                        shapes.get(i).setPosition(newPoint);
                        shapes.get(i).setEndPoint(newPoint1);
                        shapes.get(i).draw(null);
                    }

                } else {

                    for (int i = 0; i < shapes.size(); i++) {

                        currentPoint = shapes.get(i).getPosition();
                        end = shapes.get(i).getEndPoint();

                        newPoint.setLocation((currentPoint.getX() + scaleFactor),
                                (currentPoint.getY() + scaleFactor));

                        newPoint1.setLocation(end.getX() - scaleFactor,
                                end.getY() - scaleFactor);

                        shapes.get(i).setPosition(newPoint);
                        shapes.get(i).setEndPoint(newPoint1);
                        shapes.get(i).draw(null);
                    }

                }

                break;

            default:
                break;
        }
    }

}
