package cn.edu.yibinu.crm.settings.web.controller;

import cn.edu.yibinu.crm.commons.domain.ReturnObject;
import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 使用controller来访问login.jsp
     * @return 登录页面
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String loginPage(){
        return "settings/qx/user/login";
    }

    /**
     * 使用controller来访问register.jsp
     * @return 注册页面
     */
    @RequestMapping("/settings/qx/user/registerPage.do")
    public String registerPage(){
        return "settings/qx/user/register";
    }

    @RequestMapping("/settings/toSettingsIndex.do")
    public String toSettingsIndex(){
        return "settings/index";
    }

    @RequestMapping("/dictionary/toDictionaryIndex.do")
    public String toDictionaryIndex(){
        return "settings/dictionary/index";
    }

    @RequestMapping("/settings/type/index.do")
    public String toDictionaryTypeIndex(){
        return "settings/dictionary/type/index";
    }

    @RequestMapping("/settings/value/index.do")
    public String toDictionaryValueIndex(){
        return "settings/dictionary/value/index";
    }

    @RequestMapping("/workbench/settings/qx/user/toRegisterIndex.do")
    public String toRegisterIndex(){
        return "settings/qx/user/index";
    }
    /**
     *
     * @param loginAct  登录账号
     * @param loginPwd  登录密码
     * @param isRemember    是否记住密码
     * @return json格式的数据
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(@RequestParam String loginAct,
                        @RequestParam String loginPwd,
                        @RequestParam String isRemember, HttpServletRequest request, HttpSession session, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //sql语句是不需要这个参数的，所以就不需要封装到map中
        //map.put("isRemember",isRemember);

        ReturnObject returnObject = new ReturnObject();
        User user = userService.queryUserByLoginActAndPwd(map);
        if(user == null){
            //账户或者密码错误
            returnObject.setCode(Contents.RETURN_CODE_FAIL);
            returnObject.setMessage("账户或者密码错误");
        }else{
            //判断其他登录条件
            //获取当前时间
            String nowStr = DateFormatUtils.dateTimeFormat(new Date());
            if(user.getExpireTime().compareTo(nowStr) < 0){
                //登录失败，账号已过期
                returnObject.setCode(Contents.RETURN_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())){
                //登录失败，账号被锁定
                returnObject.setCode(Contents.RETURN_CODE_FAIL);
                returnObject.setMessage("账号被锁定");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //登录失败，ip地址受限
                returnObject.setCode(Contents.RETURN_CODE_FAIL);
                returnObject.setMessage("ip地址受限");
            }else{
                //登录成功
                returnObject.setCode(Contents.RETURN_CODE_SUCCESS);
                session.setAttribute(Contents.SESSION_USER,user);

                if("true".equals(isRemember)){
                    //需要写cookie来实现10天免登录
                    Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(60*60*24*10);  //10天
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(60*60*24*10);
                    response.addCookie(c2);
                }else{
                    //没有选择10天面登录，需要删除cookie
                    //由于没有删除cookie的方法，就需要生成一样的cookie，并将生存时间设置为0
                    Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookies,value可以随便取值，因为cookie马上就会消失
        Cookie c1 = new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);

        //删除session
        session.removeAttribute("user");

        //跳转到首页（重定向）
        return "redirect:/";
    }

    /**
     * 注册业务
     * @param user
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping("/settings/qx/user/register.do")
    @ResponseBody
    public Object register(User user,  HttpServletRequest request, HttpSession session, HttpServletResponse response){
        ReturnObject returnObject = new ReturnObject();
        //User user = userService.queryUserByLoginActAndPwd(map);
        user.setId(UUIDUtils.getUUID());
        //user.setCreatetime(DateFormatUtils.dateTimeFormat(new Date()));
        int ret = userService.addUser(user);

        if (ret > 0 ){
            returnObject.setCode(Contents.RETURN_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contents.RETURN_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
        }
        return returnObject;
    }


}
