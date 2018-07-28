package GUI.Parsing;

import Data_Structures.Graph.RoadMap;
import Data_Structures.Trie.Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private Parser(){}

    public static void parse(File nodes, File roads, File segments, File polygons, RoadMap roadMap, Trie trie) {
        try (BufferedReader nodeParser = new BufferedReader(new FileReader(nodes));
             BufferedReader roadsParser = new BufferedReader(new FileReader(roads));
             BufferedReader segmentsParser = new BufferedReader(new FileReader(segments));
             BufferedReader polygonsParser = new BufferedReader(new FileReader(polygons))){

            NodeParser.parse(nodeParser, roadMap);
            RoadParser.parse(roadsParser, roadMap, trie);
            SegmentParser.parse(segmentsParser, roadMap);
            PolygonParser.parse(polygonsParser, roadMap);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
