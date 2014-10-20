package com.jassoftware.picviewer2;

import java.util.ArrayList;

import com.jassoftware.picviewer.FileAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileItemAdapter extends BaseAdapter {

	ArrayList<FileItem> mList;
	Context c;
	static class ViewHolder{
		ImageView thumb;
		TextView name;
		TextView type;
	}
	LayoutInflater inflater;
	
	public FileItemAdapter(){
		mList = null;
		c = null;
	}
	
	public FileItemAdapter(Context con, ArrayList<FileItem> list){
		mList = list;
		c = con;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int pos) {
		FileItem fi = mList.get(pos);
		return fi;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View cview, ViewGroup parent) {
		ViewHolder holder;
		FileItem fi = mList.get(pos);
		if (cview==null){
			holder = new ViewHolder();
			inflater = ((Activity) c).getLayoutInflater();
			cview = inflater.inflate(R.layout.filelistitem, parent, false);
			holder.thumb = (ImageView)cview.findViewById(R.id.thumb);
			holder.name = (TextView)cview.findViewById(R.id.name);
			holder.type = (TextView)cview.findViewById(R.id.type);
			cview.setTag(holder);
		} else {
			holder = (ViewHolder)cview.getTag();
		}
			//holder.thumb.setImageBitmap(fi.getBitmap());
			if (fi.bmScaled!=null){  //if bmScaled is set, get scaled version
				holder.thumb.setImageBitmap(fi.getBitmap(3));
			} else {
				holder.thumb.setImageBitmap(fi.getBitmap(1));  //will use bm1  (the placeholder icon)
			}
			holder.name.setText(fi.getName());
			String suffix = fi.getName();
			String[] pt = MainActivity.picTypes;
			int i = suffix.indexOf(".");
			suffix = suffix.substring(i+1);
			boolean isPic = false;
			for (int j = 0;j<pt.length;j++){
				if (suffix.equalsIgnoreCase(pt[j])){
					isPic = true;
				}
			}
			if (isPic){
				holder.type.setText("pic: "+suffix);
			} else {
				holder.type.setText("file");
			}
		return cview;
	}

}
