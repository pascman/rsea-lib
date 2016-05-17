package rsea.lib.http;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import rsea.lib.http.resource.ResourceCode;


public class RSHttpPopup extends AlertDialog{

    private static RSHttpPopup mDialog;

    public int errorCode;
    public RSHttpPopup(Context context) {
        super(context);
    }
    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    public static RSHttpPopup getPopup(Context context,int errorcode,String message,final OnDismissListener listener){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        if(message == null) message="";
        mDialog = new RSHttpPopup(context);
        mDialog.setTitle("알림");
        mDialog.setErrorCode(errorcode);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage(Html.fromHtml(message));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "확인", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setOnDismissListener(listener);
        return mDialog;
    }

    public static RSHttpPopup getPopup(Context context,int errorCode,final OnDismissListener listener){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new RSHttpPopup(context);
        mDialog.setTitle("알림");
        mDialog.setErrorCode(errorCode);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage(ResourceCode.getErrorMsg(errorCode));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "확인", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setOnDismissListener(listener);
        return mDialog;
    }

    public static RSHttpPopup getReSendPopup(Context context,int errorCode,final OnClickListener resendlistener,final OnDismissListener listener){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new RSHttpPopup(context);
        mDialog.setTitle("알림");
        mDialog.setErrorCode(errorCode);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage(ResourceCode.getErrorMsg(errorCode));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "재전송", resendlistener);
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setOnDismissListener(listener);
        return mDialog;
    }

    public static RSHttpPopup getReSendPopup(Context context,int errorCode,String errorMsg,final OnClickListener resendlistener,final OnDismissListener listener){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new RSHttpPopup(context);
        mDialog.setTitle("알림");
        mDialog.setErrorCode(errorCode);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage(errorMsg);
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "재전송", resendlistener);
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setOnDismissListener(listener);
        return mDialog;
    }

}
