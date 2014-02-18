package game.main.gamelogic.meta;

import game.main.gamelogic.Cell;

import java.util.ArrayList;

/**
 * Created by Michael-PC on 16.02.14.
 */
public class SeqOfCells {
    ArrayList<Cell> sequence;
    int length;

    public SeqOfCells(){
        sequence=new ArrayList<Cell>();
        length=0;
    }

    public void setCell(Cell c){
        sequence.add(c);
        length++;
    }

    public void deleteLastCell(){
        sequence.remove(length-1);
    }

    public int getLenght(){
        return length;
    }

}
