package cmsc436.project.thermalcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cmsc436.project.thermalcamera.temperature.Scales;

// Input home temperature
public class InputHomeTemperatureActivity extends Activity implements OnItemSelectedListener {

	public static final String TEMPERATURE = "Temperature";
	public static final String SCALE = "Scale";
	
	private Scales mScale = Scales.F;
	
	private ArrayAdapter<CharSequence> mAdapter;
	private EditText mEditTextView;
	private Button mStartButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_home_temperature);
		
		//Spinners!  http://developer.android.com/guide/topics/resources/string-resource.html
		
		Spinner temperatureSpinner = (Spinner) this.findViewById(R.id.temperature_scale_spinner);
		mAdapter = ArrayAdapter.createFromResource(this, R.array.temperture_scales, android.R.layout.simple_spinner_item);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		temperatureSpinner.setAdapter(mAdapter);
		temperatureSpinner.setOnItemSelectedListener(this); //overriding OnItemSelectedListener methods to use 'this'
		
		//Setting up watcher to gray out submit button if input temperature is emtpy
		TextWatcher textWatcher = new InputTextWatcher();
		mEditTextView = (EditText) findViewById(R.id.photo_temp_input);
		mEditTextView.addTextChangedListener(textWatcher);
		
		mStartButton = (Button) this.findViewById(R.id.input_start_button);
		mStartButton.setEnabled(false);
		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startCameraActivity = new Intent(InputHomeTemperatureActivity.this, ThermalCameraActivity.class);
				
				//If they entered a home temperature, passes that value and temperature scale chosen
				EditText temperatureInput = (EditText) findViewById(R.id.photo_temp_input);
				String temperatureString = temperatureInput.getText().toString();
				if (null == temperatureString || "".equals(temperatureString)){
					temperatureString = "Not Entered";
				}
				startCameraActivity.putExtra(TEMPERATURE, temperatureString);
				startCameraActivity.putExtra(SCALE, mScale);
				
				startActivity(startCameraActivity);
			}
			
		});
		
		
		updateButtonState();
	}


	// Guided by: http://stackoverflow.com/questions/5888156/how-do-you-gray-out-a-submit-button-when-specific-edittext-boxes-are-empty
	private class InputTextWatcher implements TextWatcher {
		public void afterTextChanged(Editable s){
			updateButtonState();
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after){
			//unused
		}
		public void onTextChanged(CharSequence s, int start, int before, int count){
			//unused
		}
	}
	
	//NOTE: Must be called AFTER both mStartButton and mEditTextView are defined in onCreate()
	private void updateButtonState(){
		//Log.i("CMSC436_TEMP", mEditTextView.getText() +", "+mEditTextView.getText().toString());
		String input = mEditTextView.getText().toString();
		boolean noGood = input.equals("") || input.equals("-") || input.equals(".") || input.equals("-.");
		mStartButton.setEnabled(!noGood);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		CharSequence scale = (CharSequence) parent.getItemAtPosition(position);
		//Toast.makeText(this, scale, Toast.LENGTH_LONG).show();
		mScale = scale.equals("°F") ? Scales.F : Scales.C;
		//mScale = Scales.valueOf(scale.toString()); //doesn't work

	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		mScale = Scales.F;
	}
}
