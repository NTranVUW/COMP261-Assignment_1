package RoadMap;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RoadMap {

    private HashMap<Integer, Node> nodes = new HashMap();

    @NotNull
    public static RoadMap newInstance(){
        return new RoadMap();
    }

    public void addNode(Node n){
        nodes.put(n.getNodeID(), n);
    }
}
