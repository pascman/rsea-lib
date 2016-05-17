package rsea.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016-01-18.
 */
public class GetCameraTool {
    private static Uri g_temp_path;
    private static int REQ_CAMERA   = 0x8889;
    private static int REQ_CROP     = 0x8890;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static File getCacheFolder(Context context){
        File folder = new File(context.getExternalCacheDir() , "camera");
        if(!folder.exists()) folder.mkdir();
        return folder;
    }
    private static File getCameraTemp(Context context){
        return new File( getCacheFolder(context) , "camera_temp");
    }

    private static File generateFile(Context context){
        return new File( getCacheFolder(context) , "camera_" + formatter.format(new Date()) +".jpg");
    }

    public static void camera(Activity ac){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraTemp(ac)));
        ac.startActivityForResult(intent, REQ_CAMERA);
    }
    public static void camera(Fragment ac){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraTemp(ac.getContext())));
        ac.startActivityForResult(intent, REQ_CAMERA);
    }

    public static boolean check(int requestCode){
        if(requestCode == REQ_CAMERA || requestCode == REQ_CROP) return true;
        return false;
    }

    public static String onActivityResult(Object ac,int requestCode, int resultCode, Intent data){
        Context context  = ac instanceof Activity ? (Context) ac : ((Fragment)ac).getContext();
        Log.d("TAG", "onActivityResult: " + resultCode);
        if(resultCode != Activity.RESULT_OK) return null;
        if(getCameraTemp(context) == null) return null;
        if(requestCode == REQ_CAMERA){
            g_temp_path = Uri.fromFile(generateFile(context));
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(Uri.fromFile(getCameraTemp(context)), "image/*");
            cropIntent.putExtra("output", g_temp_path);
            cropIntent.putExtra("return-data", false);
            if(ac instanceof Activity){
                ((Activity)ac).startActivityForResult(cropIntent, REQ_CROP);
            }else if(ac instanceof Fragment){
                ((Fragment)ac).startActivityForResult(cropIntent, REQ_CROP);
            }
            return "crop";
        }else if(requestCode == REQ_CROP){
            if(g_temp_path == null) return null;
            File file = new File(g_temp_path.getPath());
            if(file.exists()) return file.getAbsolutePath();
            return null;
        }
        return null;
    }

}
