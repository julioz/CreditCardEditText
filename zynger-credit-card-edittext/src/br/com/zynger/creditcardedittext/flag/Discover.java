package br.com.zynger.creditcardedittext.flag;

import br.com.zynger.creditcardedittext.R;

public class Discover extends CardFlag {

	@Override
	public String getRegex() {
		return "^6(?:011|5[0-9]{2})[0-9]{3,}$";
	}

	@Override
	public int getCardImageResource() {
		return R.drawable.cc_disc;
	}

}
