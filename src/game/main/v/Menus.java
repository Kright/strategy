package game.main.v;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import game.main.MapActivity;
import game.main.R;

/**
 * Created by user on 06.04.14.
 */
public class Menus extends Activity {
    public static boolean playing = false;
    View loadscreen;
    View help;
    LayoutInflater ltInflater;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        ltInflater = getLayoutInflater();

        loadscreen = ltInflater.inflate(R.layout.loadscreen,null);
        if(playing) changeMenu();

        setContentView(loadscreen);

    }

    private void changeMenu(){

    }


    public void backToMain(View v){
        //zzzz
        loadscreen = ltInflater.inflate(R.layout.a,null);
        setContentView(loadscreen);
    }

    public void gogame(){
        playing = true;
        MapActivity.newgame=true;
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }


    public void lscOnClick(View view){
            switch (view.getId()){
                case R.id.btstart:
                    gogame();
                    break;
                case R.id.bthelp0:
                    help = ltInflater.inflate(R.layout.help0,null);
                    setContentView(help);
                    break;
            }
     }


    public void HelpClick(View view){
            HelpLink h = (HelpLink)view;
            int[] lts = {R.layout.help0,
                        R.layout.help1,
                        R.layout.help2,

                        };
            help = ltInflater.inflate(lts[h.whatHelp],null);
            setContentView(help);
        }

}
