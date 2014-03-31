package game.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;

/**
 * Created by lgor on 13.01.14.
 */
public class MapActivity extends Activity {

    public static Typeface font;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        font = Typeface.createFromAsset(getAssets(), "fonts/oleoscriptbold.ttf");
        setContentView(new MapView(this));
    }
}
