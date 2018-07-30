package GUI;

import Data_Structures.Graph.Node;
import Data_Structures.Graph.Road;
import Data_Structures.Graph.RoadMap;
import Data_Structures.Graph.Segment;
import Data_Structures.Trie.Trie;

import GUI.Drawing.MapDrawer;
import GUI.Drawing.Polygon;
import GUI.Parsing.Parser;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapGUI extends GUI {
    private static final double ZOOM_FACTOR = 1.25;

    private final RoadMap roadMap = RoadMap.newInstance();
    private final MapDrawer mapDrawer = MapDrawer.create(this);
    private final Trie trie = Trie.create();

    private Node highlightedNode;
    private ArrayList<Road> highlightedRoads;
    private Location pressedLocation;
    private Location origin;
    private double scale;

    private double top = Double.POSITIVE_INFINITY;
    private double bottom = Double.NEGATIVE_INFINITY;
    private double left = Double.POSITIVE_INFINITY;
    private double right = Double.NEGATIVE_INFINITY;

    private void calculateMapSize(){
        //calculates the highest, lowest y and x points from the polygons, this is the window
        for (Polygon poly : roadMap.getPolygons()) {
            for (ArrayList<Location> arr : poly.getCoords()) {
                for (Location loc: arr){
                    if (loc.x < left) {
                        left = loc.x;
                    } else if (loc.x > right) {
                        right = loc.x;
                    }

                    if (loc.y > bottom) {
                        bottom = loc.y;
                    } else if (loc.y < top) {
                        top = loc.y;
                    }
                }
            }
        }
    }

    private void calcScale(){
        calculateMapSize();
        double heightDiff = bottom - top;
        double widthDiff = right - left;
        Dimension window = super.getDrawingAreaDimension();
        //(windowSize/(maxLocation - minLocation))
        scale = Math.max((window.height/heightDiff), (window.width/widthDiff));
        origin = new Location(left, bottom); //draws the toppest, leftist point at 0,0
    }

    @Override
    protected void redraw(Graphics g) {
        if (g != null){
            mapDrawer.drawTo((Graphics2D) g)
                     .drawPolygons(roadMap.getPolygons(), origin, scale)
                     .drawSegments(roadMap.getSegments(), origin, scale)
                     .drawNodes(roadMap.getNodes(), origin, scale);
        }
    }

    @Override
    protected void onClick(MouseEvent e) {
        /**
        int clickX = e.getX();
        int clickY = e.getY();
        Location clickLocation = Location.newFromPoint(new Point(clickX, clickY), origin, scale);
        double closestDist = Double.POSITIVE_INFINITY;
        Node closestNode = null;
        for (Node n : this.roadMap.getNodes().values()){
            double dist = n.getLocation().distance(clickLocation);
            if (dist < closestDist && n.getLocation().isClose(clickLocation, 0.5)){
                closestDist = dist;
                closestNode = n;
            }
        }
        if (highlightedNode != null){
            highlightedNode.setHighlighted(false);
        }
        if (closestNode != null) {
            closestNode.setHighlighted(true);
            highlightedNode = closestNode;
            ArrayList<String> roadNames = new ArrayList<String>();
            for (Segment s : closestNode.getInSegments()){
                String name = s.getRoad().getName();
                if (!roadNames.contains(name)){
                    roadNames.add(name);
                }
            }
            for (Segment s : closestNode.getOutSegments()){
                String name = s.getRoad().getName();
                if (!roadNames.contains(name)){
                    roadNames.add(name);
                }
            }
            getTextOutputArea().setText("Node ID: " + closestNode.getNodeID() + " connecting roads: ");
            for (int i = 0; i < roadNames.size(); i++){
                if (i != roadNames.size() - 1){
                    getTextOutputArea().append(roadNames.get(i) + " and ");
                }
                else { getTextOutputArea().append(roadNames.get(i)); }
            }
        }
         */
    }

    @Override
    protected void onPress(MouseEvent e) {
        pressedLocation = Location.newFromPoint(e.getPoint(), origin, scale);
    }

    @Override
    protected void onDrag(MouseEvent e) {
         Location draggedLocation = Location.newFromPoint(e.getPoint(), origin, scale);
         Point pressedPoint = pressedLocation.asPoint(origin, scale);
         Point draggedPoint = draggedLocation.asPoint(origin, scale);
         int draggedX = draggedPoint.x - pressedPoint.x;
         int draggedY = draggedPoint.y - pressedPoint.y;
         origin = origin.moveBy(draggedX*(1/scale), draggedY*(1/scale));
    }

    @Override
    protected void onSearch() {
        String input  = getSearchBox().getText();
        if (input.isEmpty()){
            for (Road r : highlightedRoads){
                r.setHighlighted(false);
            }
            return;
        }
        List<Object> getExactMatch = trie.get(input.toCharArray());
        List<Object> getMatchingPrefix = trie.getAll(input.toCharArray());
        if (highlightedRoads != null){
            for (Road r : highlightedRoads){
                r.setHighlighted(false);
            }
        }
        highlightedRoads = new ArrayList<Road>();
        if (getExactMatch != null){
            if (!getExactMatch.isEmpty()){
                for (Object o : getExactMatch){
                    if (o != null){
                        Road r = (Road) o;
                        r.setHighlighted(true);
                        highlightedRoads.add(r);
                    }
                }
            } else if (!getMatchingPrefix.isEmpty()){
                for (Object l : (List<Object>) getMatchingPrefix){
                    if (l != null) {
                        for (Object o : (List<Object>) l){
                            if (o != null) {
                                Road r = (Road) o;
                                r.setHighlighted(true);
                                highlightedRoads.add(r);
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onMove(GUI.Move m) {

        switch (m) {
            case NORTH:
                origin = origin.moveBy(0, 100*(1/scale));
                break;
            case SOUTH:
                origin = origin.moveBy(0, -100*(1/scale));
                break;
            case EAST:
                origin = origin.moveBy(100*(1/scale), 0);
                break;
            case WEST:
                origin = origin.moveBy(-100*(1/scale), 0);
                break;
            case ZOOM_IN:
                scale = scale*ZOOM_FACTOR;
                //origin =
                break;
            case ZOOM_OUT:
                scale = scale/ZOOM_FACTOR;
                //origin = Location.newFromPoint(MouseInfo.getPointerInfo().getLocation(), origin, scale);
                break;
        }
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) {
        Parser.parse(nodes, roads, segments, polygons, roadMap, trie);
        calcScale(); //calculates the initial scale and origin
    }

    @Override
    protected void onResize(){
        calcScale();
    }

    public static void main(String[] args){ final MapGUI mapGUI = new MapGUI(); }
}
