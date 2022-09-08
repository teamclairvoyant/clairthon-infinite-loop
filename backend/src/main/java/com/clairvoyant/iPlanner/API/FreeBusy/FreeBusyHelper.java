package com.clairvoyant.iPlanner.API.FreeBusy;

import com.clairvoyant.iPlanner.CalendarQuickstart;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreeBusyHelper {

    private static Credential CREDENTIAL;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/OAuth.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the service-account.json file cannot be found.
     */
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    /**
     * Returns an authorized Credential link for user.
     */
    public static String getAuthorizationLink() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        AuthorizationCodeInstalledApp authorizationCodeInstalledApp = new AuthorizationCodeInstalledApp(flow, receiver);
        Credential credential = authorizationCodeInstalledApp.getFlow().loadCredential("user");
        String redirectUri = receiver.getRedirectUri();
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);
        new Thread(() -> {
            // receive authorization code and exchange it for an access token
            String code = null;
            try {
                code = receiver.waitForCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TokenResponse response = null;
            try {
                response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // store credential and return it
            try {
                CREDENTIAL = flow.createAndStoreCredential(response, "user");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        return authorizationUrl.build();
    }

    public static Calendar getCalendarClient() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
        return service;
    }

    public static Calendar getCalendarClientV2() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIAL).setApplicationName(APPLICATION_NAME).build();
        return service;
    }

    public static String getFreeBusy(String email) throws IOException, GeneralSecurityException {

        Calendar calendarClient = getCalendarClientV2();
        FreeBusyRequest req = new FreeBusyRequest();
        DateTime startTime = new DateTime("2022-09-08T00:00:00.000Z");
        DateTime endTime = new DateTime("2022-09-13T24:59:59.000Z");
        req.setTimeMin(startTime);
        req.setTimeMax(endTime);
        List<FreeBusyRequestItem> items = new ArrayList<>();
        FreeBusyRequestItem id = new FreeBusyRequestItem().setId(email);
        items.add(id);
        req.setItems(items);

        Calendar.Freebusy.Query fbq = calendarClient.freebusy().query(req);
        FreeBusyResponse fbresponse = fbq.execute();
        System.out.println("Getting freeBusy response");
        System.out.println(fbresponse.toString());
        return fbresponse.toString();
    }

}
