package game.main.gamelogic.userinput;

import android.util.FloatMath;

/**
 * перемещении камеры с уменьшающейся скоростью
 * Created by lgor on 18.04.14.
 */
class CameraInertia extends Gamer.State{

    public CameraInertia(Gamer gamer){
        gamer.super();
    }

    static float k = 0.9f;
    float dx, dy, minV2;

    public CameraInertia setSpeed(float vx, float vy){
        //TODO
        dx = vx;
        dy = vy;
        minV2 = 50 * FloatMath.sqrt(camera().getScreenWidth() / 1920f);
        return this;
    }

    @Override
    public Gamer.State getNext() {
        while( noTouches() && MapMoving.len2(dx, dy) > minV2 && !checkPause()){
            dx *= k;
            dy *= k;
            camera().move(dx, dy);
            repaint();
        }
        return gamer().defaultState;
    }
}
