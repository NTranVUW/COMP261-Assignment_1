package GUI.Parsing;

import Data_Structures.Graph.RoadMap;
import Data_Structures.Trie.Trie;

import java.io.File;
import java.io.IOException;

public class Parser {
    private Parser(){}

    public static void parse(File nodes, File roads, File segments, File polygons, RoadMap roadMap, Trie trie) throws IOException{
        NodeParser.parse(nodes, roadMap);
        RoadParser.parse(roads, roadMap, trie);
        SegmentParser.parse(segments, roadMap);
        PolygonParser.parse(polygons, roadMap);
    }
}
