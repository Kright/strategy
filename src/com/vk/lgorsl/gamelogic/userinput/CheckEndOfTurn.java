package com.vk.lgorsl.gamelogic.userinput;

/**
 * Created by lgor on 08.05.14.
 */
class CheckEndOfTurn extends State {

    CheckEndOfTurn(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        gamer.session.repaint();
        if (gamer.session.properties.autoEndOfTurn && gamer.country.getFreeUnits().isEmpty()) {
            return gamer.endOfTurn;
        }
        return gamer.defaultState;
    }
}
