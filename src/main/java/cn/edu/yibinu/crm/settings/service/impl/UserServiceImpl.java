package cn.edu.yibinu.crm.settings.service.impl;

import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.mapper.UserMapper;
import cn.edu.yibinu.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    //注入mapper对象
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public int addUser(User record) {
        return userMapper.insert(record);
    }




}
