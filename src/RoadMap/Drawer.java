package RoadMap;

import RoadMap.Node;
import QuadTree.QuadTree;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.awt.*;

public class Drawer {

    private Graphics2D g;

    private Drawer(){}

    public static Drawer create(){
        return new Drawer();
    }

    public Drawer drawTo(Graphics2D g){
        this.g = g;
        return this;
    }

    public Drawer drawNodes(HashMap<Integer, Node> nodes, Location origin, double scale){
        for (Map.Entry m : nodes.entrySet()){
            Node node = (Node) m.getValue();

            node.draw(g, origin, scale);
        }
        return this;
    }
    public Drawer drawSegments(HashSet<Segment> segments, Location origin, double scale){
        for (Segment s : segments){
            s.draw(g, origin, scale);
        }
        return this;
    }

    public Drawer drawQuad(QuadTree quad, Location origin, double scale){
        quad.draw(g, origin, scale);
        return this;
    }
}
