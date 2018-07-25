package RoadMap;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import RoadMap.Location;

public class Segment {
    private Road road;
    private Node toNode;
    private Node fromNode;
    private double length;
    private ArrayList<Location> coords;

    private Segment(ArrayList<Location> coords){
        this.coords = coords;
    }

     public static Segment withCoords(ArrayList<Location> coords){
         return new Segment(coords);
     }

     public Segment onRoad(Road road) { this.road = road; return this; }

     public Segment withLength(double length) { this.length = length; return this; }

     public Segment toNode(Node node){ this.toNode =  node; return this; }

     public Segment fromNode(Node node){ this.fromNode = node; return this; }

     public void draw(Graphics2D g, Location origin, double scale){
        if (road.isHighlighted()){
            g.setColor(new Color(48, 51, 107));
        } else { g.setColor(new Color(186, 220, 88)); }

        for (int i = 0; i < coords.size(); i++){
            Point2D from;
            Point2D to;
            if (i == 0){
                from = coords.get(i).asPoint2D(origin, scale);
                to = coords.get(i+1).asPoint2D(origin, scale);
            } else {
                from = coords.get(i-1).asPoint2D(origin, scale);
                to = coords.get(i).asPoint2D(origin, scale);
            }
            Line2D line = new Line2D.Double(from.getX(), from.getY(), to.getX(), to.getY());
            g.draw(line);
        }
     }

    public Road getRoad() {
        return road;
    }
}
