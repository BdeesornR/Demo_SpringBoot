package com.bdeesorn_r.demo_crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDto implements Serializable {
    private Long id;
    private Long fkUserId;
    private String description;
    private String status;
    private BigDecimal lat;
    private BigDecimal lon;
    
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    private UserDto user;
}
