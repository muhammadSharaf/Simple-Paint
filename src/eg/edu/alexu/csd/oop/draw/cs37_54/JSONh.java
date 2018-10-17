package eg.edu.alexu.csd.oop.draw.cs37_54;

import java.util.HashMap;
import java.util.Stack;

import eg.edu.alexu.csd.oop.draw.JSONi;

public class JSONh implements JSONi {
    private Node root;
    private HashMap<String, Node> m;
    private StringBuilder gj; //given json
    private StringBuilder j; //generated json

    /**
     * constructor
     *
     * @param objectName put null if reading
     */
    public JSONh(String objectName) {
        j = new StringBuilder();
        m = new HashMap<String, Node>();
        root = new Node(null, objectName, null, Node.OBJECT_I);
        gj = new StringBuilder();
    }


    @Override
    public void constructObject(String parentKey, String key) {
        if (m.get(key) != null) {
            throw null;
        }
        // TODO Auto-generated method stub
        if (m.get(parentKey).type == Node.ELEMENT_I) {
            throw null;//element cant be a parent node
        } else if (parentKey == null) {
            m.put(key, new Node(root, key, null, Node.OBJECT_I));
        } else {
            m.put(key, new Node(m.get(parentKey), key, null, Node.OBJECT_I));
        }
    }

    @Override
    public void addElement(String parentKey, String key, Object value) {
        if (m.get(key) != null && m.get(key).parent == m.get(parentKey)) {
            throw null;
        }
        // TODO Auto-generated method stub
        if (parentKey == null) {
            m.put(key, new Node(root, key, value, Node.ELEMENT_I));
        } else if (m.get(parentKey).type == Node.ELEMENT_I) {
            throw null;//element cant be a parent node
        } else {
            m.put(key, new Node(m.get(parentKey), key, value, Node.ELEMENT_I));
        }

    }

    @Override
    public void constructArray(String parentKey, String key) {
        if (m.get(key) != null && m.get(key).parent == m.get(parentKey)) {
            throw null;
        }
        // TODO Auto-generated method stub
        if (parentKey == null) {
            m.put(key, new Node(root, key, null, Node.ARRAY_I));
        } else if (m.get(parentKey).type == Node.ELEMENT_I) {
            throw null;//element cant be a parent node
        } else {
            m.put(key, new Node(m.get(parentKey), key, null, Node.ARRAY_I));
        }

    }

    @Override
    public String getJSON() {
        // TODO Auto-generated method stub
        j.append("{");
        Travel(root);
        j.append("}");
        return j.toString();
    }

    public void reset(String objectName) {
        j = new StringBuilder();
        m = new HashMap<String, Node>();
        root = new Node(null, null, null, Node.OBJECT_I);
        root = new Node(null, objectName, null, Node.OBJECT_I);
        gj = new StringBuilder();

    }

    private void Travel(Node cr) {//current root
        //todoleft
        switch (cr.type) {
            case Node.OBJECT_I:
                j.append("\"" + cr.key + "\"" + ":{");
                break;
            case Node.ARRAY_I:
                j.append("\"" + cr.key + "\"" + ":[");
                break;
            case Node.ELEMENT_I:
                j.append("\"" + cr.key + "\"" + ":\"" + cr.value + "\"");
                return;
            default:
                break;
        }
        //to do left

        for (int i = 0; i < cr.children.size(); i++) {
            if (cr.type == Node.ARRAY_I) {
                j.append("{");
            }
            Travel(cr.children.get(i));
            if (cr.type == Node.ARRAY_I) {
                j.append("}");
            }
            if (i != cr.children.size() - 1) {
                j.append(",");
            }
            //down

        }

        //todo right
        switch (cr.type) {
            case Node.OBJECT_I:
                j.append("}");
                break;
            case Node.ARRAY_I:
                j.append("]");
                break;
            default:
                break;
        }
        //todoright
    }


    @Override
    public void putJSON(String j) {
        // TODO Auto-generated method stub
        gj = new StringBuilder();
        gj.append(j);

    }


    private int after = 0;

    @Override
    public String getElement(String parentKey, String key) {
        int parentIndex = gj.indexOf(parentKey, after); //index of parent
        int index = gj.indexOf(key, parentIndex);   //index of elem
        int resultIndex = index + key.length() + 3;    //"jk":"jkl"
        int resultIndex2 = 0;
        for (int i = resultIndex; i < gj.length(); i++) {
            if (gj.charAt(i) == '\"') {
                resultIndex2 = i;
                break;
            }
        }
        if (parentIndex < 0 || resultIndex2 == 0) {
            return null;
        }
        return gj.substring(resultIndex, resultIndex2);
    }


    @Override
    public void startSearchAfter(String parentkey, String key) {
        int parentIndex = gj.indexOf(parentkey, after); //index of parent
        int index = gj.indexOf(key, parentIndex);   //index of elem
        after = index + key.length();

    }


}
