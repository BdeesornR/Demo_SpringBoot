package com.bdeesorn_r.demo_crud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bdeesorn_r.demo_crud.dto.CommonResponse;
import com.bdeesorn_r.demo_crud.dto.MapDto;
import com.bdeesorn_r.demo_crud.service.MapService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
@RequestMapping("/map")
public class MapController {
    private MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/get-map-list/{userId}")
    public ResponseEntity<CommonResponse> getMapList(@PathVariable Long userId) {        
        return new ResponseEntity<>(new CommonResponse("200", "Success", mapService.getMapList(userId)), HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createMap(@RequestBody MapDto mapDto) {
        if (mapDto.getFkUserId() == null || mapDto.getLat() == null || mapDto.getLon() == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: UserId, Lat and Lon are required"), HttpStatus.OK);
        }

        MapDto map = mapService.createMap(mapDto);

        if (map == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Record already exists"), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(new CommonResponse("200", "Success", map), HttpStatus.OK);
    }
    
    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateMap(@RequestBody MapDto mapDto) {
        if (mapDto.getId() == null || mapDto.getLat() == null || mapDto.getLon() == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Id, Lat and Lon are required"), HttpStatus.OK);
        }

        MapDto map = mapService.updateMap(mapDto);

        if (map == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Record not found"), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(new CommonResponse("200", "Success"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> deleteMap(@PathVariable Long id) {
        mapService.deleteMap(id);
        
        return new ResponseEntity<>(new CommonResponse("200", "Success"), HttpStatus.OK);
    }
}
