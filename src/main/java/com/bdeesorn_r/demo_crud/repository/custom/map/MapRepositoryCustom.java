package com.bdeesorn_r.demo_crud.repository.custom.map;

import java.util.List;

import com.bdeesorn_r.demo_crud.dao.MapDao;

public interface MapRepositoryCustom {
    public List<MapDao> findAllByFkUserIdAndStatus(Long userId, String status);
}
