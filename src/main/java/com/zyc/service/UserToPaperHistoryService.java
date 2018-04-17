package com.zyc.service;

import com.zyc.model.Example.UsertopaperhistoryExample;
import com.zyc.model.Usertopaperhistory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by YuChen Zhang on 18/03/13.
 */
@FeignClient("QUESTION-SERVICE")
@RequestMapping("userToPaperHistory")
public interface UserToPaperHistoryService {
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    String query(@RequestBody UsertopaperhistoryExample usertopaperhistoryExample);

    @RequestMapping(value = "insert",method = RequestMethod.POST)
    String insert(@RequestBody Usertopaperhistory usertopaperhistory);
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    String delete(UsertopaperhistoryExample usertopaperhistoryExample);
    @RequestMapping(value = "update",method = RequestMethod.POST)
    String update(@RequestBody Map<String, Object> map) throws Exception ;

}