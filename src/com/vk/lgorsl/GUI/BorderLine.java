package com.vk.lgorsl.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.iRender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * граница множества клеток.
 * Приспособлена для рисования - можно вычислить её один раз и рисовать её до тех пор, пока не изменится множество
 * клеток (тогда придётся вызвать init() с новым множеством)
 * Created by lgor on 05.05.14.
 */
public class BorderLine implements iRenderFeature {

    public static iRender[][] circles;

    private RenderParams renderParams;
    private int colorNum;
    private List<Task> tasks = new ArrayList<Task>();
    private Set<Integer> exists = new TreeSet<Integer>();

    public BorderLine() {
        colorNum = 0;
        renderParams = new RenderParams(new Paint());
    }

    public void setColorNum(int num) {
        this.colorNum = num;
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        renderParams.canvas = canvas;
        renderParams.setCellSize((int) camera.getCellWidth(), (int) camera.getCellHeight());
        for (Task t : tasks) {
            renderParams.x = camera.MapToX(t.x, t.y);
            renderParams.y = camera.MapToY(t.y);
            circles[colorNum][t.num].render(renderParams);
        }
    }

    public void init(List<Cell> cells) {
        tasks.clear();
        exists.clear();
        for (Cell c : cells) {
            exists.add(pack(c.x, c.y));
        }
        boolean[] b = new boolean[6];
        for (Cell c : cells) {
            b[0] = hasNeighbor(c.x, c.y - 1);
            b[1] = hasNeighbor(c.x + 1, c.y);
            b[2] = hasNeighbor(c.x + 1, c.y + 1);
            b[3] = hasNeighbor(c.x, c.y + 1);
            b[4] = hasNeighbor(c.x - 1, c.y);
            b[5] = hasNeighbor(c.x - 1, c.y - 1);
            if (b[0] && b[1] && b[2] && b[3] && b[4] && b[5]) continue;
            for (int i = 0; i < 6; i++) {
                int j = (i + 1) % 6;
                if (!b[i] && !b[j]) {
                    tasks.add(new Task(c.x, c.y, j));
                }
            }
            if (!b[0] && b[1]) tasks.add(new Task(c.x, c.y - 1, 6 + 3));
            if (!b[1] && b[2]) tasks.add(new Task(c.x + 1, c.y, 6 + 4));
            if (!b[2] && b[3]) tasks.add(new Task(c.x + 1, c.y + 1, 6 + 5));
            if (!b[3] && b[4]) tasks.add(new Task(c.x, c.y + 1, 6 + 0));
            if (!b[4] && b[5]) tasks.add(new Task(c.x - 1, c.y, 6 + 1));
            if (!b[5] && b[0]) tasks.add(new Task(c.x - 1, c.y - 1, 6 + 2));
        }
    }

    private boolean hasNeighbor(int x, int y) {
        return exists.contains(pack(x, y));
    }

    private static int pack(int x, int y) {
        return (x << 12) | y;
    }

    private static class Task {
        final int x, y, num;

        Task(int x, int y, int num) {
            this.x = x;
            this.y = y;
            this.num = num;
        }
    }
}
