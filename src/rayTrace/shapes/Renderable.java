package rayTrace.shapes;

import rayTrace.utillity.Ray;

import java.awt.*;
import java.util.List;
public abstract interface Renderable {

    public abstract boolean intersect(Ray r);
    public abstract Color Shade(Ray r, java.util.List<Object> lights, List<Object> objects, Color bgnd);
    public String toString();

}

