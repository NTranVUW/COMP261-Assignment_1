package Data_Structures.Graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import GUI.Drawing.ColorFactory;
import GUI.Drawing.Drawable;
import GUI.Location;
import GUI.GUI;

public class Segment implements Drawable {
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

     public Road getRoad() {
        return road;
    }

    public void draw(Graphics2D g, Location origin, double scale, GUI gui){
        Color c;
        if (road.isHighlighted()){
            c = ColorFactory.getHighlightedSegmentColor();
        } else { c = ColorFactory.getSegmentColor(); }
        g.setColor(c);

        for (int i = 0; i < coords.size(); i++){
            Point2D from;
            Point2D to;
            //if first iteration draw from this coordinate to next coordinate
            if (i == 0){
                from = coords.get(i).asPoint2D(origin, scale);
                to = coords.get(i+1).asPoint2D(origin, scale);
            //if not first iteration draw from previous coordinate to this coordinate
            } else {
                from = coords.get(i-1).asPoint2D(origin, scale);
                to = coords.get(i).asPoint2D(origin, scale);
            }
            //if segments are outside view then don't draw the segments
            if ((from.getX() <= 0 || from.getX() >= gui.getWindowWidth())
                    && (to.getX() <= 0 || to.getX() >= gui.getWindowWidth())
                    && (from.getY() <= 0 || from.getY() >= gui.getWindowHeight())
                    && (to.getY() <= 0 || to.getY() >= gui.getWindowHeight())){
                return;
            }
            Line2D line = new Line2D.Double(from.getX(), from.getY(), to.getX(), to.getY());
            g.draw(line);
        }
    }
}
