package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.GameSession;

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
        setGUIUnit(null);
        repaint();
        if (gamer.session.mustUpdate){
            GameSession.sleep(40);
            return this;
        }
        return gamer.defaultState;
    }
}
