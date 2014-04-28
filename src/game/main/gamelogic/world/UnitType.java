package game.main.gamelogic.world;

import game.main.utils.sprites.iRender;

/**
 * Created by lgor on 08.01.14.
 * <p/>
 * Тип юнита. Параметры атаки, защиты, условия постройки, доступные команды(атака, строительство, повышение и т.п.),
 * картинка, возможно, анимация и прочее
 */
public class UnitType {
    public String name;

    public final int movementPoints, hitPoints, pay;    //pay - стоимость содержания юнита

    /*
    float attack, defence, //атака и защита - способность наносить урон в ближнем бою,
          armor;           //armor - спопосбность снизить урон.

    boolean canRangeAttack;
    float rangeAttack;
    int distance;
    */

    public final iRender sprite;

    public UnitType(int movementPoints, int hitPoints, int pay, iRender sprite) {
        this.movementPoints = movementPoints;
        this.hitPoints = hitPoints;
        this.sprite = sprite;
        this.pay = pay;                                                           //заглушка
    }
}