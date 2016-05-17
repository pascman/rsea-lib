package rsea.lib.util;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016-01-15.
 */
public class JSONTools {

    public static void addAllJSon(JSONArray array1,JSONArray array2){
        for(int i=0;i<array2.length();i++){
            array1.put(array2.opt(i));
        }
    }

}