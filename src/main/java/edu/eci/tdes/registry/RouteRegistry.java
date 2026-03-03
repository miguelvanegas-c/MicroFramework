package edu.eci.tdes.registry;

import java.util.HashMap;
import java.util.Map;

public class RouteRegistry {
    public static Map<String, RouteHandler> routes = new HashMap<>();
    public static String fileRoute;

    public static RouteHandler get(String path, RouteHandler handler) {
        return routes.put(path,handler);
    }
    public static boolean exists(String path) {
        return routes.containsKey(path);
    }
    public static RouteHandler find(String path) {
        return routes.get(path);
    }

    public static void staticfiles(String path) {
        fileRoute = "src/"+ path;
    }
    public static String getFileRoute() {
        return fileRoute;
    }
}
