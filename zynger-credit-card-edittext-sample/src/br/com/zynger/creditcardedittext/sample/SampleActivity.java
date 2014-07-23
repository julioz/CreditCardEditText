package br.com.zynger.creditcardedittext.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.zynger.creditcardedittext.CreditCardEditText;
import br.com.zynger.creditcardedittextsample.R;

public class SampleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sample);
		
		final CreditCardEditText et = (CreditCardEditText) findViewById(R.id.activity_sample_et);
		
		Button bt = (Button) findViewById(R.id.activity_sample_button);
		
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer buffer = new StringBuffer();
				
				buffer.append("#: " + et.getCardNumber());
				buffer.append('\n');
				buffer.append("M: " + et.getExpiryMonth());
				buffer.append('\n');
				buffer.append("Y: " + et.getExpiryYear());
				buffer.append('\n');
				buffer.append("Cvv: " + et.getCvv());
				
				Toast.makeText(v.getContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
