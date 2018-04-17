package com.zyc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by YuChen Zhang on 18/03/12.
 */
@Controller
@RequestMapping("page")
public class PaperPageController {
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String index(){
        return "paper/page/index";
    }

    @RequestMapping("preview/{id}")
    public ModelAndView preview(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id",id);
        modelAndView.setViewName("paper/page/preview");
        return modelAndView;
    }

    @RequestMapping("paperList")
    public ModelAndView paperList(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("paper/page/list");
        return modelAndView;
    }
}
