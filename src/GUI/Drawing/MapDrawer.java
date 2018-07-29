package GUI.Drawing;

import GUI.Location;
import Data_Structures.QuadTree.QuadTree;
import Data_Structures.Graph.Node;
import Data_Structures.Graph.Segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.awt.*;

public class MapDrawer {

    private Graphics2D g;

    private MapDrawer(){}

    public static MapDrawer create(){
        return new MapDrawer();
    }

    public MapDrawer drawTo(Graphics2D g){
        this.g = g;
        return this;
    }

    public MapDrawer drawNodes(HashMap<Integer, Node> nodes, Location origin, double scale){
        for (Map.Entry m : nodes.entrySet()){
            Node node = (Node) m.getValue();
            node.draw(g, origin, scale);
        }
        return this;
    }
    public MapDrawer drawSegments(HashSet<Segment> segments, Location origin, double scale){
        for (Segment s : segments){
            s.draw(g, origin, scale);
        }
        return this;
    }

    public MapDrawer drawQuad(QuadTree quad, Location origin, double scale){
        quad.draw(g, origin, scale);
        return this;
    }

    public MapDrawer drawPolygons(ArrayList<GUI.Drawing.Polygon> polygons, Location origin, double scale){
        for (Polygon p : polygons){
            int endLevel = p.getEndLevel();
            //uses a boolean to determine whether or not the polygon gets drawn based on its end level
            boolean draw = false;
            switch(endLevel){
                case 2:
                    if (scale > 5){
                        draw = true;
                    }
                case 1:
                    if (scale > 10){
                        draw = true;
                    }
                case 3:
                    if (scale > 0){
                        draw = true;
                    }
            }
                if (draw){
                    p.draw(g, origin, scale);
                }
            }
        return this;
    }
}
