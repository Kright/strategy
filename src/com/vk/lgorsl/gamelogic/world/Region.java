package com.vk.lgorsl.gamelogic.world;

import android.graphics.Canvas;
import android.util.Log;
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
        //return cells.contains(cell);
        int result = Collections.binarySearch(cells, cell);
        if (cells.contains(cell) != (result > -1)) {
            Log.d("mylog", "number of cell is " + result  + ", " + cell + "\n" + this);
        }
        return result > -1;
    }

    /**
     * добавляет клетки в регион
     *
     * @param list
     */
    public void addCells(List<Cell> list) {
        cells.addAll(list);
        updateAfrerChange();
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
        for (Cell c : cells) {
            sb.append(c.toString() + ",");
        }
        sb.append('}');
        return sb.toString();
    }
}