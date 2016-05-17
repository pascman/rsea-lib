package rsea.lib.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DipUtil {
	private static DisplayMetrics metrics;

	public static void init(Context context){
		metrics = context.getResources().getDisplayMetrics();
	}
	public static int pixel(Float dip){
		Float retVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
		return retVal.intValue();
	}
	public static int pixel(Integer dip){
		Float retVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
		return retVal.intValue();
	}
}
