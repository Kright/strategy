package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;

/**
 * Created by lgor on 17.02.14.
 * добавочная штука, рисует выделение юнита
 */
public class UnitSelection implements iRenderFeature {

    private final Unit unit;
    private final Paint paint;

    public UnitSelection(Unit unit) {
        this.unit = unit;
        paint = new Paint();
        paint.setColor(0xFF0022BB);
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        Cell p = unit.getCell();
        assert p != null;
        float w = camera.getCellWidth();
        float h = camera.getCellHeight();
        float cx = camera.MapToX(p.x, p.y) + w / 2;
        float cy = camera.MapToY(p.y) + h / 2;
        float scale = 0.3f;
        RectF r = new RectF(cx - w * 0.5f * scale, cy - h * 0.433f * scale, cx + w * 0.5f * scale, cy + h * 0.433f * scale);
        canvas.drawOval(r, paint);
    }
}
