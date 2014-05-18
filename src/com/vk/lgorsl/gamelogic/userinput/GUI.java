package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.utils.Touch;

/**
 * та самая штука, которая происходит, если нажать на какую нибудь кнопочку.
 * Created by lgor on 09.05.14.
 */
class GUI extends State {

    public boolean interestedInTouch(Touch t) {
        return gamer.camera.panelGUI.rightButtonsPanel.interestedInTouch(t);
    }

    GUI(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        for (; ; ) {
            while (touchesIsEmpty()) {
                if (!gameRunning()) {
                    return gamer.screenUpdate;
                }
                GameSession.sleep(20);
            }
            if (touches().getTouch().lastTouch()){
                break;
            }
        }
        return gamer.endOfTurn;
    }
}
