package game.main.gamelogic.world;

/**
 * Created by lgor on 08.01.14.
 */
public abstract class Action {
    /**
    действие игрока над миром - команда юниту, выбор улучшения города, дипломатическое соглашение и т.п.
    нужен единый интерфейс для возможности отмены действия в случае ошибки пользователя
     */
    private final World world;

    protected Action(World world){
        this.world=world;
    }

    /**
     *  просто вызвать снаружи эту штуку - мир изменится
     */
    public final void apply() {
        if (doAction()){
            world.lastAction=this;
        }
    }

    /**
     * возвращает true если было успешно применено
     */
    protected abstract boolean doAction();

    protected abstract void cancel();
}
