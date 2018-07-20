package GUI;

import RoadMap.RoadMap;
import RoadMap.Loader;
import RoadMap.Drawer;
import RoadMap.Location;
import RoadMap.Node;
import RoadMap.Segment;
import RoadMap.Road;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapGUI extends GUI {
    private final RoadMap roadMap = RoadMap.newInstance();
    private final Drawer drawer = Drawer.create();
    private Location origin;
    private double scale;
    private Node highlightedNode;
    private Road highlightedRoad;

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
            scale = Math.min((window.height/heightDiff), (window.width/widthDiff));
            origin = new Location(left, top);
        }
    }

    @Override
    protected void redraw(Graphics g) {
        if (highlightedRoad != null){
            System.out.println("Highlighted: " + highlightedRoad.getName());
        }
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
            if (dist < closestDist && n.getLocation().isClose(clickLocation, scale*0.125)){
                closestDist = dist;
                closestNode = n;
            }

            /**
            double dist;
            if (scale <= 7) {dist =  scale;} else {dist = 7;}
            if (n.getLocation().isClose(clickLocation, dist)){
                if (highlightedNode != null){
                    highlightedNode.setHighlighted(false);
                }
                n.setHighlighted(true);
                highlightedNode = n;
                getTextOutputArea().setText("Node ID" + n.getNodeID());
            }
             */
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
        for (Road r : this.roadMap.getRoads().values()){
            if (r.getName().equals(input)){
                if (highlightedRoad != null){
                    highlightedRoad.setHighlighted(false);
                }
                System.out.println("name: " + r.getName());
                highlightedRoad = r;
                r.setHighlighted(true);
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
                                  .segmentFile(segments).build().load();
        calcScale();
    }

    public static void main(String[] args){
        final MapGUI mapGUI = new MapGUI();
    }
}
