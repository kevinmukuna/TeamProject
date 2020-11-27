package rayTrace.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import rayTrace.shapes.Sphere;
import rayTrace.shapes.Surface;
import rayTrace.utillity.Light;
import rayTrace.utillity.Ray;
import rayTrace.utillity.Vector3D;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


class Driver  {
    final static int CHUNKSIZE = 100;
    List<Object> objectList;
    List<Object> lightList;
    Surface currentSurface;
    Canvas canvas;
    GraphicsContext gc;

    Vector3D eye, lookat, up;
    Vector3D Du, Dv, Vp;
    float fov;

    Color background;

    int width, height;

    public Driver(int width, int height, String dataFile) {
        this.width = width;
        this.height = height;

        canvas = new Canvas(this.width, this.height);
        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        fov = 30;               // default horizonal field of view

        // Initialize various lists
        objectList = new ArrayList<>(CHUNKSIZE);
        lightList = new ArrayList<>(CHUNKSIZE);
        currentSurface = new Surface(0.8f, 0.2f, 0.9f, 0.2f, 0.4f, 0.4f, 10.0f, 0f, 0f, 1f);

        // Parse the scene file
        String filename = dataFile != null ? dataFile : "defaultScene.txt";

        InputStream is = null;
        try {
            is = new FileInputStream(new File(filename));
            ReadInput(is);
            is.close();
        } catch (IOException e) {
            System.err.println("Error reading "+ new File(filename).getAbsolutePath());
            e.printStackTrace();
            System.exit(-1);
        }

        // Initialize more defaults if they weren't specified
        if (eye == null) eye = new Vector3D(0, 0, 10);
        if (lookat == null) lookat = new Vector3D(0, 0, 0);
        if (up  == null) up = new Vector3D(0, 1, 0);
        if (background == null) background = Color.rgb(0,0,0);

        // Compute viewing matrix that maps a
        // screen coordinate to a ray direction
        Vector3D look = new Vector3D(lookat.getX() - eye.getX(), lookat.getY() - eye.getY(),
                lookat.getZ() - eye.getZ());
        Du = Vector3D.normalize(look.cross(up));
        Dv = Vector3D.normalize(look.cross(Du));
        float fl = (float)(width / (2*Math.tan((0.5*fov)*Math.PI/180)));
        Vp = Vector3D.normalize(look);
        Vp = new Vector3D(
                Vp.getX()*fl - 0.5f*(width*Du.getX() + height*Dv.getX()),
                Vp.getY()*fl - 0.5f*(width*Du.getY() + height*Dv.getY()),
                Vp.getZ()*fl - 0.5f*(width*Du.getZ() + height*Dv.getZ())
        );
//        Vp.getX() = Vp.getX()*fl - 0.5f*(width*Du.getX() + height*Dv.getX());
//        Vp.getY() = Vp.getY()*fl - 0.5f*(width*Du.getY() + height*Dv.getY());
//        Vp.z = Vp.getZ()*fl - 0.5f*(width*Du.getZ() + height*Dv.getZ());
    }


    double getNumber(StreamTokenizer st) throws IOException {
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            System.err.println("ERROR: number expected in line "+st.lineno());
            throw new IOException(st.toString());
        }
        return st.nval;
    }

    void ReadInput(InputStream is) throws IOException {
        StreamTokenizer st = new StreamTokenizer(is);
        st.commentChar('#');
        scan: while (true) {
            switch (st.nextToken()) {
                default:
                    break scan;
                case StreamTokenizer.TT_WORD:
                    if (st.sval.equals("sphere")) {
                        Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                        float r = (float) getNumber(st);
                        objectList.add(new Sphere(currentSurface, v, r));
                    } else
                    if (st.sval.equals("eye")) {
                        eye = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                    } else
                    if (st.sval.equals("lookat")) {
                        lookat = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                    } else
                    if (st.sval.equals("up")) {
                        up = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                    } else
                    if (st.sval.equals("fov")) {
                        fov = (float) getNumber(st);
                    } else
                    if (st.sval.equals("background")) {
                        background = Color.rgb((int) getNumber(st), (int) getNumber(st), (int) getNumber(st));
                    } else
                    if (st.sval.equals("light")) {
                        float r = (float) getNumber(st);
                        float g = (float) getNumber(st);
                        float b = (float) getNumber(st);
                        if (st.nextToken() != StreamTokenizer.TT_WORD) {
                            throw new IOException(st.toString());
                        }
                        if (st.sval.equals("ambient")) {
                            lightList.add(new Light(Light.getAMBIENT(), null, r, g, b));
                        } else
                        if (st.sval.equals("directional")) {
                            Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                            lightList.add(new Light(Light.getDIRECTIONAL(), v, r, g, b));
                        } else
                        if (st.sval.equals("point")) {
                            Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                            lightList.add(new Light(Light.getPOINT(), v, r, g, b));
                        } else {
                            System.err.println("ERROR: in line "+st.lineno()+" at "+st.sval);
                            throw new IOException(st.toString());
                        }
                    } else
                    if (st.sval.equals("surface")) {
                        float r = (float) getNumber(st);
                        float g = (float) getNumber(st);
                        float b = (float) getNumber(st);
                        float ka = (float) getNumber(st);
                        float kd = (float) getNumber(st);
                        float ks = (float) getNumber(st);
                        float ns = (float) getNumber(st);
                        float kr = (float) getNumber(st);
                        float kt = (float) getNumber(st);
                        float index = (float) getNumber(st);
                        currentSurface = new Surface(r, g, b, ka, kd, ks, ns, kr, kt, index);
                    }
                    break;
            }
        }
        is.close();
        if (st.ttype != StreamTokenizer.TT_EOF)
            throw new IOException(st.toString());
    }

    Image getRenderedImage() {
        return canvas.snapshot(null, null);
    }

    public void renderPixel(int i, int j) {
        Vector3D dir = new Vector3D(
                i*Du.getX() + j*Dv.getX() + Vp.getX(),
                i*Du.getY() + j*Dv.getY() + Vp.getY(),
                i*Du.getZ() + j*Dv.getZ() + Vp.getZ());
        Ray ray = new Ray(eye, dir);
        if (ray.trace(objectList)) {
            java.awt.Color bg = toAWTColor(background);
            gc.setFill(toFXColor(ray.Shade(lightList, objectList, bg)));
        } else {
            gc.setFill(background);
        }
        gc.fillOval(i, j, 1, 1);
    }

    private java.awt.Color toAWTColor(Color c) {
        return new java.awt.Color((float) c.getRed(),
                (float) c.getGreen(),
                (float) c.getBlue(),
                (float) c.getOpacity());
    }

    private Color toFXColor(java.awt.Color c) {
        return Color.rgb(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() / 255.0);
    }
}