package RoadMap;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public class RoadMap {

    private HashMap<Integer, Node> nodes = new HashMap();
    private HashMap<Integer, Road> roads = new HashMap();
    private HashSet<Segment> segments = new HashSet<Segment>();

    @NotNull
    public static RoadMap newInstance(){
        return new RoadMap();
    }

    public void addNode(Node n){
        nodes.put(n.getNodeID(), n);
    }

    public void addRoad(Road r){ roads.put(r.getRoadID(), r); }

    public void addSegment(Segment s) { segments.add(s); }

    public HashMap<Integer, Node> getNodes(){
        return this.nodes;
    }

    public HashMap<Integer, Road> getRoads() { return this.roads; }

    public HashSet<Segment> getSegments() { return this.segments; }
}
