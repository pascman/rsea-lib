package rsea.lib.http.resource;

import java.util.HashMap;



public class ResourceCode {
    public static final int SUCCESS = 0;
    public static final int SERVER_ERROR 	= 9993;

    public static final int E8000 = 8000;				//WIFI NOT Connect
    public static final int E9993 = SERVER_ERROR;		//USER ERROR
    public static final int E9994 = 9994;				//HTTP Error
    public static final int E9995 = 9995;				//Connection Timeout
    public static final int E9996 = 9996;				//Socket Timeout
    public static final int E9997 = 9997;				//UnKnow Host
    public static final int E9998 = 9998;				//Parsing Error
    public static final int E9999 = 9999;				//UnKnown Error


    private static final HashMap<Integer,String> ERROR_MAP = new HashMap<Integer, String>();
    static{
        ERROR_MAP.put(E8000,"[8000]인터넷이 연결되어있지않습니다.");
        ERROR_MAP.put(E9994,"[9994]데이터 통신에 실패하였습니다");
        ERROR_MAP.put(E9995,"[9995]서버 연결시간 초과");
        ERROR_MAP.put(E9996,"[9996]서버 연결시간 초과");
        ERROR_MAP.put(E9997,"[9997]서버를 찾을수 없습니다.");
        ERROR_MAP.put(E9998,"[9998]데이터 변환시 오류발생");
        ERROR_MAP.put(E9999,"[9999]알수없는 오류");

    }
    public static String getErrorMsg(int errorCode){
        return ERROR_MAP.get(errorCode);
    }

    public static boolean containDefaultError(int errorCode){
        return ERROR_MAP.containsKey(errorCode);
    }
}
