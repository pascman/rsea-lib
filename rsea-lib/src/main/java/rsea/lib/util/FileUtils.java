package rsea.lib.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Administrator on 2015-11-27.
 */
public class FileUtils {

    private final static int BUFFER_SIZE = 1024;


    public static File getFile(Context context, String folder, String filename){
        File file =  new File(context.getFilesDir() ,folder);
        if(!file.exists()){
            file.mkdir();
        }
        return new File(file, filename);
    }

    public static void deleteFiles(Context context,String folder ){
        File file =  new File(context.getFilesDir() ,folder);
        for(File f : file.listFiles()){
            f.delete();
        }
    }

    public static String downloadFile(String url,File file){
        InputStream is = null;
        FileOutputStream os = null;
        try {
            URL u = new URL(url);
            is = u.openStream();
            os = new FileOutputStream(file);
            byte[] bytes = new byte[BUFFER_SIZE];
            for (;;) {
                int count = is.read(bytes, 0, BUFFER_SIZE);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try{
                is.close();
                os.close();
            }catch (Exception e){}
        }
        return file.getName();
    }
}
