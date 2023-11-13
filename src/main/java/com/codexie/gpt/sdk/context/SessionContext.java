package com.codexie.gpt.sdk.context;

import com.codexie.gpt.sdk.session.Session;
import com.codexie.gpt.sdk.session.SessionFactory;

import java.util.HashMap;
import java.util.Map;

public class SessionContext {

    Map<String, Session> factoryMap = new HashMap<>();

    public void addSession(String company,Session session){
        factoryMap.put(company,session);
    }

    public Session getSession(String company){
        return factoryMap.get(company);
    }


}
