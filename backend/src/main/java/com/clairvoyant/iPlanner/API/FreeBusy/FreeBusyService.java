package com.clairvoyant.iPlanner.API.FreeBusy;

import com.clairvoyant.iPlanner.Utility.Literal;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FreeBusyService {

    private static FreeBusyService instance;

    /**
     * Singleton class Lazy initialization with Double check locking
     *
     * @return FreeBusyService
     */
    public static FreeBusyService getInstance() {
        if (instance == null) {
            synchronized (FreeBusyService.class) {
                instance = new FreeBusyService();
            }
        }
        return instance;
    }

    /**
     * private constructor for singleton class
     */
    private FreeBusyService() {
    }

    public Map<String, Object> initService() {
        Map<String, Object> return_map = new HashMap<>(Literal.SIX);
        try {
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.AUTH_LINK, FreeBusyHelper.getAuthorizationLink());
            return return_map;
        } catch (IOException e) {
            // todo :: improve this, kill jetty process on port 8888 before even trying to start jetty
            // in case already authorised and port in use send already existing auth link
            return_map.put(Literal.STATUS, Literal.SUCCESS);
            return_map.put(Literal.MESSAGE, e.getMessage());
            return_map.put(Literal.AUTH_LINK, FreeBusyHelper.AUTH_LINK);
            return return_map;
        } catch (GeneralSecurityException e) {
            return_map.put(Literal.STATUS, Literal.ERROR);
            return_map.put(Literal.MESSAGE, Literal.SOMETHING_WENT_WRONG);
            return_map.put(Literal.EXCEPTION, e.getLocalizedMessage());
            return return_map;
        }
    }

    public String getFreeBusy(String email) throws GeneralSecurityException, IOException {
        return FreeBusyHelper.getFreeBusy(email);
    }
}
