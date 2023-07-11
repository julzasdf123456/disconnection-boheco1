package com.lopez.julz.disconnection.api;

public class BaseURL {
    public static String baseUrl() {
//        return "http://203.177.135.179:8443/crm-noneco/public/api/";
        return "http://192.168.2.12/crm-noneco/public/api/";
    }

    public static String baseUrl(String ip) {
        return "http://" + ip + "/crm-boheco1/public/api/";
    }
}
