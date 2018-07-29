package GUI.Drawing;

import GUI.Location;

import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g, Location origin, double scale);
}
