package com.rae.cnblogs;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * view ext compat
 * Created by ChenRui on 2017/10/23 0023 21:43.
 */
public class RaeViewCompat {


    /**
     * 滚动到顶部
     */
    public static void scrollToTop(RecyclerView recyclerView) {
        if (recyclerView == null) return;


        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        int visibleCount = lastItem - firstItem;

        // 已经处于顶部
        if (firstItem <= 1) {
            return;
        }

        // 超过一屏
        if (lastItem > visibleCount) {
            layoutManager.scrollToPosition(visibleCount + 3);
        }

        recyclerView.smoothScrollToPosition(0);
    }
}
