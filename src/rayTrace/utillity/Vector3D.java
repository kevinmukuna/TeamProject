package rayTrace.utillity;

public class Vector3D {
    private static float x;
    private static float y;
    private static float z;
    public Vector3D() {

    }
    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D vector3D) {
        this.x = vector3D.x;
        this.y = vector3D.y;
        this.z = vector3D.z;
    }

    public final float dot(Vector3D B) {
        // copyOfValues() return copy of the construct values also know as defensive copying
        return (x * B.x + y*B.y +z*B.z);
    }

    public static final float dot(float Bx, float By, float Bz) {
        return (x*Bx + y*By + z*Bz);
    }

    public static final float dot(Vector3D A, Vector3D B) {
        return (A.x*B.x + A.y*B.y + A.z*B.z);
    }

    public final Vector3D cross(Vector3D B) {
        return new Vector3D(y*B.z - z*B.y, z*B.x - x*B.z, x*B.y - y*B.x);
    }

    public final Vector3D cross(float Bx, float By, float Bz) {
        return new Vector3D(y*Bz - z*By, z*Bx - x*Bz, x*By - y*Bx);
    }

    public final Vector3D cross(Vector3D A, Vector3D B) {
        return new Vector3D(A.y*B.z - A.z*B.y, A.z*B.x - A.x*B.z, A.x*B.y - A.y*B.x);
    }

    public final float length( ) {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public final float length(Vector3D A) {
        return (float) Math.sqrt(A.x*A.x + A.y*A.y + A.z*A.z);
    }

    public void normalize( ) {
        float t = x*x + y*y + z*z;
        if (t != 0 && t != 1) t = (float) (1 / Math.sqrt(t));
        x *= t;
        y *= t;
        z *= t;
    }

    public final static Vector3D normalize(Vector3D A) {
        float t = A.x*A.x + A.y*A.y + A.z*A.z;
        if (t != 0 && t != 1) t = (float)(1 / Math.sqrt(t));
        return new Vector3D(A.x*t, A.y*t, A.z*t);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3D copyOfValues(){
        return new Vector3D(x, y, z);
    }

    public String toString() {
        return new String("["+x+", "+y+", "+z+"]");
    }
}
