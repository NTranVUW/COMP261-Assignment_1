package QuadSimulator;

import Data_Structures.Graph.Node;
import Data_Structures.Graph.RoadMap;
import GUI.Drawing.MapDrawer;
import GUI.GUI;
import GUI.Parsing.NodeParser;
import GUI.Location;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuadSim extends GUI {
    private final RoadMap roadMap = RoadMap.newInstance();
    private final MapDrawer drawer = MapDrawer.create(this);
    private Location origin;
    private double scale;
    private double ZOOM = 1.25;
    private Node highlightedNode = null;
    private boolean loaded = false;

    private double top = Double.NEGATIVE_INFINITY;
    private double bottom = Double.POSITIVE_INFINITY;
    private double left = Double.POSITIVE_INFINITY;
    private double right = Double.NEGATIVE_INFINITY;

    private void calcMapSize(){
        for (Node node : roadMap.getNodes().values()) {
            Location loc = node.getLocation();
            if (loc.x < left) {
                left = loc.x;
            } else if (loc.x > right) {
                right = loc.x;
            }

            if (loc.y > top) {
                top = loc.y;
            } else if (loc.y < bottom) {
                bottom = loc.y;
            }
        }
    }

    private void calcScale(){
        calcMapSize();
        double heightDiff = top - bottom;
        double widthDiff = right - left;
        Dimension window = super.getDrawingAreaDimension();
        //(windowSize/(maxLocation - minLocation))
        scale = Math.max((window.height/heightDiff), (window.width/widthDiff));
        origin = new Location(left, top); //draws the toppest, leftist point at 0,0
    }

    @Override
    protected void redraw(Graphics g) {
        drawer.drawTo((Graphics2D) g).drawNodes(roadMap.getNodes(), origin, scale);
        System.out.println(scale);
        if (loaded){
            QuadParser.parse(drawer);
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
            getTextOutputArea().setText("Node ID: " + closestNode.getNodeID());
        }

    }

    @Override
    protected void onPress(MouseEvent e) {

    }

    @Override
    protected void onDrag(MouseEvent e) {

    }

    @Override
    protected void onSearch() {

    }

    @Override
    protected void onMove(Move m) {
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
                scale = scale* ZOOM;
                //origin =
                break;
            case ZOOM_OUT:
                scale = scale/ ZOOM;
                //origin = Location.newFromPoint(MouseInfo.getPointerInfo().getLocation(), origin, scale);
                break;
        }
    }

    @Override
    protected void onResize() {
        calcScale();
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) throws  IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(nodes))){
            NodeParser.parse(reader, roadMap);
        }
        QuadParser.create(roadMap.getNodes().values(), this);
        calcScale();
        loaded = true;
    }

    public Location getOrigin(){
        return origin;
    }

    public double getScale(){
        return scale;
    }

    public static void main(String[] args ){
        final QuadSim quad = new QuadSim();
    }
}
