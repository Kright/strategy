package game.main.gamelogic.gamer;

import android.graphics.Canvas;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.MapRender;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Country;
import game.main.gamelogic.world.Player;
import game.main.utils.Touch;

import java.util.List;

/**
 * игрок и управление
 * Created by lgor on 18.04.14.
 */
public class Gamer extends Player{

    private State currentState;
    MapRender camera;
    Country country;
    List<Touch> touches;

    final DoNothing doNothing;
    //State mapMoving

    public Gamer(GameSession session, Country country) {
        super(session, country);
        this.country = country;
        doNothing = new DoNothing(this, session);
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

    Cell getTrueCell(Touch t) {
        return camera.getCell(country.map.getTrueMap(), t.x, t.y);
    }

    Touch waitTouch(){
        while (touches.isEmpty()){
            waitAndUpdate();
        }
        return touches.remove(0);
    }

    void waitAndUpdate() {
        if (!session.checkPause()) {
            session.sleep(20);
        }
        touches.addAll(session.getTouches());
    }
}
