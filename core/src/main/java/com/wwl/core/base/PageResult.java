package com.wwl.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wwl
 * @date 2022/7/27 14:07
 * @desc 分页结果
 */
public class PageResult<T extends Serializable> implements Serializable {

    public PageResult() {
    }
    public PageResult(List<T> pageData) {
        this.code = Result.SUCCESSFUL_CODE;
        this.pageData = pageData;
    }

    public PageResult(List<T> pageData,int totalCount) {
        this.code = Result.SUCCESSFUL_CODE;
        this.totalCount = totalCount;
        this.pageData = pageData;
    }

    public PageResult(List<T> pageData,PageHelper pageHelper) {
        this.code = Result.SUCCESSFUL_CODE;

        this.pageIndex=pageHelper.getPageIndex();
        this.pageSize=pageHelper.getPageSize();
        this.totalCount=pageHelper.getTotalCount();
        this.pageData = pageData;
    }

    public PageResult(String code,String error) {
        this.code = code;
        this.error=error;
    }


    public static <R extends Serializable> PageResult<R> of(List<R> pageData,PageHelper pageHelper){
        return new PageResult<>(pageData, pageHelper);
    }

    public static <R extends Serializable> PageResult<R> of(List<R> pageData,int totalCount){
        return new PageResult<>(pageData, totalCount);
    }

    public static <R extends Serializable> PageResult<R> of(List<R> pageData){
        return new PageResult<>(pageData);
    }

    public static <R extends Serializable> PageResult<R> empty(){
        return new PageResult<>(new ArrayList<>());
    }

    /**
     * 操作失败
     * @param code 错误类型
     * @param error 错误类型
     * @return Result
     */
    public static  <R extends Serializable> PageResult<R>  fail(String code,String error) {
        if(Objects.equals(code, error)){
            code = code+"0";
        }
        return new PageResult<>(code,error);
    }

    /**
     * 操作失败
     * @param error 错误类型
     * @return Result
     */
    public static  <R extends Serializable> PageResult<R> fail(String error) {
        return new PageResult<>(Result.FAIL_CODE,error);
    }


    /**
     * 操作出错
     * @param errorType 错误类型
     * @return 错误结果
     */
    public static  <R extends Serializable> PageResult<R> fail(ErrorType errorType) {
        return fail(errorType.getCode(),errorType.getMessage());
    }


    /**
     * 指定错误信息的 错误类型 返回结果
     * @param errorType 错误类型
     * @param error 错误消息
     * @return 错误结果
     */
    public static  <R extends Serializable> PageResult<R> fail(ErrorType errorType,String error) {
        return fail(errorType.getCode(),error);
    }



    private String code;

    private String error;

    private List<T> pageData = new ArrayList<>();


    private int pageIndex=0;
    private int pageSize=0;
    private int totalCount=0;

    public PageResult<T> withTotalCount(int totalCount){
        this.totalCount = totalCount;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    /**
     * 成功code=000000
     * @return true/false
     */
    public boolean isSuccess() {
        return Result.SUCCESSFUL_CODE.equals(this.code);
    }
}
