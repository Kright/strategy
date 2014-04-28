package game.main.openGL;

import android.content.res.Resources;

/**
 * Через этот объект будут передаваться данные от потока логики потоку рисования
 * Created by lgor on 29.04.14.
 */
public class DrawingContext {

    //  флаг, поток логики устанавливает его в false, поток рисования - в true
    public volatile boolean repainted = false;

    public final Resources resources;

    public DrawingContext(Resources resources) {
        this.resources = resources;
    }
}
