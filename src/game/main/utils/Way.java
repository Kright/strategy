package game.main.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * класс, описывающий доступные для юнита клетки
 * Created by lgor on 25.02.14.
 */
public class Way implements iRenderFeature {

    private List<Cell> cells = new ArrayList<Cell>();
    private Paint p = new Paint();

    public Way(Map map, Unit unit) {
        Cell cell = unit.getCell();
        cells.add(cell);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (map.getInterval(i, j) == 1) {
                    Cell add = map.getCell(cell.x + i, cell.y + j);
                    if (add.canMove()) {
                        cells.add(add);
                    }
                }
            }
        }
    }

    //заглушка
    public Action getMoveTo(Cell c) {
        return null;
    }

    public boolean isInto(Cell c) {
        return cells.indexOf(c) != -1;
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
}
