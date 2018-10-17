package eg.edu.alexu.csd.oop.draw;
/**
 * convert from an object to json
 * object - array - key,value
 * parent key = null if the object have no parent
 * @author ABDO
 *
 */
public interface JSONi {
public void constructObject(String parentKey ,String key );
public void addElement(String parentKey,String key ,Object value);
public void constructArray(String parentKey,String key);
public String getJSON();
public void reset(String objectName);

public void putJSON(String j);
public Object getElement(String parentKey,String key );
public void startSearchAfter(String parentkey, String key);

}
