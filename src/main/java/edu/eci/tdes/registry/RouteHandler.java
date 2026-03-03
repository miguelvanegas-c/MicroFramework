package edu.eci.tdes.registry;


import edu.eci.tdes.request.Request;
import edu.eci.tdes.response.Response;

@FunctionalInterface
public interface RouteHandler {
    String handle(Request req, Response res);
}
