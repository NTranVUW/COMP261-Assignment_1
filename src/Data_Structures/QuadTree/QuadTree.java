package Data_Structures.QuadTree;

import Data_Structures.Graph.Node;
import GUI.Location;
import GUI.GUI;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class QuadTree {
    private  GUI gui;
    private Rectangle2D boundary;
    private final int capacity = 4;

    private QuadTree northWest;
    private QuadTree southWest;
    private QuadTree southEast;
    private QuadTree northEast;
    private int depth;

    private final ArrayList<Node> nodes = new ArrayList<>();

    private QuadTree(double x, double y, double width, double height, int depth, GUI gui){
        this.boundary = new Rectangle2D.Double(x, y, width, height);
        this.depth = depth;
        this.gui = gui;
    }

    public static QuadTree createFrom(double x, double y, double width, double height, GUI gui){
        return new QuadTree(x, y, width, height, 0, gui);
    }

    public boolean insert(Node n, Location origin, double scale){
        Point2D point = n.getLocation().asPoint2D(origin, scale);
        //initial quadrants
        if (depth == 0){
            subdivide();
        }
        else if (!boundary.contains(point)){
            return false;
        }
        else if (nodes.size() < capacity){
            nodes.add(n);
            return true;
        } else if (northWest == null){
            subdivide();
        }
        else {
            if (northWest.insert(n, origin, scale)){ return true; }
            if (southWest.insert(n, origin, scale)){ return true; }
            if (southEast.insert(n, origin, scale)){ return true; }
            if (northEast.insert(n, origin, scale)){ return true; }
        }
        System.out.println("node at " + point.getX() + "x " + point.getY() + "y");
        return false;
    }

    private void subdivide(){
        double x = boundary.getX();
        double y = boundary.getY();
        double w = boundary.getWidth()/2;
        double h = boundary.getHeight()/2;

        int newDepth = this.depth+1;

        northWest = new QuadTree(x, y, w, h, newDepth, gui);
        southWest = new QuadTree(x, h, w, h, newDepth, gui);
        southEast = new QuadTree(w, h, w, h, newDepth, gui);
        northEast = new QuadTree(w, y, w, h, newDepth, gui);
    }

    public void printQuad(Location origin, double scale){
        System.out.printf("Quadrant with %d depth, %f x, %f y and width %f, height %f\n", depth, boundary.getX(), boundary.getY(), boundary.getWidth(), boundary.getHeight());
        System.out.printf("With %n nodes: \n", nodes.size());
        for (Node n : nodes){
            Point2D p = n.getLocation().asPoint2D(origin, scale);
            System.out.println(n.getNodeID() + " at " + p.getX() + "x, " + p.getY() + "y");
        }
        System.out.println();
        System.out.println();
        if (northWest == null){
            return;
        }
        northWest.printQuad(origin,scale);
        southWest.printQuad(origin, scale);
        southEast.printQuad(origin, scale);
        northEast.printQuad(origin, scale);
    }

    public void draw(Graphics2D g, Location origin, double scale){

        Color c = new Color(83, 92, 104);
        g.setColor(c);

        double x = boundary.getX();
        double y = boundary.getY();
        double width = boundary.getWidth();
        double height = boundary.getHeight();

        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        g.draw(rect);

        if (northWest == null){
            return;
        }
        northWest.draw(g, origin, scale);
        southWest.draw(g, origin, scale);
        southEast.draw(g, origin, scale);
        northEast.draw(g, origin, scale);
    }
}
