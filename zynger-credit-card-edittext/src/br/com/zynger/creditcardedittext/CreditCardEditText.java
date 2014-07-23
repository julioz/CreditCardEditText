package br.com.zynger.creditcardedittext;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import br.com.zynger.creditcardedittext.flag.CardFlag;
import br.com.zynger.creditcardedittext.flag.Discover;
import br.com.zynger.creditcardedittext.flag.MasterCard;
import br.com.zynger.creditcardedittext.flag.Visa;

public class CreditCardEditText extends RelativeLayout {

	private CreditCardNumberEditText mNumberEditText;
	private CreditCardFormEditText mFormEditText;

	public CreditCardEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public CreditCardEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mNumberEditText = new CreditCardNumberEditText(getContext());
		addView(mNumberEditText);
		
		// XXX: delete me
		mNumberEditText.setText("49168496505667");
		
		mFormEditText = new CreditCardFormEditText(getContext());
		mFormEditText.setVisibility(View.INVISIBLE);
		addView(mFormEditText);
		
		mNumberEditText.setOnCreditCardInputListener(new OnCreditCardInputListener() {
			@Override
			public void onInputSuccess(CreditCardNumberEditText numberEditText) {
				Drawable cardFlagDrawable = mNumberEditText.getCardFlagDrawable();
				mFormEditText.setCardDrawable(cardFlagDrawable);
				
				String lastDigits = mNumberEditText.getLastDigits();
				mFormEditText.setCardNumberLastDigits(lastDigits);

				mNumberEditText.setVisibility(View.INVISIBLE);
				mFormEditText.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onInputError(CreditCardNumberEditText editText) {
			}
		});
		
		
		mFormEditText.setOnCreditCardFormListener(new OnCreditCardFormListener() {
			@Override
			public void onDigitsErased(CreditCardFormEditText formEditText, int deletedCharsNum) {
				mNumberEditText.setVisibility(View.VISIBLE);
				mFormEditText.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	public void setCardFlags(boolean addDefaultFlags, CardFlag... flags) {
		LinkedList<CardFlag> cardFlags = new LinkedList<CardFlag>();
		
		for (CardFlag cardFlag : flags) {
			cardFlags.add(cardFlag);
		}

		if (addDefaultFlags) {
			cardFlags.add(new Visa());
			cardFlags.add(new MasterCard());
			cardFlags.add(new Discover());
		}
		
		mNumberEditText.setCardFlags(cardFlags);
	}
	
	public String getCardNumber() {
		return mNumberEditText.getUserInput();
	}
	
	public String getExpiryMonth() {
		return mFormEditText.getExpiryMonth();
	}
	
	public String getExpiryYear() {
		return mFormEditText.getExpiryYear();
	}
	
	public String getCvv() {
		return mFormEditText.getCvv();
	}
}
