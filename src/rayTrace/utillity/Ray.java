package rayTrace.utillity;
import rayTrace.shapes.Renderable;

import java.awt.Color;
import java.util.List;

public class Ray {
    // use of public is satisfactory in this case since the const
    private static final float MAX_T = Float.MAX_VALUE;
    public float t;
    private final Vector3D origin;
    private final Vector3D direction;
    public Renderable object;
    // private Renderable object;

    public Ray(Vector3D eye, Vector3D dir) {
        origin = new Vector3D(eye);
        direction = Vector3D.normalize(dir);
    }

    public boolean trace(List<Object> objects) {
        t = MAX_T;
        object = null;
        for (Object objList : objects) {
            Renderable object = (Renderable) objList;
            object.intersect(this);
        }
        return (object != null);
    }

    public final Color Shade(List<Object> lights, List<Object> objects, Color bgnd) {
        return object.Shade(this, lights, objects, bgnd);
    }

    public String toString() {
        return ("ray origin = "+origin+"  direction = "+direction+"  t = "+t);
    }

    public Vector3D getOrigin() {
        return origin;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }
}
