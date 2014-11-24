package cmsc436.project.thermalcamera.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import cmsc436.project.thermalcamera.R;

// TODO view gallery of saved thermal camera pictures
//startActivity(new Intent(this, GalleryActivity.class));
public class GalleryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		GridView gridView = (GridView) findViewById(R.id.gridView1);
		GalleryAdapter adapter = new GalleryAdapter(this);
		gridView.setAdapter(adapter);
	}
}
