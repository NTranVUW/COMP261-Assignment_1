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
        System.out.println(1);
        g.setColor(new Color(224, 86, 253));
        for (int i = 0; i < coords.size(); i++){
            if (i == 0){
                Point from = coords.get(i).asPoint(origin, scale);
                Point to = coords.get(i+1).asPoint(origin, scale);
                g.drawLine(from.x, from.y, to.x, to.y);
            } else {
                Point from = coords.get(i-1).asPoint(origin, scale);
                Point to = coords.get(i).asPoint(origin, scale);
                g.drawLine(from.x, from.y, to.x, to.y);
            }
        }
     }
}
