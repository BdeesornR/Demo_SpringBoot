package com.bdeesorn_r.demo_crud.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bdeesorn_r.demo_crud.constant.Status;
import com.bdeesorn_r.demo_crud.dto.UserDto;
import com.bdeesorn_r.demo_crud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final String getUserURI = "/user/get-user/%s";
    private final String createUserURI = "/user/create";
    private final String updateUserURI = "/user/update";
    private final String deleteUserURI = "/user/delete/%d";
    
    @Test
    void test_getUser_success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("test_user_1");
        userDto.setDescription("test_description_1");
        userDto.setStatus(Status.ACTIVE.value);
        userDto.setCreatedBy("test_user_1");
        userDto.setCreatedDate(new Date());
        userDto.setUpdatedBy("test_user_1");
        userDto.setUpdatedDate(new Date());

        when(userService.getUser(Mockito.anyString())).thenReturn((userDto));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(getUserURI, "test_user")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Matchers.equalTo(userDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username", Matchers.equalTo(userDto.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description", Matchers.equalTo(userDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", Matchers.equalTo(userDto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdBy", Matchers.equalTo(userDto.getCreatedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdDate", Matchers.notNullValue()));
    }
    
    @Test
    void test_getUser_fail_recordNotFound() throws Exception {
        when(userService.getUser(Mockito.anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(getUserURI, "test_user")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Record not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));
    }

    @Test
    void test_createUser_success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user");
        userDto.setDescription("test_descripiton");
        userDto.setStatus(Status.ACTIVE.value);

        when(userService.createUser(Mockito.any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post(createUserURI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username", Matchers.equalTo(userDto.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description", Matchers.equalTo(userDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", Matchers.equalTo(userDto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdBy", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdDate", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updatedBy", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updatedDate", Matchers.nullValue()));

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);

        verify(userService, Mockito.times(1)).createUser(userCaptor.capture());

        UserDto createDto = userCaptor.getValue();

        Assertions.assertThat(createDto).usingRecursiveComparison().isEqualTo(userDto);
    }

    @Test
    void test_createUser_fail_requestIncomplete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(createUserURI).contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Username is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        verify(userService, Mockito.never()).createUser(Mockito.any(UserDto.class));
    }

    @Test
    void test_createUser_fail_duplicateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user");

        when(userService.createUser(Mockito.any(UserDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(createUserURI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Username already exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        verify(userService, Mockito.times(1)).createUser(Mockito.any(UserDto.class));
    }
    
    @Test
    void test_updateUser_success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("test_user");
        userDto.setDescription("test_description");
        userDto.setStatus(Status.ACTIVE.value);
        userDto.setCreatedBy("test_user");
        userDto.setCreatedDate(new Date());

        when(userService.updateUser(Mockito.any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put(updateUserURI).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);

        verify(userService, Mockito.times(1)).updateUser(userCaptor.capture());

        UserDto updateDto = userCaptor.getValue();

        Assertions.assertThat(updateDto).usingRecursiveComparison().isEqualTo(userDto);
    }
    
    @Test
    void test_updateUser_fail_requestIncomplete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(updateUserURI).contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Id is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        verify(userService, Mockito.never()).updateUser(Mockito.any(UserDto.class));
    }
    
    @Test
    void test_updateUser_fail_recordNotFound() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        when(userService.updateUser(Mockito.any(UserDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put(updateUserURI).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Record not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        verify(userService, Mockito.times(1)).updateUser(Mockito.any(UserDto.class));
    }

    @Test
    void test_deleteUser_success() throws Exception {
        Long deleteId = 1L;

        doNothing().when(userService).deleteUser(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format(deleteUserURI, deleteId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data",Matchers.nullValue()));

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(userService, Mockito.times(1)).deleteUser(userIdCaptor.capture());

        Assertions.assertThat(userIdCaptor.getValue()).isEqualTo(deleteId);
    }
}
