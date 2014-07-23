package br.com.zynger.creditcardedittext;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

public class CreditCardFormEditText extends CreditCardBaseEditText implements TextWatcher {

	private final static String REGEX = "DDDD  MM/YY  CVV   ";
	private final static char[] REGEX_CHARS = { 'D', 'M', 'Y', 'C', 'V'};
	
	private String mLastDigits;
	private OnCreditCardFormListener mListener;
	
	public CreditCardFormEditText(Context context) {
		super(context);
		
		init();
	}
	
	public CreditCardFormEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CreditCardFormEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}
	
	public void setOnCreditCardFormListener(OnCreditCardFormListener mListener) {
		this.mListener = mListener;
	}
	
	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		int index = selStart;
		try {
			String charAtIndex = String.valueOf(getText().charAt(index));
			
			if (!charAtIndex.equals(" ")) {
				super.onSelectionChanged(selStart, selEnd);
				return;
			}
			
			while (charAtIndex.equals(" ")) {
				index++;
				charAtIndex = String.valueOf(getText().charAt(index));
			}
			super.setSelection(index);
		} catch (IndexOutOfBoundsException e) {
			index--;
			if (index > 0) {
				String charAtIndex = String.valueOf(getText().charAt(index));
				
				while (charAtIndex.equals(" ")) {
					if (index == 0) {
						break;
					}
					index--;
					charAtIndex = String.valueOf(getText().charAt(index));
				}
				
				super.setSelection(index);
			} else {
				super.onSelectionChanged(selStart, selEnd);
			}
		}
	}
	
	private void init() {
		setBackgroundResource(R.drawable.creditcardedittext_edit_text_holo_light);
		
		// FIXME: still able to input one more character than should
		InputFilter maxLengthFilter = new InputFilter.LengthFilter(20);
		setFilters(new InputFilter[] { maxLengthFilter });
	}
	
	protected void setCardDrawable(Drawable drawable) {
		setCompoundDrawables(drawable, null, null, null);
	}
	
	protected void setCardNumberLastDigits(String lastDigits) {
		this.mLastDigits = lastDigits;
		setMaskedText(lastDigits);
		
		addTextChangedListener(this);
	}
	
	private void setMaskedText(String text) {
		if (text.length() > REGEX.length()) {
			return;
		}

		StringBuilder buffer = new StringBuilder(REGEX);
		int indexNotDigitNorSpace = 0;
		for (int i = 0; i < text.length(); i++) {
			char charAtIndex = text.charAt(i);
			
			while (indexNotDigitNorSpace < REGEX.length()) {
				char atIndex = buffer.charAt(indexNotDigitNorSpace);
				
				if (String.valueOf(REGEX_CHARS).contains(String.valueOf(atIndex))) {
					break;
				}
				
				indexNotDigitNorSpace++;
			}
			
			if (indexNotDigitNorSpace >= REGEX.length()) {
				return;
			}
			
			buffer.setCharAt(indexNotDigitNorSpace, charAtIndex);
		}
		
		Spannable span = new SpannableString(buffer.toString());        
		span.setSpan(new ForegroundColorSpan(Color.GRAY),
				indexNotDigitNorSpace + 1, span.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		setText(span);
		super.setSelection(indexNotDigitNorSpace + 1);
	}
	
	public void onTextChanged(CharSequence textCs, int start,
			int lengthBefore, int lengthAfter) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		removeTextChangedListener(this);
		
		String text = s.toString();
		
		String justDigits = text.replaceAll("\\D+","");
		
		if (justDigits.length() < mLastDigits.length()) {
			setText(mLastDigits);
			if (mListener != null) {
				mListener.onDigitsErased(this, justDigits.length());
			}
		} else {
			setMaskedText(justDigits);
		}
		
		addTextChangedListener(this);
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	
	public String getUserInput() {
		String text = getText().toString();
		
		for (int i = 0; i < REGEX.length(); i++) {
			char atIndex = REGEX.charAt(i);
			
			text = text.replaceAll(String.valueOf(atIndex), "");
		}
		
		return text;
	}
	
	private String getUserInput(int start, int end) {
		if (end <= start) {
			throw new RuntimeException("end cannot be less or equal than start for substring");
		}
		
		if (mLastDigits != null) {
			String userInput = getUserInput();
			userInput = userInput.replaceFirst(mLastDigits, "");
			if (userInput.length() >= end) {
				return userInput.substring(start, end);
			}
		}
		return null;
	}

	public String getExpiryMonth() {
		return getUserInput(0, 2);
	}
	
	public String getExpiryYear() {
		return getUserInput(2, 4);
	}
	
	public String getCvv() {
		return getUserInput(4, 7);
	}
}
