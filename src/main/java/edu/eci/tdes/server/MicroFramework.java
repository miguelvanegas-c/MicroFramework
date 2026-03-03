package edu.eci.tdes.server;
import static edu.eci.tdes.registry.RouteRegistry.get;
import static edu.eci.tdes.registry.RouteRegistry.staticfiles;

public class MicroFramework {

    public static void main(String[] args) {
        get("/pi",(req,res)-> "PI = " + Math.PI);
        get("/hello", (req, res) -> "hello " + req.getValue("name"));
        staticfiles("webroot/public");
    }
}

