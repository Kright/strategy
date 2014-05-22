package com.vk.lgorsl.gamelogic.world;

import android.util.Log;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.iRender;

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

    protected List<Settlement> settlements = new ArrayList<Settlement>();

    public Castle(Country country, Cell cell) {
        super(country, cell);
        ArrayList<Cell> near = new ArrayList<Cell>(7);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Map.getInterval(i, j) < 2) {
                    if(country.map.getCell(cell.x + i, cell.y + j).accessible()){
                    near.add(country.map.getCell(cell.x + i, cell.y + j));
                    }
                }
            }
        }
        region = new Region(near);
        region.setColorNum(country.id);
        //this.country.map.addCellsNear(region.cells, cell.x, cell.y);
        Iterator<Cell> iterator = region.iterator();
        Cell c;
        while (iterator.hasNext()) {      //замок может захватить только ничью территорию
            if ((c=iterator.next()).controlledByCastle() != null) {
                iterator.remove();
            }else{
                c.setCastleControl(this);
                if(c.hasSettlement()&& !(c.getSettlement() instanceof Castle)){
                    settlements.add(c.getSettlement());

                }

            }

        }

        region.updateAfrerChange();

        country.map.setCastleControll(this);
    }

    @Override
    public void nextTurn(){
        for(Settlement s:settlements){
            s.nextTurn();
        }
        //risingRegion();
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
        int taxes=0;
        for(Cell c: region){
            if(c.hasSettlement()&&c.getSettlement()!=this){
            taxes+=c.getSettlement().getTaxes();
            }
        }
        return taxes; //и ещё вычесть стоимость содержания замка, которой пока нет
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
    public double getCultureLevel() {
        return cultureLevel;
    }

    /**
     * @return эффективность сбора налогов - не все налоги доходят до казны.
     */
    public double getEfficiency() {
        return efficiency;
    }

    public void risingRegion(){
        List<Cell> listci=new ArrayList<Cell>(); // сюда добавляем все соседние клекти
        List<Cell> listc=new ArrayList<Cell>();
        Cell c;
        Iterator<Cell> i= region.iterator();

        while(i.hasNext()){
            listci.clear();
            c=i.next();
            country.map.getTrueMap().addCellsNear(listci, c.x, c.y, 1);
            for(Cell cellI: listci){


                if((!region.isInto(cellI))&& (cellI.controlledByCastle()== null)&& (!listc.contains(cellI))&&(cellI.accessible())){

                    cellI.setCastleControl(this);
                    if(cellI.hasSettlement()){
                        settlements.add(cellI.getSettlement());
                        cellI.getSettlement().setCountry(this.country);
                    }
                    listc.add(cellI);

                }
            }
        }

        region.addCells(listc);


    }


    /**
     *
     * @return возвращает поселения подконтрольные замку.
     */

    public List<Settlement> getSettlements(){
        return settlements;
    }

    @Override
    public void removeSettlement() {

    }

    protected static iRender sprite;

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }
}
