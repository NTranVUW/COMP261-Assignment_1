package GUI;

import RoadMap.RoadMap;
import RoadMap.Loader;
import RoadMap.Drawer;
import RoadMap.Location;
import RoadMap.Node;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MapGUI extends GUI {
    private final RoadMap roadMap = RoadMap.newInstance();
    private final Drawer drawer = Drawer.create();
    private Location origin;
    private double scale;

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
        drawer.drawTo(g)
              .drawSegments(roadMap.getSegments(), origin, scale)
              .drawNodes(roadMap.getNodes(), origin, scale);
    }

    @Override
    protected void onClick(MouseEvent e) {

    }

    @Override
    protected void onSearch() {

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
        Loader loader = new Loader.Builder(this.roadMap).nodeFile(nodes).roadFile(roads).segmentFile(segments).build().load();
        calcScale();
    }

    public static void main(String[] args){
        final MapGUI mapGUI = new MapGUI();
    }
}
