package br.com.zynger.creditcardedittext.flag;

import br.com.zynger.creditcardedittext.R;

public class MasterCard extends CardFlag {

	@Override
	public String getRegex() {
		return "^5[1-5][0-9]{5,}$";
	}

	@Override
	public int getCardImageResource() {
		return R.drawable.cc_mc;
	}

}
