package com.bdeesorn_r.demo_crud.dao;

import java.math.BigDecimal;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "maps", schema = "demo_crud")
public class MapDao extends CommonDao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maps_id_seq")
    @SequenceGenerator(name = "maps_id_seq", sequenceName = "demo_crud.maps_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "fk_user_id")
    private Long fkUserId;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "latitude")
    private BigDecimal lat;

    @Column(name = "longtitude")
    private BigDecimal lon;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserDao user;
}
