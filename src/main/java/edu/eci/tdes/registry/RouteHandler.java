package edu.eci.tdes.registry;


import edu.eci.tdes.registry.request.Request;
import edu.eci.tdes.registry.response.Response;

@FunctionalInterface
public interface RouteHandler {
    String handle(Request req, Response res);
}
