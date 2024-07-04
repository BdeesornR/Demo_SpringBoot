package com.bdeesorn_r.demo_crud.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bdeesorn_r.demo_crud.constant.Status;
import com.bdeesorn_r.demo_crud.dao.MapDao;
import com.bdeesorn_r.demo_crud.dao.UserDao;
import com.bdeesorn_r.demo_crud.dto.UserDto;
import com.bdeesorn_r.demo_crud.repository.MapRepository;
import com.bdeesorn_r.demo_crud.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private MapRepository mapRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void test_getUser_returnData() {
        Date createdDate = new Date();

        UserDao userDao = new UserDao();
        userDao.setId(1L);
        userDao.setUsername("test_user");
        userDao.setDescription("test_description");
        userDao.setStatus(Status.ACTIVE.value);
        userDao.setMapList(null);
        userDao.setCreatedBy("ADMIN");
        userDao.setCreatedDate(createdDate);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("test_user");
        userDto.setDescription("test_description");
        userDto.setStatus(Status.ACTIVE.value);
        userDto.setMapList(null);
        userDto.setCreatedBy("ADMIN");
        userDto.setCreatedDate(createdDate);

        when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(Arrays.asList(userDao));
        
        UserDto retrievedUserDto = userService.getUser("test_user");

        Assertions.assertThat(retrievedUserDto).usingRecursiveComparison().isEqualTo(userDto);
    }

    @Test
    void test_getUser_returnNull() {
        when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
        
        UserDto userDto = userService.getUser("test_user");

        Assertions.assertThat(userDto).isNull();
    }

    @Test
    void test_createUser_returnData() {
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user");
        userDto.setDescription("test_description_1");
        userDto.setStatus(Status.ACTIVE.value);

        when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
        when(userRepository.save(Mockito.any(UserDao.class))).thenReturn(new UserDao());

        userService.createUser(userDto);

        ArgumentCaptor<UserDao> userDaoCaptor = ArgumentCaptor.forClass(UserDao.class);

        verify(userRepository, Mockito.times(1)).save(userDaoCaptor.capture());

        UserDao createUserDao = userDaoCaptor.getValue();

        Assertions.assertThat(createUserDao).usingRecursiveComparison().ignoringFields("createdBy", "createdDate").isEqualTo(userDto);
        Assertions.assertThat(createUserDao.getCreatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(createUserDao.getCreatedDate()).isNotNull();
    }

    @Test
    void test_createUser_returnNull() {
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user");

        when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(Arrays.asList(new UserDao(), new UserDao()));

        UserDto retrievedUserDto = userService.createUser(userDto);

        Assertions.assertThat(retrievedUserDto).isNull();

        verify(userRepository, Mockito.never()).save(Mockito.any(UserDao.class));
    }
    
    @Test
    void test_updateUser_returnData() {
        UserDao userDao = new UserDao();
        userDao.setId(1L);
        userDao.setUsername("test_user_1");
        userDao.setDescription("test_description_1");
        userDao.setStatus(Status.ACTIVE.value);
        userDao.setCreatedBy("test_user_1");
        userDao.setCreatedDate(new Date());

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("test_user_2");
        userDto.setDescription("test_description_2");
        userDto.setStatus(Status.SUSPENDED.value);
        userDto.setCreatedBy("test_user_1");
        userDto.setCreatedDate(new Date());

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userDao));
        when(userRepository.save(Mockito.any(UserDao.class))).thenReturn(new UserDao());

        userService.updateUser(userDto);

        ArgumentCaptor<UserDao> userDaoCaptor = ArgumentCaptor.forClass(UserDao.class);

        verify(userRepository, Mockito.times(1)).save(userDaoCaptor.capture());

        UserDao updateUserDao = userDaoCaptor.getValue();

        Assertions.assertThat(updateUserDao).usingRecursiveComparison().ignoringFields("username", "description", "status", "updatedBy", "updatedDate").isEqualTo(userDao);
        Assertions.assertThat(updateUserDao.getUsername()).isEqualTo(userDto.getUsername());
        Assertions.assertThat(updateUserDao.getDescription()).isEqualTo(userDto.getDescription());
        Assertions.assertThat(updateUserDao.getStatus()).isEqualTo(userDto.getStatus());
        Assertions.assertThat(updateUserDao.getUpdatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(updateUserDao.getUpdatedDate()).isNotNull();
    }

    @Test
    void test_updateUser_returnNull() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        UserDto retrievedUserDto = userService.updateUser(userDto);

        Assertions.assertThat(retrievedUserDto).isNull();

        verify(userRepository, Mockito.never()).save(Mockito.any(UserDao.class));
    }

    @Test
    void test_deleteUser_success() {
        UserDao userDao = new UserDao();
        userDao.setId(1L);
        userDao.setUsername("test_user");
        userDao.setDescription("test_description");
        userDao.setStatus(Status.ACTIVE.value);
        userDao.setMapList(null);
        userDao.setCreatedBy("test_user_1");
        userDao.setCreatedDate(new Date());

        BigDecimal lat1 = new BigDecimal(Math.random());
        BigDecimal lon1 = new BigDecimal(Math.random());

        BigDecimal lat2 = new BigDecimal(Math.random());
        BigDecimal lon2 = new BigDecimal(Math.random());

        MapDao mapDao1 = new MapDao();
        mapDao1.setId(1L);
        mapDao1.setFkUserId(1L);
        mapDao1.setDescription("test_desc_1");
        mapDao1.setStatus(Status.ACTIVE.value);
        mapDao1.setLat(lat1);
        mapDao1.setLon(lon1);
        mapDao1.setCreatedBy("test_user_1");
        mapDao1.setCreatedDate(new Date());

        MapDao mapDao2 = new MapDao();
        mapDao2.setId(2L);
        mapDao2.setFkUserId(1L);
        mapDao2.setDescription("test_desc_2");
        mapDao2.setStatus(Status.ACTIVE.value);
        mapDao2.setLat(lat2);
        mapDao2.setLon(lon2);
        mapDao2.setCreatedBy("test_user_1");
        mapDao2.setCreatedDate(new Date());

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userDao));
        when(userRepository.save(Mockito.any(UserDao.class))).thenReturn(null);

        when(mapRepository.findAllByFkUserIdAndStatus(Mockito.anyLong(), Mockito.anyString())).thenReturn(Arrays.asList(mapDao1, mapDao2));
        when(mapRepository.saveAll(Mockito.anyList())).thenReturn(null);

        ArgumentCaptor<UserDao> userDaoCaptor = ArgumentCaptor.forClass(UserDao.class);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<MapDao>> mapDaoCaptor = ArgumentCaptor.forClass(List.class);

        userService.deleteUser(1L);

        verify(userRepository, Mockito.times(1)).save(userDaoCaptor.capture());
        verify(mapRepository, Mockito.times(1)).saveAll(mapDaoCaptor.capture());

        UserDao deleteUserDao = userDaoCaptor.getValue();
        Assertions.assertThat(deleteUserDao).usingRecursiveComparison().ignoringFields("status", "updatedBy", "updatedDate").isEqualTo(userDao);
        Assertions.assertThat(deleteUserDao.getStatus()).isEqualTo(Status.SUSPENDED.value);
        Assertions.assertThat(deleteUserDao.getUpdatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(deleteUserDao.getUpdatedDate()).isNotNull();

        List<MapDao> deleteMapDaoList = mapDaoCaptor.getValue();
        Assertions.assertThat(deleteMapDaoList.size()).isEqualTo(2);

        MapDao deleteMapDao1 = deleteMapDaoList.get(0);
        MapDao deleteMapDao2 = deleteMapDaoList.get(1);

        Assertions.assertThat(deleteMapDao1).usingRecursiveComparison().ignoringFields("status", "updatedBy", "updatedDate").isEqualTo(mapDao1);
        Assertions.assertThat(deleteMapDao1.getStatus()).isEqualTo(Status.SUSPENDED.value);
        Assertions.assertThat(deleteMapDao1.getUpdatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(deleteMapDao1.getUpdatedDate()).isNotNull();

        Assertions.assertThat(deleteMapDao2).usingRecursiveComparison().ignoringFields("status", "updatedBy", "updatedDate").isEqualTo(mapDao2);
        Assertions.assertThat(deleteMapDao2.getStatus()).isEqualTo(Status.SUSPENDED.value);
        Assertions.assertThat(deleteMapDao2.getUpdatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(deleteMapDao2.getUpdatedDate()).isNotNull();
    }

    @Test
    void test_deleteUser_fail() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        userService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDao.class));

        Mockito.verify(mapRepository, Mockito.never()).findAllByFkUserIdAndStatus(Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(mapRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    @Test
    void test_mapUserDaoToUserDto() {
        Date createdDate = new Date();
        Date updatedDate = new Date();

        UserDao userDao = new UserDao();
        userDao.setId(1L);
        userDao.setUsername("test_user");
        userDao.setDescription("test_description");
        userDao.setStatus(Status.ACTIVE.value);
        userDao.setMapList(null);
        userDao.setCreatedBy("test_user_1");
        userDao.setCreatedDate(createdDate);
        userDao.setUpdatedBy("test_user_2");
        userDao.setUpdatedDate(updatedDate);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("test_user");
        userDto.setDescription("test_description");
        userDto.setStatus(Status.ACTIVE.value);
        userDto.setMapList(null);
        userDto.setCreatedBy("test_user_1");
        userDto.setCreatedDate(createdDate);
        userDto.setUpdatedBy("test_user_2");
        userDto.setUpdatedDate(updatedDate);

        UserDto retrievedUserDto = UserService.mapUserDaoToUserDto(userDao);

        Assertions.assertThat(retrievedUserDto).usingRecursiveComparison().isEqualTo(userDto);
    }
}
