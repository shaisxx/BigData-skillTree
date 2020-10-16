package com.hks.hbase.controller;

import com.hks.hbase.pojo.UserDeviceTable;
import com.hks.hbase.service.IHBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hekuangsheng
 * @Date: 2018/11/30
 */
@RestController
@RequestMapping("/hbase")
@Api(tags = "大数据-【HBase】相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HbaseDemoController {

    @Autowired
    IHBaseService ihBaseService;

    @RequestMapping(value = "create",method = RequestMethod.GET)
    @ApiOperation(value = "创建Table")
    public String create(){
        UserDeviceTable table = new UserDeviceTable();
        ihBaseService.createTable(table);
        return "success";
    }
}
