package eg.edu.alexu.csd.oop.draw.cs37_54;

import java.util.LinkedList;

public class Node {
	public static final int OBJECT_I = 0;
	public static final int ELEMENT_I = 1;
	public static final int ARRAY_I = 2;
public int type ;
public Object value;
public String key;
public Node parent;
public LinkedList<Node> children;
public Node (Node parent, String key, Object value,int type){
	children  = new LinkedList<Node>();
	this.type = type;
	this.key=key;
	this.value=value;
	this.parent=parent;
	if(parent != null)
	parent.addChild(this);
}
public void addChild(Node n){
	children.add(n);
}
}
