package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * замок
 * Created by lgor on 14.03.14.
 */
public class Castle extends Settlement {
    private Region region;    //подконтрольная территория

    protected double levelOfTaxes;
    protected double cultureLevel;
    protected double efficiency;

    public Castle(Country country, Cell cell) {
        super(country, cell);
        ArrayList<Cell> near = new ArrayList<Cell>(7);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Map.getInterval(i, j) < 2) {
                    near.add(country.map.getCell(cell.x + i, cell.y + j));
                }
            }
        }
        region = new Region(near);
        this.country.map.addCellsNear(region.cells, cell.x, cell.y);
        Iterator<Cell> iterator = region.iterator();
        while (iterator.hasNext()) {      //замок может захватить только ничью территорию
            if (iterator.next().controlledByCastle() != null) {
                iterator.remove();
            }
        }
        // only for test!!
        for(Cell c: region){
            if (!c.land.landUpgrades.isEmpty()){
                c.setLandUpgrade(c.land.landUpgrades.get(0));
            }
        }
        region.updateAfrerChange();
        country.map.setCastleControll(this);
    }

    @Override
    public void nextTurn() {
    }

    /**
     * @return область, которую контролирует данный замок
     */
    public Region getControlledRegion() {
        return region;
    }

    /**
     * @return доход от замка. Налог с подконтрольных территорий собирается без ведома замка в классе Country
     */
    @Override
    public int getTaxes() {
        return 0; //и ещё вычесть стоимость содержания замка, которой пока нет
    }

    public void setLevelOfTaxes(double levelOfTaxes) {
        this.levelOfTaxes = levelOfTaxes;
    }

    /**
     * @return доля налогов от доходов поселений феода, от 0 до 1, 0.1 - десятина.
     */
    public double getLevelOfTaxes() {
        return levelOfTaxes;
    }

    /**
     * @return уровень культуры провинции
     * от него зависит максимальный размер городов
     */
    public double getCultureLevel(){
        return cultureLevel;
    }

    /**
     * @return эффективность сбора налогов - не все налоги доходят до казны.
     */
    public double getEfficiency(){
        return efficiency;
    }

    @Override
    public void removeSettlement() {

    }

    protected static Sprite sprite;

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }
}
