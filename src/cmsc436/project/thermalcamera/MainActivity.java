package cmsc436.project.thermalcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cmsc436.project.thermalcamera.gallery.GalleryActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up the onClick for the IR sensor functionality
		Button useButton = (Button) this.findViewById(R.id.use_sensor_button);
		useButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent useSensorIntent = new Intent(MainActivity.this, TempSensingMain.class);
				Intent useSensorIntent = new Intent(MainActivity.this, InputTemperatureActivity.class);
				startActivity(useSensorIntent);
			}
			
		});
		
		// Set up the onCLick for the Gallery view
		Button galleryButton = (Button) this.findViewById(R.id.view_gallery_button);
		galleryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent galleryIntent = new Intent(MainActivity.this, GalleryActivity.class);
				startActivity(galleryIntent);
			}
			
		});
	}

}
