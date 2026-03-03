package edu.eci.tdes.registry.request;

import java.util.HashMap;
import java.util.Map;

public class RequestImpl implements Request {
    private Map<String, String> queryParams = new HashMap<>();
    public String getValue(String key) {
        return queryParams.get(key);
    }

    public void setValue(String path) {
        if(path.contains("?")){
            path = path.substring(path.indexOf("?")+1);
            String[] values = path.split("&");
            for(String value : values){
                String[] keyValue = value.split("=");
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }

    }

}
