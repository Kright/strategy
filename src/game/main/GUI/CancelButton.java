package game.main.GUI;

import android.graphics.Canvas;
import game.main.gamelogic.Gamer;
import game.main.gamelogic.world.Action;
import game.main.utils.Touch;

/**
 * по нажатию кнопки в правом нижнем углу экрана отменяется предыдущее действие
 * Created by lgor on 26.02.14.
 */
public class CancelButton extends ActiveArea {

    private final MapCamera camera;
    private final Gamer gamer;

    public CancelButton(MapCamera camera, Gamer gamer) {
        this.camera = camera;
        this.gamer = gamer;
    }

    @Override
    public boolean interestedInTouch(Touch touch) {
        if (camera.getScreenWidth() - touch.x < 256 && camera.getScreenHeight() - touch.y < 256) {
            Action.getCancelAction().apply();
            gamer.cancelAction();
            return true;
        }
        return false;
    }

    @Override
    public void update(Touch touch) {
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
    }
}
