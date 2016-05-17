package rsea.lib.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rsea.lib.http.resource.HttpBaseResource;
import rsea.lib.http.resource.ResourceCode;

public class RSHttp {

	public static final int FINISH_JOB_SUCCESS 				= 2001;
	public static final int FINISH_JOB_FAIL	 				= 2002;
	public static final int FINISH_JOB_FAIL_RESEND	 		= 2003;
	
	private Context mContext;
	private String key;
	private boolean isProgress = true;
	private boolean isShowingError =  true;
	private RSHttpCallback callback;
	private static final String TAG = "RSHttp";
	public static RSHttpSetting gSetting = new RSHttpSetting();


	public interface RSHttpCallback{
		void cbRSHttpSuccess(String key, HttpBaseResource... resp);
		void cbRSHttpFail(String key, int errorCode, HttpBaseResource... resp);
	}
	public static void init(RSHttpSetting setting){
		gSetting = setting;
	}
	public static RSHttp session(Context context,String key){
		return new RSHttp(context,key);
	}
	public RSHttp(Context context,String key){
		mContext = context;
		this.key = key;
	}
	public RSHttp(Context context){
		mContext = context;
	}
	public RSHttp callback(RSHttpCallback callback){
		this.callback = callback;
		return this;
	}
	public RSHttp progress(boolean isProgress){
		this.isProgress = isProgress;
		return this;
	}
	public RSHttp showError(boolean isShowingError){
		this.isShowingError = isShowingError;
		return this;
	}

	public ExecutorService service = Executors.newFixedThreadPool(10);
	public void req(HttpBaseResource... resource){
		service.execute(createRunnable(isProgress,isShowingError,resource));
	}
	private  void reSendResource(Object obj){
		Object[] objs = (Object[]) obj;
		HttpBaseResource[] castResources = (HttpBaseResource[]) objs[0];
		RSHttpRunnable.HttpRunnableSetting runSetting = (RSHttpRunnable.HttpRunnableSetting) objs[1];
		service.execute(createRunnable(runSetting.isProgressDialog, runSetting.isShowingEDialog, castResources));
	}
	public RSHttpRunnable createRunnable(boolean isProgress,boolean isShowingEDialog,HttpBaseResource... resources){
		RSHttpRunnable runnable = new RSHttpRunnable(mResultHandler,mContext,resources);
		runnable.setShowingEDialog(isShowingEDialog);
		runnable.setProgress(isProgress);
		return runnable;
	}
	
	private void onResultRunnable(Object resources){
		HttpBaseResource[] results = (HttpBaseResource[]) resources;
		if(callback != null){
			callback.cbRSHttpSuccess(key, results);
		}
	}

	private void onFailRunnable(Object obj){
		Object[] objs = (Object[]) obj;
		HttpBaseResource[] castResources = (HttpBaseResource[]) objs[0];
		Integer value = (Integer) objs[1];

		if(callback != null){
			callback.cbRSHttpFail(key, value, castResources);
		}
	}
	private Handler mResultHandler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FINISH_JOB_SUCCESS:
				onResultRunnable(msg.obj);
				
				break;
			case FINISH_JOB_FAIL:
				onFailRunnable(msg.obj);
				break;
			case FINISH_JOB_FAIL_RESEND:
				reSendResource(msg.obj);
				break;
			default:
				break;
			}
		}
	};
	
	public static Object req(Context context, HttpBaseResource resource) {
		HttpURLConnection conn = null;
		Object retVal = null;
		try {
			resource.setContext(context);
			// Header Log Start
//			resource.getReqHeaders().put("User-Agent", DBInfo.getPackage(context) + "/" + DBInfo.getVersion(context));
			conn = (HttpURLConnection) resource.makeRequestRes();
			conn.setConnectTimeout(RSHttp.gSetting.getTimeout());
			conn.setReadTimeout(RSHttp.gSetting.getTimeout());
			Iterator<String> reqHeader = conn.getHeaderFields().keySet().iterator();
			if (gSetting.isDebug()) {
				Log.d(TAG, "network detail uri " + conn.getURL().toString());
				while (reqHeader.hasNext()) {
					String key = reqHeader.next();
					Log.d(TAG, "Request Header " + key + " : " + conn.getHeaderFields().get(key));
				}
			}
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				resource.parsorRes(conn.getInputStream());
				if (gSetting.isDebug()) {
					Log.d(TAG, "network received " + resource);
				}
				retVal = resource.body();
			} else {
				resource.errorCode = ResourceCode.E9994;
				throw new Exception("HTTP ResponseCode " + conn.getResponseCode() + " , " + conn.getResponseMessage());
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(conn != null){
				try{ conn.disconnect(); } catch (Exception ee){}
			}
		}
		return retVal;
	}
	

}
