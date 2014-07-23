package br.com.zynger.creditcardedittext;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import br.com.zynger.creditcardedittext.flag.CardFlag;
import br.com.zynger.creditcardedittext.flag.Discover;
import br.com.zynger.creditcardedittext.flag.MasterCard;
import br.com.zynger.creditcardedittext.flag.Visa;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class CreditCardNumberEditText extends CreditCardBaseEditText implements TextWatcher {

	private enum Theme {
		DEFAULT(R.drawable.creditcardedittext_edit_text_holo_light),
		ERROR(R.drawable.creditcardedittexterror_edit_text_holo_light);
		
		private int mBackgroundRes;
		
		private Theme(int backgroundRes) {
			this.mBackgroundRes = backgroundRes;
		}
		
		public int getBackgroundResource() {
			return mBackgroundRes;
		}
	}
	
	private static final int MAX_LENGTH = 16;
	private static final int LENGTH_WITH_SEPARATORS = MAX_LENGTH + 3 * SEPARATOR.length();
	
	private List<CardFlag> mCardFlags;
	private Theme mTheme;
	private OnCreditCardInputListener mListener;

	public CreditCardNumberEditText(Context context) {
		super(context);
		
		init();
	}
	
	public CreditCardNumberEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CreditCardNumberEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init() {
		mCardFlags = new ArrayList<CardFlag>();
		mCardFlags.add(new Visa());
		mCardFlags.add(new MasterCard());
		mCardFlags.add(new Discover());
		
		setHint("1234 5678 9012 3456");
		setHintTextColor(Color.GRAY);
		InputFilter maxLengthFilter = new InputFilter.LengthFilter(LENGTH_WITH_SEPARATORS);
		setFilters(new InputFilter[] { maxLengthFilter });
		addTextChangedListener(this);
		
		setTheme(Theme.DEFAULT);
		
		if (!isInEditMode()) {
			setDefaultIcon();
		}
	}
	
	public void setCardFlags(List<CardFlag> cardFlags) {
		this.mCardFlags = cardFlags;
	}
	
	private void setTheme(Theme theme) {
		if (theme != mTheme) {
			setBackgroundResource(theme.getBackgroundResource());
			mTheme = theme;
		}
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) { }

	public String getUserInput() {
		return getText().toString().replaceAll(SEPARATOR, "");
	}

	@Override
	public void afterTextChanged(Editable editable) {
		removeTextChangedListener(this);

		// record cursor position as setting the text in the textview
		// places the cursor at the end
		int cursorPosition = getSelectionStart();
		String withSpaces = groupText(editable);
		setText(withSpaces);
		// set the cursor at the last position + the spaces added since the
		// space are always added before the cursor
		setSelection(cursorPosition + (withSpaces.length() - editable.length()));

		// if a space was deleted also deleted just move the cursor
		// before the space
		if (mSeparatorDeleted) {
			setSelection(getSelectionStart() - 1);
			mSeparatorDeleted = false;
		}

		setTheme(Theme.DEFAULT);
		setDefaultIcon();
		
		for (CardFlag flag : mCardFlags) {
			if (getUserInput().matches(flag.getRegex())) {
				setCardIcon(flag.getCardImageResource());
				break;
			}
		}
		
		if (getUserInput().length() == MAX_LENGTH) {
			boolean isValid = LuhnValidator.isValid(getUserInput());

			if (!isValid) {
				setTheme(Theme.ERROR);
				YoYo.with(Techniques.Shake).playOn(this);
				
				if (mListener != null) {
					mListener.onInputError(this);
				}
			} else {
				if (mListener != null) {
					mListener.onInputSuccess(this);
				}
			}
		}

		addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		CharSequence charDeleted = s.subSequence(start, start + count);
		mSeparatorDeleted = SEPARATOR.equals(charDeleted.toString());
	}

	private String groupText(CharSequence text) {
		StringBuilder formatted = new StringBuilder();
		int count = 0;
		for (int i = 0; i < text.length(); ++i) {
			if (Character.isDigit(text.charAt(i))) {
				if (count % 4 == 0 && count > 0)
					formatted.append(SEPARATOR);
				formatted.append(text.charAt(i));
				++count;
			}
		}
		return formatted.toString();
	}

	public String getLastDigits() {
		String userInput = getUserInput();
		if (userInput.length() <= 4) {
			return userInput;
		}
		
		return userInput.substring(userInput.length() - 4);
	}
	
	protected void setOnCreditCardInputListener(OnCreditCardInputListener mListener) {
		this.mListener = mListener;
	}
}
