package com.clairvoyant.iPlanner.API.FreeBusy;

import com.clairvoyant.iPlanner.CalendarQuickstart;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleCredentialHelper {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static String AUTH_LINK = null;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/OAuth.json";
    public static NetHttpTransport HTTP_TRANSPORT = null;

    /**
     * Returns an authorized Credential link for user.
     * Creates a StoredCredential in token folder
     */
    public static String getAuthorizationLink() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Credentials json file not found: " + CREDENTIALS_FILE_PATH);
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
            // store credentials into StoredCredential file
            try {
                flow.createAndStoreCredential(response, "user");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        // set auth link
        AUTH_LINK = authorizationUrl.build();
        return AUTH_LINK;
    }

    public static Calendar getCalendarClientV2() throws GeneralSecurityException, IOException {
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleCredentialHelper.getCredential(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Creates an authorized Credential object from saved File StoredCredential .
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential getCredential(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        DataStoreFactory dataStoreFactory = new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH));
        final GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .build();
        final StoredCredential storedCredential = StoredCredential.getDefaultDataStore(dataStoreFactory).get("user");
        if (storedCredential != null) {
            credential.setAccessToken(storedCredential.getAccessToken());
            credential.setRefreshToken(storedCredential.getRefreshToken());
            credential.setExpirationTimeMilliseconds(storedCredential.getExpirationTimeMilliseconds());
        }
        credential.refreshToken();
        return credential;
    }
}
