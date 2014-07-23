package br.com.zynger.creditcardedittext;


public interface OnCreditCardInputListener {
	public void onInputError(CreditCardNumberEditText editText);
	public void onInputSuccess(CreditCardNumberEditText editText);
}