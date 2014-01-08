package com.shinkansen.touchcolor.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Pager Adapter for ViewPager
 * @author huuthang
 */
public class ImageViewPagerAdapter extends PagerAdapter {

	Context context;
	private ArrayList<Integer> pics;
	public ImageViewPagerAdapter(Context context, ArrayList<Integer> pics){
		this.context=context;
		this.pics = pics;
	}
	
	@Override
	public int getCount() {
		return pics.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {

		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setImageResource(pics.get(position));
		((ViewPager) container).addView(imageView, 0);
		
		return imageView;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}
}
