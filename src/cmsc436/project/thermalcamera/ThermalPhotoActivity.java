package cmsc436.project.thermalcamera;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ThermalPhotoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		//getIntent().getExtras();
		
		ImageView imageView = (ImageView) findViewById(R.id.thermal_photo);
		//imageView.setImageBitmap(BitmapFactory.decodeFile(chosenFile.getAbsolutePath()););
	}

}
