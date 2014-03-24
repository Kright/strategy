package game.main.GUI;

import android.graphics.Canvas;

/**
 * Created by lgor on 10.03.14.
 */
public abstract class Button {

    protected int width, height;

    public abstract void click();

    public boolean isInto(float x, float y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public abstract void render(Canvas canv, int x, int y, boolean isPressed);

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
