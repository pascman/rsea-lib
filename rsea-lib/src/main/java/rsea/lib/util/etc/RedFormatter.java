package rsea.lib.util.etc;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RedFormatter {
	
	public static String douleToMoney(double money){
		DecimalFormat format = new DecimalFormat("###,###.##");
		double convert_money = money;
		return format.format(convert_money);
	}
	public static String StringToMoney(String money){
		return douleToMoney(Double.valueOf(money));
	}

	public static String longToMoney(Long money){
		DecimalFormat format = new DecimalFormat("###,###");
		return format.format(money);
	}

	public static String dateToString(long timestamp){
		Date tmp_date = new Date(timestamp);
		SimpleDateFormat frt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return frt.format(tmp_date);
	}
	public static String stringToDate(String timestamp){
		SimpleDateFormat frt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = frt.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date != null){
			return ""+date.getTime();
		} else {
			return "0";
		}
	}
}
