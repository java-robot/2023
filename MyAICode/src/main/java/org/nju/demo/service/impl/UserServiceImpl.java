package org.nju.demo.service.impl;
import org.nju.demo.dao.AUserMapper;
import org.nju.demo.entity.AUserExample;
import org.nju.demo.entity.AUser;
import org.nju.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private AUserMapper userMapper;

    @Autowired
    private HttpSession session;

    @Override
    public boolean tryLogin(AUser user) {
        AUserExample userExample = new AUserExample();
        AUserExample.Criteria criteria = userExample.createCriteria();

        criteria.andUsernameEqualTo(user.getUsername());
        criteria.andPasswordEqualTo(user.getPassword());

        List<AUser> result = userMapper.selectByExample(userExample);
        if (result.size() == 0)
            return false;
        return true;
    }

    @Override
    public boolean isExist(String username) {
        AUserExample userExample = new AUserExample();
        AUserExample.Criteria criteria = userExample.createCriteria();

        criteria.andUsernameEqualTo(username);

        List<AUser> userList = userMapper.selectByExample(userExample);
        if (userList.size() == 0)
            return false;
        return true;
    }

    @Override
    public int addUser(AUser user) {
        return userMapper.insert(user);
    }

    @Override
    public AUser getUserByUsername(String username) {
        AUserExample userExample = new AUserExample();
        AUserExample.Criteria criteria = userExample.createCriteria();

        criteria.andUsernameEqualTo(username);

        List<AUser> userList = userMapper.selectByExample(userExample);
        return userList.get(0);
    }

    @Override
    public int updateUser(AUser user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
