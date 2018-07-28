package GUI.Parsing;

import GUI.Location;
import Data_Structures.Graph.Node;
import Data_Structures.Graph.Road;
import Data_Structures.Graph.RoadMap;
import Data_Structures.Graph.Segment;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class SegmentParser {
    private SegmentParser(){}

    public static void parse(BufferedReader reader, RoadMap roadMap) throws IOException {
        String line;
        reader.readLine(); //skips over first line
        while ((line = reader.readLine()) != null){
            String[] split = line.split("\t");

            int roadID = Integer.parseInt(split[0]);
            Road road = roadMap.getRoads().get(roadID);
            double length = Double.parseDouble(split[1]);
            int nodeID = Integer.parseInt(split[2]);
            Node toNode = roadMap.getNodes().get(nodeID);
            nodeID = Integer.parseInt(split[3]);
            Node fromNode = roadMap.getNodes().get(nodeID);

            ArrayList<Location> coords = new ArrayList<Location>();
            //reads coordinates from index 4 till end of line
            for (int i = 4; i < split.length; i+=2){
                double lat = Double.parseDouble(split[i]);
                double lon = Double.parseDouble(split[i+1]);
                Location location = Location.newFromLatLon(lat, lon);
                coords.add(location);
            }

            Segment s = Segment.withCoords(coords)
                               .onRoad(road)
                               .withLength(length)
                               .toNode(toNode)
                               .fromNode(fromNode);

            roadMap.addSegment(s);
            road.addSegment(s);
            toNode.addInSegment(s);
            fromNode.addOutSegment(s);
        }
    }
}
