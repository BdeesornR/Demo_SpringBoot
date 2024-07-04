package com.bdeesorn_r.demo_crud.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
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
import com.bdeesorn_r.demo_crud.dto.MapDto;
import com.bdeesorn_r.demo_crud.service.MapService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = MapController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class) 
public class MapControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MapService mapService;

    private final String getMapListURI = "/map/get-map-list/%d";
    private final String createMapURI = "/map/create";
    private final String updateMapURI = "/map/update";
    private final String deleteMapURI = "/map/delete/%d";

    @Test
    void test_getMapList_success() throws Exception {
        MapDto mapDto1 = new MapDto();
        mapDto1.setId(1L);
        mapDto1.setFkUserId(1L);
        mapDto1.setDescription("test_description");
        mapDto1.setStatus(Status.ACTIVE.value);
        mapDto1.setLat(new BigDecimal(Math.random()));
        mapDto1.setLon(new BigDecimal(Math.random()));
        mapDto1.setCreatedBy("test_user_1");
        mapDto1.setCreatedDate(new Date());
        mapDto1.setUpdatedBy("test_user_2");
        mapDto1.setUpdatedDate(new Date());

        MapDto mapDto2 = new MapDto();
        mapDto2.setId(2L);
        mapDto2.setFkUserId(1L);
        mapDto2.setDescription("test_description");
        mapDto2.setStatus(Status.ACTIVE.value);
        mapDto2.setLat(new BigDecimal(Math.random()));
        mapDto2.setLon(new BigDecimal(Math.random()));
        mapDto2.setCreatedBy("test_user_1");
        mapDto2.setCreatedDate(new Date());
        mapDto2.setUpdatedBy("test_user_2");
        mapDto2.setUpdatedDate(new Date());

        when(mapService.getMapList(Mockito.anyLong())).thenReturn(Arrays.asList(mapDto1, mapDto2));
        
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(getMapListURI, 1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Matchers.equalTo(mapDto1.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].fkUserId", Matchers.equalTo(mapDto1.getFkUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description", Matchers.equalTo(mapDto1.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status", Matchers.equalTo(mapDto1.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lat", Matchers.equalTo(mapDto1.getLat())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lon", Matchers.equalTo(mapDto1.getLon())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].createdBy", Matchers.equalTo(mapDto1.getCreatedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].createdDate", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].updatedBy", Matchers.equalTo(mapDto1.getUpdatedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].updatedDate", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Matchers.equalTo(mapDto2.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].fkUserId", Matchers.equalTo(mapDto2.getFkUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].description", Matchers.equalTo(mapDto2.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].status", Matchers.equalTo(mapDto2.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].lat", Matchers.equalTo(mapDto2.getLat())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].lon", Matchers.equalTo(mapDto2.getLon())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].createdBy", Matchers.equalTo(mapDto2.getCreatedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].createdDate", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].updatedBy", Matchers.equalTo(mapDto2.getUpdatedBy())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].updatedDate", Matchers.notNullValue()));
    }

    @Test
    void test_createMap_success() throws Exception {
        MapDto mapDto = new MapDto();
        mapDto.setFkUserId(1L);
        mapDto.setDescription("test_description");
        mapDto.setStatus(Status.ACTIVE.value);
        mapDto.setLat(new BigDecimal(Math.random()));
        mapDto.setLon(new BigDecimal(Math.random()));

        when(mapService.createMap(Mockito.any(MapDto.class))).thenReturn(mapDto);

        mockMvc.perform(MockMvcRequestBuilders.post(createMapURI).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mapDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fkUserId", Matchers.equalTo(mapDto.getFkUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description", Matchers.equalTo(mapDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", Matchers.equalTo(mapDto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lat", Matchers.equalTo(mapDto.getLat())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lon", Matchers.equalTo(mapDto.getLon())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdBy", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdDate", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updatedBy", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updatedDate", Matchers.nullValue()));

        ArgumentCaptor<MapDto> mapCaptor = ArgumentCaptor.forClass(MapDto.class);

        verify(mapService, Mockito.times(1)).createMap(mapCaptor.capture());

        MapDto createdMapDto = mapCaptor.getValue();

        Assertions.assertThat(createdMapDto).usingRecursiveComparison().isEqualTo(mapDto);
    }
    
    @Test
    void test_createMap_fail_requestIncomplete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(createMapURI).contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: UserId, Lat and Lon are required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        verify(mapService, Mockito.never()).createMap(Mockito.any(MapDto.class));
    }
    
    @Test
    void test_createMap_fail_duplicateMap() throws Exception {
        MapDto mapDto = new MapDto();
        mapDto.setId(1L);
        mapDto.setFkUserId(1L);
        mapDto.setDescription("test_description");
        mapDto.setStatus(Status.ACTIVE.value);
        mapDto.setLat(new BigDecimal(Math.random()));
        mapDto.setLon(new BigDecimal(Math.random()));

        when(mapService.createMap(Mockito.any(MapDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(createMapURI).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mapDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Record already exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        ArgumentCaptor<MapDto> mapCaptor = ArgumentCaptor.forClass(MapDto.class);

        verify(mapService, Mockito.times(1)).createMap(mapCaptor.capture());

        MapDto createdMapDto = mapCaptor.getValue();

        Assertions.assertThat(createdMapDto).usingRecursiveComparison().isEqualTo(mapDto);
    }

    @Test
    void test_updateMap_success() throws Exception {
        MapDto mapDto = new MapDto();
        mapDto.setId(1L);
        mapDto.setFkUserId(1L);
        mapDto.setDescription("test_description");
        mapDto.setStatus(Status.ACTIVE.value);
        mapDto.setLat(new BigDecimal(Math.random()));
        mapDto.setLon(new BigDecimal(Math.random()));
        mapDto.setCreatedBy("test_user");
        mapDto.setCreatedDate(new Date());

        when(mapService.updateMap(Mockito.any(MapDto.class))).thenReturn(mapDto);

        mockMvc.perform(MockMvcRequestBuilders.put(updateMapURI).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mapDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        ArgumentCaptor<MapDto> mapCaptor = ArgumentCaptor.forClass(MapDto.class);

        verify(mapService, Mockito.times(1)).updateMap(mapCaptor.capture());

        MapDto updatedMapDto = mapCaptor.getValue();

        Assertions.assertThat(updatedMapDto).usingRecursiveComparison().isEqualTo(mapDto);
    }
    
    @Test
    void test_updateMap_fail_requestIncomplete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(updateMapURI).contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Id, Lat and Lon are required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        verify(mapService, Mockito.never()).updateMap(Mockito.any(MapDto.class));
    }
    
    @Test
    void test_updateMap_fail_recordNotFound() throws Exception {
        MapDto mapDto = new MapDto();
        mapDto.setId(1L);
        mapDto.setFkUserId(1L);
        mapDto.setDescription("test_description");
        mapDto.setStatus(Status.ACTIVE.value);
        mapDto.setLat(new BigDecimal(Math.random()));
        mapDto.setLon(new BigDecimal(Math.random()));
        mapDto.setCreatedBy("test_user");
        mapDto.setCreatedDate(new Date());

        when(mapService.updateMap(Mockito.any(MapDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put(updateMapURI).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mapDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Failed: Record not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        ArgumentCaptor<MapDto> mapCaptor = ArgumentCaptor.forClass(MapDto.class);

        verify(mapService, Mockito.times(1)).updateMap(mapCaptor.capture());

        MapDto updatedMapDto = mapCaptor.getValue();

        Assertions.assertThat(updatedMapDto).usingRecursiveComparison().isEqualTo(mapDto);
    }

    @Test
    void test_deleteMap_success() throws Exception {
        Long deleteId = 1L;

        doNothing().when(mapService).deleteMap(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format(deleteMapURI, deleteId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo("200")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.nullValue()));

        ArgumentCaptor<Long> mapIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(mapService, Mockito.times(1)).deleteMap(mapIdCaptor.capture());

        Assertions.assertThat(mapIdCaptor.getValue()).isEqualTo(deleteId);
    }
}
