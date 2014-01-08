package com.shinkansen.touchcolor.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * ImageAdapter for Gallery
 * @author huuthang
 */
public class ImageAdapter extends BaseAdapter {
	private ArrayList<Integer> pics;
	private Context ctx;

	public ImageAdapter(Context c, ArrayList<Integer> pics) {
		ctx = c;
		this.pics = pics;
	}

	@Override
	public int getCount() {
		
		return pics.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ImageView iv = new ImageView(ctx);
		iv.setImageResource(pics.get(arg0));
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setLayoutParams(new Gallery.LayoutParams(250,150));
		return iv;
	}



}
