package com.bdeesorn_r.demo_crud.repository.custom.user;

import java.util.List;

import com.bdeesorn_r.demo_crud.dao.UserDao;

public interface UserRepositoryCustom {
    public List<UserDao> findByUsernameAndStatus(String username, String status);
}
