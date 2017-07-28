package com.rae.cnblogs.service.job;

/**
 * JOB event
 * Created by ChenRui on 2017/7/28 0028 18:33.
 */
public class JobEvent {
    private int action;


    public JobEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
