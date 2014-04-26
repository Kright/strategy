package game.main.gamelogic.world;

import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.utils.Touch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * абстрактный класс игрока
 * Created by lgor on 09.02.14.
 */
public abstract class Player implements Serializable{

    protected final World world;
    protected List<iRenderFeature> features = new ArrayList<iRenderFeature>();
    protected Country country;

    public Player(World world, Country country) {
        this.world = world;
        this.country = country;
    }

    public List<iRenderFeature> getRenderFeatures() {
        return features;
    }

    public Country getCountry(){
        return country;
    }

    /*
    обновить состояние городов, казны и т.п.
    вызывается до того, как будет вызван update
     */
    public void startNextTurn() {
        country.startNextTurn();
    }

    /*
    вызывается после update==false
    подлечить юнитов, восполнить очки ходов и т.п.
     */
    public void beforeEndTurn() {
        country.beforeEndTurn();
    }

    /*
    вызывается до тех пор, пока не вернёт fasle.
    между вызовами мир рисуется на экран
     */
    public abstract boolean update(MapCamera camera, List<Touch> touches);
}
