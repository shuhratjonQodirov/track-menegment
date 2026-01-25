package com.example.trackmenegment.service.impl;

import com.example.trackmenegment.dto.req.TruckReqDto;
import com.example.trackmenegment.dto.res.TruckResDto;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.error.ExistsNameException;
import com.example.trackmenegment.mapper.TruckMapper;
import com.example.trackmenegment.model.Truck;
import com.example.trackmenegment.repository.TruckRepository;
import com.example.trackmenegment.service.TruckService;
import com.example.trackmenegment.utils.ApiResponse;
import com.example.trackmenegment.validator.TruckValidator;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;
    private final TruckValidator truckValidator;
    private final TruckMapper truckMapper;

    @Override
    public ApiResponse create(TruckReqDto truckReqDto) {

        truckValidator.validateForCreate(truckReqDto);

        if (truckRepository.existsByTruckNumberAndDeletedFalse(truckReqDto.getTruckNumber().toUpperCase())) {
            throw new ExistsNameException("This truck number already exists");
        }

        Truck truck = truckMapper.toEntity(truckReqDto);

        truckRepository.save(truck);

        return new ApiResponse("truck  data saved successfully", true);
    }
    @Override
    public ApiResponse getAllList() {
        List<TruckResDto> list = truckRepository.findAllByDeletedFalse().stream()
                .map(truckMapper::toDto)
                .toList();
        return new ApiResponse("", true, list);
    }
    @Override
    public ApiResponse update(Long id, TruckReqDto dto) {

        Truck truck = truckRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Truck not found"));
        truckValidator.validateForUpdate(dto);

        if (!truck.getTruckNumber().equals(dto.getTruckNumber()) &&
                truckRepository.existsByTruckNumberAndDeletedFalse(dto.getTruckNumber())) {
            throw new ByIdException("Truck number already exists");
        }
        if (StringUtils.isNotBlank(dto.getTruckNumber())) {
            truck.setTruckNumber(dto.getTruckNumber().toUpperCase());
        }
        if (StringUtils.isNotBlank(dto.getModelName())) {
            truck.setModelName(dto.getModelName().toUpperCase());
        }
        if (dto.getManufactureYear() != 0) {
            truck.setManufactureYear(dto.getManufactureYear());
        }
        if (dto.getBuyPrice() != null) {
            truck.setBuyPrice(dto.getBuyPrice());
        }
        if (dto.getBuyDate() != null) {
            truck.setBuyDate(dto.getBuyDate());
        }
        if (dto.getTruckStatus() != null) {
            truck.setTruckStatus(dto.getTruckStatus());
        }
        truckRepository.save(truck);
        return new ApiResponse("Truck updated", true);
    }

    @Override
    public ApiResponse getById(Long id) {
        Truck truck = truckRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Truck not found"));
        return new ApiResponse("Truck by id", true, truckMapper.toDto(truck));
    }

    @Override
    public ApiResponse delete(Long id) {

        Truck truck = truckRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Truck not found"));
        truck.setDeleted(true);
        truckRepository.save(truck);
        return new ApiResponse("Truck successfully deleted", true);

    }
}
