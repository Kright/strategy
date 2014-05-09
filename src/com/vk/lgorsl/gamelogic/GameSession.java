package com.vk.lgorsl.gamelogic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.vk.lgorsl.gamelogic.world.World;
import com.vk.lgorsl.gamelogic.world.utils.GameProperties;
import com.vk.lgorsl.gamelogic.world.utils.iWorldLoader;
import com.vk.lgorsl.utils.TouchBuffer;
import com.vk.lgorsl.utils.sprites.SpriteBank;

/**
 * альтернативная реализация (я надеюсь ,в ней будет меньше всего лишнего)
 * внутри есть поток-демон, который сам запускается и останавливается.
 * Теоретически, он может работать даже когда приложение свернуто.
 * Например, сохраняться в фоне.
 * Created by lgor on 03.05.14.
 */
public class GameSession implements Runnable {

    public final TouchBuffer touchBuffer;

    protected volatile Thread me = null;
    public volatile boolean running = false;
    public volatile boolean mustStop = false;
    public volatile boolean mustUpdate = false;

    private volatile SurfaceHolder holder;
    private final iWorldLoader loader;
    private World world;
    private final SpriteBank spriteBank;

    private Player currentPlayer;
    private MapRender mapRender;
    public GameProperties properties;

    public GameSession(iWorldLoader loader, SpriteBank spriteBank) {
        this.loader = loader;
        touchBuffer = new TouchBuffer();
        this.spriteBank = spriteBank;
    }

    @Override
    public void run() {
        world = loader.load(this);

        properties = new GameProperties();
        mapRender = new MapRender(128, spriteBank, properties);
        currentPlayer = world.getNextPlayer();

        while (true) {
            currentPlayer.run(mapRender);
            if (mustStop) {
                break;
                //если игрок завершит ход после того, как мы захотим закрыть приложение, сохранится, что ход не завершён
            }
            currentPlayer = world.getNextPlayer();
        }

        loader.save(this);
    }

    public void render(Canvas canvas) {
        currentPlayer.paint(canvas, mapRender);
    }

    public void onResume(SurfaceHolder holder) {
        this.holder = holder;
        this.running = true;
        if (me == null) {
            me = new Thread(this);
            me.setDaemon(true);
            me.start();
        }
        mustUpdate = true;
    }

    public void onPause() {
        this.running = false;
    }

    /**
     * ждёт, когда поток остановится
     */
    public void stop() {
        mustStop = true;
        running = false;
        while (me.isAlive()) {
            try {
                me.join();
            } catch (InterruptedException e) {
                //nothing
            }
        }
    }

    public boolean repaint() {
        boolean success = false;
        Canvas canvas = null;
        try {                       // получаем объект Canvas и выполняем отрисовку
            canvas = holder.lockCanvas(null);
            if (canvas != null)
                synchronized (holder) {
                    render(canvas);
                    success = true;
                    mustUpdate = false;
                }
        } finally {
            if (canvas != null) {   // отрисовка выполнена. выводим результат на экран
                holder.unlockCanvasAndPost(canvas);
            }
        }
        return success;
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}
