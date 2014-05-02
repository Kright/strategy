package game.main.v;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import game.main.R;

/**
 * Created by user on 06.04.14.
 */
public class HelpLink extends Button {

    public int  whatHelp = 2;
    public HelpLink(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.HelpLinkStyle);
        whatHelp = a.getInteger(R.styleable.HelpLinkStyle_whatHelp,whatHelp);
        a.recycle();
    }

    public HelpLink(Context context) {
        super(context);
    }


}
