package eg.edu.alexu.csd.oop.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;

import java.awt.*;

/**
 * Created by M.Sharaf on 16/11/2017.
 */
public interface Fx {
    public void setEndPoint(Point endPoint);
    public Point getEndPoint();
    public HBox getLayer();

    public void setLayer(HBox layer);
    public void gcSetter (GraphicsContext gc);
    public GraphicsContext getGc();
}
