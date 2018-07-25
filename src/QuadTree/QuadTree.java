package QuadTree;

import RoadMap.Location;
import RoadMap.Node;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

public class QuadTree {

    private Rectangle2D boundary;
    private final int capacity = 1;

    private QuadTree northWest;
    private QuadTree southWest;
    private QuadTree southEast;
    private QuadTree northEast;

    //private final HashMap<Point2D, Node> points = new HashMap<>();
    private final ArrayList<Point2D> points = new ArrayList<>();
    private int count;


    private QuadTree(double x, double y, double width, double height){
        this.boundary = new Rectangle2D.Double(x, y, width, height);
        this.count = 0;
    }

    public static QuadTree createFrom(double x, double y, double width, double height){
        return new QuadTree(x, y, width, height);
    }

    public boolean insert(Point2D p){
        System.out.println("capacity: " + points.size());
        if (!boundary.contains(p)){
            System.out.println("false");
            return false;
        }
        if (points.size() < capacity){
            //points.put(p, n);
            points.add(p);
            //count++;
            return true;
        }
        if (northWest == null){ System.out.println("divide"); subdivide(); }

        if (northWest.insert(p)){ return true; }
        if (southWest.insert(p)){ return true; }
        if (southEast.insert(p)){ return true; }
        if (northEast.insert(p)){ return true; }

        return false;
    }

    private void subdivide(){
        double x = boundary.getX();
        double y = boundary.getY();
        double w = boundary.getWidth()/2;
        double h = boundary.getHeight()/2;

        northWest = new QuadTree(x, y, w, h);
        southWest = new QuadTree(x, h, w, h);
        southEast = new QuadTree(w, h, w, h);
        northEast = new QuadTree(w, y, w, h);
    }

    public void draw(Graphics2D g, Location origin, double scale){

        Color c = new Color(83, 92, 104);
        g.setColor(c);

        double x = boundary.getX();
        double y = boundary.getY();
        double width = boundary.getWidth();
        double height = boundary.getHeight();

        Point2D startPoint = new Point2D.Double(x, y);
        Point2D widthPoint = new Point2D.Double(x+width, y);
        Point2D heightPoint = new Point2D.Double(x, y+height);

        Point2D newStartPoint = Location.newFromPoint2D(startPoint, origin, scale).asPoint(origin, scale);
        Point2D newWidthPoint = Location.newFromPoint2D(widthPoint, origin, scale).asPoint(origin, scale);
        Point2D newHeightPoint = Location.newFromPoint2D(heightPoint, origin, scale).asPoint(origin, scale);

        double newX = newStartPoint.getX();
        double newY = newStartPoint.getY();
        double newWidth = newWidthPoint.getX() - newStartPoint.getX();
        double newHeight = newHeightPoint.getY() - newStartPoint.getY();

        Rectangle2D rect = new Rectangle2D.Double(newX, newY, newWidth, newHeight);
        g.draw(rect);
        System.out.println(rect.getX());

        if (northWest == null){
            return;
        }
        northWest.draw(g, origin, scale);
        southWest.draw(g, origin, scale);
        southEast.draw(g, origin, scale);
        northEast.draw(g, origin, scale);
    }
}
