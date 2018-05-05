package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.CategoriesFragment;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

/**
 * 分类编辑
 * Created by ChenRui on 2017/7/19 0019 10:10.
 */
@Route(path = AppRoute.PATH_CATEGORY)
public class CategoryActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(com.rae.cnblogs.R.anim.slide_in_bottom, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        showHomeAsUp();
        List<CategoryBean> data = getIntent().getParcelableArrayListExtra("data");
        getSupportFragmentManager().beginTransaction().add(R.id.content, CategoriesFragment.newInstance(data)).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, com.rae.cnblogs.R.anim.slide_out_bottom);
    }
}
