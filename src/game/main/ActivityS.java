package game.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceView;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.world.utils.NewWorldLoader;
import game.main.utils.sprites.SpriteBank;

/**
 * активити. Создаёт и запускает игровую сессию и, если надо, ставит на паузу.
 * В конце принудительно её завершает
 * Created by lgor on 03.05.14.
 */
public class ActivityS extends Activity {

    public static volatile Typeface font;

    private static GameSession session;

    private SurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        view = new SurfaceView(this);
        SpriteBank sp = new SpriteBank(getResources());
        if (session == null) {
            session = new GameSession(new NewWorldLoader(sp, 120, 140), sp);
        }
        setContentView(view);
        view.setOnTouchListener(session.touchBuffer);
    }

    @Override
    protected void onResume() {
        session.onResume(view.getHolder());
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (session!=null){
            session.onPause();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        session.stop();
        session = null;
        super.onBackPressed();
    }
}
