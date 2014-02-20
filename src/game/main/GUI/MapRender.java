package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.gamelogic.GameProperties;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.World;

import java.util.List;

/**
 * Created by lgor on 20.02.14.
 */
public class MapRender extends MapCamera {

    private Paint p = new Paint();

    {
        p.setTextSize(p.getTextSize() * 2);
    }

    public MapRender(int spriteWidth, int spriteHeight) {
        super(spriteWidth, spriteHeight);
    }

    public void render(World world, Canvas canv, GameProperties properties, List<iRenderFeature> features) {
        screenH = canv.getHeight();
        screenW = canv.getWidth();
        checkPosition(screenW, screenH);

        canv.drawColor(0xFFFF00FF); //фон
        drawLandscape(world, canv);
        if (properties.renderBorders) {
            drawBorders(canv);
        }
        if (features != null)
            for (iRenderFeature rf : features)
                rf.render(this, canv);
        drawUnits(world, canv);
        if (properties.showFPS)
            canv.drawText("fps=" + fps.get(), 20, 20, p);
    }

    private void drawLandscape(World world, Canvas canv) {
        int minY = Math.max(0, (int) YonMap(0) - 1);
        int maxY = Math.min(world.map.height, (int) YonMap(screenH) + 1);
        Rect r = new Rect();
        for (int y = minY; y < maxY; y++) {
            int minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
            int maxX = Math.min(world.map.width, (int) XonMap(screenW, y * dy - position.y + 1) + 1);
            int yy = MapToY(y);
            for (int x = minX; x < maxX; x++) {
                int xx = MapToX(x, y);
                r.set(xx - 1, yy - 1, xx + (int) (w + 1), yy + (int) (h + 1));  //рисуем немного внахлёст
                world.map.getCell(x, y).render(canv, r);
            }
        }
    }

    private void drawUnits(World world, Canvas canvas) {
        int minY = Math.max(0, (int) YonMap(0) - 1);
        int maxY = Math.min(world.map.height, (int) YonMap(screenH) + 1);
        Rect r = new Rect();
        for (int y = minY; y < maxY; y++) {
            int minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
            int maxX = Math.min(world.map.width, (int) XonMap(screenW, y * dy - position.y + 1) + 1);
            int yy = MapToY(y);
            for (int x = minX; x < maxX; x++) {
                Cell c = world.map.getCell(x, y);
                if (!c.hasUnit()) {
                    continue;
                }
                int xx = MapToX(x, y);
                r.set(xx - 1, yy - 1, xx + (int) (w + 1), yy + (int) (h + 1));  //рисуем немного внахлёст
                c.getUnit().render(canvas, r);
            }
        }
    }

    private void drawBorders(Canvas canv) {
        int minY = (int) YonMap(0) - 1;
        int maxY = (int) YonMap(screenH) + 1;
        for (int y = minY; y < maxY; y++) {
            int minX = (int) XonMap(0, y * dy - position.y + 1);
            int maxX = (int) XonMap(screenW, y * dy - position.y + 1) + 1;
            float yy = y * dy - position.y;
            for (int x = minX; x < maxX; x++) {
                float xx = x * w - y * w * 0.5f - position.x;
                canv.drawLine(xx, yy + h / 4, xx + w / 2, yy, p);
                canv.drawLine(xx + w / 2, yy, xx + w, yy + h / 4, p);
                canv.drawLine(xx, yy + h / 4, xx, yy + dy, p);
            }
        }
    }
}
