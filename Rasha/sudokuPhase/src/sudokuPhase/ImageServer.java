package sudokuPhase;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import javax.imageio.ImageIO;

public class ImageServer {
    /**
 * Starts the image server on port 8080.
 *
 * @param args The command-line arguments (not used).
 *
 * @throws IOException if there is an error creating the server.
 */
    public static void main(String[] args) throws IOException {
        //initializes an HttpServer instance.
        
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        //sets up context handlers for /image and / requests,
        server.createContext("/image", new ImageHandler());
        server.createContext("/", new IndexHandler());
        //starts the server and outputs a message to the console.   
        server.setExecutor(null);
        server.start();

        System.out.println("Server started");
    }

    static class ImageHandler implements HttpHandler {
         /**
     * Handles HTTP requests to the /image endpoint by returning the image specified by the "image" query parameter.
     *
     * @param exchange The HttpExchange object representing the incoming HTTP request and response.
     * 
     * @throws IOException if there is an error reading the image file or writing the response.
     */
        @Override
        //The handle() method is called when a request is received by the server that matches the context of this handler (in this case, requests to the /image endpoint).
        public void handle(HttpExchange exchange) throws IOException {//The exchange parameter is an object that represents the HTTP request and response. The handler can use methods of this object to get information about the request (e.g. query parameters) and send a response back to the client (e.g. with a status code and response body).
            // Get requested image name from query parameter
            //The getRequestURI() method of the HttpExchange object returns a URI object representing the request URI. The getQuery() method of the URI object returns the query string of the URI (i.e. the part after the ? character), which in this case should be something like image=image1.
            //The split("=")[1] call splits the query string by the = character, which separates the parameter name from the value. The [1] index returns the second element of the resulting array, which should be the value of the image parameter (e.g. image1).
            String imageName = exchange.getRequestURI().getQuery().split("=")[1];

            // Load image from file
            BufferedImage image = ImageIO.read(new File("C:\\Users\\rasha\\Desktop\\" + imageName + ".png"));

            // Convert image to byte array and send response
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageData = baos.toByteArray();
            exchange.sendResponseHeaders(200, imageData.length);
            OutputStream os = exchange.getResponseBody();
            os.write(imageData);
            os.close();
        }
    }

    static class IndexHandler implements HttpHandler {
/**
 * Handles requests for images.
 * 
 * @param exchange the HttpExchange object representing the client's request and the server's response
 * @throws IOException if an I/O error occurs while processing the request
 */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Serve HTML page with links to the two images
           String response = "<!DOCTYPE html>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <title>Select the sudoku</title>\n" +
        "    <style>\n" +
        "      body {\n" +
        "        font-family: Arial, sans-serif;\n" +
        "        background-color: #f1f1f1;\n" +
        "      }\n" +
        "      h1 {\n" +
        "        color: #007bff;\n" +
        "        margin-top: 50px;\n" +
        "        text-align: center;\n" +
        "      }\n" +
        "      a {\n" +
        "        display: block;\n" +
        "        text-align: center;\n" +
        "        font-size: 24px;\n" +
        "        color: #fff;\n" +
        "        background-color: #007bff;\n" +
        "        border-radius: 10px;\n" +
        "        padding: 10px;\n" +
        "        width: 200px;\n" +
        "        margin: 0 auto;\n" +
        "        margin-top: 30px;\n" +
        "        text-decoration: none;\n" +
        "      }\n" +
        "      a:hover {\n" +
        "        background-color: #0056b3;\n" +
        "      }\n" +
        "    </style>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <h1>Select the sudoku</h1>\n" +
        "    <div class=\"image-links\">\n" +
        "      <a href=\"/image?image=solved\">Solved Sudoku</a>\n" +
        "      <a href=\"/image?image=notsolved\">Unsolved Sudoku</a>\n" +
        "    </div>\n" +
        "  </body>\n" +
        "</html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
