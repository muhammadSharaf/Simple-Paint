package eg.edu.alexu.csd.oop.draw.cs37_54;

public class Contact {
    public static final String XML = "XML";
    public static final String JSON = "JSON";
    public static final String CURRENTSHAPES = "currentShapes";
    public static final String COLOR = "color";
    public static final String FILLCOLOR = "fillclr";
    public static final String POSITION = "pos";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String THICKNESS = "thickness";
    public static final String MAIN_OBJECT = "MainObject";


    public  class DefaultValues{
    	public static final double thicknessDefault = 1 ;
    	public static final double radiusDefault = 5 ;
    	public static final double diamondAngleDefault = 120 ;
    	public static final double diamondSideDefault = 5 ;
    	public static final double lineLengthDefault = 5 ;
    	public static final double lineAngleDefault = 180 ;
    	public static final double nforNside = 5 ;
    	public static final double nSideDefaultLen = 5 ;
    	public static final double OvalmajorAxis = 120 ;
    	public static final double OvalminorAxis = 100 ;
    	public static final double rectwidth = 4 ;
    	public static final double rectheight = 6 ;
    	public static final double squareSideLen = 5 ;
    	


    }
    public static final String SHAPE_TYPE = "type";

    public class SupportedShapes {
        public static final int CIRCLE = 0;
        public static final int DIAMOND = 1;
        public static final int LINE = 2;
        public static final int NSIDE = 3;
        public static final int OVAL = 4;
        public static final int RECTANGLE = 5;
        public static final int SQUARE = 6;
        public static final int TRIANGLE = 7;
    }


    public static final String PROPERTIES = "props";

    public class Properties {
        public static final String LINE_ANGLE = "lineAngle";
        public static final String RADIUS = "radius";
        public static final String SIDE_LENGTH = "SIDE_LENGTH";
        public static final String SIDE_COUNT = "SIDE_COUNT";
        public static final String ANGLE = "ANGLE";
        public static final String MAJOR_AXIS = "MAJOR_AXIS";
        public static final String MINOR_AXIS = "MINOR_AXIS";
        public static final String MAJOR_ANGLE = "MAJOR_ANGLE";
        public static final String MINOR_ANGLE = "MINOR_ANGLE";
        public static final String HEIGHT = "HEIGHT";
        public static final String WIDTH = "WIDTH";
        public static final String LENGTH = "LENGTH";
        public static final String SIDE_LENGTH_SQUARE = "SIDE_LENGTH_SQUARE";

    }


//	public static final String HEIGHT = "HEIGHT";
//	public static final String HEIGHT = "HEIGHT";

//	public static final String HEIGHT = "HEIGHT";

}
