package com.aidingyun.ynlive.mvp.model.api;



import com.aidingyun.ynlive.mvp.model.api.executor.Executor;
import com.aidingyun.ynlive.mvp.model.api.executor.MainThread;

import javax.inject.Inject;

/**
 * Domain层中抽象UseCase
 * 实现UseCase的基本操作，生命周期控制
 * 举个例子：当Activity被销毁的时候，我们应该取消掉这个任务 volatile的目的是为了多线程都可以调用
 *
 * @author wujiajun
 */
public abstract class AbstractUseCase implements UseCase {

    @Inject
    protected Executor mThreadExecutor;
    @Inject
    protected MainThread mMainThread;

    protected volatile boolean mIsCanceled;
    protected volatile boolean mIsRunning;

    protected Callback mCallback;

    /**
     * 业务逻辑实现方法
     * 别主动调用他，Executor中自动执行调用
     *
     * @see Executor
     */
    public abstract void run();

    /**
     * 取消当前任务
     */
    public void cancel() {
        mIsCanceled = true;
        mIsRunning = false;
    }

    /**
     * 检测任务是否正在进行中
     *
     * @return true进行中 false处理完成/还没有处理/被取消掉
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 完成操作
     */
    public void onFinished() {
        mIsRunning = false;
        mIsCanceled = false;
    }

    /**
     * 执行任务
     */
    public void execute() {
        this.mIsRunning = true;
        mThreadExecutor.execute(this);
    }

    /**
     * 设置任务回调接口
     *
     * @param callback
     */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * 获取任务回调接口
     *
     * @return
     */
    public Callback getCallback() {
        return mCallback;
    }


}
