import java.io.*;
import java.net.*;
import java.nio.file.*;

public class HTTPServer {
    public static void main(String[] args) throws IOException {
        int port = 8070;
        // Create a new ServerSocket object to listen for incoming client connections on the specified port
        try (
        ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                // Wait for a client to connect and accept the connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                //Read the HTTP request from the client
                handleRequest(clientSocket);

                // Close the client socket and print a message indicating that the client has disconnected
                clientSocket.close();
                System.out.println("Client disconnected");
            }
        }
    }

    /** 
    * handles an HTTP request sent by a client to the server.
    * @param clientSocket representing the client connection
    * @throws IOException if the clientSocket is not valid or is closed.
    * @return serving the requested file or a 404 response
    */
    private static void handleRequest(Socket clientSocket) throws IOException {
        // Read the HTTP request from the client
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String request = in.readLine();
        System.out.println("Received request: " + request);
    
        // Get the output stream to write the HTTP response to the client
        OutputStream out = clientSocket.getOutputStream();
    
        // Check if the request is for one of the available files
        if (request.startsWith("GET /index.html")) {
            serveFile("index.html", "text/html", out);
        } else if (request.startsWith("GET /QSudoku.jpg")) {
            serveFile("QSudoku.jpg", "image/jpeg", out);
        } else if (request.startsWith("GET /SSudoku.jpg")) {
            serveFile("SSudoku.jpg", "image/jpeg", out);
        } else if (request.startsWith("GET /QHexSudoku.jpg")) {
            serveFile("QHexSudoku.jpg", "image/jpeg", out);
        } else if (request.startsWith("GET /QHexSudoku.jpg")) {
            serveFile("QHexSudoku.jpg", "image/jpeg", out);
        } else {
            // If the request is for an unknown resource, serve an HTTP 404 response
            serve404(out);
        }
    }

    /** 
    * Writing the file's contents to an output stream along with an HTTP response 
    * @param filename arepresenting the name of the file to be served 
    * @param contentType representing the content type of the file
    * @param out represents the output stream to which the file contents will be written
    * @throws IOException if the file does not exist 
    * @return writes an HTTP response header to out
    */
    private static void serveFile(String filename, String contentType, OutputStream out) throws IOException {
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Content-Length: " + data.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(data);
        out.flush();
    }

    /** 
    * Sends an HTTP 404 response to the client that the requested resource was not found on the server.
    * @param out represents the output stream 
    * @throws IOException if the file does not exist 
    * @return Sends an HTTP 404 "Not Found" response to a client.
    */
    private static void serve404(OutputStream out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/plain");
        writer.println();
        writer.println("404 Not Found");
        writer.flush();
    }
}