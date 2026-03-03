package edu.eci.tdes.server;

import edu.eci.tdes.registry.RouteHandler;
import edu.eci.tdes.registry.RouteRegistry;
import edu.eci.tdes.registry.request.Request;
import edu.eci.tdes.registry.request.RequestImpl;
import edu.eci.tdes.registry.response.Response;
import edu.eci.tdes.registry.response.ResponseImpl;

import java.net.*;
import java.io.*;
import java.nio.file.Files;


import static edu.eci.tdes.registry.RouteRegistry.*;

public class HttpServer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String reqPath = "";
            String reqStruri = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (firstLine) {
                    String[] flTokens = inputLine.split(" ");
                    reqStruri = flTokens[1];
                    URI requri = new URI(reqStruri);
                    reqPath = requri.getPath();
                    System.out.println("Path: " + reqPath);

                    firstLine = false;
                }

                if (!in.ready()) {
                    break;
                }
            }
            if (RouteRegistry.exists(reqPath)) {
                RouteHandler handler = find(reqPath);
                Request req = new RequestImpl();
                Response res = new ResponseImpl();
                req.setValue(reqStruri);
                String response = handler.handle(req,res);
                outputLine
                        = "HTTP/1.1 200 OK\n\r"
                        + "Content-Type: text/html\n\r"
                        + "\n\r"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>PI</title>\n"
                        + "</head>"
                        + "<body>"
                        + response
                        + "</body>"
                        + "</html>";
            } else {
                if(reqPath.equals("/")) {
                    reqPath = "/index.html";
                }
                String type = getFileType(reqPath);
                String fileRoute = getFileRoute(); // ejemplo: target/classes/webroot
                System.out.println("Route: " + fileRoute + reqPath);
                File file = new File(fileRoute + reqPath);

                if (!file.exists()) {
                    outputLine = "HTTP/1.1 404 Not Found\r\n"
                            + "Content-Type: text/html\r\n\r\n"
                            + "<h1>404 File Not Found</h1>";
                } else {
                    if (type.equals("image/png")) {

                        byte[] bytes = Files.readAllBytes(file.toPath());
                        OutputStream rawOut = clientSocket.getOutputStream();

                        rawOut.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        rawOut.write(("Content-Type: " + type + "\r\n").getBytes());
                        rawOut.write("\r\n".getBytes());
                        rawOut.write(bytes);
                        rawOut.flush();

                        outputLine = null; // IMPORTANTE: no usar out.println después

                    } else {
                        BufferedReader fileReader = new BufferedReader(new FileReader(file));
                        String line;
                        StringBuilder content = new StringBuilder();

                        while ((line = fileReader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        fileReader.close();

                        outputLine = "HTTP/1.1 200 OK\r\n"
                                + "Content-Type: " + type + "\r\n\r\n"
                                + content.toString();
                    }
                }
            }
            if (outputLine != null) {
                out.println(outputLine);
            }
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }


    private static String getFileType(String fileName) {
        if (fileName.endsWith(".html")) {
            return  "text/html";
        } else if (fileName.endsWith(".css")) {
            return  "text/css";
        } else if (fileName.endsWith(".js")) {
            return  "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return  "image/png";
        } else {
            return  "text/html";
        }
    }

}
