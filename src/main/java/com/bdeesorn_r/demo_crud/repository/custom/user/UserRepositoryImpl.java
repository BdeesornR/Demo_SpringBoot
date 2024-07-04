package com.bdeesorn_r.demo_crud.repository.custom.user;

import java.util.ArrayList;
import java.util.List;

import com.bdeesorn_r.demo_crud.dao.UserDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDao> findByUsernameAndStatus(String username, String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDao> cq = cb.createQuery(UserDao.class);
        Root<UserDao> user = cq.from(UserDao.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(user.get("username"), username));

        if (status != null) {
            predicates.add(cb.equal(user.get("status"), status));
        }

        cq.select(user).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    };
}
