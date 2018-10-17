package eg.edu.alexu.csd.oop.draw.cs37_54;
import eg.edu.alexu.csd.oop.draw.Shape;

public class HistoryElement {
    private Shape currentShape;
    private Shape oldShape;
    private String operation;
    private int index;

    public HistoryElement (Shape currentShape, Shape oldShape, String operation){
        this.currentShape = currentShape;
        this.oldShape = oldShape;
        this.operation = operation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public Shape getOldShape() {
        return oldShape;
    }

    public void setOldShape(Shape oldShape) {
        this.oldShape = oldShape;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
