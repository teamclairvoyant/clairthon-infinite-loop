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
        synchronized (FreeBusyService.class) {
            if (instance == null) {
                instance = new FreeBusyService();
            }
            return instance;
        }
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
        } catch (Exception e) {
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
