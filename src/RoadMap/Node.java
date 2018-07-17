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
        this.location = loc; return this;
    }

    public int getNodeID(){
        return this.nodeID;
    }

    public void draw(Graphics g){
        g.drawOval((int)location.x, (int)location.y, 10, 10);
    }
}
