package RoadMap;

import RoadMap.Location;

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
}
