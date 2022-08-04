package com.wwl.core.base;

/**
 * @author wwl
 * @date 2022/7/27 14:11
 * @desc 消费指定类型方法参数
 */
@FunctionalInterface
public interface ConsumerTwo<T1,T2> {
    /**
     * 调用消费的方法
     *
     * @param t1 指定参数类型的参数1
     * @param t2 指定参数类型的参数2
     */
    void invoke(T1 t1, T2 t2);

    @FunctionalInterface
    interface WithReturn<T1, T2, R> {

        /**
         * 调用消费的方法
         *
         * @param t1 指定参数类型的参数1
         * @param t2 指定参数类型的参数2
         * @return 指定类型数据返回
         */
        R invoke(T1 t1, T2 t2);
    }
}
