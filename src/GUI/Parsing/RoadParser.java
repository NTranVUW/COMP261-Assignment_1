package GUI.Parsing;

import Data_Structures.Graph.Road;
import Data_Structures.Graph.RoadMap;

import Data_Structures.Trie.Trie;

import java.io.BufferedReader;
import java.io.IOException;

public class RoadParser {
    private RoadParser(){}

    public static void parse(BufferedReader reader, RoadMap roadMap, Trie trie) throws IOException {
        String line;
        reader.readLine(); //skips over the first line
        while((line = reader.readLine()) != null){
            String[] split = line.split("\t");

            int roadID = Integer.parseInt(split[0]);
            String name = split[2];
            String city = split[3];
            int oneway = Integer.parseInt(split[4]);
            int speed = Integer.parseInt(split[5]);
            int roadClass = Integer.parseInt(split[6]);
            int notforcar = Integer.parseInt(split[7]);
            int notforpedestrian = Integer.parseInt(split[8]);
            int notforbicycle = Integer.parseInt(split[9]);

            Road road = Road.Builder.createWithID(roadID)
                                    .name(name)
                                    .city(city)
                                    .isOneWay(oneway)
                                    .speedlimit(speed)
                                    .roadClass(roadClass)
                                    .isNotForCar(notforcar)
                                    .isNotForPed(notforpedestrian)
                                    .isNotForBicycle(notforbicycle)
                                    .build();

            roadMap.addRoad(road);
            trie.add(road.getName().toCharArray(), road);
            trie.add(road.getFullName().toCharArray(), road);
        }
    }
}
