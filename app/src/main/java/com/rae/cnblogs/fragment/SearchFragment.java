package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索
 * Created by ChenRui on 2017/8/28 0028 14:51.
 */
public class SearchFragment extends BaseActivity {
    @BindView(R.id.et_search_text)
    EditText mSearchView;
    @BindView(R.id.img_edit_delete)
    ImageView mDeleteView;

    @BindView(R.id.btn_search)
    TextView mSearchButton;

    //    @Override
    protected int getLayoutId() {
        return R.layout.fm_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = mSearchView.length();
                mDeleteView.setVisibility(length > 0 ? View.VISIBLE : View.GONE);
                mSearchButton.setSelected(length > 0);
                if (length > 0) {
                    mSearchButton.setText(R.string.search);
                } else {
                    mSearchButton.setText(R.string.cancel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @OnClick(R.id.rl_edit_delete)
    public void onEditDeleteClick() {
        mSearchView.setText("");
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick() {
        if (mSearchButton.isSelected()) {
            // 执行搜索
        } else {
            // 退出
            finish();
        }
    }
}
