package cmsc436.project.thermalcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import cmsc436.project.thermalcamera.gallery.GalleryAdapter;

// TODO take picture, overlay sensor data
// Capture image intent code from http://developer.android.com/guide/topics/media/camera.html
public class ThermalCameraActivity extends Activity implements OnItemSelectedListener {
	private static final String TAG = "ThermalCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
	
	private EditText tempInput;
	private Scales mScale = Scales.F;
	private ArrayAdapter<CharSequence> mAdapter;

	private ImageView imagePreview;
	private Uri lastImageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// Setting up spinner
		Spinner temperatureSpinner = (Spinner) this.findViewById(R.id.photo_temp_scale);
		mAdapter = ArrayAdapter.createFromResource(this, R.array.temperture_scales, android.R.layout.simple_spinner_item);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		temperatureSpinner.setAdapter(mAdapter);
		temperatureSpinner.setOnItemSelectedListener(this); //overriding OnItemSelectedListener methods to use 'this'

		imagePreview = (ImageView) this.findViewById(R.id.camera_preview);

		tempInput = (EditText) this.findViewById(R.id.photo_temp_input);
		Button buttonSetTemp = (Button) this.findViewById(R.id.button_set_photo_temp);
		buttonSetTemp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String temperature = tempInput.getText().toString();
				String homeTemperature = null; // TODO get user's home temperature from intent
				Log.i(TAG, "Set temp to " + temperature + mScale + ", home temperature is: " + homeTemperature);

				// TODO ask user for temperature, save temperature in filename
				//insert temperature value (e.g. 43C or 81F) to filename
				//going from: 
				//    IMG_timestamp.jpg 
				//to: IMG_timestamp_hometemp_temperature.jpg
				//
				// e.g. IMG_timestamp_25C_22C.jpg
			}
		});
		
		lastImageUri = takePicture();
	}

	private Uri takePicture() {
		// create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri fileUri = getOutputImageFileUri(); // create a file to save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
		// start the image capture Intent
		Log.i(TAG, "Starting intent to capture image.");
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		return fileUri;
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
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) { // Image captured and saved to fileUri specified in the Intent
				Log.i(TAG, "Thermal image saved to:" + lastImageUri);
				Toast.makeText(this, "Image saved to:\n" + lastImageUri, Toast.LENGTH_LONG).show();

				// show image preview
				updateImagePreview(lastImageUri);
				
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else { // Image capture failed, advise user
				Toast.makeText(this, "Image capture failed.", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void updateImagePreview(Uri uri) {
		try {
			imagePreview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
