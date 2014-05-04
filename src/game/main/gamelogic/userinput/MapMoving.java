package game.main.gamelogic.userinput;

import android.util.FloatMath;
import game.main.gamelogic.GameSession;
import game.main.utils.Touch;

/**
 * состояние, в котором игрок двигает камеру по карте или меняет её масштаб
 * в конце переходит в состояние "CamInertia"
 * Created by lgor on 04.05.14.
 */
class MapMoving extends State {

    MapMoving(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        float vx = 0, vy = 0;
        float vx2 = 0, vy2 = 0;
        for (; ; ) {
            while (gamer.session.touchBuffer.isEmpty()) {
                if (!gamer.session.running) {
                    return gamer.screenUpdate;
                }
                GameSession.sleep(20);
            }
            Touch t = gamer.session.touchBuffer.getTouch();
            if (t.count() == 1) {
                vx -= t.dx();
                vy -= t.dy();
                gamer.camera.move(-t.dx(), -t.dy());
                if (t.lastTouch()) {
                    return gamer.cameraInertia.setSpeed(vx2, vy2);
                }
            } else {
                Touch t2 = t.next;
                gamer.camera.move((-t.dx() - t2.dx()) / 2, (-t.dy() - t2.dy()) / 2);
                float scale = FloatMath.sqrt(len2(t.x - t2.x, t.y - t2.y) /
                        len2(t.oldX() - t2.oldX(), t.oldY() - t2.oldY()));
                gamer.camera.scale(scale, (t.x + t2.x) / 2, (t.y + t2.y) / 2);
            }
            if (gamer.session.touchBuffer.isEmpty()) {
                vx2 = vx;
                vy2 = vy;
                vx = 0;
                vy = 0;
                gamer.session.repaint();
            }
        }
    }

    public static float len2(float x, float y) {
        return x * x + y * y;
    }
}
