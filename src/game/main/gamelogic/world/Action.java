package game.main.gamelogic.world;

/**
 * действие игрока над миром - команда юниту, выбор улучшения города, дипломатическое соглашение и т.п.
 * нужен единый интерфейс для возможности отмены действия в случае ошибки пользователя
 * <p/>
 * Action.addUnit(), Action.addSettlement() дают Action, добавляющие юнита или поселение на карту
 * <p/>
 * Created by lgor on 08.01.14.
 */
public abstract class Action {
    protected static World world;

    /**
     * просто вызвать снаружи эту штуку - мир изменится
     */
    public final void apply() {
        if (doAction()) {
            world.lastAction = this;
        }
    }

    /**
     * возвращает true если было успешно применено
     */
    protected abstract boolean doAction();

    /**
     * отменить действие.
     * Может быть вызвано, только если doAction возвратит true!
     */
    protected abstract void cancel();

    public static void init(World w) {
        world = w;
    }

    /**
     * Action отмены предыдущего action, отменить отмену нельзя
     */
    public static Action getCancelAction() {
        return cancel;
    }

    private static Action cancel = new Action() {
        @Override
        protected boolean doAction() {
            world.cancelPreviousAction();
            return true;
        }

        @Override
        protected void cancel() {
        }  //нечего отменять отмену предыдущего действия
    };

    /**
     * Action, метод apply не изменяет вообще ничего, как будто его и не было.
     */
    public static Action getNullAction() {
        return nullAction;
    }

    private static Action nullAction = new Action() {
        @Override
        protected boolean doAction() {
            // всё время возвращается false, действие не считается применённым и не отмечается как "последнее"
            return false;
        }

        @Override
        protected void cancel() {
        }
    };

    /**
     * добавление поселения на карту
     */
    public static Action addSettlement(final Settlement settlement, final int x, final int y) {
        return new Action() {
            @Override
            protected boolean doAction() {
                Cell cell = world.map.getCell(x, y);
                if (cell.accessible() && !cell.hasSettlement()) {
                    world.map.addSettlement(settlement, x, y);
                    return true;
                }
                return false;
            }

            @Override
            protected void cancel() {
                world.map.addSettlement(null, x, y);
            }
        };
    }

    //добавление юнита на акрту
    public static Action addUnit(final Unit unit, final int x, final int y) {
        return new Action() {
            @Override
            protected boolean doAction() {
                if (!world.map.getCell(x, y).canMove(unit))
                    return false;
                world.map.setUnit(unit, x, y);
                return true;
            }

            @Override
            protected void cancel() {
                unit.getCell().setUnit(null);
            }
        };
    }
}
