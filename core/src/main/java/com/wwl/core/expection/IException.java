package com.wwl.core.expection;

/**
 * @author wwl
 * @date 2022/7/27 14:20
 * @desc 异常接口定义
 */
public interface IException {

    /**
     * 获取异常信息
     * @return 异常信息描述
     */
    String getExceptionMessage();


    /**
     * 获取异常代码
     * @return 异常代码
     */
    int getExceptionCode();

}
