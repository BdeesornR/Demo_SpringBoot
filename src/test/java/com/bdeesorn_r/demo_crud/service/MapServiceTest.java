package com.bdeesorn_r.demo_crud.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import com.bdeesorn_r.demo_crud.dto.MapDto;
import com.bdeesorn_r.demo_crud.repository.MapRepository;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {
    @Mock
    private MapRepository mapRepository;

    @InjectMocks
    private MapService mapService;

    @Test
    void test_getMapList_returnData() {
        when(mapRepository.findAllByFkUserIdAndStatus(Mockito.anyLong(), Mockito.anyString())).thenReturn(Arrays.asList(new MapDao(), new MapDao()));

        List<MapDto> mapDtoList = mapService.getMapList(1L);

        Assertions.assertThat(mapDtoList.size()).isEqualTo(2);
    }

    @Test
    void test_createMap_returnData() {
        MapDto mapDto = new MapDto();
        mapDto.setFkUserId(1L);
        mapDto.setDescription("test_desc_1");
        mapDto.setStatus(Status.ACTIVE.value);
        mapDto.setLat(new BigDecimal(Math.random()));
        mapDto.setLon(new BigDecimal(Math.random()));

        when(mapRepository.save(Mockito.any(MapDao.class))).thenReturn(new MapDao());

        mapService.createMap(mapDto);

        ArgumentCaptor<MapDao> mapDaoCaptor = ArgumentCaptor.forClass(MapDao.class);

        verify(mapRepository, Mockito.times(1)).save(mapDaoCaptor.capture());

        MapDao createdMapDao = mapDaoCaptor.getValue();

        Assertions.assertThat(createdMapDao).usingRecursiveComparison().ignoringFields("createdBy", "createdDate").isEqualTo(mapDto);
        Assertions.assertThat(createdMapDao.getCreatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(createdMapDao.getCreatedDate()).isNotNull();
    }

    @Test
    void test_updateMap_returnData() {
        MapDao retrieveDao = new MapDao();
        retrieveDao.setId(1L);
        retrieveDao.setFkUserId(1L);
        retrieveDao.setDescription("test_desc_1");
        retrieveDao.setStatus(Status.ACTIVE.value);
        retrieveDao.setLat(new BigDecimal(Math.random()));
        retrieveDao.setLon(new BigDecimal(Math.random()));
        retrieveDao.setCreatedBy("test_user_1");
        retrieveDao.setCreatedDate(new Date());

        MapDto updatedDto = new MapDto();
        updatedDto.setId(1L);
        updatedDto.setFkUserId(1L);
        updatedDto.setDescription("test_desc_2");
        updatedDto.setStatus(Status.SUSPENDED.value);
        updatedDto.setLat(new BigDecimal(Math.random()));
        updatedDto.setLon(new BigDecimal(Math.random()));
        updatedDto.setCreatedBy("test_user_1");
        updatedDto.setCreatedDate(new Date());

        when(mapRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(retrieveDao));
        when(mapRepository.save(Mockito.any(MapDao.class))).thenReturn(new MapDao());

        mapService.updateMap(updatedDto);

        ArgumentCaptor<MapDao> mapDaoCaptor = ArgumentCaptor.forClass(MapDao.class);

        verify(mapRepository, Mockito.times(1)).save(mapDaoCaptor.capture());

        MapDao updatedMapDao = mapDaoCaptor.getValue();

        Assertions.assertThat(updatedMapDao).usingRecursiveComparison().ignoringFields("updatedBy", "updatedDate").isEqualTo(updatedDto);
        Assertions.assertThat(updatedMapDao.getUpdatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(updatedMapDao.getUpdatedDate()).isNotNull();
    }

    @Test
    void test_updateMap_returnNull() {
        MapDto mapDto = new MapDto();
        mapDto.setId(1L);

        when(mapRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        MapDto updatedMapDto = mapService.updateMap(mapDto);

        Assertions.assertThat(updatedMapDto).isNull();

        verify(mapRepository, Mockito.never()).save(Mockito.any(MapDao.class));
    }

    @Test
    void test_deleteMap_success() {
        MapDao mapDao = new MapDao();

        when(mapRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mapDao));
        when(mapRepository.save(Mockito.any(MapDao.class))).thenReturn(null);

        mapService.deleteMap(1L);

        ArgumentCaptor<MapDao> mapDaoCaptor = ArgumentCaptor.forClass(MapDao.class);

        Mockito.verify(mapRepository, Mockito.times(1)).save(mapDaoCaptor.capture());

        MapDao deleteMapDao = mapDaoCaptor.getValue();
        Assertions.assertThat(deleteMapDao.getStatus()).isEqualTo(Status.SUSPENDED.value);
        Assertions.assertThat(deleteMapDao.getUpdatedBy()).isEqualTo("ADMIN");
        Assertions.assertThat(deleteMapDao.getUpdatedDate()).isNotNull();
    }

    @Test
    void test_deleteMap_fail() {
        when(mapRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mapService.deleteMap(1L);

        Mockito.verify(mapRepository, Mockito.never()).save(Mockito.any(MapDao.class));
    }

    @Test
    void test_mapMapDaoToMapDto() {
        BigDecimal lat = new BigDecimal(Math.random());
        BigDecimal lon = new BigDecimal(Math.random());
        Date createdDate = new Date();
        Date updatedDate = new Date();

        MapDao mapDao = new MapDao();
        mapDao.setId(1L);
        mapDao.setFkUserId(1L);
        mapDao.setDescription("test_desc_1");
        mapDao.setStatus(Status.ACTIVE.value);
        mapDao.setLat(lat);
        mapDao.setLon(lon);
        mapDao.setCreatedBy("test_user_1");
        mapDao.setCreatedDate(createdDate);
        mapDao.setUpdatedBy("test_user_2");
        mapDao.setUpdatedDate(updatedDate);

        MapDto mapDto = new MapDto();
        mapDto.setId(1L);
        mapDto.setFkUserId(1L);
        mapDto.setDescription("test_desc_1");
        mapDto.setStatus(Status.ACTIVE.value);
        mapDto.setLat(lat);
        mapDto.setLon(lon);
        mapDto.setCreatedBy("test_user_1");
        mapDto.setCreatedDate(createdDate);
        mapDto.setUpdatedBy("test_user_2");
        mapDto.setUpdatedDate(updatedDate);

        MapDto retrievedMapDto = MapService.mapMapDaoToMapDto(mapDao);

        Assertions.assertThat(retrievedMapDto).usingRecursiveComparison().isEqualTo(mapDto);
    }
}
