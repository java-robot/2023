package org.nju.demo.service;

import org.nju.demo.entity.AUser;
public interface UserService {

    boolean tryLogin(AUser user);
    boolean isExist(String username);

    int addUser(AUser user);

    AUser getUserByUsername(String username);

    int updateUser(AUser user);

}
