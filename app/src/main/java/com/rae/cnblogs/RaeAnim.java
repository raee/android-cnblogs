package com.rae.cnblogs;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by ChenRui on 2017/1/19 0019 14:02.
 */
public final class RaeAnim {
    public static void fadeIn(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in));
    }

    public static void fadeIn(View view, long duration) {
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
        anim.setDuration(duration);
        anim.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(anim);
    }

    public static void fadeOut(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_out));
    }

    public static void fadeOut(View view, long duration) {
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_out);
        anim.setDuration(duration);
        anim.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(anim);
    }

    public static void scaleIn(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in));
    }
}
