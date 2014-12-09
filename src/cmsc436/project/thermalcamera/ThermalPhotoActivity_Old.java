package cmsc436.project.thermalcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cmsc436.project.thermalcamera.gallery.GalleryActivity;
import cmsc436.project.thermalcamera.temperature.Temperature;
import cmsc436.project.thermalcamera.temperature.TemperatureUtil;

// View a specific photo, returning requested deletes to thermal camera
public class ThermalPhotoActivity_Old extends Activity {
	private TextView temperatureText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		//Assumes this extras will never be null
		Intent intent =  getIntent();
		final String photoPath = intent.getStringExtra(GalleryActivity.PHOTO_PATH); 
		final long photoID = intent.getLongExtra(GalleryActivity.PHOTO_ID, -1);

		temperatureText = (TextView) findViewById(R.id.temperature_value);
		
		ImageView imageView = (ImageView) findViewById(R.id.thermal_photo);
		Bitmap overlaidPhoto = putTempGraidentOverlay(photoPath);
		imageView.setImageBitmap(overlaidPhoto);
		//imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));

		displayTemperature(photoPath);
		
		Button deleteButton = (Button) findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(GalleryActivity.PHOTO_PATH, photoPath);
				intent.putExtra(GalleryActivity.PHOTO_ID, photoID);
				ThermalPhotoActivity_Old.this.setResult(RESULT_OK, intent);
				
				finish();
			}
			
		});
	}

	private Bitmap putTempGraidentOverlay(String photoPath) {
		Bitmap img = BitmapFactory.decodeFile(photoPath);
		String fileName = new File(photoPath).getName();
		Temperature[] temperatures = TemperatureUtil.loadTemperaturesFromFileName(fileName);
		if (temperatures != null) {
			Temperature photoTemp = temperatures[0];
			Temperature homeTemp = temperatures[1];
			int overlayColor;
			if (homeTemp.compareTo(photoTemp) < 0){
				//home temp is cooler than photo temp, make photo RED
				overlayColor = Color.RED;
			} else if (homeTemp.compareTo(photoTemp) > 0){
				overlayColor = Color.BLUE;
			} else { 
				return img;
			}
			
			//overlay guided by: http://stackoverflow.com/questions/11968040/imageview-colorfilter-on-non-tranparent-pixels-clip
			final Paint p = new Paint();
			
			final Bitmap bm1 = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Config.ARGB_8888);
			Canvas c = new Canvas(bm1);
			
			p.setColorFilter(new PorterDuffColorFilter(overlayColor, PorterDuff.Mode.OVERLAY));
			c.drawBitmap(img, 0, 0, p);
			
			final Bitmap bm2 = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Config.ARGB_8888);
			c = new Canvas(bm2);
			p.setColorFilter(new PorterDuffColorFilter(overlayColor, PorterDuff.Mode.SRC_ATOP));
			c.drawBitmap(img, 0, 0, p);
			
			p.setColorFilter(null);
			p.setXfermode(new AvoidXfermode(overlayColor, 0, AvoidXfermode.Mode.TARGET));
	        c.drawBitmap(bm1, 0, 0, p);
	        
	    	return bm2;
		} 
		return img; 
	}

	private void displayTemperature(String photoPath) {
		String fileName = new File(photoPath).getName();
		Temperature[] temperatures = TemperatureUtil.loadTemperaturesFromFileName(fileName);
		if (temperatures != null) {
			Temperature photoTemp = temperatures[0];
			Temperature homeTemp = temperatures[1];
			temperatureText.setText("PhotoTemp: " + photoTemp + ", HomeTemp:" + homeTemp);
		}
	}
}
