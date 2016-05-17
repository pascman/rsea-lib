package rsea.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by RedSea on 2016. 1. 17..
 */
public class GetImageTools {

    public static final int GALLERY = 0x8888;
    public static Uri g_tempFile;
    public static boolean checkGetImage(int requestCode){
        if(requestCode == GALLERY ) return true;
        return false;
    }

    public static void photo(Object ac){
        Context context = ac instanceof Activity ? (Context) ac : ((Fragment)ac).getContext();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(ac instanceof Activity){
            ((Activity)ac).startActivityForResult(intent, GALLERY);
        }else if(ac instanceof Fragment){
            ((Fragment)ac).startActivityForResult(intent, GALLERY);
        }
    }


    public static File onActivityResult(Object ac,int requestCode, int resultCode, Intent data) {
        Context context = ac instanceof Activity ? (Context) ac : ((Fragment)ac).getContext();
        switch (requestCode) {
            case GALLERY:
                if ( resultCode == Activity.RESULT_CANCELED)
                    return null;
                if (data.getData() != null) {
                    try{
                        File output = generatorCacheFile(context);
                        Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(),data.getData());
                        image.compress(Bitmap.CompressFormat.JPEG,95,new FileOutputStream(output));
                        return output;
                    }catch (Exception e){}
                }
                break;
        }
        return null;
    }

    public static File generatorCacheFile(Context ac){
        File folder = new File(ac.getCacheDir(),"select_image");
        if(!folder.exists()) folder.mkdir();
        return new File( folder ,"generator_" + System.currentTimeMillis() + ".jpg");
    }

}
