package RoadMap;

import java.awt.*;
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

     public void draw(Graphics g, Location origin, double scale){
        if (road.isHighlighted()){
            g.setColor(new Color(249, 202, 36));
        } else { g.setColor(new Color(224, 86, 253)); }

        for (int i = 0; i < coords.size(); i++){
            Point from;
            Point to;
            if (i == 0){
                from = coords.get(i).asPoint(origin, scale);
                to = coords.get(i+1).asPoint(origin, scale);
            } else {
                from = coords.get(i-1).asPoint(origin, scale);
                to = coords.get(i).asPoint(origin, scale);
            }
            g.drawLine(from.x, from.y, to.x, to.y);
        }
     }

    public Road getRoad() {
        return road;
    }
}
