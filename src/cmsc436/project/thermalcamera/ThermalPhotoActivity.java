package cmsc436.project.thermalcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import cmsc436.project.thermalcamera.gallery.GalleryActivity;
import cmsc436.project.thermalcamera.temperature.Temperature;
import cmsc436.project.thermalcamera.temperature.TemperatureUtil;

// View a specific photo, returning requested deletes to thermal camera
public class ThermalPhotoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		//Assumes this extras will never be null
		Intent intent =  getIntent();
		final String photoPath = intent.getStringExtra(GalleryActivity.PHOTO_PATH); 
		final long photoID = intent.getLongExtra(GalleryActivity.PHOTO_ID, -1);
		
		ImageView imageView = (ImageView) findViewById(R.id.thermal_photo);
		imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));

		displayTemperature(photoPath);
		
		Button deleteButton = (Button) findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(GalleryActivity.PHOTO_PATH, photoPath);
				intent.putExtra(GalleryActivity.PHOTO_ID, photoID);
				ThermalPhotoActivity.this.setResult(RESULT_OK, intent);
				
				finish();
			}
			
		});
	}

	private void displayTemperature(String photoPath) {
		// TODO display temperature from filename if present
		String fileName = new File(photoPath).getName();
		Temperature[] temperatures = TemperatureUtil.loadTemperaturesFromFileName(fileName);
		if (temperatures != null) {
			Temperature photoTemp = temperatures[0];
			Temperature homeTemp = temperatures[1];
			Log.i(ThermalCameraActivity.TAG, photoTemp + "," + homeTemp);
		}
	}
}
