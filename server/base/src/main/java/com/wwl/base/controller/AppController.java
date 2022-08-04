package com.wwl.base.controller;

import com.wwl.base.config.AppConfig;
import com.wwl.core.base.Result;
import com.wwl.core.model.AppModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wwl
 * @date 2022/8/1 10:59
 * @desc TODO
 */
@RestController
@RequestMapping(path = "/app")
public class AppController {

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/info")
    public Result<AppModel> appInfo() {
        return Result.success(new AppModel(appConfig.getProjectName(), appConfig.getProjectVersion()));
    }


}
