package rayTrace.shapes;

import rayTrace.utillity.Light;
import rayTrace.utillity.Ray;
import rayTrace.utillity.Vector3D;

import java.awt.*;
import java.util.List;

public class Surface {
    private final float ir, ig, ib;        // surface's intrinsic color
    private final float ka, kd, ks, ns;    // constants for phong model
    private final float kt, kr, nt;
    private static final float TINY = 0.001f;
    private static final float I255 = 0.00392156f;  // 1/255

    public Surface(float rval, float gval, float bval, float a, float d, float s, float n, float r, float t, float index) {
        ir = rval; ig = gval; ib = bval;
        ka = a; kd = d; ks = s; ns = n;
        kr = r*I255; kt = t; nt = index;
    }

    public Color Shade(Vector3D p, Vector3D n, Vector3D v, java.util.List<Object> lights, List<Object> objects, Color bgnd) {
        float r = 0;
        float g = 0;
        float b = 0;
        for (Object lightSources : lights) {
            Light light = (Light) lightSources;
            if (light.lightType == Light.getAMBIENT()) {
                r += ka*ir*light.getIr();
                g += ka*ig*light.getIg();
                b += ka*ib*light.getIb();
            } else {
                Vector3D l;
                if (light.lightType == Light.getPOINT()) {
                    l = new Vector3D(light.getLvec().getX() - p.getX(), light.getLvec().getY() - p.getY(),
                            light.getLvec().getZ() - p.getZ());
                    l.normalize();
                } else {
                    l = new Vector3D(-light.getLvec().getX(), -light.getLvec().getY(), -light.getLvec().getZ());
                }

                // Check if the surface point is in shadow
                Vector3D poffset = new Vector3D(p.getX() + TINY*l.getX(), p.getY() + TINY*l.getY(),
                        p.getZ() + TINY*l.getZ());
                Ray shadowRay = new Ray(poffset, l);
                if (shadowRay.trace(objects))
                    break;

                float lambert = Vector3D.dot(n,l);
                if (lambert > 0) {
                    if (kd > 0) {
                        float diffuse = kd*lambert;
                        r += diffuse*ir*light.getIr();
                        g += diffuse*ig*light.getIg();
                        b += diffuse*ib*light.getIb();
                    }
                    if (ks > 0) {
                        lambert *= 2;
                        float spec = v.dot(lambert*n.getX() - l.getX(), lambert*n.getY() - l.getY(), lambert*n.getZ() - l.getZ());
                        if (spec > 0) {
                            spec = ks*((float) Math.pow((double) spec, (double) ns));
                            r += spec*light.getIr();
                            g += spec*light.getIg();
                            b += spec*light.getIb();
                        }
                    }
                }
            }
        }

        // Compute illumination due to reflection
        if (kr > 0) {
            float t = v.dot(n);
            if (t > 0) {
                t *= 2;
                Vector3D reflect = new Vector3D(t*n.getX() - v.getX(), t*n.getY() - v.getY(),
                        t*n.getZ() - v.getZ());
                Vector3D poffset = new Vector3D(p.getX() + TINY*reflect.getX(), p.getY() + TINY*reflect.getY(),
                        p.getZ() + TINY*reflect.getZ());
                Ray reflectedRay = new Ray(poffset, reflect);
                if (reflectedRay.trace(objects)) {
                    Color rcolor = reflectedRay.Shade(lights, objects, bgnd);
                    r += kr*rcolor.getRed();
                    g += kr*rcolor.getGreen();
                    b += kr*rcolor.getBlue();
                } else {
                    r += kr*bgnd.getRed();
                    g += kr*bgnd.getGreen();
                    b += kr*bgnd.getBlue();
                }
            }
        }

        // Add code for refraction here

        r = Math.min(r, 1f);
        g = Math.min(g, 1f);
        b = Math.min(b, 1f);

        r = (r < 0) ? 0 : r;
        g = (g < 0) ? 0 : g;
        b = (b < 0) ? 0 : b;

        return new Color(r, g, b);
    }

}
