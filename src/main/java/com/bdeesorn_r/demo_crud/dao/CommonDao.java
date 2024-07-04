package com.bdeesorn_r.demo_crud.dao;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class CommonDao {
    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "created_date")
    protected Date createdDate;

    @Column(name = "updated_by")
    protected String updatedBy;

    @Column(name = "updated_date")
    protected Date updatedDate;
}
