package cmsc436.project.thermalcamera.gallery;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

// Adapter for a gallery of images
public class GalleryAdapter extends BaseAdapter {
	private static final String APP_FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	private Context context;
	private File[] files; // TODO load elsewhere?
	// TODO scrolling

	public GalleryAdapter(Context context) {
		this.context = context;
		this.files = getAllFiles();
		for (File file : files) {
			Log.d("ListFiles", "Image File: " + file.getName());
		}
	}

	private File[] getAllFiles() {
		File dir = new File(APP_FILEPATH);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".png");
			}
		});
		return files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//		return super.getView(position, convertView, parent);
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new GridView.LayoutParams(150, 150));

		if (files.length < 0) {
			File chosenFile = files[new Random().nextInt(files.length)];
			Log.i("Loading", chosenFile.getAbsolutePath());
			if (chosenFile.exists()) {
				Log.i("Loading", "file exists");
				Bitmap imageBitmap = BitmapFactory.decodeFile(chosenFile.getAbsolutePath());
				imageView.setImageBitmap(imageBitmap);
				return imageView;
			}
		} else {
			Log.i("Loading", "generating placeholder image for gallery");
			Bitmap drawnBitmap = generatePlaceHolderImage();
			imageView.setImageBitmap(drawnBitmap);
			return imageView;
		}

		return imageView;
	}

	private Bitmap generatePlaceHolderImage() {
		Bitmap.Config bitmapConfiguration = Bitmap.Config.ARGB_8888;
		Bitmap drawnBitmap = Bitmap.createBitmap(150, 150, bitmapConfiguration);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(drawnBitmap);
		paint.setColor(Color.GREEN);
		canvas.drawRect(0, 0, 30, 30, paint);
		paint.setColor(Color.RED);
		canvas.drawRect(30, 30, 60, 60, paint);
		paint.setColor(Color.BLUE);
		canvas.drawRect(60, 60, 90, 90, paint);
		paint.setColor(Color.CYAN);
		canvas.drawRect(90, 90, 120, 120, paint);
		paint.setColor(Color.YELLOW);
		canvas.drawRect(120, 120, 150, 150, paint);
		return drawnBitmap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 13;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
