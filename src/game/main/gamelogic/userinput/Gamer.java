package game.main.gamelogic.userinput;

import android.graphics.Canvas;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.MapRender;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Country;
import game.main.gamelogic.world.Player;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * игрок и управление
 * Мне кажется, получится довольно много кода - фактически, при помощи переключения state сделан конечный автомат.
 * Created by lgor on 18.04.14.
 */
public class Gamer extends Player {

    final Default defaultState = new Default(this);
    final CameraInertia cameraInertia = new CameraInertia(this);
    final MapMoving mapMoving = new MapMoving(this);
    final UnitActivation unitActivation = new UnitActivation(this);
    final UnitSecondActivation unitSecondActivation = new UnitSecondActivation(this);

    private State currentState = defaultState;
    MapRender camera;
    final Country country;
    private final List<Touch> touches = new ArrayList<Touch>();

    public Gamer(GameSession session, Country country) {
        super(session, country);
        this.country = country;
    }

    @Override
    protected void doTurn(MapRender render) {
        camera = render;
        currentState = currentState.getNext();
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        currentState.paint(canvas, render);
    }

    abstract class State {
        public abstract State getNext();

        public void paint(Canvas canvas, MapRender render) {
            render.render(canvas, country.map);
        }

        final Cell getTrueCell(Touch t) {
            return camera.getCell(country.map.getTrueMap(), t.x, t.y);
        }

        /**
         * @return touch or waits while no screen touches
         */
        final Touch waitTouch() {
            while (touches.isEmpty()) {
                waitAndUpdate();
            }
            return touches.remove(0);
        }

        final boolean noTouches(){
            if (!touches.isEmpty()){
                return false;
            }
            touches.addAll(session.getTouches());
            return touches.isEmpty();
        }

        final void waitAndUpdate() {
            if (!session.checkPause()) {
                if (session.screenNotUpdated()){
                    session.repaint();
                } else {
                    session.sleep(20);
                }
            }
            touches.addAll(session.getTouches());
        }

        final void repaint() {
            session.repaint();
        }

        final void needRepaint(){
            session.needUpdate(true);
        }

        /**
         * update screen if last needRepaint() was called later than last repaint()
         */
        final void mayBeRepaint() {
            if (session.screenNotUpdated()){
                session.repaint();
            }
        }

        final MapRender camera(){
            return camera;
        }

        final Gamer gamer(){
            return Gamer.this;
        }

        final boolean checkPause(){
            return session.checkPause();
        }
    }
}
