package cn.edu.yibinu.crm.settings.web.interceptor;

import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //实现验证登录逻辑

        //通过判断session里面是否有user属性来判断是否登录过了
        User user = (User)httpServletRequest.getSession().getAttribute(Contents.SESSION_USER);
        //System.out.println("观察找到的user属性：" + user);

        if(user == null){
            //说明没有用户，就没有进行登录，需要跳转到登录页面，这里使用重定向
            httpServletResponse.sendRedirect("/crm");
            return false;   //不放行
        }
        return true;    //否则就放行

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //这个方法执行的时机是在处理方法完成之后执行
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //这个方法是在请求响应完成之后执行
    }
}
