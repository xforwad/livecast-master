package com.aidingyun.ynlive.mvp.model.api;


/**
 * Domain层中UseCase抽象接口
 *
 * @author wujiajun
 */
public interface UseCase {

    /**
     * UserCase执行方法
     * 可在任何线程执行
     */
    void execute();

    /**
     * 任务回调通用接口
     */
    interface Callback<T> {
        void success(T t);

        void fail();
    }
}
