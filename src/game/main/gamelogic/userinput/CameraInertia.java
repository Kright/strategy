package game.main.gamelogic.userinput;

import android.util.FloatMath;

/**
 * движение камеры по инерции после отпускания
 * прекращается, если вдруг появляется нажатия на экран или становистя слишком маленькой скорость
 * Created by lgor on 04.05.14.
 */
class CameraInertia extends State {

    CameraInertia(Gamer gamer) {
        super(gamer);
    }

    static float k = 0.9f;
    float dx, dy, minV2;

    public CameraInertia setSpeed(float vx, float vy){
        dx = vx;
        dy = vy;
        minV2 = 50 * FloatMath.sqrt(gamer.camera.getScreenWidth() / 1920f);
        return this;
    }

    @Override
    State getNext() {
        if (!gamer.session.touchBuffer.isEmpty() || !gamer.session.running){
            return gamer.screenUpdate;
        }
        dx *= k;
        dy *= k;
        gamer.camera.move(dx, dy);
        gamer.session.repaint();
        if (MapMoving.len2(dx, dy) > minV2){
            return this.setSpeed(dx, dy);
        }
        return gamer.defaultState;
    }
}
