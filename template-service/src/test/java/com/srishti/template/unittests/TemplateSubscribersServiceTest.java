package com.srishti.template.unittests;

import com.srishti.template.client.SubscriberClient;
import com.srishti.template.dto.request.SubscriberListRequest;
import com.srishti.template.dto.response.SubscriberResponse;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.entity.Template;
import com.srishti.template.exception.template.TemplateNotFoundException;
import com.srishti.template.mapper.TemplateMapper;
import com.srishti.template.repository.SubscriberIdRepository;
import com.srishti.template.repository.TemplateRepository;
import com.srishti.template.service.TemplateSubscribersService;
import feign.FeignException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class TemplateSubscribersServiceTest {

    @Mock
    private SubscriberIdRepository subscriberIdRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private TemplateMapper mapper;

    @Mock
    private SubscriberClient subscriberClient;

    @InjectMocks
    private TemplateSubscribersService templateSubscribersService;

    private final String TEMPLATE_TITLE = "title";

    private final String TEMPLATE_CONTENT = "content";

    private final Long OWNER_ID = 8L;

    private final Long TEMPLATE_ID = 1L;

    private final Long SUBSCRIBER_ID = 3L;

    private final Template TEMPLATE = Template.builder()
            .title(TEMPLATE_TITLE)
            .content(TEMPLATE_CONTENT)
            .build();

    private final SubscriberListRequest SUBSCRIBER_LIST_REQUEST =
            new SubscriberListRequest(Collections.singletonList(SUBSCRIBER_ID));

    private final SubscriberResponse SUBSCRIBER_RESPONSE = SubscriberResponse.builder()
            .id(SUBSCRIBER_ID)
            .build();

    private final TemplateResponse TEMPLATE_RESPONSE = TemplateResponse.builder()
            .title(TEMPLATE_TITLE)
            .content(TEMPLATE_CONTENT)
            .id(TEMPLATE_ID)
            .subscriberIds(Collections.singletonList(SUBSCRIBER_RESPONSE))
            .build();

    private final TemplateResponse TEMPLATE_RESPONSE_NO_SUBSCRIBERS = TemplateResponse.builder()
            .title(TEMPLATE_TITLE)
            .content(TEMPLATE_CONTENT)
            .id(TEMPLATE_ID)
            .build();


    @Test
    public void testAddSubscribers() {
        // Arrange
         when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));
         when(subscriberIdRepository.existsByTemplateIdAndSubscriberId(TEMPLATE_ID, SUBSCRIBER_ID)).thenReturn(false);
         when(subscriberClient.getSubscriberById(OWNER_ID, SUBSCRIBER_ID))
                 .thenReturn(ResponseEntity.ok(SUBSCRIBER_RESPONSE));
         when(mapper.mapToResponse(TEMPLATE, subscriberClient)).thenReturn(TEMPLATE_RESPONSE);

        // Act
        TemplateResponse response = templateSubscribersService
                .addSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST);

        // Assert
        assertEquals(TEMPLATE_RESPONSE, response);
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(subscriberIdRepository, times(1)).existsByTemplateIdAndSubscriberId(TEMPLATE_ID, 3L);
        verify(subscriberClient, times(1)).getSubscriberById(OWNER_ID, 3L);
        verify(templateRepository, times(1)).save(TEMPLATE);
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }

    @Test
    public void testAddSubscribersTemplateNotFound() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TemplateNotFoundException.class,
                () -> templateSubscribersService.addSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST));
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verifyNoInteractions(subscriberIdRepository);
        verifyNoInteractions(subscriberClient);
        verifyNoInteractions(mapper);
    }

    @Test
    public void testAddSubscribersAlreadyRegistered() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));
        when(subscriberIdRepository.existsByTemplateIdAndSubscriberId(TEMPLATE_ID, SUBSCRIBER_ID)).thenReturn(true);
        when(mapper.mapToResponse(TEMPLATE, subscriberClient)).thenReturn(TEMPLATE_RESPONSE);

        // Act
        TemplateResponse response = templateSubscribersService
                .addSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST);

        // Assert
        assertEquals(TEMPLATE_RESPONSE, response);
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(subscriberIdRepository, times(1)).existsByTemplateIdAndSubscriberId(TEMPLATE_ID, 3L);
        verifyNoInteractions(subscriberClient);
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }

    @Test
    public void testAddSubscribersNotFound() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));
        when(subscriberIdRepository.existsByTemplateIdAndSubscriberId(TEMPLATE_ID, SUBSCRIBER_ID)).thenReturn(false);
        when(subscriberClient.getSubscriberById(OWNER_ID, SUBSCRIBER_ID))
                .thenThrow(FeignException.NotFound.class);

        // Act
        TemplateResponse response = templateSubscribersService
                .addSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST);

        // Assert
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(subscriberIdRepository, times(1)).existsByTemplateIdAndSubscriberId(TEMPLATE_ID, SUBSCRIBER_ID);
        verify(subscriberClient, times(1)).getSubscriberById(OWNER_ID, SUBSCRIBER_ID);
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }

    @Test
    public void testRemoveSubscribers() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));
        when(subscriberIdRepository.existsByTemplateIdAndSubscriberId(TEMPLATE_ID, SUBSCRIBER_ID)).thenReturn(true);
        when(subscriberClient.getSubscriberById(OWNER_ID, SUBSCRIBER_ID))
                .thenReturn(ResponseEntity.ok(SUBSCRIBER_RESPONSE));
        when(mapper.mapToResponse(TEMPLATE, subscriberClient)).thenReturn(TEMPLATE_RESPONSE_NO_SUBSCRIBERS);

        // Act
        TemplateResponse response = templateSubscribersService
                .removeSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST);

        // Assert
        assertEquals(TEMPLATE_RESPONSE_NO_SUBSCRIBERS, response);
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(subscriberIdRepository, times(1)).existsByTemplateIdAndSubscriberId(TEMPLATE_ID, 3L);
        verify(subscriberClient, times(1)).getSubscriberById(OWNER_ID, 3L);
        verify(templateRepository, times(1)).save(TEMPLATE);
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }

    @Test
    public void testRemoveSubscribersTemplateNotFound() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TemplateNotFoundException.class,
                () -> templateSubscribersService.removeSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST));
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verifyNoInteractions(subscriberIdRepository);
        verifyNoInteractions(subscriberClient);
        verifyNoInteractions(mapper);
    }

    @Test
    public void testRemoveSubscribersNotRegistered() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));
        when(subscriberIdRepository.existsByTemplateIdAndSubscriberId(TEMPLATE_ID, SUBSCRIBER_ID)).thenReturn(false);
        when(mapper.mapToResponse(TEMPLATE, subscriberClient)).thenReturn(TEMPLATE_RESPONSE_NO_SUBSCRIBERS);

        // Act
        TemplateResponse response = templateSubscribersService
                .removeSubscribers(OWNER_ID, TEMPLATE_ID, SUBSCRIBER_LIST_REQUEST);

        // Assert
        assertEquals(TEMPLATE_RESPONSE_NO_SUBSCRIBERS, response);
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(subscriberIdRepository, times(1)).existsByTemplateIdAndSubscriberId(TEMPLATE_ID, 3L);
        verifyNoInteractions(subscriberClient);
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }
}
