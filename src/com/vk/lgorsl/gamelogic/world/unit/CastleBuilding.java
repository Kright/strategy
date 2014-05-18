package com.vk.lgorsl.gamelogic.world.unit;

import android.util.Log;
import com.vk.lgorsl.gamelogic.world.Action;

/**
 * строительство замка юнитом
 * Created by lgor on 18.05.14.
 */
class CastleBuilding extends UnitTask {

    private int time;

    CastleBuilding(int time) {
        this.time = time;
    }

    @Override
    public UnitTask process(Unit unit) {
        time--;
        Log.e("wtf?", "Build castle, time = " + time);
        if (time > 0) {
            unit.movementPoints = 0;
            return this;
        }
        Log.e("wtf?", "Build castle, time less then zero " + time);
        if (unit.cell.controlledByCastle() == null && !unit.cell.hasSettlement()) {
            //TODO test!
            unit.buildCastle().apply();
            Action.UnCanceledAction.apply();
        }
        return emptyTask;
    }
}
