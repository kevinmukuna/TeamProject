package rayTrace.utillity;


public class Normal
{
    private float x, y, z;
    public Normal()
    {

    }

    public Normal(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Normal(Normal normal)
    {
        this.x = normal.x;
        this.y = normal.y;
        this.z = normal.z;
    }

    private final void normalize()
    {
        float t = x*x + y*y + z*z;
        if (t != 0 && t != 1) t = (float) (1 / Math.sqrt(t));
        x *= t;
        y *= t;
        z *= t;
    }

    private final Normal normalize(Normal A)
    {
        float t = A.x*A.x + A.y*A.y + A.z*A.z;
        if (t != 0 && t != 1) t = (float)(1 / Math.sqrt(t));
        return new Normal(A.x*t, A.y*t, A.z*t);
    }

    private final float dot(Normal normal)
    {
        return x*normal.x + y*normal.y + z*normal.z;
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
}
