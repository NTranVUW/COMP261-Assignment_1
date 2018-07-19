package RoadMap;

import RoadMap.Location;

import java.awt.*;

public class Node {
    private int nodeID;
    private Location location;

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
        g.setColor(new Color( 48, 51, 107));
        Point point = location.asPoint(origin, scale);
        g.fillOval((int)point.x, (int)point.y, 10, 10);
    }
}
