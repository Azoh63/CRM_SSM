package cn.edu.yibinu.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {
    /**
     * 处理对首页的请求
     */
    @RequestMapping(value = {"/","/index"})
    public String indexPage(){
        //这个controller是用来解决不能直接访问web-inf下面资源的问题
        return "index";
    }
}
