package com.vk.lgorsl.gamelogic.world.unit;

import com.vk.lgorsl.gamelogic.world.Action;

/**
 * задача для юнита
 * например, построить за несколько ходов замок или сделать ещё что-нибудь
 * Created by lgor on 18.05.14.
 */
public abstract class UnitTask {

    public abstract UnitTask process(Unit unit);

    public static final UnitTask emptyTask = new UnitTask() {
        @Override
        public UnitTask process(Unit unit) {
            return this;
        }
    };

    public static final UnitTask defence = new UnitTask() {
        @Override
        public UnitTask process(Unit unit) {
            return this;
        }
    };

    public static UnitTask getCastleBuilding() {
        return new CastleBuilding(3);
    }

    public static Action getTaskSettingAction(final Unit unit, final UnitTask task) {
        return new Action() {
            private Unit prevState = unit.getClone();

            @Override
            protected boolean doAction() {
                unit.setTask(task);
                return false;
            }

            @Override
            protected void cancel() {
                //убираем нового юнита, возвращаем его же в предыдущем состоянии
                unit.movementPoints = 0;
                unit.cell.setUnit(null);
                prevState.cell.setUnit(prevState);
            }
        };
    }
}
