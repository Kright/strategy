package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.utils.Touch;

/**
 * дефолтное состояние - длёт нажатий на экран
 * Created by lgor on 04.05.14.
 */
class Default extends State {

    Default(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        if (gamer.session.mustUpdate) {
            gamer.session.repaint();
        }
        if (gamer.session.touchBuffer.isEmpty()) {
            GameSession.sleep(20);
        } else {
            Touch t = gamer.session.touchBuffer.getTouchWithoutRemove();
            Cell c = getTrueCell(t);
            if (c.hasUnit()) {
                return gamer.unitFirstActivation.setUnit(c.getUnit());
            } else {
                return gamer.mapMoving;
            }
        }
        return gamer.defaultState;
    }
}
