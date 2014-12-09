package cmsc436.project.thermalcamera.gallery;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cmsc436.project.thermalcamera.R;
import cmsc436.project.thermalcamera.temperature.Temperature;
import cmsc436.project.thermalcamera.temperature.TemperatureUtil;

// From http://developer.android.com/training/implementing-navigation/lateral.html
public class GalleryCollectionPagerAdapter extends FragmentStatePagerAdapter {
	public static final String APP_FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "thermalcamera";
	private List<File> files = new ArrayList<File>(5);

	public GalleryCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
		this.files = getAllFiles();
	}

	private List<File> getAllFiles() {
		File dir = new File(APP_FILEPATH);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".png") || filename.endsWith(".jpg")
						|| filename.endsWith(".gif") || filename.endsWith(".jpeg");
			}
		});
		if (files == null) {
			return new ArrayList<File>();
		}
		ArrayList<File> fileList = new ArrayList<File>(10);
		fileList.addAll(Arrays.asList(files));
		return fileList;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new GalleryImageFragment(this, files.get(i));
		return fragment;
	}

	// Allows delete to change view
    public int getItemPosition(Object item) {
    	GalleryImageFragment fragment = (GalleryImageFragment)item;
        int position = files.indexOf(fragment.getFile());

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }

	@Override
	public int getCount() {
		return this.files.size();
	}

	public void remove(File file) {
		files.remove(file);
		file.delete();
		this.notifyDataSetChanged();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return (position + 1) + "";
	}

	public static class GalleryImageFragment extends Fragment {
		private GalleryCollectionPagerAdapter adapter; // used to delete
		private File file;

		public GalleryImageFragment(GalleryCollectionPagerAdapter adapter, File file) {
			this.adapter = adapter;
			this.file = file;
		}

		public File getFile() {
			return file;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
			
			displayPhoto(rootView);
			
			displayTemperature(rootView, file.getPath());

			Button deleteButton = (Button) rootView.findViewById(R.id.delete_button);
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					adapter.remove(file);
				}
			});
			return rootView;
		}

		private void displayPhoto(View rootView) {
			ImageView imageView = ((ImageView) rootView.findViewById(R.id.thermal_photo));
			imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

			int overlayColor = getOverlayColor(file.getPath());
			if (overlayColor > -1) {
				imageView.setColorFilter(overlayColor);
			}
		}

		private int getOverlayColor(String photoPath) {
			String fileName = new File(photoPath).getName();
			Temperature[] temperatures = TemperatureUtil.loadTemperaturesFromFileName(fileName);
			if (temperatures != null) {
				Temperature photoTemp = temperatures[0];
				Temperature homeTemp = temperatures[1];
				if (homeTemp.compareTo(photoTemp) < 0){
					//home temp is cooler than photo temp, make photo overly red
					return Color.argb(127, 255, 127, 0);
				} else if (homeTemp.compareTo(photoTemp) > 0){
					return Color.argb(127, 0, 127, 255);
				} 
			}
			return -1;
		}

		private void displayTemperature(View rootView, String photoPath) {
			String fileName = new File(photoPath).getName();
			Temperature[] temperatures = TemperatureUtil.loadTemperaturesFromFileName(fileName);
			Log.i("Temp", fileName);
			if (temperatures != null) {
				Temperature photoTemp = temperatures[0];
				Temperature homeTemp = temperatures[1];
				TextView temperatureText = (TextView) rootView.findViewById(R.id.temperature_value);
				temperatureText.setText("PhotoTemp: " + photoTemp + ", HomeTemp:" + homeTemp);
			}
		}
	}
}
