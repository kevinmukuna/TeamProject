package rayTrace.utillity;

public class Light {
    private static final int AMBIENT = 0;
    private static final int DIRECTIONAL = 1;
    private static final int POINT = 2;

    public int lightType;
    Vector3D lvec;           // the position of a point light or
    // the direction to a directional light
    private float ir, ig, ib;        // intensity of the light source

    public Light(int type, Vector3D v, float r, float g, float b) {
        lightType = type;
        ir = r;
        ig = g;
        ib = b;
        if (type != AMBIENT) {
            lvec = v;
            if (type == DIRECTIONAL) {
                lvec.normalize();
            }
        }
    }

    public static int getAMBIENT() {
        return AMBIENT;
    }

    public static int getDIRECTIONAL() {
        return DIRECTIONAL;
    }

    public static int getPOINT() {
        return POINT;
    }

    public float getIr() {
        return ir;
    }

    public float getIg() {
        return ig;
    }

    public float getIb() {
        return ib;
    }

    public Vector3D getLvec() {
        return lvec;
    }
}
