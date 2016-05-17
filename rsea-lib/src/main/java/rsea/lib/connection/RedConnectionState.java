package rsea.lib.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RedConnectionState {

	public static  boolean isOnlineToWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isOnlineToMobile(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfos = cm.getAllNetworkInfo();
		for(NetworkInfo info : netInfos){
			if(info !=null && info.isConnected()){
				return true;
			}
		}
		return false;
	}

	
}
