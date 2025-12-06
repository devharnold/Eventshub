package com.eventhub.events.controller;

import com.eventhub.events.dao.EventsDao;
import com.eventhub.events.service.EventsService;
import com.eventhub.events.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testEventController {
    @Mock
    private EventsDao eventsDao;

    @InjectMocks
    private EventsController eventsController;

    @Spy
    private EventsService eventsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventsService = spy(new EventsService(eventsDao));
        eventsController = new EventsController(eventsDao);
    }

    @Test
    void testGetEventById_Found() {
        // Arrange
        Events mockEvent = new Events();
        mockEvent.setEventId("123");
        when(eventsDao.findById("123")).thenReturn(mockEvent);

        //Act
        ResponseEntity<Events> response = eventsController.getEventById("123");

        // Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("123", response.getBody().getEventId());
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventsDao.findById("999")).thenReturn(null);

        ResponseEntity<Events> response = eventsController.getEventById("999");

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNull(response.getBody());
    }

    @Test
    void testGetEvents_AllEvents() {
        List<Events> mockEvents = Arrays.asList(new Events(), new Events());
        when(eventsDao.findAll()).thenReturn(mockEvents);

        ResponseEntity<List<Events>> response = eventsController.getEvents(null, null);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetEvents_ByDateAndLocation() {
        List<Events> mockEvents = Arrays.asList(new Events());
        when(eventsDao.findByDateAndLocation("Nairobi", "2025-11-06", 0, 10)).thenReturn(mockEvents);

        ResponseEntity<List<Events>> response = eventsController.getEvents("Nairobi", "2025-11-06");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreateEvent() {
        Events mockEvent = new Events();
        mockEvent.setEventId("321");

        doNothing().when(eventsDao).createEvent(mockEvent);

        ResponseEntity<Events> response = eventsController.createEvent(mockEvent);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals("321", response.getBody().getEventId());
        verify(eventsDao, times(1)).createEvent(mockEvent);
    }
}
