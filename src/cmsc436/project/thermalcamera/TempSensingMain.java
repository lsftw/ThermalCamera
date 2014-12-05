package cmsc436.project.thermalcamera;

import ioio.lib.api.TwiMaster;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

// TODO add required libraries to get this code to compile
// Code adapted from: http://web.media.mit.edu/~talfaro/Site/DemoIOIOThermometer.shtml
public class TempSensingMain extends IOIOActivity {
	private final String TAG = "TempSensingMain";

	//Sensor I2C
	private TwiMaster twi;
	double sensortemp;

	//Sensor data process
	byte[] req = new byte[] { 0x07 };//Byte address to ask for sensor data
	byte[] tempdata = new byte[2];   //Byte to save sensor data
	double receivedTemp = 0x0000;    //Value after processing sensor data
	double tempFactor = 0.02;        //0.02 degrees per LSB 
	//(measurement resolution of the MLX90614)

	//UI
	private TextView TempCelsius;
	private TextView TempFahrenheit;

	//Very Simple onCreate with nothing but the text that will display the values

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_sensing_main);

		TempCelsius = (TextView) findViewById(R.id.tempC);
		TempFahrenheit = (TextView) findViewById(R.id.tempF);
	}

	class Looper extends BaseIOIOLooper {
		//Here we initialize 'twi' as an I2C port
		@Override
		protected void setup() throws ConnectionLostException {
			twi = ioio_.openTwiMaster(0, TwiMaster.Rate.RATE_100KHz, true);

		}

		/*
		 * twi.writeRead does the request for value.
		 * 0x5A is the sensor I2C address.
		 * req is the address to read the values we are interested in.
		 * tempdata will be the value received
		 */
		@Override
		public void loop() throws ConnectionLostException {

			try {
				twi.writeRead(0x5A, false, req,req.length,tempdata,tempdata.length);

				receivedTemp = (double)(((tempdata[1] & 0x007f) << 8)+ tempdata[0]);
				receivedTemp = (receivedTemp * tempFactor)-0.01;

				handleTemp(receivedTemp);
				Log.d(TAG, "RAW: "+sensortemp);

				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
}
