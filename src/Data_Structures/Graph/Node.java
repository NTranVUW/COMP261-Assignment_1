package Data_Structures.Graph;

import GUI.Drawing.ColorFactory;
import GUI.Drawing.Drawable;
import GUI.Location;
import GUI.GUI;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Node implements Drawable {
    private final int nodeID;
    private final ArrayList<Segment> inSegments = new ArrayList<Segment>();
    private final ArrayList<Segment> outSegments = new ArrayList<Segment>();
    private Location location;
    private boolean highlighted;

    private Node(int nodeID){
        this.nodeID = nodeID;
    }

    public static Node withID(int nodeID){ return new Node(nodeID); }

    public Node atLocation(Location loc){ this.location = loc; return this; }

    public int getNodeID(){ return this.nodeID; }

    public Location getLocation() { return this.location; }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public ArrayList<Segment> getInSegments() {
        return inSegments;
    }

    public ArrayList<Segment> getOutSegments() {
        return outSegments;
    }

    public void addInSegment(Segment s){
        inSegments.add(s);
    }

    public void addOutSegment(Segment s){
        outSegments.add(s);
    }

    public void draw(Graphics2D g, Location origin, double scale, GUI gui){
        Color c;
        if (highlighted){
            c = ColorFactory.getHighlightedNodeColor();
        } else { c = ColorFactory.getNodeColor(); }
        g.setColor(c);

        Point2D point = location.asPoint2D(origin, scale);
        double size;
        //only increase the node size up to a certain point so they don't get too big
        if (scale <= 7) {size = scale;} else {size = 7;}
        //if node is outside view then don't draw this node
        if ((point.getX() <= 0 || point.getX() >= gui.getWindowWidth())
                && (point.getY() <= 0 || point.getY() >= gui.getWindowHeight())){
            return;
        }
        Rectangle2D rect = new Rectangle2D.Double(point.getX()+(size/2), point.getY()+(size/2), size, size);
        g.fill(rect);
    }
}
