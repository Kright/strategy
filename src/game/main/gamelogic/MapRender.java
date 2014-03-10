package game.main.gamelogic;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.GUI.GamePanel;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.world.World;

import java.util.Iterator;

/**
 * объект, который рисует карту и эффекты на ней
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

    public void render(GameSession session, Canvas canv, GamePanel panel) {
        //setScreenSize(canv.getWidth(), canv.getHeight());
        setScreenSize(panel.getFreeRight(canv.getWidth()), canv.getHeight());
        checkPosition(screenW, screenH, session.world.map.width * w, session.world.map.height * dy + h - dy);

        canv.drawColor(0xFFFF00FF); //фон
        drawLandscape(session.world, canv, null);
        if (session.properties.renderBorders) {
            drawBorders(canv);
        }
        for (iRenderFeature rf : session.currentPlayer.getRenderFeatures()) {
            rf.render(this, canv);
        }
        drawUnits(session.world, canv, null);
        if (session.properties.showFPS)
            canv.drawText("fps=" + fps.get(), 20, 20, p);
    }

    private void drawLandscape(World world, Canvas canv, Paint paint) {
        Iterator<RenderObject> iter = getIterator(world.map);
        while (iter.hasNext()) {
            RenderObject ro = iter.next();
            ro.cell.render(canv, ro.rect, paint);
        }
    }

    private void drawUnits(World world, Canvas canvas, Paint paint) {
        Iterator<RenderObject> iter = getIterator(world.map);
        while (iter.hasNext()) {
            RenderObject ro = iter.next();
            if (ro.cell.hasUnit()) {
                ro.cell.getUnit().render(canvas, ro.rect, paint);
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
