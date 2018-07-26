package RoadMap;

import QuadTree.QuadTree;
import Trie.Trie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Loader {
    private final RoadMap roadMap;
    private final File nodeFile;
    private final File roadFile;
    private final File segmentFile;
    private final File polygonFile;

    private int count = 0;

    public static class Builder {
        private final RoadMap roadMap;

        private File nodeFile = null;
        private File roadFile = null;
        private File segmentFile = null;
        private File polygonFile = null;

        public Builder(RoadMap roadMap){this.roadMap = roadMap;}

        public Builder nodeFile(File file){this.nodeFile = file; return this;}
        public Builder roadFile(File file){this.roadFile = file; return this;}
        public Builder segmentFile(File file){this.segmentFile = file; return this;}
        public Builder polygonFile(File file){this.polygonFile = file; return this;}

        public Loader build(){
            return new Loader(this);
        }
    }

    private Loader(Builder builder){
        this.roadMap = builder.roadMap;
        this.nodeFile = builder.nodeFile;
        this.roadFile = builder.roadFile;
        this.segmentFile = builder.segmentFile;
        this.polygonFile = builder.polygonFile;
    }

    public Loader load(Trie trie, QuadTree quad, Location origin, double scale) throws IOException{
        if (this.nodeFile != null){
            this.loadNodes(quad, origin, scale);
        }
        if (this.roadFile != null){
            this.loadRoads(trie);
        }
        if (this.segmentFile != null){
            this.loadSegments();
        }
        if (this.polygonFile != null){
            this.loadPolygons();
        }
        return this;
    }

    private void loadNodes(QuadTree quad, Location origin, double scale)throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(this.nodeFile))){
            String line = null;
            while((line = reader.readLine()) != null){
                String[] split = line.split("\t");
                int nodeID = Integer.parseInt(split[0]);
                double lat = Double.parseDouble(split[1]);
                double lon = Double.parseDouble(split[2]);
                Location location = Location.newFromLatLon(lat,lon);
                Node node = Node.withID(nodeID)
                                .atLocation(location);
                this.roadMap.addNode(node);
            }
        }
    }

    private void loadRoads(Trie trie) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(this.roadFile))){
            String line = reader.readLine();
            while((line = reader.readLine()) != null){
                String[] split = line.split("\t");
                int roadID = Integer.parseInt(split[0]);
                String name = split[2];
                String city = split[3];
                int oneway = Integer.parseInt(split[4]);
                int speed = Integer.parseInt(split[5]);
                int roadClass = Integer.parseInt(split[6]);
                int notforcar = Integer.parseInt(split[7]);
                int notforped = Integer.parseInt(split[8]);
                int notforbicy = Integer.parseInt(split[9]);

                Road road = Road.Builder.createWithID(roadID)
                                        .name(name)
                                        .city(city)
                                        .isOneWay(oneway)
                                        .speedlimit(speed)
                                        .roadClass(roadClass)
                                        .isNotForCar(notforcar)
                                        .isNotForPed(notforped)
                                        .isNotForBicycle(notforbicy)
                                        .build();
                this.roadMap.addRoad(road);
                trie.add(road.getName().toCharArray(), road);
                trie.add(road.getFullName().toCharArray(), road);
            }
        }
    }

    private void loadSegments() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(this.segmentFile))){
            String line = reader.readLine();
            while ((line = reader.readLine()) != null){
                String[] split = line.split("\t");
                Road road = this.roadMap.getRoads().get(Integer.parseInt(split[0]));
                double length = Double.parseDouble(split[1]);
                Node toNode = this.roadMap.getNodes().get(Integer.parseInt(split[2]));
                Node fromNode = this.roadMap.getNodes().get(Integer.parseInt(split[3]));
                ArrayList<Location> coords = new ArrayList<Location>();
                for (int i = 4; i < split.length; i+=2){
                    double lat = Double.parseDouble(split[i]);
                    double lon = Double.parseDouble(split[i+1]);
                    coords.add(Location.newFromLatLon(lat, lon));
                }
                Segment s = Segment.withCoords(coords)
                                   .onRoad(road)
                                   .withLength(length)
                                   .toNode(toNode)
                                   .fromNode(fromNode);
                this.roadMap.addSegment(s);
                road.addSegment(s);
                toNode.addInSegment(s);
                fromNode.addOutSegment(s);
            }
        }
    }

    private void loadPolygons() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(this.polygonFile))){
            String line;
            String[] split;
            Polygon poly = Polygon.create();
            while ((line = reader.readLine()) != null){
                if (line.equals("[POLYGON]")){
                    poly = Polygon.create();
                }
                if (line.startsWith("Type")){
                    split = line.split("=");
                    poly.ofType(split[1]);
                }
                if (line.startsWith("Label")){
                    split = line.split("=");
                    poly.withLabel(split[1]);
                }
                if (line.startsWith("EndLevel")){
                    split = line.split("=");
                    poly.withEndLevel(Integer.parseInt(split[1]));
                }
                if (line.startsWith("CityIdx")){
                    split = line.split("=");
                    poly.withCityIdx(Integer.parseInt(split[1]));
                }
                if (line.startsWith("Data0")){
                    split = line.split("[=(),]");
                    ArrayList<String> strlist = new ArrayList<>();
                    for (String s : split){
                        if (!s.isEmpty()){
                            strlist.add(s);
                        }
                    }
                    for (int i = 1; i < strlist.size(); i+=2){
                        double lat = Double.parseDouble(strlist.get(i));
                        double lon = Double.parseDouble(strlist.get(i+1));
                        Location coord = Location.newFromLatLon(lat, lon);
                        poly.addCoord(coord);
                    }
                }
                if (line.equals("[END]")){
                    roadMap.addPolygon(poly);
                }
            }

        }

    }

}
