package cmsc436.project.thermalcamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import cmsc436.project.thermalcamera.gallery.GalleryActivity;
import cmsc436.project.thermalcamera.gallery.GalleryAdapter;
import cmsc436.project.thermalcamera.temperature.Scales;
import cmsc436.project.thermalcamera.temperature.Temperature;
import cmsc436.project.thermalcamera.temperature.TemperatureUtil;

// Capture image intent code from http://developer.android.com/guide/topics/media/camera.html
public class ThermalCameraActivity extends Activity implements OnItemSelectedListener {
	public static final String TAG = "ThermalCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;

	private EditText tempInput;
	private Scales mScale = Scales.F;
	private ArrayAdapter<CharSequence> mAdapter;

	private ImageView imagePreview;
	private String lastImagePath; // .getPath() from Uri
	private Button buttonSetTemp;
	
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
		TextWatcher inputWatcher = new InputTextWatcher();
		tempInput.addTextChangedListener(inputWatcher);
		buttonSetTemp = (Button) this.findViewById(R.id.button_set_photo_temp);
		buttonSetTemp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTemperatureInFilename();

				promptNextAction();
			}
		});
		updateButtonState();

		lastImagePath = takePicture().getPath();
		
		
	}

	protected void setTemperatureInFilename() {
		String photoTempDegrees = tempInput.getText().toString();
		Scales photoTempScale = mScale;
		Temperature photoTemp = new Temperature(photoTempDegrees, photoTempScale);

		String homeTempDegrees = getIntent().getStringExtra(InputHomeTemperatureActivity.TEMPERATURE);
		Scales homeTempScale = (Scales)getIntent().getSerializableExtra(InputHomeTemperatureActivity.SCALE);
		Temperature homeTemp = new Temperature(homeTempDegrees, homeTempScale);
		Log.i(TAG, "Set temp to " + photoTemp + ", home temperature is: " + homeTemp);

		// ask user for temperature, save temperature in filename
		//insert temperature value (e.g. 43C or 81F) to filename
		//going from: 
		//    IMG_timestamp.jpg 
		//to: IMG_timestamp_hometemp_temperature.jpg
		//
		// e.g. IMG_timestamp_25C_22C.jpg

		File file = new File(lastImagePath);

		String fileName = file.getName();
		String newFileName = TemperatureUtil.storeTemperaturesInFileName(fileName, photoTemp, homeTemp);
		Log.i(TAG, "File: " + file + " will be renamed to: " + newFileName);
		File newFile = new File(file.getParent(), newFileName);
		Log.i(TAG, "New file: " + newFile);
		boolean result = file.renameTo(newFile);
		Log.i(TAG, "Rename success? " + result);
		if (result) {
			lastImagePath = newFileName;
		}
	}
	protected void promptNextAction() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setPositiveButton("Take Another", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				lastImagePath = takePicture().getPath();
			}
		});
		alert.setNegativeButton("Go to Gallery", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(ThermalCameraActivity.this, GalleryActivity.class);
				startActivity(intent);
			}
		});
		alert.show();
	}
	
	
//	protected void promptSetTemperature() {
//		AlertDialog.Builder alert = new AlertDialog.Builder(this);
//		alert.setTitle("Title");
//		alert.setMessage("Message");
//
//		final EditText input = new EditText(this);
//		alert.setView(input);
//		
//		alert.setPositiveButton("Set Temperature", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				Log.i(TAG, "Setting temperature data for image: " + lastImagePath);
//				// Do something with value!
//			}
//		});
//
//		alert.setNegativeButton("Discard Image", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				Log.i(TAG, "Discarding image with no temperature data: " + lastImagePath);
//				File file = new File(lastImagePath);
//				file.delete();
//			}
//		});
//
//		alert.show();
//	}

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
				Log.i(TAG, "Thermal image saved to:" + lastImagePath);
				Toast.makeText(this, "Image saved to:\n" + lastImagePath, Toast.LENGTH_LONG).show();

				// show image preview
				updateImagePreview(lastImagePath);

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else { // Image capture failed, advise user
				Toast.makeText(this, "Image capture failed.", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void updateImagePreview(String path) {
		imagePreview.setImageBitmap(BitmapFactory.decodeFile(path));
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		CharSequence scale = (CharSequence) parent.getItemAtPosition(position);
		mScale = scale.equals("�F") ? Scales.F : Scales.C;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		mScale = Scales.F;
	}
	
	
	
	
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
	private void updateButtonState(){
		//Log.i("CMSC436_TEMP", mEditTextView.getText() +", "+mEditTextView.getText().toString());
		String input = tempInput.getText().toString();
		boolean noGood = input.equals("") || input.equals("-") || input.equals(".") || input.equals("-.");
		buttonSetTemp.setEnabled(!noGood);
	}
	
}
