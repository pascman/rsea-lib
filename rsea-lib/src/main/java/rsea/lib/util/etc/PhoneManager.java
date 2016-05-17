package rsea.lib.util.etc;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneManager {
	private static final String[] PHONES = {
		"010",
		"011",
		"018",
		"019",
		"011",
		"017"
	};
	public static boolean isChkPhoneNumber(Context context,String number){
		String myNumber = getPhone(context);
		
		if(myNumber==null){
			return false;
		}
		if(myNumber.equals(number)){
			return true;
		}else{
			return false;
		}

	}

	public static String getPhone(Context context){
		String myNumber = null;
		try{
			TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			myNumber = mTelephonyMgr.getLine1Number();
			if(myNumber.substring(0,1).equals("+")){
				myNumber = "0"+myNumber.substring(3);
			}
		}catch (Exception e) {
			myNumber = "";
			e.printStackTrace();
		}
		return myNumber;
	}
	
	public static String getInterNationalPhone(Context context,String phone){
		String myNumber = null;
		try{
			if(phone.length() > 9  && phone.startsWith("0")){
				myNumber = "+82" + phone.substring(1);
			}
		}catch (Exception e) {
			myNumber = phone;
			e.printStackTrace();
		}
		return myNumber;
	}
	

	public static String getGeneralPhone(Context context,String phone){
		String myNumber = phone;
		try{
			if(myNumber.substring(0,1).equals("+")){
				myNumber = "0"+myNumber.substring(3);
			}
		}catch (Exception e) {
			myNumber = "";
			e.printStackTrace();
		}
		return myNumber;
	}

	public static String[] getTelNumber(String number){
		try {
			if(number == null) return new String[]{ "", "", ""};

			if(number.length() > 10){
				if(number.substring(0,1).equals("+")){
					number = "0"+number.substring(3);
				}
			} else if(number.length() == 8){
				Pattern pattern15xx = Pattern.compile("^(1544|1566|1577|1588|1644|1688|1599)-?([0-9]{4})");
				Matcher matcher15xx = pattern15xx.matcher( number);
				if(matcher15xx.matches()) {
					return new String[]{ matcher15xx.group(1), matcher15xx.group(2)};
				}
			}
			
			
//			Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
			
		  Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
			
			Matcher matcher = tellPattern.matcher( number);
			if(matcher.matches()) {
				return new String[]{ matcher.group(1), matcher.group(2), matcher.group(3)};
			} else {
				return new String[]{};
			}
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return new String[]{ "", "", ""};
	}
	
	public static String getParseNumber(String number){
		return PhoneNumberUtils.formatNumber(number);
		
//		String[] telNum = getTelNumber(number);
//		try{
//			if(number.equals("")) return "";
//			if(telNum.length == 0) return number;
//			if(telNum.length == 2) return telNum[0]+"-"+telNum[1];
//			return telNum[0]+"-"+telNum[1]+"-"+telNum[2];
//		}catch(Exception e){
//			return "";
//		}
	}
	
	public static boolean checkHP(String phone){
		for(String prefix : PHONES){
			if(phone.startsWith(prefix)){
				return true; 
			}
		}
		return false;
	}
	
	public static String makePhoneNumber(String value){
		   Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
	       if(value == null)
	       {
	        return value;
	       }
	       value = value.trim();
	       Matcher matcher = tellPattern.matcher( value);
	       if(matcher.matches())
	       {
	           return matcher.group( 1) + "-" + matcher.group( 2) + "-" + matcher.group( 3);
	       }
	       else
	       {
	           return value;
	       }
		}   
}
