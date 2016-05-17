package rsea.lib.util.etc;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CurrencyWatcher implements TextWatcher {

	private EditText editor;
	private String strAmount = "";
	private DecimalFormat formater = new DecimalFormat("###,###");
	OnChangeListener mListener;
	public interface OnChangeListener{
		public void onChange();
	}
	public void setOnChangeListener(OnChangeListener l){
		mListener = l;
	}
	
	public CurrencyWatcher(EditText e) {
		editor = e;
		String longMaxStr = Long.MAX_VALUE+"";
		InputFilter.LengthFilter lengthF = new InputFilter.LengthFilter(longMaxStr.length()-1);
		editor.setFilters(new InputFilter[]{lengthF});
	}
	
	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!s.toString().equals(strAmount)) {
			try{
				strAmount = makeStringComma(s.toString().replace(",", ""));
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
			editor.setText(strAmount);
			Editable e = editor.getText();
			Selection.setSelection(e, strAmount.length());
			if(mListener!=null){
				mListener.onChange();
			}
			
		}
	}

	public String makeStringComma(String str) {
		if (str.length() == 0)
			return "";
		long value = Long.valueOf(str);
		return formater.format(value);
	}
}
