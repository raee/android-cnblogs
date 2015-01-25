package com.rae.cnblogs.view;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rae.cnblogs.BaseSlideActivity;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.MenuCategoryAdapter;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.model.Category;

/**
 * 菜单视图
 * 
 * @author ChenRui
 * 
 */
public class SlideMenuView extends LinearLayout implements OnItemClickListener {

	private Context mContext;
	private CnBlogsOpenAPI sdk;

	private MenuCategoryAdapter mCategoryAdapter;
//	private float mRawX;
	private List<Category> datalist;
	private BlogListView mBlogListView;
	private ListView lvCategory;

	public SlideMenuView(Context context, BlogListView blogListView) {
		super(context);
		this.mContext = context;
		sdk = CnBlogsOpenAPI.getInstance(context);
		initView();
		this.mBlogListView = blogListView;
	}

	public void initView() {
		View menuView = LayoutInflater.from(mContext).inflate(
				R.layout.view_slide_menu, this);
		lvCategory = (ListView) menuView.findViewById(android.R.id.list);
		datalist = sdk.getDataProvider().getCategories();

		// // 首页
		// View headView = LayoutInflater.from(mContext).inflate(
		// R.layout.item_menu_category, null);
		// TextView tvName = (TextView) headView
		// .findViewById(R.id.tv_category_name);
		// tvName.setText("首页");
		// setCurrentItemView(headView, 0);
		// lvCategory.addHeaderView(headView); // 添加首页

		mCategoryAdapter = new MenuCategoryAdapter(mContext, datalist);
		mCategoryAdapter.setListView(lvCategory);
		lvCategory.setAdapter(mCategoryAdapter);
		lvCategory.setOnItemClickListener(this);
	}

	// private void setCurrentItemView(View view, int postion) {
	//
	// int len = lvCategory.getChildCount();
	// view.setBackgroundColor(getResources().getColor(
	// R.color.menu_category_bg));
	// view.findViewById(R.id.view_menu_category_left_bg).setVisibility(
	// View.VISIBLE);
	//
	// for (int i = 0; i < len; i++) {
	// if (i == postion) {
	// continue;
	// }
	//
	// View v = lvCategory.getChildAt(i);
	// v.setBackgroundColor(getResources().getColor(android.R.color.white));
	// v.findViewById(R.id.view_menu_category_left_bg).setVisibility(
	// View.GONE);
	// }
	//
	// }

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		lvCategory.setSelection(position);
		Category model = datalist.get(position);
		String cateId = model.getId();
		if (!TextUtils.isEmpty(cateId)) {
			cateId = "cate/" + cateId;
		}
		// if (position > 0) {
		// model = datalist.get(position - 1);
		// cateId = "cate/" + model.getId();
		// } else {
		// model = new Category();
		// model.setId("");
		// model.setName("博客园首页");
		// }
		BaseSlideActivity at = (BaseSlideActivity) mContext;
		at.toggleMenu();
		// setCurrentItemView(view, position);
		mBlogListView.load(cateId); // 获取数据，刷新ListView
		at.setTitle(model.getName());
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int action = event.getAction();
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//			this.mRawX = event.getRawX();
//			break;
//
//		case MotionEvent.ACTION_MOVE:
//			break;
//		case MotionEvent.ACTION_UP:
//			float span = event.getRawX() - mRawX;
//			if (span > 50) {
//				BaseSlideActivity at = (BaseSlideActivity) getContext();
//				at.toggleMenu();
//			}
//
//			break;
//
//		default:
//			break;
//		}
//
//		return super.onTouchEvent(event);
//	}
}
