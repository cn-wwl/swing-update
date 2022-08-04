package com.wwl.core.base;

/**
 * @author wwl
 * @date 2022/7/27 14:10
 * @desc 消费指定类型方法参数
 */
@FunctionalInterface
public interface Consumer<T> {
    /**
     * 调用消费的方法
     * @param t 指定参数类型的参数
     */
    void invoke(T t);

    @FunctionalInterface
    interface WithReturn<T,R>{

        /**
         * 调用消费的方法
         * @param t 指定参数类型的参数
         * @return 指定类型数据返回
         */
        R invoke(T t);
    }
}
