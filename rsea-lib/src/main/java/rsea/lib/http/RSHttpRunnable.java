package rsea.lib.http;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;

import rsea.lib.connection.RedConnectionState;
import rsea.lib.http.exception.ParsingException;
import rsea.lib.http.resource.HttpBaseResource;
import rsea.lib.http.resource.ResourceCode;


public class RSHttpRunnable implements Runnable, DialogInterface.OnDismissListener {


    public static class HttpRunnableSetting {
        public boolean isProgressDialog = true;
        public boolean isShowingEDialog = true;
    }

    private static final int THREAD_PRE = 1001;
    private static final int THREAD_POST = 1002;

    private static final String TAG = "RSHttpRunnable";
    private RSHttpProgressDialog mProgress;
    private Handler mExcuterHandler;
    private Context mContext;
    private HttpBaseResource[] mResources;
    private HttpRunnableSetting mSetting = new HttpRunnableSetting();

    private DialogInterface.OnClickListener mResendingListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Object[] objs = new Object[2];
            objs[0] = mResources;
            objs[1] = mSetting;
            mExcuterHandler.sendMessage(Message.obtain(mExcuterHandler, RSHttp.FINISH_JOB_FAIL_RESEND, objs));
        }
    };

    public RSHttpRunnable(Handler ex, Context context, HttpBaseResource[] resources) {
        mExcuterHandler = ex;
        mContext = context;
        mResources = resources;
    }

    public void setProgress(boolean b) {
        mSetting.isProgressDialog = b;
    }

    public void setShowingEDialog(boolean b) {
        mSetting.isShowingEDialog = b;
    }

    private void onPreExecute() {
        showDialog();
    }

    private void onPostExecute() {
        hideDialog();

        int errorCode = ResourceCode.SUCCESS;
        String errorMsg = "";
        for (HttpBaseResource resource : mResources) {
            if(resource.errorCode != ResourceCode.SUCCESS){
                errorCode = resource.errorCode;
                errorMsg = resource.errorMsg;
            }
        }
        switch (errorCode) {
            case ResourceCode.SUCCESS:
                mExcuterHandler.sendMessage(Message.obtain(mExcuterHandler, RSHttp.FINISH_JOB_SUCCESS, mResources));
                break;
            case ResourceCode.SERVER_ERROR:
                if (mSetting.isShowingEDialog) {
                    RSHttpPopup.getPopup(mContext, errorCode, errorMsg, this).show();
//                        Toast.makeText(mContext, resource.errorMsg, Toast.LENGTH_LONG).show();
//                        Object[] obj = new Object[2];
//                        obj[0] = mResources;
//                        obj[1] = resource.errorCode;
//                        mExcuterHandler.sendMessage(Message.obtain(mExcuterHandler, RSHttp.FINISH_JOB_FAIL, obj));
                }
                break;
            case ResourceCode.E9994:
                if (mSetting.isShowingEDialog) {
                    RSHttpPopup.getReSendPopup(mContext, errorCode, errorMsg,mResendingListener, this).show();
                } else {
                    Object[] obj = new Object[2];
                    obj[0] = mResources;
                    obj[1] = errorCode;
                    mExcuterHandler.sendMessage(Message.obtain(mExcuterHandler, RSHttp.FINISH_JOB_FAIL, obj));
                }
                break;
            default:
                if (mSetting.isShowingEDialog) {
                    RSHttpPopup.getReSendPopup(mContext, errorCode, mResendingListener, this).show();
                } else {
                    Object[] obj = new Object[2];
                    obj[0] = mResources;
                    obj[1] = errorCode;
                    mExcuterHandler.sendMessage(Message.obtain(mExcuterHandler, RSHttp.FINISH_JOB_FAIL, obj));
                }
                break;
        }
    }

    @Override
    public void run() {
        mRunnableHandler.sendEmptyMessage(THREAD_PRE);
        for (int i = 0; i < mResources.length; i++) {
            HttpBaseResource param = mResources[i];
            param.setContext(mContext);
            if (i != 0 && param.paramFactory != null) {
                if( mResources[i - 1].errorCode == ResourceCode.SUCCESS){
                    param.paramFactory.interceptParam(mResources[i - 1], param);
                }else{
                    continue;
                }
            }
            if (!RedConnectionState.isOnline(mContext)) {
                param.errorCode = ResourceCode.E8000;
                continue;
            }
            HttpURLConnection conn = null;
            try {
                // Header Log Start
                conn = (HttpURLConnection) param.makeRequestRes();
                conn.setConnectTimeout(RSHttp.gSetting.getTimeout());
                conn.setReadTimeout(RSHttp.gSetting.getTimeout());
                Iterator<String> respHeader = conn.getHeaderFields().keySet().iterator();
                if(RSHttp.gSetting.isDebug()){
                    Log.d(TAG, "network detail uri " + conn.getURL().toString());
                    while (respHeader.hasNext()){
                        String key = respHeader.next();
                        Log.d(TAG, "Response Header " + key + " : " + conn.getHeaderFields().get(key));
                    }
                }
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    param.headerParsor(conn);
                    param.errorCode = ResourceCode.SUCCESS;
                    param.parsorRes(conn.getInputStream());
                }else{
                    param.errorCode = ResourceCode.E9994;
                    param.errorMsg = conn.getResponseMessage();
                    if(RSHttp.gSetting.isDebug()) {
                        Log.d(TAG,"HTTP ResponseCode " + conn.getResponseCode()  + " , " + conn.getResponseMessage());
                    }
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                param.errorCode = ResourceCode.E9996;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                param.errorCode = ResourceCode.E9997;
            } catch (ParsingException e) {
                e.printStackTrace();
                param.errorCode = ResourceCode.E9998;
            } catch (Exception e) {
                e.printStackTrace();
                param.errorCode = ResourceCode.E9999;
            } finally {
                if(conn != null){
                    try{ conn.disconnect(); } catch (Exception ee){}
                }
            }
        }
        mRunnableHandler.sendEmptyMessage(THREAD_POST);
    }

    private void showDialog() {
        mProgress = new RSHttpProgressDialog(mContext);
        mProgress.setCancelable(false);
        if (mSetting.isProgressDialog) {
            try {
                mProgress.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hideDialog() {
        if (mSetting.isProgressDialog) {
            try {
                mProgress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        RSHttpPopup popup = (RSHttpPopup) dialog;
        Object[] objs = new Object[2];
        objs[0] = mResources;
        objs[1] = Integer.valueOf(popup.errorCode);
        mExcuterHandler.sendMessage(Message.obtain(mExcuterHandler, RSHttp.FINISH_JOB_FAIL, objs));
        if (mSetting.isShowingEDialog)
            dialog.dismiss();
    }



    private Handler mRunnableHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case THREAD_PRE:
                    onPreExecute();
                    break;
                case THREAD_POST:
                    onPostExecute();
                    break;
                default:
                    break;
            }
        }
    };

}
