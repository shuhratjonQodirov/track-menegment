package com.example.trackmenegment.service;


import com.example.trackmenegment.dto.req.TruckReqDto;
import com.example.trackmenegment.utils.ApiResponse;

public interface TruckService  {

    ApiResponse create(TruckReqDto dto);

    ApiResponse update(Long id, TruckReqDto dto);

    ApiResponse getAllList();

    ApiResponse getById(Long id);

    ApiResponse delete(Long id);
}
