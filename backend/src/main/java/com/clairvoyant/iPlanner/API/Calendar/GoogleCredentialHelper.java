package com.clairvoyant.iPlanner.API.Calendar;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleCredentialHelper {

    private static Logger logger = LoggerFactory.getLogger(GoogleCredentialHelper.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static String AUTH_LINK = null;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final List<String> SCOPES = new ArrayList<>(CalendarScopes.all());
    private static final String CREDENTIALS_FILE_PATH = "/OAuth.json";
    public static NetHttpTransport HTTP_TRANSPORT = null;
    public static Calendar CALENDAR_CLIENT = null;
    public static Credential CREDENTIAL = null;

    /**
     * Returns an authorized Credential link for user.
     * Creates a StoredCredential in token folder
     */
    public static String getAuthorizationLink() throws IOException, GeneralSecurityException, InterruptedException {
        // Build a new authorized API client service.
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        InputStream fileInputStream = null;
        int t = 30;
        // try for 30 seconds/ 30 times
        while (t-- > 0 && fileInputStream == null) {
            Thread.sleep(1000);
            logger.info("Try count :: " + (30 - t) + "/30 time to get credentials file from resource");
            fileInputStream = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (fileInputStream != null) {
                logger.info("Credentials File found");
            }
        }
        if (fileInputStream == null) {
            logger.error("Credentials file not found at resource " + CREDENTIALS_FILE_PATH);
            throw new FileNotFoundException("Credentials file not found at resource " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(fileInputStream));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline").setApprovalPrompt("force").build();

        LocalServerReceiver receiver = new LocalServerReceiver
                .Builder()
                .setPort(8888)
                .build();
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
            // store credentials into StoredCredential file at tokens folder
            try {
                flow.createAndStoreCredential(response, "user");
                logger.info("Google Credentials created and saved ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        // set auth link
        AUTH_LINK = authorizationUrl.build();
        return AUTH_LINK;
    }

    /**
     * Get Calendar service client
     *
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static Calendar getCalendarClient() throws GeneralSecurityException, IOException {
        if (HTTP_TRANSPORT == null) {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }
        if (CREDENTIAL == null) {
            CREDENTIAL = GoogleCredentialHelper.getCredential(HTTP_TRANSPORT);
        }
        if (CALENDAR_CLIENT == null) {
            CALENDAR_CLIENT = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIAL).setApplicationName(APPLICATION_NAME).build();
        }
        return CALENDAR_CLIENT;
    }

    /**
     * Creates an authorized Credential object from saved File StoredCredential .
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential getCredential(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        DataStoreFactory dataStoreFactory = new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH));
        final StoredCredential storedCredential = StoredCredential.getDefaultDataStore(dataStoreFactory).get("user");

        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Credentials json file not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        final GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientSecrets)
                .build();

        if (storedCredential != null) {
            credential.setAccessToken(storedCredential.getAccessToken());
            credential.setRefreshToken(storedCredential.getRefreshToken());
            credential.setExpirationTimeMilliseconds(storedCredential.getExpirationTimeMilliseconds());
        }
        if (credential.refreshToken()) {
            logger.info("Refreshed GoogleCredential accessToken");
        }
        return credential;
    }
}
