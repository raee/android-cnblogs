package com.rae.cnblogs.sdk.service.task;

/**
 * 服务的任务接口
 * Created by ChenRui on 2017/2/1 22:01.
 */
public interface IServiceTask {

    /**
     * 任务名称，方便查看日志
     */
    String getTaskName();

    /**
     * 开始任务
     */
    void startTask();

    /**
     * 停止任务
     */
    void stopTask();

    /**
     * 任务是否已经完成
     */
    boolean isFinish();


    void setOnTaskFinishListener(OnTaskFinishListener listener);
}
