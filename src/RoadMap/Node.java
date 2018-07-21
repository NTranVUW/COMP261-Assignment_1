package RoadMap;

import RoadMap.Location;

import java.awt.*;
import java.util.ArrayList;

public class Node {
    private final int nodeID;
    private final ArrayList<Segment> inSegments = new ArrayList<Segment>();
    private final ArrayList<Segment> outSegments = new ArrayList<Segment>();
    private Location location;
    private boolean highlighted;

    private Node(int nodeID){
        this.nodeID = nodeID;
    }

    public static Node withID(int nodeID){ return new Node(nodeID); }

    public Node atLocation(Location loc){
        this.location = loc;
        return this;
    }

    public int getNodeID(){
        return this.nodeID;
    }
    public Location getLocation() { return this.location; }

    public void draw(Graphics g, Location origin, double scale){
        if (highlighted){
            g.setColor(new Color( 19, 15, 64));
        } else {g.setColor(new Color( 106, 176, 76));}
        Point point = location.asPoint(origin, scale);
        int size;
        if (scale <= 7) {size = (int) scale;} else {size = 7;}
        g.fillRect(point.x, point.y, size, size);
    }

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
}
