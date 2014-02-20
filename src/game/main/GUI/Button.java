package game.main.GUI;

import android.graphics.Rect;
import game.main.utils.Touch;

/**
 * Created by lgor on 20.02.14.
 */
public abstract class Button extends ActiveArea {

    protected final Rect area;

    protected Button(Rect rectangle) {
        this.area = rectangle;
    }

    @Override
    public boolean interestedInTouch(Touch touch) {
        boolean interested = touch.into(area);
        if (interested) {
            Click(touch);
        }
        return interested;
    }

    protected abstract void Click(Touch t);
}
