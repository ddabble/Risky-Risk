package no.ntnu.idi.tdt4240.util.gl;

import com.badlogic.gdx.graphics.Color;

public class ColorArray {
    /**
     * Should equal the number of components that the vector array in the shader has.
     */
    private final int numComponents;

    private final float[] colors;

    public ColorArray(int size, int numComponents) {
        if (numComponents < 3 || numComponents > 4)
            throw new IllegalArgumentException("Number of components has to be either 3 or 4!");

        this.numComponents = numComponents;
        colors = new float[size * numComponents];
    }

    public float[] getFloatArray() {
        // TODO: should return copy?
        return colors;
    }

    public void setColor(int index, int color) {
        setColor(index, new Color(color));
    }

    public void setColor(int index, Color color) {
        switch (numComponents) {
            case 4:
                colors[index * numComponents + 3] = color.a;
            case 3:
                colors[index * numComponents] = color.r;
                colors[index * numComponents + 1] = color.g;
                colors[index * numComponents + 2] = color.b;
                break;
        }
    }
}
