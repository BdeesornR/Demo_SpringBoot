package com.bdeesorn_r.demo_crud.repository.custom.map;

import java.util.ArrayList;
import java.util.List;

import com.bdeesorn_r.demo_crud.dao.MapDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class MapRepositoryImpl implements MapRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public List<MapDao> findAllByFkUserIdAndStatus(Long userId, String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MapDao> cq = cb.createQuery(MapDao.class);
        Root<MapDao> root = cq.from(MapDao.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("fkUserId"), userId));
        
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    };
}
