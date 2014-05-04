package game.main.gamelogic.userinput;

/**
 * состояние, которое обновляет экран и переходит к дефолтному
 * Created by lgor on 04.05.14.
 */
class ScreenUpdate extends State {

    ScreenUpdate(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        gamer.session.repaint();
        if (gamer.session.mustUpdate){
            return this;
        }
        return gamer.defaultState;
    }
}
