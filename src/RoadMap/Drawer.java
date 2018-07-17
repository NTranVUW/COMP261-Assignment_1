package RoadMap;

import RoadMap.Node;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;

public class Drawer {

    Graphics g;

    private Drawer(Graphics g){
        this.g = g;
    }

    public static Drawer drawTo(Graphics g){
        return new Drawer(g);
    }

    public Drawer drawNodes(HashMap<Integer, Node> nodes){
        for (Map.Entry m : nodes.entrySet()){
            Node node = (Node) m.getValue();
            node.draw(g);
        }
        return this;
    }

}
