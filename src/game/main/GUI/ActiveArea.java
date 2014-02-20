package game.main.GUI;

import android.graphics.Canvas;
import game.main.utils.Touch;

/**
 * Created by lgor on 20.02.14.
 */
public abstract class ActiveArea implements iRenderFeature {

    public abstract boolean interestedInTouch(Touch touch);

    public abstract void update(Touch touch);

    @Override
    public abstract void render(MapCamera camera, Canvas canvas);
}
