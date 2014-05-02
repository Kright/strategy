package game.main.openGL;

import android.util.FloatMath;
import game.main.gamelogic.world.Cell;
import game.main.utils.Vector2f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Iterator;

/**
 * игровая камера
 * размер экрана: от -1 до 1 по каждой из осей
 * Created by lgor on 01.05.14.
 */
public class MapCameraGL {

    public interface CellIterator extends Iterator<Cell>, Iterable<Cell> {

        public float getX();

        public float getY();

        /**
         * после вызова этого метода итератор начинает обход сначала
         * Можно использовать вместо того, чтобы создавать новый итератор.
         */
        public void clearState();
    }

    protected Vector2f position;        //позиция центра камеры на карте (центра экрана)
    protected Vector2f spriteSize;
    protected float dH;                 //== spriteSize*0.75

    public MapCameraGL() {
        position = new Vector2f();
        spriteSize = new Vector2f();
    }

    public void scale(float mul, float screenRatio) {
        setSpriteSize(spriteSize.y * mul, screenRatio);
    }

    public void setSpriteSize(float height, float screenRatio){
        spriteSize.y = height;
        spriteSize.x = spriteSize.y * 1.5f / screenRatio;
        dH = spriteSize.y * 0.75f;
    }

    /**
     * @param yCoord from [-1,1]
     * @return y coordinate of map cell
     */
    public int getYonMap(float yCoord) {
        return Math.round((position.y - yCoord) / dH);
    }

    public int getXonMap(float x, float y) {
        return Math.round((position.x + x + spriteSize.x * 0.5f * getYonMap(y)) / spriteSize.x);
    }

    /**
     * @param y - cell.y
     * @return y , screen coordinate from [-1,1]
     */
    public float getMapToY(int y) {
        return -y * dH + position.y;
    }

    public float getMapToX(int x, int y) {
        return (x - y * 0.5f) * spriteSize.x - position.x;
    }

    public Grid getTestGrid() {
        return new Grid();
    }

    /**
     * TODO переделать, сейчас тестовый варинат
     */
    public class Grid {

        public int rectsNum;
        public FloatBuffer aExtPos, vertPos;

        public Grid() {
        }

        public void mapTableLoad(CellIterator iterator) {
            int maxCount = 3000;
            float[] f = new float[maxCount * 6 * 4];
            iterator.clearState();
            int count;
            for (count = 0; iterator.hasNext() && count < maxCount; count++) {
                putRect(f, count, iterator.getX(), iterator.getY());
                iterator.next();
            }
            float[] ff;
            rectsNum = count;
            if (count == maxCount) {
                ff = f;
            } else {
                ff = new float[count * 24];
                System.arraycopy(f, 0, ff, 0, ff.length);
            }
            aExtPos = makeBuf(ff);
            vertPos = makeBuf(new float[count * 12]);   //TODO убрать потом эту строчку
        }

        public Vector2f getSpriteSize() {
            return spriteSize;
        }

        void putRect(float[] f, int pos, float x, float y) {
            pos *= 6;
            putV(f, pos, x, y, 0, 1);
            putV(f, pos + 1, x, y, 1, 1);
            putV(f, pos + 2, x, y, 1, 0);
            putV(f, pos + 3, x, y, 0, 1);
            putV(f, pos + 4, x, y, 1, 0);
            putV(f, pos + 5, x, y, 0, 0);
        }

        void putV(float[] f, int pos, float x, float y, float dx, float dy) {
            pos *= 4;
            f[pos] = x;
            f[pos + 1] = y;
            f[pos + 2] = dx;
            f[pos + 3] = dy;
        }
    }

    public static FloatBuffer makeBuf(float[] f) {
        FloatBuffer result = ByteBuffer.allocateDirect(f.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.position(0);
        result.put(f);
        return result;
    }

    /**
     * bounds may be not correct.
     * minX < maxX
     * minY < maxY
     */
    protected MapCameraGL.CellIterator getIterator(/*final Map map, */final float minX,
                                                   final float minY, final float maxX, final float maxY) {
        return new MapCameraGL.CellIterator() {
            private int x;
            private int y;
            private boolean hasNext;

            {
                clearState();
            }

            @Override
            public void clearState() {
                y = getYonMap(maxY);
                x = getXonMap(maxX, maxY);
                hasNext = true;
            }

            private void incXY() {
                x--;    //неочевидно, но название inc более логично, как-никак переход к следующей паре чисел
                if (getMapToX(x, y) < minX) {
                    y++;
                    float yc = getMapToY(y);
                    if (yc < minY) {
                        hasNext = false;
                        return;
                    }
                    x = getXonMap(maxX, yc);
                }
            }

            @Override
            public Iterator<Cell> iterator() {
                return this;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Cell next() {
                //Cell c = map.getCell(x, y);
                incXY();
                //return c;
                return null;
            }

            @Override
            public float getX() {
                return getMapToX(x, y);
            }

            @Override
            public float getY() {
                return getMapToY(y);
            }

            @Override
            public void remove() {
                //nothing
            }
        };
    }

    /**
     * bounds may be not correct.
     * minX < maxX
     * minY < maxY
     */
    protected MapCameraGL.CellIterator getIterator2(/*final Map map, */final float minX,
                                                   final float minY, final float maxX, final float maxY) {
        return new MapCameraGL.CellIterator() {
            private boolean hasNext;
            private int dx,dy, maxDx, maxDy;

            {
                clearState();
            }

            @Override
            public void clearState() {
                hasNext = true;
                dx = 0;
                maxDx = (int)( (maxX-minY)/spriteSize.x);
                maxDy = (int)( (maxY-minY)/dH);
            }

            private void incXY() {

            }

            @Override
            public Iterator<Cell> iterator() {
                return this;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Cell next() {
                //Cell c = map.getCell(x, y);
                incXY();
                //return c;
                return null;
            }

            @Override
            public float getX(){
                return 0;   //TODO
            }

            @Override
            public float getY(){
                return 0;   //TODO
            }

            @Override
            public void remove() {
                //nothing
            }
        };
    }
}
