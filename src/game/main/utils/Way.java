package game.main.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Actions.MoveUnit;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.Unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * класс, описывающий доступные для юнита клетки
 * Created by lgor on 25.02.14.
 */
public class Way implements iRenderFeature {

    private List<Cell> cells = new ArrayList<Cell>();
    private Paint p = new Paint();
    private int maxWay=1000;
    private int x0;
    private int y0;
    private int mPoints;
    int[][][] s;
    int[][] k = {{1, 1}, {0, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, 0}};// связь с какими клетками имеется
    public Way(Map map, Unit unit) {
        Cell cell = unit.getCell();
        mPoints = unit.getMovementPoints();
        x0 = cell.x;
        y0 = cell.y;

        int controlSum = 0;
        s= new int[2 * mPoints + 1][2 * mPoints + 1][2];
        for (int i = 0; i < 2 * mPoints + 1; i++)
            for (int j = 0; j < 2 * mPoints + 1; j++) {
                if (!map.getCell(x0 - mPoints + i, y0 - mPoints + j).canMove()) {
                    s[i][j][0] = 1; // клетку посетил
                    s[i][j][1] = maxWay; // путь бусконечный
                    controlSum += 1;
                } else {
                    s[i][j][1] = maxWay;
                    s[i][j][0] = 0; // клетку еще не посещал
                }
            }
        s[mPoints][mPoints][0] = 0;
        s[mPoints][mPoints][1] = 0;



        int x = mPoints;
        int y = mPoints;
        int movCost;
        while (controlSum != (2 * mPoints + 1) * (2 * mPoints + 1)) {
            for (int count = 0; count < 6; count++) {
                if ((x + k[count][0] >= 0) && (x + k[count][0] <= 2 * mPoints) && (y + k[count][1] >= 0) && (y + k[count][1] <= 2 * mPoints)) {
                    movCost = map.getCell(x0 - mPoints + x + k[count][0], y0 - mPoints + y + k[count][1]).getMovindCost();

                    if (mPoints - s[x][y][1] >0) {
                        if ((s[x + k[count][0]][y + k[count][1]][1] > movCost + s[x][y][1])) {
                            s[x + k[count][0]][y + k[count][1]][1] = movCost + s[x][y][1];
                        }

                    }
                }
            }

            s[x][y][0] = 1;
            if (s[x][y][1] <= maxWay-1) {
                cells.add(map.getCell(x0 - mPoints + x, y0 - mPoints + y));
            }
            controlSum += 1;
            if (controlSum == (2 * mPoints + 1) * (2 * mPoints + 1))
                break;
            int min = maxWay + 1;
            for (int i = 0; i < 2 * mPoints + 1; i++)
                for (int j = 0; j < 2 * mPoints + 1; j++) {
                    if (s[i][j][0] == 0) {
                        if (s[i][j][1] < min) {
                            x = i;
                            y = j;
                            min = s[i][j][1];
                        }
                    }
                }
        }
    }

        /*for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (map.getInterval(i, j) == 1) {
                    Cell add = map.getCell(cell.x + i, cell.y + j);
                    if (add.canMove()) {
                        cells.add(add);
                    }
                }
            }
        }*/

    /**
     * заглушка
     * в идеале будет формировать последовательность соседних клеток от первой до конечной
     */
    public Action getMoveTo(Cell c) {
        assert cells.get(0).hasUnit();
        List<Cell> path = new ArrayList<Cell>();
        path.add(c);
        Cell cc=c;
        int x=c.x-x0+mPoints;
        int y=c.y-y0+mPoints;
        // связь с какими клетками имеется
        while(s[x][y][1]!=0){
            for(int i=0; i<6; i++){
                if(s[x + k[i][0]][y + k[i][1]][1] == s[x][y][1]-cc.getMovindCost()){
                    x=x + k[i][0];
                    y=y + k[i][1];
                    break;
                }
            }
        for(Cell cCount: cells)
            if ((cCount.x==x)&&(cCount.y==y)){
                path.add(cCount);
                cc=cCount;
                break;
            }
        }

        Collections.reverse(path);
        /*int m=path.size();

        for (int i=0; i<m/2;i++){
            cc=path.get(i);
            path.set(i,path.get(m-1-i));
            path.set(m-1-i,cc);
        }*/
       // path.add(cells.get(0));
        //path.add(c);
        return new MoveUnit(cells.get(0).getUnit(), path);
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
