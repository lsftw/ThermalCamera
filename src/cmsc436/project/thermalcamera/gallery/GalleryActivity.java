package cmsc436.project.thermalcamera.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cmsc436.project.thermalcamera.R;
import cmsc436.project.thermalcamera.ThermalPhotoActivity;


public class GalleryActivity extends Activity {

	public final static String PHOTO_PATH = "PHOTO_PATH";
	public final static String PHOTO_ID = "PHOTO_ID";
	public final static Integer REQUEST_CODE = 436;

	private GalleryAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		GridView gridView = (GridView) findViewById(R.id.gridView1);
		mAdapter = new GalleryAdapter(this);
		gridView.setAdapter(mAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object tag = view.getTag();
				if (tag != null) {
					Log.d("ViewImage", tag.toString());
					// TODO open detailed view
					
					Intent intent = new Intent(GalleryActivity.this, ThermalPhotoActivity.class);
					intent.putExtra(PHOTO_PATH, tag.toString());
					intent.putExtra(PHOTO_ID, id);
					startActivityForResult(intent, REQUEST_CODE);
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_all, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.option_delete_all) {
			mAdapter.clear();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			mAdapter.remove(data.getLongExtra(PHOTO_ID, -1));
		}

	}
	
}
