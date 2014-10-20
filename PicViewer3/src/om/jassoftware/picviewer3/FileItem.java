package om.jassoftware.picviewer3;

import java.io.File;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class FileItem {

	Bitmap icon;
	Bitmap full;
	Bitmap scaled;
	File src;
	
	public FileItem(File f){
		src = f;
		//find type of file and set icon
		if (src.isDirectory()){
			icon = BitmapFactory.decodeResource(MainActivity.c.getResources(), R.drawable.ic_dir);
		} else {
			String suffix = src.getName();  //get name
			int sep = suffix.indexOf(".");  //find extension
			suffix = suffix.substring(sep + 1);  //get the extension
			String[] picTypes = {"jpg","jpeg","png","gif","bmp"};  //list known pic extensions
			boolean isPic = false;
			for (int i = 0;i < picTypes.length;i++){
				if (suffix.equalsIgnoreCase(picTypes[i])){
					isPic = true;
				}
			}
			if (isPic){
				icon = BitmapFactory.decodeResource(MainActivity.c.getResources(), R.drawable.ic_launcher);
			} else {
				icon = BitmapFactory.decodeResource(MainActivity.c.getResources(), R.drawable.ic_file);
			}
		}
		full = null;
		scaled = null;
		loadBitmap(src);
	}
	
	public Bitmap getBitmap(int type){
		switch (type){
			case 0:  //icon
				return icon;
			case 1:  //full, if available
				if (full!=null){
					return full;
				} else {
					return icon;
				}
			case 2:  //caled, if available
				if (scaled!=null){
					return scaled;
				} else {
					return icon;
				}
			default:
				return icon;
		}
	}
	
	protected void loadBitmap(File f){
		BitmapLoader loader = new BitmapLoader(f);
		loader.execute(f);
	}
	
	
	public class BitmapLoader extends AsyncTask<File, Void, Bitmap>{
		
		File src;
		
		public BitmapLoader(File f){
			src = f;
		}
	
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result!=null){
				full = result;
				int picsize;
				DisplayMetrics metrics = null;
				WindowManager wm = (WindowManager) MainActivity.c.getSystemService(Context.WINDOW_SERVICE);
				wm.getDefaultDisplay().getMetrics(metrics);
				switch (metrics.densityDpi){
					case DisplayMetrics.DENSITY_LOW:
						picsize = 36;
						break;
					case DisplayMetrics.DENSITY_MEDIUM:
						picsize = 48;
						break;
					case DisplayMetrics.DENSITY_HIGH:
						picsize = 72;
						break;
					case DisplayMetrics.DENSITY_XHIGH:
						picsize = 96;
						break;
					case DisplayMetrics.DENSITY_XXHIGH:
						picsize = 144;
						break;
					case DisplayMetrics.DENSITY_XXXHIGH:
						picsize = 192;
						break;
					default:  //probably the google tv setting, so 512
						picsize = 512;
						break;
				}
				scaled = Bitmap.createScaledBitmap(result, picsize, picsize, false);
			}
		}
		
		@Override
		protected Bitmap doInBackground(File... f) {
			Bitmap result = null;
			result = BitmapFactory.decodeFile(f[0].getAbsolutePath());
			return result;
		}
		
	} //end of BitmapLoader
}  //end of FileItem
