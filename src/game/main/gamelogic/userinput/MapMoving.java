package game.main.gamelogic.userinput;

import android.util.FloatMath;
import game.main.utils.Touch;

/**
 * Перемещение или изменение масштаба карты
 * Created by lgor on 18.04.14.
 */
class MapMoving extends Gamer.State {

    public MapMoving(Gamer gamer) {
        gamer.super();
    }

    @Override
    public Gamer.State getNext() {
        float vx = 0, vy = 0;
        float vx2 = 0, vy2 = 0;
        for (; ; ) {
            Touch t = waitTouch();
            if (t.count() == 1) {
                vx -= t.dx();
                vy -= t.dy();
                camera().move(-t.dx(), -t.dy());
                if (t.lastTouch()) {
                    return gamer().cameraInertia.setSpeed(vx2, vy2);
                }
            } else {
                Touch t2 = t.next;
                camera().move((-t.dx() - t2.dx()) / 2, (-t.dy() - t2.dy()) / 2);
                float scale = FloatMath.sqrt(len2(t.x - t2.x, t.y - t2.y) /
                        len2(t.oldX() - t2.oldX(), t.oldY() - t2.oldY()));
                camera().scale(scale, (t.x + t2.x) / 2, (t.y + t2.y) / 2);
            }
            if (noTouches()){
                vx2 = vx;
                vy2 = vy;
                vx = 0;
                vy = 0;
                repaint();
            }
        }
    }

    public static float len2(float x, float y) {
        return x * x + y * y;
    }
}
