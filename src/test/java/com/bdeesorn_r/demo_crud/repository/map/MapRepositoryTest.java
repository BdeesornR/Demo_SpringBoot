package com.bdeesorn_r.demo_crud.repository.map;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bdeesorn_r.demo_crud.constant.Status;
import com.bdeesorn_r.demo_crud.dao.MapDao;
import com.bdeesorn_r.demo_crud.repository.MapRepository;

@DataJpaTest
public class MapRepositoryTest {
    @Autowired
    private MapRepository mapRepository;

    @Test
    void test_findAllByFkUserIdAndStatus_returnData() {
        MapDao mapDao1 = new MapDao();
        mapDao1.setFkUserId(1L);
        mapDao1.setDescription("test_description_1");
        mapDao1.setStatus(Status.ACTIVE.value);
        mapDao1.setLat(new BigDecimal(Math.random()));
        mapDao1.setLon(new BigDecimal(Math.random()));
        mapDao1.setCreatedBy("test_user_1");
        mapDao1.setCreatedDate(new Date());

        MapDao mapDao2 = new MapDao();
        mapDao2.setFkUserId(1L);
        mapDao2.setDescription("test_description_2");
        mapDao2.setStatus(Status.ACTIVE.value);
        mapDao2.setLat(new BigDecimal(Math.random()));
        mapDao2.setLon(new BigDecimal(Math.random()));
        mapDao2.setCreatedBy("test_user_1");
        mapDao2.setCreatedDate(new Date());

        mapRepository.saveAll(Arrays.asList(mapDao1, mapDao2));

        List<MapDao> mapDaoList = mapRepository.findAllByFkUserIdAndStatus(1L, Status.ACTIVE.value);

        Assertions.assertThat(mapDaoList.size()).isEqualTo(2);

        MapDao retrievedMapDao1 = mapDaoList.get(0);
        
        Assertions.assertThat(retrievedMapDao1).usingRecursiveComparison().ignoringFields("id").isEqualTo(mapDao1);
        Assertions.assertThat(retrievedMapDao1.getId()).isNotNull();

        MapDao retrievedMapDao2 = mapDaoList.get(1);

        Assertions.assertThat(retrievedMapDao2).usingRecursiveComparison().ignoringFields("id").isEqualTo(mapDao2);
        Assertions.assertThat(retrievedMapDao2.getId()).isNotNull();
    }

    @Test
    void test_findAllByFkUserIdAndStatus_returnEmpty() {
        List<MapDao> mapDaoList = mapRepository.findAllByFkUserIdAndStatus(1L, null);

        Assertions.assertThat(mapDaoList).isEmpty();
    }
}
