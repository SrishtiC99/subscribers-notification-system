package com.srishti.template.unittests;

import com.srishti.template.client.SubscriberClient;
import com.srishti.template.dto.request.TemplateRequest;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.entity.Template;
import com.srishti.template.exception.template.TemplateNotFoundException;
import com.srishti.template.exception.template.TemplateTitleAlreadyExistsException;
import com.srishti.template.mapper.TemplateMapper;
import com.srishti.template.repository.TemplateRepository;
import com.srishti.template.service.TemplateService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private TemplateMapper mapper;

    @Mock
    private SubscriberClient subscriberClient;

    @InjectMocks
    private TemplateService templateService;

    private final String TEMPLATE_TITLE = "title";

    private final String TEMPLATE_CONTENT = "content";

    private final Long OWNER_ID = 8L;

    private final Long TEMPLATE_ID = 1L;

    private final TemplateRequest TEMPLATE_REQUEST = new TemplateRequest(TEMPLATE_TITLE, TEMPLATE_CONTENT);

    private final TemplateResponse TEMPLATE_RESPONSE = TemplateResponse.builder()
            .title(TEMPLATE_TITLE)
            .content(TEMPLATE_CONTENT)
            .id(TEMPLATE_ID)
            .build();

    private final Template TEMPLATE = Template.builder()
            .title(TEMPLATE_TITLE)
            .content(TEMPLATE_CONTENT)
            .build();

    @BeforeAll
    public static void setUp() {
    }

    @Test
    public void testCreateTemplate() {
        // Arrange
        when(templateRepository.existsTemplateByOwnerIdAndTitle(OWNER_ID, TEMPLATE_TITLE)).thenReturn(false);
        when(mapper.mapToEntity(TEMPLATE_REQUEST)).thenReturn(TEMPLATE);
        when(templateRepository.save(any(Template.class))).thenReturn(TEMPLATE);
        when(mapper.mapToResponse(TEMPLATE, subscriberClient)).thenReturn(TEMPLATE_RESPONSE);

        // Act
        TemplateResponse response = templateService.create(OWNER_ID, TEMPLATE_REQUEST);

        // Assert
        assertNotNull(response);
        verify(templateRepository, times(1)).existsTemplateByOwnerIdAndTitle(OWNER_ID, TEMPLATE_TITLE);
        verify(mapper, times(1)).mapToEntity(TEMPLATE_REQUEST);
        verify(templateRepository, times(1)).save(any(Template.class));
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }

    @Test
    public void testCreateTemplateWithExistingTitle() {
        // Arrange
        when(templateRepository.existsTemplateByOwnerIdAndTitle(OWNER_ID, TEMPLATE_TITLE)).thenReturn(true);

        // Act and Assert
        assertThrows(TemplateTitleAlreadyExistsException.class, () -> templateService.create(OWNER_ID, TEMPLATE_REQUEST));
        verify(templateRepository, times(1)).existsTemplateByOwnerIdAndTitle(OWNER_ID, TEMPLATE_TITLE);
        verifyNoInteractions(mapper);
        verifyNoInteractions(subscriberClient);
    }

    @Test
    public void testGetTemplate() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));
        when(mapper.mapToResponse(TEMPLATE, subscriberClient)).thenReturn(TEMPLATE_RESPONSE);

        // Act
        TemplateResponse response = templateService.get(OWNER_ID, TEMPLATE_ID);

        // Assert
        assertNotNull(response);
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(mapper, times(1)).mapToResponse(TEMPLATE, subscriberClient);
    }

    @Test
    public void testGetTemplateNotFound() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TemplateNotFoundException.class, () -> templateService.get(OWNER_ID, TEMPLATE_ID));
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verifyNoInteractions(mapper);
        verifyNoInteractions(subscriberClient);
    }

    @Test
    public void testDeleteTemplate() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.of(TEMPLATE));

        // Act
        Boolean result = templateService.delete(OWNER_ID, TEMPLATE_ID);

        // Assert
        assertTrue(result);
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
        verify(templateRepository, times(1)).delete(TEMPLATE);
    }

    @Test
    public void testDeleteTemplateNotFound() {
        // Arrange
        when(templateRepository.findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TemplateNotFoundException.class, () -> templateService.delete(OWNER_ID, TEMPLATE_ID));
        verify(templateRepository, times(1)).findByIdAndOwnerId(TEMPLATE_ID, OWNER_ID);
    }
}
