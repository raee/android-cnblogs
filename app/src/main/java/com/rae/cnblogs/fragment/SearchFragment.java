package com.rae.cnblogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.SearchSuggestionAdapter;
import com.rae.cnblogs.message.SearchEvent;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ISearchContract;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.swift.app.RaeFragmentAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索
 * Created by ChenRui on 2017/8/28 0028 14:51.
 */
public class SearchFragment extends BaseFragment implements ISearchContract.View, TabLayout.OnTabSelectedListener {

    private SearchBlogFragment mSearchBlogFragment;
    private SearchBloggerFragment mSearchBloggerFragment;
    private SearchBlogFragment mSearchNewsFragment;
    private SearchBlogFragment mSearchKbFragment;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @BindView(R.id.et_search_text)
    EditText mSearchView;
    @BindView(R.id.img_edit_delete)
    ImageView mDeleteView;

    @BindView(R.id.btn_search)
    TextView mSearchButton;

    @BindView(R.id.rec_search)
    RecyclerView mRecyclerView;

    @BindView(R.id.tab_category)
    TabLayout mTabLayout;

    @BindView(R.id.vp_search)
    ViewPager mViewPager;

    SearchSuggestionAdapter mSuggestionAdapter;

    private ISearchContract.Presenter mPresenter;
    private TextWatcher mSearchTextWatcher;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_search;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = CnblogsPresenterFactory.getSearchPresenter(getContext(), this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        if (mTabLayout != null) {
            mTabLayout.removeOnTabSelectedListener(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSuggestionAdapter = new SearchSuggestionAdapter();
        mRecyclerView.setAdapter(mSuggestionAdapter);

        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getChildFragmentManager());
        mSearchBlogFragment = SearchBlogFragment.newInstance(BlogType.BLOG);
        mSearchBloggerFragment = SearchBloggerFragment.newInstance();
        mSearchNewsFragment = SearchBlogFragment.newInstance(BlogType.NEWS);
        mSearchKbFragment = SearchBlogFragment.newInstance(BlogType.KB);

        adapter.add("博客", mSearchBlogFragment);
        adapter.add("博主", mSearchBloggerFragment);
        adapter.add("新闻", mSearchNewsFragment);
        adapter.add("知识库", mSearchKbFragment);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.addOnTabSelectedListener(this);

        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    preformSearch();
                    return true;
                }
                return false;
            }
        });

        // 搜索建议上档点击
        mSuggestionAdapter.setSelectedClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (String) v.getTag();
                if (text != null) {
                    mSearchView.setText(text);
                    mSearchView.setSelection(text.length());
                }
            }
        });

        mSuggestionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // fix bug #506
                if (mRecyclerView != null && mSuggestionAdapter.getItemCount() > 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        mSuggestionAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<String>() {
            @Override
            public void onItemClick(String item) {
                // 执行搜索
                mSearchView.removeTextChangedListener(mSearchTextWatcher);
                mSearchView.setText(item);
                mSearchView.setSelection(item.length());
                mSearchView.addTextChangedListener(mSearchTextWatcher);
                preformSearch();
            }

        });

        mSearchTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = mSearchView.length();
                mDeleteView.setVisibility(length > 0 ? View.VISIBLE : View.INVISIBLE);
                mSearchButton.setSelected(length > 0);
                if (length > 0) {
                    mSearchButton.setText(R.string.search);
                } else {
                    mSearchButton.setText(R.string.cancel);
                }

                // 自动完成
                mPresenter.suggest();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mSearchView.addTextChangedListener(mSearchTextWatcher);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity().getIntent() != null) {
            int position = getActivity().getIntent().getIntExtra("position", 0);
            if (position > 0 && mViewPager != null) {
                mViewPager.setCurrentItem(position);
            }
        }
    }

    @OnClick(R.id.rl_edit_delete)
    public void onEditDeleteClick() {
        mSearchView.setText("");
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick() {
        if (mSearchButton.isSelected()) {
            // 执行搜索
            preformSearch();
        } else {
            // 退出
            getActivity().finish();
        }
    }


    /**
     * 执行搜索
     */
    private void preformSearch() {
        // 弹下键盘
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        }

        // 取消/清除搜索建议数据
        mSuggestionAdapter.clear();
        mSuggestionAdapter.notifyDataSetChanged();

        // 显示或者隐藏搜索建议
        mRecyclerView.setVisibility(mSearchView.length() > 0 ? View.GONE : View.VISIBLE);

        EventBus.getDefault().post(new SearchEvent(getSearchText()));
    }

    @Override
    public String getSearchText() {
        return mSearchView.getText().toString();
    }

    @Override
    public void onSuggestionSuccess(List<String> data) {
        mSuggestionAdapter.invalidate(data);
        mSuggestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // 返回顶部
        int position = mViewPager.getCurrentItem();
        switch (position) {
            case 0: // 博客
                mSearchBlogFragment.scrollToTop();
                break;
            case 1: // 博主
                mSearchBloggerFragment.scrollToTop();
                break;
            case 2: // 新闻
                mSearchNewsFragment.scrollToTop();
                break;
            case 3: // 知识库
                mSearchKbFragment.scrollToTop();
                break;
        }
    }
}
