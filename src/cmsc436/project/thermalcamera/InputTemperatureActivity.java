package cmsc436.project.thermalcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InputTemperatureActivity extends Activity implements OnItemSelectedListener {

	public final String TEMPERATURE = "Temperature";
	public final String SCALE = "Scale";
	
	private Scales mScale = Scales.F;
	
	private ArrayAdapter<CharSequence> mAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_temperature);
		
		//Spinners!  http://developer.android.com/guide/topics/resources/string-resource.html
		
		Spinner temperatureSpinner = (Spinner) this.findViewById(R.id.temperature_scale_spinner);
		mAdapter = ArrayAdapter.createFromResource(this, R.array.temperture_scales, android.R.layout.simple_spinner_item);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		temperatureSpinner.setAdapter(mAdapter);
		temperatureSpinner.setOnItemSelectedListener(this); //overriding OnItemSelectedListener methods to use 'this'
		
		
		Button startButton = (Button) this.findViewById(R.id.input_start_button);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startCameraActivity = new Intent(InputTemperatureActivity.this, ThermalCameraActivity.class);
				
				//If they entered a home temperature, passes that value and temperature scale chosen
				EditText temperatureInput = (EditText) findViewById(R.id.user_temp_input);
				String temperatureString = temperatureInput.getText().toString();
				if (null == temperatureString || "".equals(temperatureString)){
					//TODO handle how we do this.. pass an N/A?
					temperatureString = "Not Entered";
				}
				startCameraActivity.putExtra(TEMPERATURE, temperatureString);
				startCameraActivity.putExtra(SCALE, mScale);
				
				startActivity(startCameraActivity);
			}
			
		});
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
		// TODO Make sure this works
		mScale = Scales.F;
	}
	

}
