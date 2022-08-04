package com.wwl.desktop.service;

import com.alibaba.fastjson.JSON;
import com.wwl.core.base.Result;
import com.wwl.core.model.AppModel;
import com.wwl.desktop.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author wwl
 * @date 2022/8/1 11:09
 * @desc 应用服务
 */
@Service
public class BaseService {

    @Autowired
    private RequestUtils requestUtils;

    private final static String BASE_SERVICE_PREFIX_URL = "http://127.0.0.1:6041";


    public Result<AppModel> appInfo() {

        Result result = this.requestUtils.doGetData(BASE_SERVICE_PREFIX_URL + "/app/info", new HashMap<>(0));
        return result;
    }


}
