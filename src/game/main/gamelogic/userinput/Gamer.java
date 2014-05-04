package game.main.gamelogic.userinput;

import android.graphics.Canvas;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.MapRender;
import game.main.gamelogic.world.Country;
import game.main.gamelogic.world.Player;

/**
 * интерфейс игрока
 * Created by lgor on 04.05.14.
 */
public class Gamer extends Player {

    private State currentState;
    MapRender camera;

    final Default defaultState = new Default(this);
    final MapMoving mapMoving = new MapMoving(this);
    final EndOfTurn endOfTurn = new EndOfTurn(this);
    final CameraInertia cameraInertia = new CameraInertia(this);
    final ScreenUpdate screenUpdate = new ScreenUpdate(this);
    final UnitFirstActivation unitFirstActivation = new UnitFirstActivation(this);
    final UnitSecondActivation unitSecondActivation = new UnitSecondActivation(this);

    public Gamer(GameSession sessionS, Country country) {
        super(sessionS, country);
        currentState = defaultState;
    }

    @Override
    protected void doTurn(MapRender render) {
        camera = render;
        currentState = defaultState;
        for(;;) {
            currentState = currentState.getNext();
            if (currentState == endOfTurn || session.mustStop){
                return;
            }
        }
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        camera = render;
        currentState.paint(canvas, render);
    }
}
