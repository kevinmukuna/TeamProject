package rayTrace.shapes;

import rayTrace.utillity.Ray;
import rayTrace.utillity.Vector3D;

import java.awt.*;
import java.util.List;

public class Sphere implements Renderable {
    private final Surface surface;
    private final Vector3D center;
    private final float radius;
    private final float radSqr;

    public Sphere(Surface s, Vector3D c, float r) {
        surface = s;
        center = c;
        radius = r;
        radSqr = r*r;
    }

    @Override
    public boolean intersect(Ray ray) {
        float dx = center.getX() - ray.getOrigin().getX();
        float dy = center.getY() - ray.getOrigin().getY();
        float dz = center.getZ() - ray.getOrigin().getZ();
        float v = ray.getDirection().dot(dx, dy, dz);

        if (v - radius > ray.t)
            return false;

        float t = radSqr + v*v - dx*dx - dy*dy - dz*dz;
        if (t < 0)
            return false;

        t = v - ((float) Math.sqrt(t));
        if ((t > ray.getT()) || (t < 0))
            return false;

        ray.t = t;
        ray.object = this;
        return true;
    }



    public Color Shade(Ray ray, List<Object> lights, List<Object> objects, Color bgnd) {
        float px = ray.getOrigin().getX() + ray.t*ray.getDirection().getX();
        float py = ray.getOrigin().getY() + ray.t*ray.getDirection().getY();
        float pz = ray.getOrigin().getZ() + ray.t*ray.getDirection().getZ();

        Vector3D p = new Vector3D(px, py, pz);
        Vector3D v = new Vector3D(-ray.getDirection().getX(), -ray.getDirection().getY(), -ray.getDirection().getZ());
        Vector3D n = new Vector3D(px - center.getX(), py - center.getY(), pz - center.getZ());
        n.normalize();

        // The illumination model is applied
        // by the surface's Shade() method
        return surface.Shade(p, n, v, lights, objects, bgnd);
    }



    public String toString() {
        return ("sphere "+center+" "+radius);
    }

}