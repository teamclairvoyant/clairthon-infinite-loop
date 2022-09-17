package com.clairvoyant.iPlanner.API.Calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class CalendarHelper {

    public static List<Event> getEvents(String email, DateTime startTime, DateTime endTime) throws IOException, GeneralSecurityException {
        // Initialize Calendar service with valid OAuth credentials
        Calendar calendarClient = GoogleCredentialHelper.getCalendarClient();
        // Retrieve all events
        Events events = calendarClient.events().list(email)
                .setTimeMin(startTime)
                .setTimeMax(endTime)
                .setTimeZone("IST")
                .execute();
        return events.getItems();
    }

    public static String getPhotoUrl(String email) {
        // todo getPhotoUrl
        return "";
    }

    public static FreeBusyResponse getFreeBusy(List<String> email_list, DateTime startTime, DateTime endTime) throws IOException, GeneralSecurityException {
        Calendar calendarClient = GoogleCredentialHelper.getCalendarClient();
        FreeBusyRequest req = new FreeBusyRequest();
        req.setTimeZone("IST");
        req.setTimeMin(startTime);
        req.setTimeMax(endTime);

        List<FreeBusyRequestItem> items = new ArrayList<>();
        email_list.forEach(email -> items.add(new FreeBusyRequestItem().setId(email)));
        req.setItems(items);

        Calendar.Freebusy.Query fbq = calendarClient.freebusy().query(req);
        return fbq.execute();
    }
}
