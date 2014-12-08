package cmsc436.project.thermalcamera;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import cmsc436.project.thermalcamera.gallery.GalleryActivity;

public class ThermalPhotoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		//Assumes this extras will never be null
		String photoPath = getIntent().getExtras().getString(GalleryActivity.PHOTO_PATH); 
		
		ImageView imageView = (ImageView) findViewById(R.id.thermal_photo);
		imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));
	}

}
