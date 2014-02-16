package game.main.GUI;

/**
 * Счётчик кадров в секунду
 * Created by lgor on 31.12.13.
 */
public class FPS {

    private int fps = 0;
    private long time;
    int count = 0;

    public FPS() {
        time = System.currentTimeMillis();
    }

    /**
     * возвращает усреднённое количество вызовов этого метода за секунду
     */
    public int get() {
        count++;
        long now = System.currentTimeMillis();
        int delta = (int) (now - time);
        if (delta > 1000) {
            fps = count * 1000 / delta;
            count = 0;
            time = now;
        }
        return fps;
    }
}
