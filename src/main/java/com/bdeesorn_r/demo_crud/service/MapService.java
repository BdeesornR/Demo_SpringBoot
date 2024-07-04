package com.bdeesorn_r.demo_crud.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bdeesorn_r.demo_crud.constant.Status;
import com.bdeesorn_r.demo_crud.dao.MapDao;
import com.bdeesorn_r.demo_crud.dto.MapDto;
import com.bdeesorn_r.demo_crud.repository.MapRepository;

@Service
public class MapService {
    private MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public List<MapDto> getMapList(Long userId) {
        return mapRepository.findAllByFkUserIdAndStatus(userId, Status.ACTIVE.value).stream().map(MapService::mapMapDaoToMapDto).toList();
    }

    public MapDto createMap(MapDto mapDto) {
        MapDao map = new MapDao();

        map.setFkUserId(mapDto.getFkUserId());
        map.setDescription(mapDto.getDescription());
        map.setStatus(mapDto.getStatus());
        map.setLat(mapDto.getLat());
        map.setLon(mapDto.getLon());
        map.setUser(null);

        map.setCreatedBy("ADMIN");
        map.setCreatedDate(new Date());

        map = mapRepository.save(map);

        return mapMapDaoToMapDto(map);
    }

    public MapDto updateMap(MapDto mapDto) {
        Optional<MapDao> mapOpt = mapRepository.findById(mapDto.getId());

        if (!mapOpt.isPresent()) {
            return null;
        }

        MapDao map = mapOpt.get();
        map.setDescription(mapDto.getDescription());
        map.setStatus(mapDto.getStatus());
        map.setLat(mapDto.getLat());
        map.setLon(mapDto.getLon());
        map.setUser(null);

        map.setUpdatedBy("ADMIN");
        map.setUpdatedDate(new Date());

        map = mapRepository.save(map);

        return mapMapDaoToMapDto(map);
    }

    public void deleteMap(Long id) {
        Optional<MapDao> mapOpt = mapRepository.findById(id);

        if (!mapOpt.isPresent()) {
            return;
        }

        MapDao map = mapOpt.get();
        map.setStatus(Status.SUSPENDED.value);
        map.setUser(null);

        map.setUpdatedBy("ADMIN");
        map.setUpdatedDate(new Date());

        mapRepository.save(map);
    }

    public static MapDto mapMapDaoToMapDto(MapDao mapDao) {
        MapDto mapDto = new MapDto();

        mapDto.setId(mapDao.getId());
        mapDto.setFkUserId(mapDao.getFkUserId());
        mapDto.setDescription(mapDao.getDescription());
        mapDto.setStatus(mapDao.getStatus());
        mapDto.setLat(mapDao.getLat());
        mapDto.setLon(mapDao.getLon());

        mapDto.setCreatedBy(mapDao.getCreatedBy());
        mapDto.setCreatedDate(mapDao.getCreatedDate());
        mapDto.setUpdatedBy(mapDao.getUpdatedBy());
        mapDto.setUpdatedDate(mapDao.getUpdatedDate());

        return mapDto;
    }
}
