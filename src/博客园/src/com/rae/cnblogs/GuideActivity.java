package com.rae.cnblogs;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GuideActivity extends BaseActivity implements OnClickListener {

	private ViewPager		mViewPager;
	private LinearLayout	mIndexLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mIndexLinearLayout = (LinearLayout) findViewById(R.id.ll_guide_index);
		
		// 按钮
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_reg).setOnClickListener(this);

		ArrayList<View> viewLists = new ArrayList<View>();
		viewLists.add(getImageView(R.drawable.guide_one));
		viewLists.add(getImageView(R.drawable.guide_two));
		viewLists.add(getImageView(R.drawable.guide_three));

		// 生成点
		for (int i = 0; i < viewLists.size(); i++) {
			ImageView img = new ImageView(this);
			if (i == 0) {
				img.setImageResource(R.drawable.user_guide_index_bg_selected);
			}
			else {
				img.setImageResource(R.drawable.user_guide_index_bg_normal);
			}
			img.setPadding(0, 0, 10, 0);
			mIndexLinearLayout.addView(img);
		}

		mViewPager.setAdapter(new GuideViewPager(viewLists));

		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				for (int i = 0; i < mIndexLinearLayout.getChildCount(); i++) {
					ImageView view = (ImageView) mIndexLinearLayout.getChildAt(i);
					if(i== index){
						view.setImageResource(R.drawable.user_guide_index_bg_selected);
					}else{
						view.setImageResource(R.drawable.user_guide_index_bg_normal);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private ImageView getImageView(int resId) {
		ImageView img = new ImageView(this);
		img.setScaleType(ScaleType.FIT_XY);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		img.setImageResource(resId);
		return img;
	}

	class GuideViewPager extends PagerAdapter {

		private ArrayList<View>	mViewLists;

		public GuideViewPager(ArrayList<View> viewLists) {
			this.mViewLists = viewLists;
		}

		@Override
		public int getCount() {
			return mViewLists.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViewLists.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = mViewLists.get(position);
			container.addView(view);
			return view;
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_login:
					startActivity(new Intent(this, LoginActivity.class));
					this.finish();
				break;

			default:
				break;
		}
	}

}
