package com.rae.cnblogs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.swift.Rx;

import java.util.List;

/**
 * ITEM 适配器
 * Created by ChenRui on 2016/12/15 22:50.
 */
public abstract class BaseItemAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /* 正常类型 */
    static final int VIEW_TYPE_NORMAL = 0;
    /*新闻类型*/
    static final int VIEW_TYPE_NEWS = 1;
    /* 正在加载中 */
    static final int VIEW_TYPE_LOADING = 2;
    /* 内容为空的时候显示 */
    static final int VIEW_TYPE_EMPTY = 3;


    public interface onItemClickListener<T> {
        void onItemClick(T item);
    }

    protected List<T> mDataList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private onItemClickListener<T> mOnItemClickListener;


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }


        return onCreateViewHolder(mLayoutInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final T dataItem = getDataItem(position);
        onBindViewHolder(holder, position, dataItem);
        if (mOnItemClickListener != null && dataItem != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(dataItem);
                }
            });
        }
    }


    T getDataItem(int position) {
        return Rx.isEmpty(mDataList) ? null : mDataList.get(position % getItemCount());
    }

    @Override
    public int getItemCount() {
        return Rx.getCount(mDataList);
    }


    public abstract VH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position, T m);


    /**
     * 通知数据集合发生改变
     *
     * @param data 数据
     */
    public void invalidate(List<T> data) {
        if (mDataList != null && mDataList != data) {
            mDataList.clear();
            mDataList = null;
        }
        mDataList = data;
    }


    /**
     * 初始化布局文件
     *
     * @param parent   可以为空
     * @param layoutId 布局ID
     * @return 视图
     */
    View inflateView(ViewGroup parent, int layoutId) {
        return mLayoutInflater.inflate(layoutId, parent, false);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 移除数据实体
     *
     * @param item 实体
     */
    public void remove(T item) {
        mDataList.remove(item);
    }

    public void clear() {
        if (mDataList != null)
            mDataList.clear();
    }

    /**
     * 获取数据实体所在的索引
     *
     * @param item 实体
     * @return 索引
     */
    public int getItemPosition(T item) {
        return mDataList.indexOf(item);
    }

    /**
     * 设置ITEM的点击事件
     */
    public void setOnItemClickListener(onItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public void destroy() {
        mDataList.clear();
        mLayoutInflater = null;
        mContext = null;
    }
}
