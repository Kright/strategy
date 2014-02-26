package game.main.gamelogic.world;

/**
 * Created by lgor on 08.01.14.
 */
public abstract class Action {
    /**
     * действие игрока над миром - команда юниту, выбор улучшения города, дипломатическое соглашение и т.п.
     * нужен единый интерфейс для возможности отмены действия в случае ошибки пользователя
     */
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
}
