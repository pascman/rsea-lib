package rsea.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.Collection;

/**
 * Created by Administrator on 2015-04-02.
 */
public class ViewUtils {

    public static void setChildSelected(View parent,boolean isSelected){
        ViewGroup vg = (ViewGroup) parent;
        for(int i=0;i<vg.getChildCount();i++){
//            if(vg.getChildAt(i) instanceof  ViewGroup ){
//                setChildSelected(vg.getChildAt(i),);
//            }else{
                vg.getChildAt(i).setSelected(isSelected);
//            }
        }
    }

    public static void cleanTextView(View v){
        if(v instanceof Spinner) {
            ((Spinner)v).setSelection(0);
        }else if(v instanceof TextView || v instanceof EditText) {
            ((TextView) v).setText("");
        }else if(v instanceof ViewGroup){
            for(int i=0;i<((ViewGroup) v).getChildCount();i++){
                cleanTextView(((ViewGroup) v).getChildAt(i));
            }
        }
    }

    public static RadioButton getCheckedRadio(RadioGroup bGroups){
        for(int i=0;i<bGroups.getChildCount();i++){
            RadioButton btn = (RadioButton) bGroups.getChildAt(i);
            if(btn.isChecked()){
                return btn;
            }
        }
        return null;
    }

    public static View getSelectedView(View parent){
        ViewGroup vg = (ViewGroup) parent;
        for(int i=0;i<vg.getChildCount();i++){
            if(vg.getChildAt(i).isSelected()){
                return vg.getChildAt(i);
            }
        }
        return null;
    }

    public static boolean isLastPosition(Collection<?> collection,int position){
        if(position == collection.size()-1){
            return true;
        }
        return false;
    }

    public static boolean isLastPosition(JSONArray json,int position){
        if(position == json.length()-1){
            return true;
        }
        return false;
    }

    public static void performClickNoSound(View v){
        v.setSoundEffectsEnabled(false);
        v.performClick();
        v.setSoundEffectsEnabled(true);
    }

    public static int calculateWidth(Context context){
        WindowManager window_manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        window_manager.getDefaultDisplay().getSize(size);
        return size.x;
    }
    public static int calculateHeight(Context context){
        WindowManager window_manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        window_manager.getDefaultDisplay().getSize(size);
        return size.y;
    }

    public static int getStatusBarSize(Activity ac){
        Rect checkRect = new Rect();
        ac.getWindow().getDecorView().getWindowVisibleDisplayFrame(checkRect);
        Log.d("ViewUtils", "getStatusBarSize: " + checkRect);
        return checkRect.top;
    }

}
