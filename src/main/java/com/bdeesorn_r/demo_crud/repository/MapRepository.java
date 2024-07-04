package com.bdeesorn_r.demo_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdeesorn_r.demo_crud.dao.MapDao;
import com.bdeesorn_r.demo_crud.repository.custom.map.MapRepositoryCustom;

@Repository
public interface MapRepository extends JpaRepository<MapDao, Long>, MapRepositoryCustom {
}
