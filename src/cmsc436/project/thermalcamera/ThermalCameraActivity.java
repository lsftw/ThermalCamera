package cmsc436.project.thermalcamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import cmsc436.project.thermalcamera.gallery.GalleryAdapter;

// TODO take picture, overlay sensor data
// Capture image intent code from http://developer.android.com/guide/topics/media/camera.html
public class ThermalCameraActivity extends Activity implements OnItemSelectedListener {
	private static final String TAG = "ThermalCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
	
	private Scales mScale = Scales.F;
	private ArrayAdapter<CharSequence> mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		Uri fileUri = getOutputImageFileUri(); // create a file to save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

		
		// Setting up spinner
		Spinner temperatureSpinner = (Spinner) this.findViewById(R.id.temperature_scale_spinner);
		mAdapter = ArrayAdapter.createFromResource(this, R.array.temperture_scales, android.R.layout.simple_spinner_item);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		temperatureSpinner.setAdapter(mAdapter);
		temperatureSpinner.setOnItemSelectedListener(this); //overriding OnItemSelectedListener methods to use 'this'
		
		// start the image capture Intent
		Log.i(TAG, "Starting intent to capture image.");
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	// Create a file Uri for saving an image or video
	private static Uri getOutputImageFileUri() {
		return Uri.fromFile(getOutputImageFile());
	}


	// Create a File for saving an image.
	private static File getOutputImageFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(GalleryAdapter.APP_FILEPATH);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) { // Image captured and saved to fileUri specified in the Intent
				Log.i(TAG, "Thermal image saved to:" + getOutputImageFileUri());
				Toast.makeText(this, "Image saved to:\n" + getOutputImageFileUri(), Toast.LENGTH_LONG).show();
				
				
				
				
				
				// TODO ask user for temperature, save temperature in filename
				//insert temperature value (e.g. 43C or 81F) to filename
				//resulting filename should be: IMG_timestamp_temperature.jpg

				//TODO Steven - also get the preview of image/picture just taken to work
				
				
				
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else { // Image capture failed, advise user
				Toast.makeText(this, "Image capture failed.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		CharSequence scale = (CharSequence) parent.getItemAtPosition(position);
		mScale = scale.equals("°F") ? Scales.F : Scales.C;
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		mScale = Scales.F;
	}
}
