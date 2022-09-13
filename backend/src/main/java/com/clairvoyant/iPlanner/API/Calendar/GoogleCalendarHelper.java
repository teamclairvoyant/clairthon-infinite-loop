package com.clairvoyant.iPlanner.API.Calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleCalendarHelper {

    public static List<Event> getEvents(String email, DateTime startTime, DateTime endTime) throws IOException, GeneralSecurityException {
        // Initialize Calendar service with valid OAuth credentials
        Calendar calendarClient = GoogleCredentialHelper.getCalendarClient();
        // Retrieve all events
        Events events = calendarClient.events().list(email)
                .setTimeMin(startTime)
                .setTimeMax(endTime)
                .setTimeZone("IST")
                .execute();
        events.getItems()
                .forEach(event -> System.out.println("EVENT NAME :::::::::::::" + event.getSummary()));
        return events.getItems();
    }
}
