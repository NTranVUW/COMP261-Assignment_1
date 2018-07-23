package GUI;

import RoadMap.RoadMap;
import RoadMap.Loader;
import RoadMap.Drawer;
import RoadMap.Location;
import RoadMap.Node;
import RoadMap.Segment;
import RoadMap.Road;
import Trie.Trie;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapGUI extends GUI {
    private final RoadMap roadMap = RoadMap.newInstance();
    private final Drawer drawer = Drawer.create();
    private final Trie trie = Trie.create();
    private Location origin;
    private double scale;
    private Node highlightedNode;
    private ArrayList<Road> highlightedRoads;

    protected void calcScale(){
        Double top = Double.NEGATIVE_INFINITY;
        Double bottom = Double.POSITIVE_INFINITY;
        Double left = Double.POSITIVE_INFINITY;
        Double right = Double.NEGATIVE_INFINITY;

        for (Node n: roadMap.getNodes().values()){
            Location loc = n.getLocation();
            if (loc.x < left){
                left = loc.x;
            } else if (loc.x > right){
                right =loc.x;
            }

            if (loc.y > top){
                top = loc.y;
            } else if (loc.y < bottom){
                bottom = loc.y;
            }
            double heightDiff = top - bottom;
            double widthDiff = right-left;
            Dimension window = super.getDrawingAreaDimension();
            scale = Math.max((window.height/heightDiff), (window.width/widthDiff));
            origin = new Location(left, top);
        }
    }

    @Override
    protected void redraw(Graphics g) {
        //System.out.println("Scale: " + scale);
        drawer.drawTo(g)
              .drawSegments(roadMap.getSegments(), origin, scale)
              .drawNodes(roadMap.getNodes(), origin, scale);
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


    }

    @Override
    protected void onSearch() {
        String input  = getSearchBox().getText();
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
                System.out.println("exact not null");
                for (Object o : getExactMatch){
                    if (o != null){
                        Road r = (Road) o;
                        System.out.println("exact: " + r.getName());
                        r.setHighlighted(true);
                        highlightedRoads.add(r);
                    }
                }
            } else if (!getMatchingPrefix.isEmpty()){
                System.out.println("gui not null");
                for (Object l : (List<Object>) getMatchingPrefix){
                    if (l != null) {
                        for (Object o : (List<Object>) l){
                            if (o != null) {
                                Road r = (Road) o;
                                System.out.println("prefix: " + r.getName());
                                r.setHighlighted(true);
                                highlightedRoads.add(r);
                            }

                        }
                    }
                }
            }
        }

        /**
        if (highlightedRoads != null){
            for (Road r : highlightedRoads){
                r.setHighlighted(false);
            }
        }
        highlightedRoads = new ArrayList<Road>();
        for (Road r : this.roadMap.getRoads().values()){
            if (r.getName().equals(input) || r.getFullName().equals(input)){
                r.setHighlighted(true);
                highlightedRoads.add(r);
                System.out.println("name: " + r.getName());
            }
        }
        */
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
                scale = scale+(scale/5);
                break;
            case ZOOM_OUT:
                scale = scale-(scale/5);
                break;
        }

    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) throws IOException {
        Loader loader = new Loader.Builder(this.roadMap)
                                  .nodeFile(nodes)
                                  .roadFile(roads)
                                  .segmentFile(segments).build().load(trie);
        calcScale();
    }

    public static void main(String[] args){ final MapGUI mapGUI = new MapGUI(); }
}
