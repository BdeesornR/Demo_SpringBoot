package com.bdeesorn_r.demo_crud.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String description;
    private String status;

    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    List<MapDto> mapList;
}
