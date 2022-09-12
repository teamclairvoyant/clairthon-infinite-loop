package com.clairvoyant.iPlanner.API.FreeBusy;

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
        Calendar calendarClient = GoogleCredentialHelper.getCalendarClientV2();
        // Retrieve all events
        Events events = calendarClient.events().list("primary").setTimeMin(startTime).setTimeMax(endTime).execute();

        return events.getItems();

    }
}
