package br.com.zynger.creditcardedittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

public class CreditCardBaseEditText extends EditText {

	protected static final String SEPARATOR = " ";
	
	protected boolean mSeparatorDeleted;
	private Drawable mCardFlagDrawable;
	
	public CreditCardBaseEditText(Context context) {
		super(context);
		
		init();
	}
	
	public CreditCardBaseEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}
	
	public CreditCardBaseEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}
	
	private void init() {
		setInputType(InputType.TYPE_CLASS_NUMBER);
	}
	
	protected void setCardIcon(int res) {
		Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.25), 
                                 (int)(drawable.getIntrinsicHeight()*0.25));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1.0f, 1.0f);
        mCardFlagDrawable = sd.getDrawable();
		setCompoundDrawables(mCardFlagDrawable, null, null, null);
	}
	
	public Drawable getCardFlagDrawable() {
		return mCardFlagDrawable;
	}
	
	protected void setDefaultIcon() {
		setCardIcon(R.drawable.cc_front);
	}

}
