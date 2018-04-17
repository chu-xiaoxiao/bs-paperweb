package com.zyc.service.fallback;

import com.zyc.model.Example.PaperconfigExample;
import com.zyc.model.Paperconfig;
import com.zyc.service.PaperConfigService;
import com.zyc.util.Page;

public class PaperConfigServiceFallback implements PaperConfigService {

    @Override
    public String save(Paperconfig paperconfig) {
        return null;
    }

    @Override
    public String query(Page<Paperconfig, PaperconfigExample> page) {
        return null;
    }
}
