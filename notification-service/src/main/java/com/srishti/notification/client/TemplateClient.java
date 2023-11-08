package com.srishti.notification.client;

import com.srishti.notification.dto.response.TemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "template-service")
public interface TemplateClient {

    @GetMapping("/api/v1/templates/{id}")
    ResponseEntity<TemplateResponse> getTemplateByIdAndByOwnerId(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId);
}
