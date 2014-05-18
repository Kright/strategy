package com.vk.lgorsl.gamelogic.world;

import android.graphics.Canvas;
import com.vk.lgorsl.GUI.BorderLine;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.GUI.iRenderFeature;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * некая область на карте
 * умеет рисовать сама себя
 * Created by lgor on 17.03.14.
 */
public class Region implements iRenderFeature, Iterable<Cell> {

    protected List<Cell> cells;
    private BorderLine borderLine;
    private boolean borderInit = false;

    protected Region(List<Cell> cells) {
        this.cells = cells;
        borderLine = new BorderLine();
        updateAfrerChange();
    }

    public void setColorNum(int colorNum) {
        borderLine.setColorNum(colorNum);
    }

    /**
     * принадлежит ли клетка области
     */
    public boolean isInto(Cell cell) {
        return -1 != Collections.binarySearch(cells, cell);
    }

    /**
     * обновление внутреннего состояния, после того как были добавлены или убраны новые клетки.
     */
    public void updateAfrerChange() {
        Collections.sort(cells);            //сортируем, чтобы работал бинарный поиск
        borderInit = false;
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        if (!borderInit) {
            borderInit = true;
            borderLine.init(cells);
        }
        borderLine.render(camera, canvas);
    }

    @Override
    public Iterator<Cell> iterator() {
        return cells.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size = " + cells.size());
        sb.append('{');
        for(Cell c: cells){
            sb.append(c.toString()+",");
        }
        sb.append('}');
        return sb.toString();
    }
}