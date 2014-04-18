package game.main.gamelogic.gamer;

import android.graphics.Canvas;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.MapRender;

/**
 * состояние интерфейса игрока.
 * Например, перемотка карты или ещё что-нибудь
 * Created by lgor on 18.04.14.
 */
abstract class State {
    final Gamer g;
    final GameSession session;

    public State(Gamer gamer, GameSession session) {
        this.g = gamer;
        this.session = session;
    }

    public abstract State getNext();

    public void paint(Canvas canvas, MapRender render) {
        render.render(canvas, g.country.map);
    }

    static float len2(float x, float y) {
        return (x * x + y * y);
    }
}
