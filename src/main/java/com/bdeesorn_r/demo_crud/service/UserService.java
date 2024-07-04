package com.bdeesorn_r.demo_crud.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bdeesorn_r.demo_crud.constant.Status;
import com.bdeesorn_r.demo_crud.dao.MapDao;
import com.bdeesorn_r.demo_crud.dao.UserDao;
import com.bdeesorn_r.demo_crud.dto.UserDto;
import com.bdeesorn_r.demo_crud.repository.MapRepository;
import com.bdeesorn_r.demo_crud.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private MapRepository mapRepository;

    public UserService(UserRepository userRepository, MapRepository mapRepository) {
        this.userRepository = userRepository;
        this.mapRepository = mapRepository;
    }

    public UserDto getUser(String username) {
        List<UserDao> userList = userRepository.findByUsernameAndStatus(username, Status.ACTIVE.value);

        if (CollectionUtils.isEmpty(userList) || userList.size() > 1) {
            return null;
        }

        UserDao userDao = userList.get(0);
        UserDto userDto = mapUserDaoToUserDto(userDao);

        if (!CollectionUtils.isEmpty(userDao.getMapList())) {
            userDto.setMapList(userDao.getMapList().stream().map(MapService::mapMapDaoToMapDto).toList());
        }

        return userDto;
    }

    public UserDto createUser(UserDto userDto) {
        UserDao user = new UserDao();

        List<UserDao> existingUser = userRepository.findByUsernameAndStatus(userDto.getUsername(), Status.ACTIVE.value);

        if (!existingUser.isEmpty()) {
            return null;
        }

        user.setUsername(userDto.getUsername());
        user.setDescription(userDto.getDescription());
        user.setStatus(userDto.getStatus());
        user.setMapList(null);

        user.setCreatedBy("ADMIN");
        user.setCreatedDate(new Date());

        user = userRepository.save(user);

        return mapUserDaoToUserDto(user);
    }

    public UserDto updateUser(UserDto userDto) {
        Optional<UserDao> userOpt = userRepository.findById(userDto.getId());

        if (!userOpt.isPresent()) {
            return null;
        }

        UserDao user = userOpt.get();
        user.setUsername(userDto.getUsername());
        user.setDescription(userDto.getDescription());
        user.setStatus(userDto.getStatus());
        user.setMapList(null);

        user.setUpdatedBy("ADMIN");
        user.setUpdatedDate(new Date());

        user = userRepository.save(user);

        return mapUserDaoToUserDto(user);
    }

    public void deleteUser(Long id) {
        Optional<UserDao> userOpt = userRepository.findById(id);

        if (!userOpt.isPresent()) {
            return;
        }

        UserDao user = userOpt.get();
        user.setStatus(Status.SUSPENDED.value);
        user.setMapList(null);
        user.setUpdatedBy("ADMIN");
        user.setUpdatedDate(new Date());

        userRepository.save(user);

        List<MapDao> mapList = mapRepository.findAllByFkUserIdAndStatus(user.getId(), Status.ACTIVE.value);
        mapList.stream().forEach(dao -> {
            dao.setStatus(Status.SUSPENDED.value);
            dao.setUser(null);
            
            dao.setUpdatedBy("ADMIN");
            dao.setUpdatedDate(new Date());
        });

        mapRepository.saveAll(mapList);
    }

    public static UserDto mapUserDaoToUserDto(UserDao userDao) {
        UserDto userDto = new UserDto();

        userDto.setId(userDao.getId());
        userDto.setUsername(userDao.getUsername());
        userDto.setDescription(userDao.getDescription());
        userDto.setStatus(userDao.getStatus());
        
        userDto.setCreatedBy(userDao.getCreatedBy());
        userDto.setCreatedDate(userDao.getCreatedDate());
        userDto.setUpdatedBy(userDao.getUpdatedBy());
        userDto.setUpdatedDate(userDao.getUpdatedDate());

        return userDto;
    }
}
