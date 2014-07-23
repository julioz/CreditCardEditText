package br.com.zynger.creditcardedittext.flag;

import br.com.zynger.creditcardedittext.R;

public class Visa extends CardFlag {

	@Override
	public String getRegex() {
		return "^4[0-9]{6,}$";
	}

	@Override
	public int getCardImageResource() {
		return R.drawable.cc_visa;
	}

}
