package GUI.Parsing;

import GUI.Location;
import Data_Structures.Graph.Node;
import Data_Structures.Graph.RoadMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NodeParser {
    private NodeParser(){}

    public static void parse(File nodes, RoadMap roadMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(nodes))){
            String line;
            while((line = reader.readLine()) != null){
                String[] split = line.split("\t");
                int nodeID = Integer.parseInt(split[0]);
                double lat = Double.parseDouble(split[1]);
                double lon = Double.parseDouble(split[2]);
                Location location = Location.newFromLatLon(lat,lon);
                Node node = Node.withID(nodeID)
                                .atLocation(location);
                roadMap.addNode(node);
            }
        }
    }
}

