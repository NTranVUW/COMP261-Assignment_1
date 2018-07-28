package GUI.Parsing;

import GUI.Drawing.Polygon;

import GUI.Location;
import Data_Structures.Graph.RoadMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class PolygonParser {
    private PolygonParser(){}

    public static void parse(BufferedReader reader, RoadMap roadMap) throws IOException {
        String line;
        String[] split;
        Polygon poly = Polygon.create();
        boolean readCoords = false;
        while ((line = reader.readLine()) != null){
            if (line.equals("[POLYGON]")){
                poly = Polygon.create();
            }
            //following if statements splits line at '=' so first element split[0] is the identifier
            //and split[1] is the value
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
            //Data0 identifies the list of coordinates
            //[=(),] gets rid of all the special characters and gives us just the coordinates
            if (line.startsWith("Data0") || line.startsWith("Data1")) {
                readCoords = true;
            }
            if (readCoords) {
                ArrayList<Location> locations = new ArrayList<>();
                split = line.split("[=(),]");
                ArrayList<String> strlist = new ArrayList<>();
                for (String s : split) {
                    //splitting leaves us with lots of empty strings, we only want the coordinates
                    if (!s.isEmpty()) {
                        strlist.add(s);
                    }
                }
                //starts at 1 because 0 is 'Data0'
                for (int i = 1; i < strlist.size(); i += 2) {
                    double lat = Double.parseDouble(strlist.get(i));
                    double lon = Double.parseDouble(strlist.get(i + 1));
                    Location coord = Location.newFromLatLon(lat, lon);
                    locations.add(coord);
                }
                poly.addCoords(locations);
            }

            if (line.equals("[END]")){
                roadMap.addPolygon(poly);
                readCoords = false;
            }
        }
    }
}
