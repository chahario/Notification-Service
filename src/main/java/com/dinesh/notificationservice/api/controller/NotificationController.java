package com.dinesh.notificationservice.api.controller;

import com.dinesh.notificationservice.api.dto.NotificationRequest;
import com.dinesh.notificationservice.api.dto.NotificationResponse;
import com.dinesh.notificationservice.domain.model.Notification;
import com.dinesh.notificationservice.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse createNotification(
            @RequestBody NotificationRequest request) {

        log.info(
                "Received Notification Request userId={}, type={}",
                request.getUserId(),
                request.getType()
        );

        Notification notification = notificationService.createNotification(
                request.getUserId(),
                request.getType(),
                request.getPayload(),
                request.getIdempotencyKey()
        );

        log.info("Notification created with id={}, userId={}",
                notification.getId(),
                notification.getUserId()
        );

        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .status(notification.getStatus())
                .build();
    }
}