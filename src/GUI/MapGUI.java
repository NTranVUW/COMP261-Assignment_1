package GUI;

import Data_Structures.Graph.Node;
import Data_Structures.Graph.Road;
import Data_Structures.Graph.RoadMap;
import Data_Structures.Graph.Segment;
import Data_Structures.QuadTree.QuadTree;
import Data_Structures.Trie.Trie;

import GUI.Drawing.MapDrawer;
import GUI.Drawing.Polygon;
import GUI.Parsing.Parser;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MapGUI extends GUI {
    private static final double ZOOM_FACTOR = 1.25;

    private final RoadMap roadMap = RoadMap.newInstance();
    private final MapDrawer mapDrawer = MapDrawer.create(this);
    private final Trie trie = Trie.create();
    private QuadTree quad;

    private Node highlightedNode;
    private ArrayList<Road> highlightedRoads;
    private Location pressedLocation;
    private Location origin;
    private double scale;
    private long timeStart;
    private long timeEnd;

    //in relative coordinates
    private double maxTop = Double.NEGATIVE_INFINITY;
    private double maxBottom = Double.POSITIVE_INFINITY;
    private double maxLeft = Double.POSITIVE_INFINITY;
    private double maxRight = Double.NEGATIVE_INFINITY;
    //in pixel points
    private double currentTop = Double.POSITIVE_INFINITY;
    private double currentBottom = Double.NEGATIVE_INFINITY;
    private double currentLeft = Double.POSITIVE_INFINITY;
    private double currentRight = Double.NEGATIVE_INFINITY;

    private void calculateMapSize(){
        //calculates the highest, lowest y and x points from the polygons, this is the window
        for (Polygon poly : roadMap.getPolygons()) {
            for (ArrayList<Location> arr : poly.getCoords()) {
                for (Location loc: arr){
                    if (loc.x < maxLeft) {
                        maxLeft = loc.x;
                    } else if (loc.x > maxRight) {
                        maxRight = loc.x;
                    }

                    if (loc.y > maxTop) {
                        maxTop = loc.y;
                    } else if (loc.y < maxBottom) {
                        maxBottom = loc.y;
                    }
                }
            }
        }
    }

    private void calculateCurrentMapSize(){
        //calculates the highest, lowest y and x points from the polygons, this is the window
        for (Polygon poly : roadMap.getPolygons()) {
            for (ArrayList<Location> arr : poly.getCoords()) {
                for (Location loc: arr){
                    Point2D point = loc.asPoint2D(origin, scale);
                    if (point.getX() < currentLeft && !(point.getX() > getWindowWidth())) {
                        currentLeft = point.getX();
                    } else if (point.getX() > currentRight && !(point.getX() < 0)) {
                        currentRight = point.getX();
                    }

                    if (point.getY() > currentBottom && !(point.getY() > getWindowHeight())) {
                        currentBottom = point.getY();
                    } else if (point.getY() < currentTop && !(point.getY() < 0)) {
                        currentTop = point.getY();
                    }
                }
            }
        }
    }

    private void calcScale(){
        calculateMapSize();
        double heightDiff = maxTop - maxBottom;
        double widthDiff = maxRight - maxLeft;
        Dimension window = super.getDrawingAreaDimension();
        //(windowSize/(maxLocation - minLocation))
        scale = Math.max((window.height/heightDiff), (window.width/widthDiff));
        origin = new Location(maxLeft, maxTop); //draws the toppest, leftist point at 0,0
    }

    @Override
    protected void redraw(Graphics g) {
        if (g != null){
            timeEnd = System.currentTimeMillis();
            //System.out.println("time: " + (timeEnd - timeStart));
            mapDrawer.drawTo((Graphics2D) g)
                     //.drawPolygons(roadMap.getPolygons(), origin, scale)
                    //.drawSegments(roadMap.getSegments(), origin, scale)
                     .drawNodes(roadMap.getNodes(), origin, scale)
                     .drawQuad(quad, origin, scale);
        }
    }

    @Override
    protected void onClick(MouseEvent e) {
        int clickX = e.getX();
        int clickY = e.getY();
        Location clickLocation = Location.newFromPoint(new Point(clickX, clickY), origin, scale);
        double closestDist = Double.POSITIVE_INFINITY;
        Node closestNode = null;
        for (Node n : this.roadMap.getNodes().values()){
            double dist = n.getLocation().distance(clickLocation);
            if (dist < closestDist && n.getLocation().isClose(clickLocation, 0.75)){
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
            for (Segment s : closestNode.getInSegments()) {
                String name = s.getRoad().getName();
                if (!roadNames.contains(name)) {
                    roadNames.add(name);
                }
            }
            for (Segment s : closestNode.getOutSegments()) {
                String name = s.getRoad().getName();
                if (!roadNames.contains(name)) {
                    roadNames.add(name);
                }
            }
            getTextOutputArea().setText("Node ID: " + closestNode.getNodeID() + " connecting roads: ");
            for (int i = 0; i < roadNames.size(); i++) {
                if (i != roadNames.size() - 1) {
                    getTextOutputArea().append(roadNames.get(i) + " and ");
                } else {
                    getTextOutputArea().append(roadNames.get(i));
                }
            }
        }
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
         //lmfao I don't even know how this works but it does
         origin = origin.moveBy(-(draggedX/(25*scale*0.1)), draggedY/(25*scale*0.1));
         initialiseQuad();
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
                getAutoComplete().removeAllItems();
                HashSet<String> checkForDuplicates = new HashSet<>();
                for (Object l : (List<Object>) getMatchingPrefix){
                    if (l != null) {
                        for (Object o : (List<Object>) l){
                            if (o != null) {
                                Road r = (Road) o;
                                if (!checkForDuplicates.contains(((Road) o).getFullName())){
                                    getAutoComplete().addItem(((Road) o).getFullName());
                                }
                                checkForDuplicates.add(((Road) o).getFullName());
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
                break;
            case ZOOM_OUT:
                scale = scale/ZOOM_FACTOR;
                break;
        }
        initialiseQuad();
    }

    private void initialiseQuad(){
        quad = QuadTree.createFrom(0,0,getWindowWidth(), getWindowHeight(), this);
        for (Node node : roadMap.getNodes().values()){
            quad.insert(node, origin, scale);
        }
        //quad.printQuad(origin,scale);
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) {
        timeStart = System.currentTimeMillis();
        Parser.parse(nodes, roads, segments, polygons, roadMap, trie);
        calcScale(); //calculates the initial scale and origin
        initialiseQuad();
    }

    @Override
    protected void onResize(){
        calcScale();
        initialiseQuad();
    }

    public static void main(String[] args){ final MapGUI mapGUI = new MapGUI(); }
}
