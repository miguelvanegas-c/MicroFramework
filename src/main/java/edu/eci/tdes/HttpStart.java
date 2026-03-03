package edu.eci.tdes;


import edu.eci.tdes.server.HttpServer;
import edu.eci.tdes.server.MicroFramework;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpStart {
    public static void main(String[] args) throws IOException, URISyntaxException {
        MicroFramework.main(args);
        HttpServer.main(args);
    }
}
