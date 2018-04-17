package com.zyc.service;

import com.zyc.model.Example.PaperExample;
import com.zyc.model.Paper;
import com.zyc.util.Page;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by YuChen Zhang on 18/03/12.
 */
@FeignClient("QUESTION-SERVICE")
@RequestMapping("/paper")
public interface PaperService {
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    String createPaper(@RequestBody Map<String, Object> paperConfig);

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    String deletePaper(@RequestBody PaperExample paperExample);
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    String deletePaper(@PathVariable("id") Integer id);

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    String queryPaper(@RequestBody Page result);
    @RequestMapping(value = "/queryPaper/{id}",method = RequestMethod.GET)
    String queryPaper(@PathVariable("id") Integer id);
}
