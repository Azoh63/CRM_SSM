package cn.edu.yibinu.crm.settings.service;

import cn.edu.yibinu.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);
    List<User> queryAllUsers();
    int addUser(User record);

}
