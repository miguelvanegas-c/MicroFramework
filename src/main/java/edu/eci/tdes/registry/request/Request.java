package edu.eci.tdes.registry.request;

public interface Request {
    String getValue(String key);
    void setValue(String path);


}
