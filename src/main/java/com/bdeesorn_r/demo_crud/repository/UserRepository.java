package com.bdeesorn_r.demo_crud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bdeesorn_r.demo_crud.dao.UserDao;
import com.bdeesorn_r.demo_crud.repository.custom.user.UserRepositoryCustom;

@Repository
public interface UserRepository extends CrudRepository<UserDao, Long>, UserRepositoryCustom {
}
