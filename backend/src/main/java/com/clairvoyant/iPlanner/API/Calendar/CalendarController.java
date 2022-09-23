package com.clairvoyant.iPlanner.API.Calendar;

import com.clairvoyant.iPlanner.API.APIEndpoints;
import com.clairvoyant.iPlanner.Exceptions.RequestValidationException;
import com.clairvoyant.iPlanner.Exceptions.TokenValidationException;
import com.clairvoyant.iPlanner.Services.TokenService;
import com.clairvoyant.iPlanner.Shared.DTO.ReactCalendarEvent;
import com.clairvoyant.iPlanner.Utility.Literal;
import com.clairvoyant.iPlanner.Utility.Utility;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping(APIEndpoints.CALENDAR)
public class CalendarController {

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @GetMapping("/test")
    public String testFreeBusy() {
        return "SUCCESS - FreeBusyController";
    }

    @PostMapping("/initialize")
    // TODO :: Allow the option to upload StoredCredentials file also directly along with OAuth file
    public Map<String, Object> initFreeBusyCredentials(@RequestParam(name = "file", required = false) MultipartFile file) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            if (file == null || file.isEmpty()) {
                throw new RequestValidationException("Please provide credentials.json file for Google OAuth");
            } else {
                Utility.saveCredentialsFile(file);
            }
            return CalendarService.getInstance().initService();
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }

    }

    /**
     * {
     * "email" : "abhinav.gogoi@clairvoyantsoft.com",
     * "start_time": "2022-09-13T12:00:00+05:30",
     * "end_time": "2022-09-13T15:59:00+05:30"
     * }
     *
     * @param req_map
     * @return
     */
    @PostMapping("/freeBusy")
    public String getFreeBusy(@RequestBody Map<String, Object> req_map) {
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            String email;
            DateTime start;
            DateTime end;
            try {
                email = req_map.get(Literal.email).toString();
                start = new DateTime(req_map.get(Literal.start_time).toString());
                end = new DateTime(req_map.get(Literal.end_time).toString());
            } catch (Exception e) {
                throw new RequestValidationException(e.getMessage());
            }
            return CalendarService.getInstance().getFreeBusy(email, start, end).toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * {
     * "emails" : ["abhinav.gogoi@clairvoyantsoft.com", "kedar.shivshette@clairvoyantsoft.com"],
     * "start_time": "2022-12-18T00:00:00",
     * "end_time": "2022-12-18T23:59:00"
     * }
     *
     * @param req_map
     * @return
     */
    @PostMapping("/filterBusyOnes")
    public Map<String, Object> filterBusyOnes(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            List<String> emails_list;
            DateTime start_time;
            DateTime end_time;
            try {
                emails_list = (List<String>) req_map.get(Literal.emails);
                start_time = new DateTime(req_map.get(Literal.start_time).toString());
                end_time = new DateTime(req_map.get(Literal.end_time).toString());
            } catch (Exception e) {
                throw new RequestValidationException(e.getMessage());
            }
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, CalendarService.getInstance().filterBusyEmails(emails_list, start_time, end_time));
            return return_map;
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }

    /**
     * {
     * "email" : "abhinav.gogoi@clairvoyantsoft.com",
     * "start_time": "2022-12-18T00:00:00",
     * "end_time": "2022-12-18T23:59:00"
     * }
     *
     * @param req_map
     * @return
     */
    @PostMapping("/events")
    public Map<String, Object> getCalendar(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            List<String> emails = new ArrayList<>();
            DateTime start_time;
            DateTime end_time;
            try {
                emails = (List<String>) req_map.get(Literal.emails);
                start_time = new DateTime(req_map.get(Literal.start_time).toString());
                end_time = new DateTime(req_map.get(Literal.end_time).toString());
            } catch (Exception e) {
                throw new RequestValidationException(e.getMessage());
            }
            List<Map<String, Object>> all_users_events = new ArrayList<>();

            for (String email : emails) {
                List<Event> events = null;
                try {
                    events = CalendarService.getInstance().getEvents(email, start_time, end_time);
                    List<ReactCalendarEvent> mapped_events = CalendarService.convertEventsToResource(events);
                    Map<String, Object> single_user_events = new HashMap<>();
                    single_user_events.put(email, mapped_events);
                    all_users_events.add(single_user_events);
                } catch (GeneralSecurityException | IOException e) {
                    logger.info("Could not get events for the user :: " + email);
                }
            }
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, all_users_events);
            return return_map;
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }

    /**
     * {
     * "emails" : ["abhinav.gogoi@clairvoyantsoft.com", "kedar.shivshette@clairvoyantsoft.com"],
     * "start_time": "2022-12-18T00:00:00+05:30",
     * "end_time": "2022-12-18T23:59:00+05:30",
     * "availability" : true,
     * "event_keywords" : ["Interview", "free time", "slot", "iPlanner"]
     * }
     *
     * @param req_map
     * @return
     */
    @PostMapping("/filter")
    public Map<String, Object> filterCalendarEvents(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            CalendarService.validateFilterCalendarEventsRequest(req_map);

            List<String> emails = (List<String>) req_map.get(Literal.emails);
            boolean availability = true;
            if (!Utility.isEmptyString(req_map.get(Literal.availability))) {
                availability = Boolean.parseBoolean(req_map.get(Literal.availability).toString());
            }

            List<String> event_keywords = null;
            if (req_map.get(Literal.event_keywords) != null) {
                event_keywords = (List<String>) req_map.get(Literal.event_keywords);
            }

            List<Map<String, Object>> all_users_events = new ArrayList<>();

            for (String email : emails) {
                List<Event> events = null;
                try {
                    events = CalendarService.getInstance().getEvents(email, new DateTime(req_map.get(Literal.start_time).toString()), new DateTime(req_map.get(Literal.end_time).toString()));

                    /**
                     * remove the events not matching the keywords
                     */
                    if (event_keywords != null) {
                        events = CalendarService.filterByEventKeywords(events, event_keywords);
                    }
                    /**
                     * keep only the 'free/transparent' events that do not block the calendar
                     */
//                    if (availability) {
//                        events = CalendarService.filterByAvailability(events);
//                    }
                    List<ReactCalendarEvent> mapped_events = CalendarService.convertEventsToResource(events);
                    Map<String, Object> single_user_events = new HashMap<>();
                    single_user_events.put(email, mapped_events);
                    all_users_events.add(single_user_events);
                } catch (GeneralSecurityException | IOException e) {
                    logger.info("Could not get events for the user :: " + email);
                }
            }

            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.DATA, all_users_events);
            return return_map;
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }


    /**
     * {
     * "title" : "Interview Scheduled",
     * "start_time": "2022-09-14T00:00:00+05:30",
     * "end_time": "2022-09-16T23:59:00+05:30",
     * "description" : "some html or text content",
     * "attendees" : ["abhinav.gogoi@clairvoyantsoft.com", "kedar.shivshette@clairvoyantsoft.com"]
     * }
     */
    @PostMapping("/createEvent")
    public Map<String, Object> createCalendarEvent(@RequestBody Map<String, Object> req_map) {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            /**
             * Check token
             */
            TokenService.getInstance().validateToken(request);
            /**
             * validate the request data
             */
            CalendarService.validateCreateCalendarEventRequest(req_map);
            /**
             * call the service
             */
            Event createdEvent = CalendarService.createCalendarEvent(req_map);
            /**
             * return success data
             */
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.MESSAGE, Literal.EVENT_CREATED_SUCCESSFULLY);
            return_map.put(Literal.DATA, createdEvent);
            return_map.put(Literal.EVENT, CalendarService.convertEventsToResource(new ArrayList<>(Collections.singleton(createdEvent))));
            return return_map;
        } catch (TokenValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.TOKEN_INVALID);
            return return_map;
        } catch (RequestValidationException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.REQUEST_VALIDATION_FAILED);
            return return_map;
        } catch (Exception e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.EXCEPTION, Literal.SOMETHING_WENT_WRONG);
            return return_map;
        }
    }
}
