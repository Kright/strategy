package game.main.gamelogic.world;

import android.graphics.*;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.utils.sprites.AdvancedSprite;
import game.main.utils.sprites.RenderParams;
import java.util.List;

/**
 * Created by arturor on 02.04.2014.
 * Рисуем стрелочки по пути, где идет Unit
 * NE - северо-восток
 * E - восток
 * SE - юго-восток
 * SW - юго-запад
 * W - запад
 * NW - северо-запад
 */

public class PathPaint implements iRenderFeature {

    public static AdvancedSprite[] arrows;
    protected List<Cell> path;
    protected Paint p;

    public PathPaint(List<Cell> path) {
        this.path = path;
        p = new Paint();
    }

    public void render(MapCamera camera, Canvas canvas) {
        int i;
        RenderParams renderParams = new RenderParams(p);
        renderParams.setCellSize((int) camera.getCellWidth() + 1, (int) camera.getCellHeight() + 1);
        renderParams.canvas = canvas;

        for (i = 0; i < path.size(); i++) {

            if ((path.get(i + 1).x - path.get(i).x == 0) && (path.get(i + 1).y - path.get(i).y == -1)) {    // NE
                arrows[0].render(renderParams);
            }
            if ((path.get(i + 1).x - path.get(i).x == 1) && (path.get(i + 1).y - path.get(i).y == 0)) {    // E
                arrows[1].render(renderParams);
            }
            if ((path.get(i + 1).x - path.get(i).x == 1) && (path.get(i + 1).y - path.get(i).y == 1)) {    // SE
                arrows[2].render(renderParams);
            }
            if ((path.get(i + 1).x - path.get(i).x == 0) && (path.get(i + 1).y - path.get(i).y == 1)) {    // SW
                arrows[3].render(renderParams);
            }
            if ((path.get(i + 1).x - path.get(i).x == -1) && (path.get(i + 1).y - path.get(i).y == 0)) {    // W
                arrows[4].render(renderParams);
            }
            if ((path.get(i + 1).x - path.get(i).x == -1) && (path.get(i + 1).y - path.get(i).y == -1)) {    // NW
                arrows[5].render(renderParams);
            }

        }
    }
}

