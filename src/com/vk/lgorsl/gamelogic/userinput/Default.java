package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.utils.Touch;

/**
 * дефолтное состояние - ждёт нажатий на экран
 * Created by lgor on 04.05.14.
 */
class Default extends State {

    Default(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        setGUIUnit(null);
        if (gamer.session.mustUpdate) {
            repaint();
        }
        if (touches().isEmpty()) {
            GameSession.sleep(20);
        } else {
            Touch t = touches().getTouchWithoutRemove();
            if (gamer.gui.interestedInTouch(t)) {
                return gamer.gui;
            }
            Cell c = getTrueCell(t);
            if (c.hasUnit()) {
                if (c.getUnit().country == gamer.country){
                    return gamer.unitFirstActivation.setUnit(c.getUnit());
                }
            } else {
                return gamer.mapMoving;
            }
        }
        return gamer.defaultState;
    }
}
