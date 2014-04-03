package game.main.v;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import game.main.MapActivity;


/**
 * Created by Vova on 01.04.14.
 */
public class MenusActivity extends Activity  {
    LoadScreen loadScreen = null;
    HelpScreen helpScreen = null;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        helpScreen = new HelpScreen(this, getAssets());
        loadScreen = new LoadScreen(this, getAssets() , this);
        setContentView(loadScreen);

    }

    public void goGame(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void closeHelp(){
        setContentView(loadScreen);
    }
    public void goHelp(){
        setContentView(helpScreen);
    }


}