package cmsc436.project.thermalcamera.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cmsc436.project.thermalcamera.R;
import cmsc436.project.thermalcamera.ThermalPhotoActivity;

// TODO view gallery of saved thermal camera pictures
//startActivity(new Intent(this, GalleryActivity.class));
public class GalleryActivity extends Activity {

	public final static String PHOTO_PATH = "PHOTO_PATH";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		GridView gridView = (GridView) findViewById(R.id.gridView1);
		GalleryAdapter adapter = new GalleryAdapter(this);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object tag = view.getTag();
				if (tag != null) {
					Log.d("ViewImage", tag.toString());
					// TODO open detailed view
					
					Intent intent = new Intent(GalleryActivity.this, ThermalPhotoActivity.class);
					intent.putExtra(PHOTO_PATH, tag.toString());
				}
			}
		});
	}
}
