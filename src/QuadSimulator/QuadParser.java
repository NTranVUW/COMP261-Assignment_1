package QuadSimulator;

import Data_Structures.Graph.Node;
import Data_Structures.QuadTree.QuadTree;
import GUI.Drawing.MapDrawer;
import GUI.Location;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

public class QuadParser {
    private static Collection<Node> nodes;
    private static QuadTree quad;
    private static double windowWidth;
    private static double windowHeight;
    private static Location origin;
    private static double scale;

    private QuadParser(Collection<Node> nodes, QuadSim quadSim){
        QuadParser.nodes = nodes;
        QuadParser.windowWidth = quadSim.getWindowWidth();
        QuadParser.windowHeight = quadSim.getWindowHeight();
        QuadParser.origin = quadSim.getOrigin();
        QuadParser.scale = quadSim.getScale();
    }

    public static QuadParser create(Collection<Node> nodes, QuadSim quadSim){
        Collection<Node> nodeList = nodes;
        return new QuadParser(nodeList, quadSim);
    }

    public static void parse(MapDrawer drawer){
        quad = QuadTree.createFrom(0, 0, windowWidth, windowHeight);
        for (Node node : nodes){
            Point2D point = node.getLocation().asPoint2D(origin, scale);
            if ((point.getX() >= 0 || point.getX() <= windowWidth)
                    && (point.getY() >= 0 || point.getY() <= windowHeight)){
                return;
            }
            quad.insert(node.getLocation().asPoint(origin, scale));
            drawer.drawQuad(quad, origin, scale);
        }
    }
}
