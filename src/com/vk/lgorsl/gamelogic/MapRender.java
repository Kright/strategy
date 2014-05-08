package com.vk.lgorsl.gamelogic;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.vk.lgorsl.ActivityS;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.GUI.iRenderFeature;
import com.vk.lgorsl.gamelogic.world.Castle;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.Map;
import com.vk.lgorsl.gamelogic.world.Settlement;
import com.vk.lgorsl.gamelogic.world.utils.GameProperties;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.SpriteBank;
import com.vk.lgorsl.utils.sprites.iRender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * объект, который рисует карту и эффекты на ней
 * Created by lgor on 20.02.14.
 */
public class MapRender extends MapCamera {

    protected final RenderParams renderParams;
    private final iRender[] roads, arrows;
    protected final GameProperties properties;
    private final Matrix mirror, identity;
    private Set<Castle> castleSet =new HashSet<Castle>();

    public MapRender(int spriteHeight, SpriteBank sprites, GameProperties properties) {
        super(spriteHeight / 2 * 3, spriteHeight);
        this.properties = properties;
        renderParams = new RenderParams(new Paint());
        renderParams.paint.setTypeface(ActivityS.font);
        renderParams.paint.setTextSize(36);
        renderParams.paint.setColor(0xFF000055);
        roads = new iRender[]{sprites.getSprite("road100"), sprites.getSprite("road010"),
                sprites.getSprite("road110"), sprites.getSprite("road001"), sprites.getSprite("road101"),
                sprites.getSprite("road011"), sprites.getSprite("road111")};
        arrows = new iRender[]{sprites.getSprite("↗"), sprites.getSprite("→"), sprites.getSprite("↘")};
        identity = new Matrix();
        mirror = new Matrix();
        mirror.setScale(-1, 1);

        Set<Castle> castleSet= new HashSet<Castle>();

    }

    public void render(Canvas canv, Map map) {
        CellIterator iterator = initRender(canv, map);
        drawLands(map, iterator);
        drawUnits(iterator);
    }

    /**
     * универсальный вызов, делает сразу всё
     */
    public void render(Canvas canv, Map map, iRenderFeature feature) {
        CellIterator iterator = initRender(canv, map);
        drawLands(map, iterator);
        feature.render(this, canv);
        drawUnits(iterator);
    }

    /*
     * набор самостоятельного рисовальщика - можно рисовать землю, потом пририсовать что-нибудь на Canvas (например,
     * путь юнита) и потом поверх - юнитов
     */
    public CellIterator initRender(Canvas canv, Map map) {
        setScreenSize(canv.getWidth(), canv.getHeight());
        checkPosition(screenW, screenH, map.width * w, map.height * dy + h - dy);
        renderParams.setCellSize((int) getCellWidth() + 1, (int) getCellHeight() + 1);
        renderParams.canvas = canv;
        canv.drawColor(0xFF444444); //фон
        return getIterator(map, renderParams);
    }

    public void drawLands(Map map, CellIterator iter) {
        drawLandscapeAndRoads(map, iter, renderParams);
        drawFlora(iter, renderParams);
        if (properties.renderBorders){
            drawBorders(renderParams.canvas);
        }
    }

    public void drawUnits(CellIterator iter) {
        drawUnitsAndShadows(iter, renderParams);
        if (properties.showFPS) {
            renderParams.canvas.drawText("fps=" + fps.get(), 20, 30, renderParams.paint);
        }
    }

    private void drawLandscapeAndRoads(Map map, CellIterator iterator, RenderParams renderParams) {
        iterator.clearState();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.render(renderParams);

            if (cell.hasRoad()) {
                int number = map.getRoads(cell) - 1;
                if (number >= 0) {
                    roads[number].render(renderParams);
                }
            }
        }
    }

    private void drawFlora(CellIterator iterator, RenderParams renderParams) {
        castleSet.clear();
        iterator.clearState();
        while (iterator.hasNext()) {
            Cell c = iterator.next();
            c.nextRender.render(renderParams);
            if(c.controlledByCastle()!=null)
            castleSet.add(c.controlledByCastle());
        }
        for(Castle castle: castleSet){
            castle.getControlledRegion().render(this, renderParams.canvas);
        }
    }

    private void drawUnitsAndShadows(CellIterator iterator, RenderParams renderParams) {
        iterator.clearState();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (cell.shadowded) {
                Settlement.shadow.render(renderParams);
            } else {
                if (cell.hasUnit()) {
                    cell.getUnit().render(renderParams);
                }
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
                canv.drawLine(xx, yy + h / 4, xx + w / 2, yy, renderParams.paint);
                canv.drawLine(xx + w / 2, yy, xx + w, yy + h / 4, renderParams.paint);
                canv.drawLine(xx, yy + h / 4, xx, yy + dy, renderParams.paint);
            }
        }
    }

    /**
     * рисует путь красными стрелочками
     * @param path
     */
    public void renderPath(List<Cell> path) {
        if (path.isEmpty())
            return;
        Cell f = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            Cell s = path.get(i);
            int dx = s.x - f.x;
            int dy = s.y - f.y;
            setXY(renderParams, f);
            if (dx == 0 && dy == -1) arrows[0].render(renderParams);
            if (dx == 1 && dy == 0) arrows[1].render(renderParams);
            if (dx == 1 && dy == 1) arrows[2].render(renderParams);
            f = s;
        }
        renderParams.canvas.setMatrix(mirror);
        f = path.get(0);
        int w = - (int) getCellWidth();
        for (int i = 1; i < path.size(); i++) {
            Cell s = path.get(i);
            int dx = s.x - f.x;
            int dy = s.y - f.y;
            setXY(renderParams, f);
            renderParams.x = w - renderParams.x;
            if (dx == -1 && dy == -1) arrows[0].render(renderParams);
            if (dx == -1 && dy == 0) arrows[1].render(renderParams);
            if (dx == 0 && dy == 1) arrows[2].render(renderParams);
            f = s;
        }
        renderParams.canvas.setMatrix(identity);
    }
}
