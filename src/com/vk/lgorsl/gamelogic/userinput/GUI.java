package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.world.Unit;
import com.vk.lgorsl.utils.Touch;

import java.util.List;

/**
 * та самая штука, которая происходит, если нажать на какую нибудь кнопочку.
 * Created by lgor on 09.05.14.
 */
class GUI extends State {

    GUI(Gamer gamer) {
        super(gamer);
    }

    public boolean interestedInTouch(Touch t) {
        return gamer.camera.panelGUI.rightButtonsPanel.onTouch(t);
    }

    public State set(Unit unit) {
        //TODO спец кнопочки для юнитов
        return this;
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
            while (!touchesIsEmpty()) {
                Touch t = touches().getTouch();
                gamer.camera.panelGUI.onTouch(t);
                if (t.lastTouch()) {
                    return doActions();
                }
            }
            repaint();
        }
    }

    private State doActions() {
        switch (gamer.camera.panelGUI.rightButtonsPanel.getLastPressedButton()) {
            case nextTurn:
                repaint();
                return gamer.endOfTurn;

            case cancel:
                gamer.session.getWorld().cancelPreviousAction();
                return gamer.screenUpdate;

            case selectUnit: {
                List<Unit> free = gamer.country.getFreeUnits();
                if (free.isEmpty()) {
                    break;
                }
                Unit unit = free.get(0);
                gamer.camera.setPosition(unit.getCell());
                return gamer.unitSecondActivation.setUnit(unit);
            }
            case empty:
                break;

            default:
                throw new UnsupportedOperationException("gamer.GUI : unknown button was pressed");
        }
        return gamer.screenUpdate;
    }
}
