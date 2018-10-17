package eg.edu.alexu.csd.oop.draw.cs37_54;

import java.awt.Color;
import java.awt.Point;

import eg.edu.alexu.csd.oop.draw.shapes.Circle;
import eg.edu.alexu.csd.oop.draw.shapes.Line;
import eg.edu.alexu.csd.oop.draw.shapes.Oval;
//import eg.edu.alexu.csd.oop.draw.shapes.Rectangle;

public class test {

	public static void main(String[] args) {
		
MyDrawingEngine i = MyDrawingEngine.getInstance();
Line l =new Line();
i.addShape(l);
Line l2 =new Line();
i.addShape(l2);
i.addShape(new Circle());
i.addShape(new Oval());
//i.addShape(new Rectangle());

i.save("dir.json");
i.load("dir.json");
i.getShapes();
		
		

	}

}
