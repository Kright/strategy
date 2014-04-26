package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * некая область на карте
 * умеет рисовать сама себя
 * Created by lgor on 17.03.14.
 */
public class Region implements iRenderFeature, Iterable<Cell>, Serializable {

    protected List<Cell> cells;
    transient protected Paint p;
    protected Region(List<Cell> cells) {
        this.cells = cells;
        p = new Paint();
        updateAfrerChange();
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
    public void updateAfrerChange(){
        Collections.sort(cells);            //сортируем, чтобы работал бинарный поиск
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        for (Cell c : cells) {
            float y = camera.MapToY(c.y);
            float x = camera.MapToX(c.x, c.y);
            float h = camera.getCellHeight();
            float w = camera.getCellWidth();
            canvas.drawLine(x, y + h / 4, x + w / 2, y, p);
            canvas.drawLine(x + w / 2, y, x + w, y + h / 4, p);
            canvas.drawLine(x, y + h / 4, x, y + h * 3 / 4, p);
            canvas.drawLine(x + w, y + h / 4, x + w, y + h * 3 / 4, p);
            canvas.drawLine(x, y + h * 3 / 4, x + w / 2, y + h, p);
            canvas.drawLine(x + w / 2, y + h, x + w, y + h * 3 / 4, p);
        }
    }

    @Override
    public Iterator<Cell> iterator() {
        return cells.iterator();
    }
}