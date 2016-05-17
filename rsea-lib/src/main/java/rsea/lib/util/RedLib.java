package rsea.lib.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016-05-17
 */
public class RedLib {

    private static Toast gToast;



    public static Unbinder bind(Activity ac){
        return ButterKnife.bind(ac);
    }
    public static Unbinder bind(Object target,View source){
        return ButterKnife.bind(target,source);
    }
    public static Unbinder bind(View view){
        return ButterKnife.bind(view);
    }


    public static void toast(Activity ac,String message){
        toast(ac,message);
    }
    public static void toast(Fragment fragment,String message){
        toast(fragment,message);
    }
    public static void toast(Context context,String message){
        if(gToast == null){
            gToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }else{
            gToast.setText(message);
        }
        gToast.show();
    }

    public static void fullscreen(Activity ac){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ac.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ac.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE // [API 16] fix screen resolution for app to minimum UI state
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // [API 16] always pretend we're fullscreen, even when not fullscreen
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // [API 16] hide status bar, fullscreen mode
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE // [API 14] dim on-screen UI
            );
        }
    }
}
