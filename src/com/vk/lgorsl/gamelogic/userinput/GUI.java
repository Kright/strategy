package com.vk.lgorsl.gamelogic.userinput;

import android.util.Log;
import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.world.unit.Unit;
import com.vk.lgorsl.gamelogic.world.unit.UnitTask;
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
        return gamer.camera.panelGUI.onTouch(t);
    }

    private Unit unit;

    public State set(Unit unit) {
        this.unit = unit;
        gamer.camera.panelGUI.leftButtonsPanel.setUnit(unit);
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
                    try {
                        if (gamer.camera.panelGUI.rightButtonsPanel.onTouch(t)) {
                            return doActions();
                        }
                        if (gamer.camera.panelGUI.leftButtonsPanel.onTouch(t)) {
                            return doActionsLeft();
                        }
                    } catch (NullPointerException ex) {
                        return gamer.screenUpdate;
                    }
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
                unit = free.get(0);
                gamer.camera.setPosition(unit.getCell());
                setGUIUnit(unit);
                repaint();
                while(!touchesIsEmpty()){
                    touches().getTouch();
                }
                return gamer.unitSecondActivation.setUnit(unit);
            }

            case empty:
                break;

            default:
                throw new UnsupportedOperationException("gamer.GUI : unknown button was pressed");
        }
        return gamer.screenUpdate;
    }

    private State doActionsLeft() {
        set(unit);
        switch (gamer.camera.panelGUI.leftButtonsPanel.getLastState()) {
            case wait:
                unit.country.getFreeUnits().remove(unit);
                break;
            case defence:
                UnitTask.getTaskSettingAction(unit, UnitTask.defence).apply();
                break;
            case buildCastle:
                UnitTask.getTaskSettingAction(unit, UnitTask.getCastleBuilding()).apply();
                break;
            case nothing:
                break;
            default:
                throw new UnsupportedOperationException("gamer.GUI : unknown button was pressed");
        }
        return gamer.checkEndOfTurn;
    }
}
