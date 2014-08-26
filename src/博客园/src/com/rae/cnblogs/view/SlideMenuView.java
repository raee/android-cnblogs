package com.rae.cnblogs.view;

import java.util.List;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.MenuCategoryAdapter;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.model.Category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 菜单视图
 * 
 * @author ChenRui
 * 
 */
public class SlideMenuView extends LinearLayout
{
	/**
	 * 菜单项目点击监听接口
	 * 
	 * @author ChenRui
	 * 
	 */
	public interface SlideMenuListener
	{
		/**
		 * 向左滑动时触发
		 * 
		 * @param view
		 * @param x
		 * @param span
		 */
		void onSlideLeft(SlideMenuView view, int x, int span);
		
		void onItemClick(SlideMenuView view, int position);
	}
	
	private Context				mContext;
	private CnBlogsOpenAPI		sdk;
	
	private MenuCategoryAdapter	mCategoryAdapter;
	private float	mRawX;
	
	public SlideMenuView(Context context)
	{
		this(context, null);
	}
	
	public SlideMenuView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		sdk = CnBlogsOpenAPI.getInstance(context);
		initView();
	}
	
	public void initView()
	{
		View menuView = LayoutInflater.from(mContext).inflate(R.layout.view_slide_menu, this);
		ListView lvCategory = (ListView) menuView.findViewById(android.R.id.list);
		List<Category> datalist = sdk.getDataProvider().getCategories();
		View headView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_category, null);
		headView.setBackgroundColor(getResources().getColor(R.color.menu_category_bg));
		headView.findViewById(R.id.view_menu_category_left_bg).setVisibility(View.VISIBLE);
		TextView tvName = (TextView) headView.findViewById(R.id.tv_category_name);
		tvName.setText("首页");
		lvCategory.addHeaderView(headView);
		
		mCategoryAdapter = new MenuCategoryAdapter(mContext, datalist);
		//mCategoryAdapter.setOnItemClickListener(this.getSlidingMenu(), lvCategory, getBlogListView());
		lvCategory.setAdapter(mCategoryAdapter);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				this.mRawX = event.getRawX();
				break;
			
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				break;
			
			default:
				break;
		}
		
		return super.onTouchEvent(event);
	}
}
