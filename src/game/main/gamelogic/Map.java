package game.main.gamelogic;

import game.main.gamelogic.meta.LandType;
import game.main.gamelogic.meta.SeqOfCells;
import game.main.gamelogic.meta.Settlement;

import java.util.Random;

/**
 * Created by lgor on 31.12.13.
 */
public class Map {

    /*
    Класс карты - массив ячеек, операции по нахождению кратчайших путей и прочего
    клетки массива могут быть null по краям
     */

    //потом table станет private
    public final Cell[][] table;
    //сначала у, потом х
    public final int height, width;

    public Map(int width, int height){
        this.height=height;
        this.width=width;
        table = new Cell[height][width];
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                table[y][x]=new Cell();
            }
        }
    }

    /**
     * в перспективе добавить область видимости
     * @param x
     * @param y
     * @return
     */
    public Cell getCell(int x, int y, int playerId){
        if (x>=0 && y>=0 && x<width && y<height){
            return table[y][x];
        }
        return null;
    }


    public SeqOfCells shortPath(int xBegin, int yBegin, int xEnd, int yEnd){
        SeqOfCells sc=new SeqOfCells();

        return sc;
    }

    void fillRandom(LandType[] types){
        Random rnd=GameSession.now.rnd;
        for(Cell[] cc:table){
            for(Cell c:cc){
                c.land=types[rnd.nextInt(types.length)];
                if (c.land==types[0] && rnd.nextInt(4)==0){
                    c.settlement=new Settlement();
                }
            }
        }
    }
}
