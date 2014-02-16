package game.main.gamelogic.meta;

import game.main.GUI.Sprite;

/**
 * Created by lgor on 08.01.14.
 * <p/>
 * Тип юнита. Параметры атаки, защиты, условия постройки, доступные команды(атака, строительство, повышение и т.п.),
 * картинка, возможно, анимация и прочее
 */
public class UnitType {
    public String name;

    public final int movementPoints, hitPoints; //у мирных hitPoints == 0

    /*
    float attack, defence, //атака и защита - способность наносить урон в ближнем бою,
          armor;           //armor - спопосбность снизить урон.

    boolean canRangeAttack;
    float rangeAttack;
    int distance;
    */

    public final Sprite sprite;

    public UnitType(int movementPoints, int hitPoints, Sprite sprite) {
        this.movementPoints = movementPoints;
        this.hitPoints = hitPoints;
        this.sprite = sprite;
    }
}