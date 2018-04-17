package com.zyc.service;

import com.zyc.model.Example.PaperconfigExample;
import com.zyc.model.Paperconfig;
import com.zyc.service.fallback.PaperConfigServiceFallback;
import com.zyc.util.Page;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by YuChen Zhang on 18/03/12.
 */
@FeignClient(value = "QUESTION-SERVICE",fallback = PaperConfigServiceFallback.class)
@RequestMapping("/paperConfig")
public interface PaperConfigService {
    @RequestMapping("save")
    String save(@RequestBody Paperconfig paperconfig);

    @RequestMapping(value = "queryPage",method = RequestMethod.POST)
    String query(Page<Paperconfig, PaperconfigExample> page);

}
