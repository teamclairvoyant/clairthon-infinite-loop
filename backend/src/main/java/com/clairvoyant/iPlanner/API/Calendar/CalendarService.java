package com.clairvoyant.iPlanner.API.Calendar;

import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Shared.DTO.ReactCalendarEvent;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.Utility;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyResponse;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private static CalendarService instance;

    /**
     * Singleton class Lazy initialization with Double check locking
     *
     * @return FreeBusyService
     */
    public static CalendarService getInstance() {
        if (instance == null) {
            synchronized (CalendarService.class) {
                instance = new CalendarService();
            }
        }
        return instance;
    }

    /**
     * private constructor for singleton class
     */
    private CalendarService() {
    }

    public static List<ReactCalendarEvent> convertEventsToResource(List<Event> google_calendar_events) {
        List<ReactCalendarEvent> converted_events = new ArrayList<ReactCalendarEvent>();
        google_calendar_events.parallelStream().forEach(google_event -> {
            // todo test this; change  class 'fc-event-primary' based on logic
            ReactCalendarEvent reactCalendarEvent = new ReactCalendarEvent();
            ReactCalendarEvent.Type type = new ReactCalendarEvent.Type();
            type.setLabel(google_event.getStatus());
            type.setValue("fc-event-primary");

            reactCalendarEvent.setId(google_event.getId());
            if(google_event.getSummary() == null) {
                // means it's a private event
                reactCalendarEvent.setTitle(Literal.PRIVATE_EVENT);
            } else {
                reactCalendarEvent.setTitle(google_event.getSummary());
            }
            reactCalendarEvent.setStart(google_event.getStart().getDateTime().toString());
            reactCalendarEvent.setEnd(google_event.getEnd().getDateTime().toString());
            reactCalendarEvent.setClassName("fc-event-primary");
            /**
             * parse the description html to String text
             */
            if(google_event.getDescription() != null) {
                reactCalendarEvent.setDescription(Jsoup.parse(google_event.getDescription()).text());
            }
            reactCalendarEvent.setType(type);

            /**
             * Metadata will have other important info about the event
             */
            ReactCalendarEvent.Metadata metadata = new ReactCalendarEvent.Metadata();
            metadata.setColour(google_event.getColorId());
            metadata.setHangoutsLink(google_event.getHangoutLink());
            // set availability means free or busy status
            if(google_event.getTransparency()!=null && google_event.getTransparency().equalsIgnoreCase(Literal.OPAQUE)) {
                // transparency opaque means person has blocked calendar event as BUSY on the UI
                metadata.setAvailability(Literal.busy);
            }
            if(google_event.getTransparency()!=null && google_event.getTransparency().equalsIgnoreCase(Literal.TRANSPARENT)) {
                // transparency opaque means person has blocked calendar event as FREE on the UI
                metadata.setAvailability(Literal.free);
            }
            reactCalendarEvent.setMetadata(metadata);

            converted_events.add(reactCalendarEvent);
        });
        return converted_events;
    }

    public static void validateFilterCalendarEventsRequest(Map<String, Object> req_map) throws RequestValidationException {
        List<String> emails;
        DateTime start_time;
        DateTime end_time;
        boolean availability;
        try{
            emails = (List<String>) req_map.get(Literal.emails);
            start_time = new DateTime(req_map.get(Literal.start_time).toString());
            end_time = new DateTime(req_map.get(Literal.end_time).toString());
            if(!Utility.isEmptyString(req_map.get(Literal.availability))) {
                availability = Boolean.parseBoolean(req_map.get(Literal.availability).toString());
            }
        } catch (Exception e) {
            throw new RequestValidationException(e.getMessage());
        }
    }

    /**
     * the will filter and include only those events that have the keywords
     * @return
     */
    public static List<Event> filterByEventKeywords(List<Event> events, List<String> keywords) {
        // todo null pointer exception
        List<Event> ret_events = new ArrayList<Event>();
        // make keywords lowercase
        final List<String> lowercase_keywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toList());
        events.forEach(event -> {
            if(event.getSummary() != null && !Utility.isEmptyString(event.getSummary())) {
                List<String> words_in_event_title = Arrays.asList(event.getSummary().split(" "));

                for (String word: words_in_event_title) {
                    if(lowercase_keywords.contains(word.toLowerCase())){
                        ret_events.add(event);
                    }
                    break;
                }
//                words_in_event_title.forEach(word -> {
//                    if(lowercase_keywords.contains(word.toLowerCase())){
//                        ret_events.add(event);
//                    }
//                });
            }
        });
        return ret_events;
    }

    public static List<Event> filterByAvailability(List<Event> events) {
        return events.parallelStream().filter(event -> event.getTransparency().equalsIgnoreCase("transparent")).collect(Collectors.toList());
    }

    public Map<String, Object> initService() {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.AUTH_LINK, GoogleCredentialHelper.getAuthorizationLink());
            return return_map;
        } catch (IOException e) {
            // todo :: improve this, kill jetty process on port 8888 before even trying to start jetty
            // in case already authorised and port in use send already existing auth link
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.AUTH_LINK, GoogleCredentialHelper.AUTH_LINK);
            return return_map;
        } catch (GeneralSecurityException | InterruptedException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getLocalizedMessage());
            return return_map;
        }
    }

    public FreeBusyResponse getFreeBusy(String email, DateTime startTime, DateTime endTime) throws GeneralSecurityException, IOException {
        return CalendarHelper.getFreeBusy(new ArrayList<String>(Collections.singletonList(email)), startTime, endTime);
    }

    public List<String> filterBusyEmails(List<String> email_list, DateTime startTime, DateTime endTime) throws GeneralSecurityException, IOException {
        FreeBusyResponse freeBusyResponse = CalendarHelper.getFreeBusy(email_list, startTime, endTime);
        Map<String, FreeBusyCalendar> calendars = freeBusyResponse.getCalendars();
        if(!calendars.isEmpty()) {
            // if the calendars.email.busy is not empty in calendars, means the person is busy, so remove from the list
            calendars.forEach((k, v) -> {
                FreeBusyCalendar freeBusyCalendar = calendars.get(k);
                if (!freeBusyCalendar.getBusy().isEmpty()) {
                    email_list.remove(k);
                }
            });
        }
        return email_list;
    }

    public List<Event> getEvents(String email, DateTime start_time, DateTime end_time) throws GeneralSecurityException, IOException {
        return CalendarHelper.getEvents(email, start_time, end_time);
    }

    public String getPhotoUrl(String email) throws GeneralSecurityException, IOException {
        return CalendarHelper.getPhotoUrl(email);

    }
}