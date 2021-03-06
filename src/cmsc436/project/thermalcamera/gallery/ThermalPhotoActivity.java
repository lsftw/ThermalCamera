package cmsc436.project.thermalcamera.gallery;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import cmsc436.project.thermalcamera.R;

// Scrollable detailed image view
public class ThermalPhotoActivity extends FragmentActivity {
	private GalleryCollectionPagerAdapter mDemoCollectionPagerAdapter;
	private ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_pager);

		mDemoCollectionPagerAdapter = new GalleryCollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);

		final int photoID = getIntent().getIntExtra(GalleryActivity.PHOTO_POSITION, -1);
		mViewPager.setCurrentItem(photoID);
	}
}
