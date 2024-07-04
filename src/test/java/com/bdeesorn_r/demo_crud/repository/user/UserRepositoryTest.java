package com.bdeesorn_r.demo_crud.repository.user;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bdeesorn_r.demo_crud.constant.Status;
import com.bdeesorn_r.demo_crud.dao.UserDao;
import com.bdeesorn_r.demo_crud.repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void test_findByUsernameAndStatus_returnData() {
        UserDao userDao = new UserDao();

        userDao.setUsername("test_username");
        userDao.setDescription("test_description");
        userDao.setStatus(Status.ACTIVE.value);
        userDao.setCreatedBy("test_user_1");
        userDao.setCreatedDate(new Date());

        userRepository.save(userDao);

        List<UserDao> userDaoList = userRepository.findByUsernameAndStatus("test_username", Status.ACTIVE.value);

        Assertions.assertThat(userDaoList.size()).isEqualTo(1);

        UserDao retrievedUserDao = userDaoList.get(0);

        Assertions.assertThat(retrievedUserDao).usingRecursiveComparison().ignoringFields("id").isEqualTo(userDao);
        Assertions.assertThat(retrievedUserDao.getId()).isNotNull();
    }

    @Test
    void test_findByUsernameAndStatus_returnEmpty() {
        List<UserDao> userDaoList = userRepository.findByUsernameAndStatus("test_username", null);

        Assertions.assertThat(userDaoList).isEmpty();
    }
}
