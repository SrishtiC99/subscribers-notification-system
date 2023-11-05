package com.srishti.template.controller;

import com.srishti.template.dto.request.TemplateRequest;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.service.TemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("/")
    public ResponseEntity<TemplateResponse> create(
            @RequestHeader Long ownerId,
            @RequestBody @Valid TemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.create(ownerId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> get(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId) {
        return ResponseEntity.status(HttpStatus.OK).body(templateService.get(ownerId, templateId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId) {
        return ResponseEntity.status(HttpStatus.OK).body(templateService.delete(ownerId, templateId));
    }
}
