package com.srishti.subscriber.service;

import com.srishti.subscriber.dto.kafka.AccountType;
import com.srishti.subscriber.dto.kafka.NewAccountEventDto;
import com.srishti.subscriber.dto.request.GeolocationRequest;
import com.srishti.subscriber.dto.request.SubscriberRequest;
import com.srishti.subscriber.dto.response.SubscriberResponse;
import com.srishti.subscriber.entity.TemplateId;
import com.srishti.subscriber.exception.subscriber.SubscriberAlreadyExistException;
import com.srishti.subscriber.exception.subscriber.SubscriberNotFoundException;
import com.srishti.subscriber.exception.subscriber.SubscriberRegistrationException;
import com.srishti.subscriber.mapper.SubscriberMapper;
import com.srishti.subscriber.repository.SubscriberRepository;
import com.srishti.subscriber.repository.TemplateIdRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final TemplateIdRepository templateIdRepository;

    private final SubscriberRepository subscriberRepository;

    private final KafkaTemplate<String, NewAccountEventDto> kafkaTemplate;

    private final String NEW_SUBSCRIBER_TOPIC = "billing-account-topic";

    private final SubscriberMapper mapper;

    public List<SubscriberResponse> registerList(Long ownerId, MultipartFile file) {
        Workbook workbook;
        try {
            InputStream inputStream = file.getInputStream();
            workbook = WorkbookFactory.create(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing Excel file");
        }
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        List<SubscriberRequest> subscriberList = readSubscriberListFromExcel(sheet);

        List<SubscriberResponse> responses = new ArrayList<>();
        for (SubscriberRequest request : subscriberList) {
            SubscriberResponse response = register(ownerId, request);
            responses.add(response);
        }

        return responses;
    }

    private List<SubscriberRequest> readSubscriberListFromExcel(Sheet sheet) {
        List<SubscriberRequest> subscriberList = new ArrayList<>();

        for (Row row : sheet) {
            if (row == null) break;

            try {
                subscriberList.add(
                        SubscriberRequest.builder()
                        .name(getCellValue(row.getCell(0)))
                        .email(getCellValue(row.getCell(1)))
                        .phoneNumber(getCellValue(row.getCell(2)))
                        .telegramId(getCellValue(row.getCell(3)))
                        .geolocation(buildGeolocationRequest(row))
                        .build());
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return subscriberList;
    }

    private String getCellValue(Cell cell) {
        return cell != null ? cell.toString() : null;
    }

    private GeolocationRequest buildGeolocationRequest(Row row) {
        Cell latitudeCell = row.getCell(4);
        Cell longitudeCell = row.getCell(5);

        if (latitudeCell != null && longitudeCell != null) {
            return GeolocationRequest.builder()
                    .latitude(Double.parseDouble(latitudeCell.toString()))
                    .longitude(Double.parseDouble(longitudeCell.toString()))
                    .build();
        } else {
            return null;
        }
    }

    public SubscriberResponse register(Long ownerId, SubscriberRequest request) {
        if(subscriberRepository.findByEmailAndOwnerId(request.email(), ownerId).isPresent()) {
            throw new SubscriberAlreadyExistException("Subscriber already registered");
        }

        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(subscriber -> subscriber.addOwner(ownerId))
                .map(subscriberRepository::save)
                .map(subscriber -> {
                    kafkaTemplate.send(NEW_SUBSCRIBER_TOPIC, NewAccountEventDto.builder()
                                    .userId(subscriber.getId())
                                    .email(subscriber.getEmail())
                                    .telegramId(subscriber.getTelegramId())
                                    .phoneNumber(subscriber.getPhoneNumber())
                                    .accountType(AccountType.SUBSCRIBER)
                            .build());
                    return subscriber;
                })
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new SubscriberRegistrationException("Registration failed: " + request.email()));

    }

    public List<SubscriberResponse> getByOwner(Long ownerId) {
        return subscriberRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(mapper::mapToResponse)
                .toList();
    }

    public SubscriberResponse get(Long ownerId, Long subscriberId) {
        return subscriberRepository.findByIdAndOwnerId(subscriberId, ownerId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found: " + subscriberId));
    }

    public List<SubscriberResponse> getByTemplate(Long ownerId, Long templateId) {
        return templateIdRepository.findAllBySubscriber_ownerIdAndTemplateId(ownerId, templateId)
                .stream()
                .map(TemplateId::getSubscriber)
                .map(mapper::mapToResponse)
                .toList();
    }

    public Boolean remove(Long ownerId, Long subscriberId) {
        subscriberRepository.findByIdAndOwnerId(subscriberId, ownerId)
                        .map(subscriber -> {
                            subscriberRepository.delete(subscriber);
                            return subscriber;
                        })
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found: " + subscriberId));
        return true;
    }

    public SubscriberResponse update(Long ownerId, Long subscriberId, SubscriberRequest request) {
        return subscriberRepository.findByIdAndOwnerId(subscriberId, ownerId)
                .map(subscriber -> mapper.update(request, subscriber))
                .map(subscriberRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found: " + subscriberId));


    }
}
