package eg.edu.alexu.csd.oop.draw.cs37_54;

import eg.edu.alexu.csd.oop.draw.*;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.shapes.*;
//import eg.edu.alexu.csd.oop.draw.shapes.Rectangle;
import javafx.scene.canvas.GraphicsContext;

import org.omg.Messaging.SyncScopeHelper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@XmlRootElement
public class MyDrawingEngine implements DrawingEngine {
    /**
     * max history cap
     */
    private final int historyCapacity = 20;
    private final int maxCurrentShapes = 1000;
    private static MyDrawingEngine instance = null;

    public static MyDrawingEngine getInstance() {
        if (instance == null) {
            instance = new MyDrawingEngine();
            supportedShapes = new LinkedList<Class<? extends Shape>>();
            //supportedShapes.add(Rectangle.class);
            supportedShapes.add(Line.class);
            supportedShapes.add(Circle.class);
            supportedShapes.add(Diamond.class);
            supportedShapes.add(NSide.class);
            supportedShapes.add(Oval.class);
            supportedShapes.add(Square.class);
            supportedShapes.add(Triangle.class);
        }
        return instance;
    }

    /**
     * a linked list -- sub of list -- contains all the classes that extends shape
     */
    //@XmlElement
    private static LinkedList<Class<? extends Shape>> supportedShapes;

    /**
     * the current shapes
     */
    // @XmlElement
    private LinkedList<Shape> currentShapes = new LinkedList<>();

    /**
     * history index pointer initialized with -1
     * to prevent calling undo and redo without existing history
     */
    //TODO @XmlElement
    private int historyOperationIndex = -1;

    /**
     * Stores operations history
     */
    //TODO @XmlElement
    private LinkedList<HistoryElement> history = new LinkedList<>();


    /**
     * adding a shape by the user
     *
     * @param s
     */
    public void addShapeToSupported(Class<? extends MyShape> s) {
        supportedShapes.add(s);
    }

    /**
     * loops through all the current shapes and
     * recalls the drawing method with the new canvas as pram
     */

    private GraphicsContext gc;

    public void gcSetter(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void refresh(Graphics canvas) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (Shape shape : this.currentShapes) {
            shape.draw(null);
        }
    }

    @Override
    public void addShape(Shape shape) {
        currentShapes.add(shape);
        addToHistory(shape, null, "add", 0);
        //TODO refresh class
    }

    @Override
    public void removeShape(Shape shape) {
        int index = currentShapes.indexOf(shape);
        currentShapes.remove(shape);
        addToHistory(shape, null, "remove", index);
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        //TODO add condition if old shape doesn't exist (online tester)!!
        int oldShapeIndex = currentShapes.indexOf(oldShape);
        currentShapes.set(oldShapeIndex, newShape);
        addToHistory(newShape, oldShape, "update", 0);
    }

    @Override
    public Shape[] getShapes() {
        Shape[] shapes = new Shape[currentShapes.size()];
        for (int i = 0; i < currentShapes.size(); i++) {
            shapes[i] = currentShapes.get(i);
        }
        return shapes;
    }


    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return this.supportedShapes;
    }

    @Override
    public void undo() {
        if (historyOperationIndex >= 0) {
            String operation = history.get(historyOperationIndex).getOperation();
            Shape currentShape = history.get(historyOperationIndex).getCurrentShape();

            if (operation.equals("add")) {
                currentShapes.remove(currentShape);
            } else if (operation.equals("remove")) {
                int index = history.get(historyOperationIndex).getIndex();
                currentShapes.add(index, currentShape);
            } else { //update case
                int newShapeIndex = currentShapes.indexOf(currentShape);
                Shape oldShape = history.get(historyOperationIndex).getOldShape();
                currentShapes.set(newShapeIndex, oldShape);
            }

            historyOperationIndex--;
        }
    }

    @Override
    public void redo() {
        historyOperationIndex++;
        if (historyOperationIndex < history.size() &&
                historyOperationIndex <= historyCapacity && !history.isEmpty()) {
            String operation = history.get(historyOperationIndex).getOperation();
            Shape currentShape = history.get(historyOperationIndex).getCurrentShape();

            if (operation.equals("add")) {
                currentShapes.add(currentShape);
            } else if (operation.equals("remove")) {
                currentShapes.remove(currentShape);
            } else { //update case
                int oldShapeIndex = currentShapes.indexOf(history.get(historyOperationIndex).getOldShape());
                Shape newShape = currentShape;
                currentShapes.set(oldShapeIndex, newShape);
            }
        } else {
            historyOperationIndex--;
        }
    }

    /**
     * making history xD
     *
     * @param shape     shape that an operation was done to it
     * @param oldShape  only in case of update shapes (stores old shape)
     * @param operation specify the operation done on the shape
     */
    private void addToHistory(Shape shape, Shape oldShape, String operation, int index) {
        HistoryElement historyElement = new HistoryElement(shape, oldShape, operation);
        if (operation.equals("remove")) {
            historyElement.setIndex(index);
        }
        //if history was full and user did some redo and added new shape so
        //remove operations after current 'historyOperationIndex'
        // as the user don't want them
        for (int i = history.size() - 1; i > historyOperationIndex; i--) {
            history.removeLast();
        }
        //necessary if user didn't commit any redo and history is 20
        if (history.size() == historyCapacity) {
            history.removeFirst();
        }
        history.addLast(historyElement);
        //after each add to history current index will be the last operation
        historyOperationIndex = history.size() - 1;
    }

    public void plugin(String path, Class mainclass) throws Exception {
        //Note that this method assume that the path is .jar and its name is the same name as the class you want from inside
        File f = new File(path);
        StringBuilder str = new StringBuilder();
        str.append(f.getName());
        String nameA = str.substring(0, str.indexOf("."));
        URL[] urlarr = {new URL("file:\\" + path)};
        URLClassLoader child = new URLClassLoader(urlarr, this.getClass().getClassLoader());
        Class<? extends MyShape> clazz = (Class<? extends MyShape>) Class.forName("eg.edu.alexu.csd.oop.draw.shapes." + nameA, true, child);

        this.addShapeToSupported(clazz);
    }

    public int getHistoryState() {
        if (this.historyOperationIndex == history.size() - 1) {
            return 1;
        } else if (this.historyOperationIndex == -1) {
            return -1;
        }
        return 0;
    }


    /**
     * save in the the "path" as JSON file
     *
     * @param path
     */
    private void saveJSON(String path) {
        JSONh h = new JSONh(Contact.MAIN_OBJECT);
        h.constructArray(null, Contact.CURRENTSHAPES);
        for (int i = 0; i < currentShapes.size(); i++) {
            MyShape m = (MyShape) currentShapes.get(i);
            h.constructObject(Contact.CURRENTSHAPES, Contact.CURRENTSHAPES + i);
            h.addElement(Contact.CURRENTSHAPES + i, Contact.THICKNESS, m.getThickness());
            h.addElement(Contact.CURRENTSHAPES + i, Contact.COLOR, m.getColor().getRGB());            // saving col rgb
            h.addElement(Contact.CURRENTSHAPES + i, Contact.FILLCOLOR, m.getFillColor().getRGB());
            h.constructArray(Contact.CURRENTSHAPES + i, Contact.POSITION);                                // pos is array of two x and y
            h.addElement(Contact.POSITION, Contact.X, m.getPosition().getX());
            h.addElement(Contact.POSITION, Contact.Y, m.getPosition().getY());
            h.constructArray(Contact.CURRENTSHAPES + i, Contact.POSITION + "2");                                // pos is array of two x and y
            h.addElement(Contact.POSITION + "2", Contact.X + "2", m.getEndPoint().getX());
            h.addElement(Contact.POSITION + "2", Contact.Y + "2", m.getEndPoint().getY());
            h.constructArray(Contact.CURRENTSHAPES + i, Contact.PROPERTIES);                                // props is array
            for (Entry<String, Double> entry : m.getProperties().entrySet())            // loooping in the map props
            {
                h.addElement(Contact.PROPERTIES, entry.getKey(), entry.getValue());
            }
        }

        try {
            File file = new File(path);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(h.getJSON());
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            throw null; //RUNTIME EXCEPTION
        }
    }

    private void loadJSON(String path) {
        JSONh h = new JSONh(Contact.MAIN_OBJECT);
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String s = new String();
            s = br.readLine();
            while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
        } catch (Exception e) {
            throw null; //RUNTIME EXCEPTION
        }
        h.putJSON(sb.toString());
        currentShapes = new LinkedList<>();
        for (int i = 0; i < this.maxCurrentShapes; i++) {
            String th = h.getElement(Contact.CURRENTSHAPES + i, Contact.THICKNESS);
            String c = h.getElement(Contact.CURRENTSHAPES + i, Contact.COLOR);
            String fc = h.getElement(Contact.CURRENTSHAPES + i, Contact.FILLCOLOR);
            String px = h.getElement(Contact.POSITION, Contact.X);
            String py = h.getElement(Contact.POSITION, Contact.Y);
            String px2 = h.getElement(Contact.POSITION + "2", Contact.X + "2");
            String py2 = h.getElement(Contact.POSITION + "2", Contact.Y + "2");
            String shapeType = h.getElement(Contact.PROPERTIES, Contact.SHAPE_TYPE);
            if (th == null || c == null || fc == null || px == null || py == null || shapeType == null) {
                break;
            }
            switch ((int) Double.parseDouble(shapeType)) {
                case Contact.SupportedShapes.CIRCLE:
                    String radius = h.getElement(Contact.PROPERTIES, Contact.Properties.RADIUS);
                    currentShapes.add(new Circle(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th),
                                    Double.parseDouble(radius)
                            )
                    );
                    break;
                case Contact.SupportedShapes.DIAMOND:
                    String sideLen = h.getElement(Contact.PROPERTIES, Contact.Properties.SIDE_LENGTH);
                    String Maa = h.getElement(Contact.PROPERTIES, Contact.Properties.MAJOR_ANGLE);
                    String Mia = h.getElement(Contact.PROPERTIES, Contact.Properties.MINOR_ANGLE);
                    currentShapes.add(new Diamond(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th),
                                    Double.parseDouble(sideLen),
                                    Double.parseDouble(Maa)
                            )
                    );
                    break;
                case Contact.SupportedShapes.LINE:
                    String len = h.getElement(Contact.PROPERTIES, Contact.Properties.LENGTH);
                    currentShapes.add(new Line(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th),
                                    Double.parseDouble(len)
                            )
                    );
                    break;
                case Contact.SupportedShapes.NSIDE:
                    String sidelen = h.getElement(Contact.PROPERTIES, Contact.Properties.SIDE_LENGTH);
                    String n = h.getElement(Contact.PROPERTIES, Contact.Properties.SIDE_COUNT);
                    currentShapes.add(new NSide(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th),
                                    (int) Double.parseDouble(n)
                                    ,
                                    (double) Double.parseDouble(sidelen)
                            )
                    );
                    break;
                case Contact.SupportedShapes.OVAL:
                    currentShapes.add(new Oval(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th)
                            )
                    );

                    break;
//                case Contact.SupportedShapes.RECTANGLE:
//                    currentShapes.add(new Rectangle(
//                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
//                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
//                                    new Color(Integer.parseInt(c)),
//                                    new Color(Integer.parseInt(fc)),
//                                    Double.valueOf(th)
//                            )
//                    );
//                    break;
                case Contact.SupportedShapes.SQUARE:
                    String Sidee = h.getElement(Contact.PROPERTIES, Contact.Properties.SIDE_LENGTH_SQUARE);
                    currentShapes.add(new Square(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th),
                                    Double.parseDouble(Sidee)
                            )
                    );
                    break;
                case Contact.SupportedShapes.TRIANGLE:
                    currentShapes.add(new Triangle(
                                    new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)),
                                    new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)),
                                    new Color(Integer.parseInt(c)),
                                    new Color(Integer.parseInt(fc)),
                                    Double.valueOf(th)
                            )
                    );
                    break;
                default:
                    MyShape unknown = new MyShape();
                    unknown.setColor(new Color(Integer.parseInt(c)));
                    unknown.setFillColor(new Color(Integer.parseInt(fc)));
                    unknown.setEndPoint(new Point((int) Double.parseDouble(px2), (int) Double.parseDouble(py2)));
                    unknown.setPosition(new Point((int) Double.parseDouble(px), (int) Double.parseDouble(py)));
                    unknown.setColor(new Color(Integer.parseInt(c)));
                    unknown.setThickness(Double.valueOf(th));

                    currentShapes.add(unknown);
                    break;
            }


            h.startSearchAfter(Contact.CURRENTSHAPES + i, Contact.PROPERTIES);//to search after the last elem -- avoid pos error
        }


    }

    public void loadXMLn(String path) {
        try {

            XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
            this.currentShapes = (LinkedList<Shape>) decode.readObject();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException("abdooooooooooo9"
            );

        }


    }

    private Document convertstrtodoc(String s) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(s)));

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("abdooooooooooo9"
            );

        }
    }

    public void saveXMLn(String path) {
        try (FileWriter newfile = new FileWriter(path)) {

            XMLEncoder pen = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));

            pen.writeObject(this.currentShapes);
            pen.close();

            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            sb.replace(sb.indexOf("UTF-8"), sb.indexOf("UTF-8") + 5, "ISO-8859-1");
            Document doc = this.convertstrtodoc(sb.toString());
            doc.setXmlStandalone(true);
            DOMSource docSource = new DOMSource(doc);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            StreamResult s = new StreamResult(new File(path));
            transformer.transform(docSource, s);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException("abdooooooooooo9");

        }


    }


    /**
     * save in the the "path" as XML file
     *
     * @param path
     */
    private void saveXML(String path) {
        try {

            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //JAXBElement<MyDrawingEngine> jaxbElement = new JAXBElement<MyDrawingEngine>(new QName(null, "MyDrawingEngine"), MyDrawingEngine.class, this);

        } catch (JAXBException e) {
            e.printStackTrace();
            throw null; //RUNTIME EXCEPTION
        }


    }

    /**
     * used in both load XML and JSON - take  the instance t - taken from file , clone it to this
     *
     * @param t
     */
    private void copyToThisInstance(MyDrawingEngine t) {
        this.currentShapes = t.currentShapes;
        //  this.historyOperationIndex = t.historyOperationIndex;
        //  this.supportedShapes = t.supportedShapes;
        //TODO if done with history
    }

    private void loadXML(String path) {
        try {

            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(MyDrawingEngine.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            MyDrawingEngine temp = (MyDrawingEngine) jaxbUnmarshaller.unmarshal(file);
            copyToThisInstance(temp);

        } catch (Exception e) {
            // e.printStackTrace();
            throw null;
        }
    }


    @Override
    public void save(String path) {

        String[] pathExtention = path.split("\\.");
        if (pathExtention.length == 2) {

            //int pathIndex =0;
            int extentionIndex = 1;
            if (pathExtention[extentionIndex].compareToIgnoreCase(Contact.XML) == 0) {
                saveXMLn(path);
            } else if (pathExtention[extentionIndex].compareToIgnoreCase(Contact.JSON) == 0) {
                saveJSON(path);
            } else {
                throw null;
            }
        }
    }

    @Override
    public void load(String path) {
        String[] pathExtention = path.split("\\.");
        if (pathExtention.length == 2) {
            //int pathIndex =0;
            int extentionIndex = 1;
            if (pathExtention[extentionIndex].compareToIgnoreCase(Contact.XML) == 0) {
                loadXMLn(path);
            } else if (pathExtention[extentionIndex].compareToIgnoreCase(Contact.JSON) == 0) {
                loadJSON(path);
            } else {
                throw null;
            }
        }
    }
}
