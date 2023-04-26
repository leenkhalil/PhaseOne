import java.io.*;
import java.net.*;
import java.nio.file.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
/*---------------------------------------Test strategy------------------------------------------- */
    /**Partition: 
     * 0. Null socket input
     * 
     * 1. Valid socket input
     *    Subdomains:
     *    1.0. Valid GET request with valid resource path
     *    1.1. Valid GET request with invalid resource path
     *    1.2. Invalid HTTP method in request
     *    1.3. Invalid HTTP version in request
     *    1.4. Valid POST request with valid resource path
     *    1.5. Valid POST request with invalid resource path
     *    1.6. Invalid Content-Length header in request
     *
     *    Test cases:
     *    1. Valid GET request with valid resource path Cover Subdomain 1.0
     *    2. Valid GET request with invalid resource path Cover Subdomain 1.1
     *    3. Invalid HTTP method in request Cover Subdomain 1.2
     *    4. Invalid HTTP version in request Cover Subdomain 1.3
     *    5. Valid POST request with valid resource path Cover Subdomain 1.4
     *    6. Valid POST request with invalid resource path Cover Subdomain 1.5
     *    7. Invalid Content-Length header in request Cover Subdomain 1.6
     *  
     * 2. Invalid input
     * 
     *    Subdomains:
     *    2.0. The request is null or does not start with "GET /"
     *    2.1. The requested file name is null or empty
     *    2.2. The requested file name does not match any of the available files
     *    
     *    Test cases:
     *    1. Request = null covers subdomain 2.0
     *    2. Request = "POST /index.html" covers subdomain 2.0
     *    3. Request = "GET /" covers subdomains 2.1 and 2.2
     *    4. Request = "GET /invalid.html" covers subdomain 2.2
     *    5. Request = "GET /QSudoku.php" covers subdomain 2.2
    **/
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

    /* Partition:
     * 0. Null socket input
     */
    @Test
    public void testNullSocket() {
        Assertions.assertThrows(NullPointerException.class, () -> handleRequest(null));
    }

    /* Partition:
     * 1. Valid socket input
     * 
     *    Subdomains:
     *    1.0. Valid GET request with valid resource path
     *    1.1. Valid GET request with invalid resource path
     *    1.2. Invalid HTTP method in request
     *    1.3. Invalid HTTP version in request
     *    1.4. Valid POST request with valid resource path
     *    1.5. Valid POST request with invalid resource path
     *    1.6. Invalid Content-Length header in request
     *
     *    Test cases:
     *    1. Valid GET request with valid resource path
     *    2. Valid GET request with invalid resource path
     *    3. Invalid HTTP method in request
     *    4. Invalid HTTP version in request
     *    5. Valid POST request with valid resource path
     *    6. Valid POST request with invalid resource path
     *    7. Invalid Content-Length header in request
     */

    @Test
    public void testValidSocket() throws IOException {
        // Subdomain 1.0
        String request1 = "GET /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        InputStream inputStream1 = new ByteArrayInputStream(request1.getBytes());
        OutputStream outputStream1 = new ByteArrayOutputStream();
        Socket socket1 = createMockSocket(inputStream1, outputStream1);
        handleRequest(socket1);
        Assertions.assertFalse(outputStream1.toString().isEmpty());

        // Subdomain 1.1
        String request2 = "GET /invalid.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        InputStream inputStream2 = new ByteArrayInputStream(request2.getBytes());
        OutputStream outputStream2 = new ByteArrayOutputStream();
        Socket socket2 = createMockSocket(inputStream2, outputStream2);
        handleRequest(socket2);
        Assertions.assertFalse(outputStream2.toString().isEmpty());

        // Subdomain 1.2
        String request3 = "INVALID /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        InputStream inputStream3 = new ByteArrayInputStream(request3.getBytes());
        OutputStream outputStream3 = new ByteArrayOutputStream();
        Socket socket3 = createMockSocket(inputStream3, outputStream3);
        handleRequest(socket3);
        Assertions.assertFalse(outputStream3.toString().isEmpty());

        // Subdomain 1.3
        String request4 = "GET /index.html HTTP/2.0\r\nHost: localhost\r\n\r\n";
        InputStream inputStream4 = new ByteArrayInputStream(request4.getBytes());
        OutputStream outputStream4 = new ByteArrayOutputStream();
        Socket socket4 = createMockSocket(inputStream4, outputStream4);
        handleRequest(socket4);
        Assertions.assertFalse(outputStream4.toString().isEmpty());

        // Subdomain 1.4
        String request5 = "POST /index.html HTTP/1.1\r\nHost: localhost\r\nContent-Length: 10\r\n\r\n1234567890";
        InputStream inputStream5 = new ByteArrayInputStream(request5.getBytes());
        OutputStream outputStream5 = new ByteArrayOutputStream();
        Socket socket5 = createMockSocket(inputStream5, outputStream5);
        handleRequest(socket5);
        Assertions.assertFalse(outputStream5.toString().isEmpty());

        // Subdomain 1.5
        String request6 = "POST /invalid.html HTTP/1.1\r\nHost: localhost\r\nContent-Length: 10\r\n\r\n1234567890";
        InputStream inputStream6 = new ByteArrayInputStream(request6.getBytes());
        OutputStream outputStream6 = new ByteArrayOutputStream();
        Socket socket6 = createMockSocket(inputStream6, outputStream6);
        handleRequest(socket6);
        Assertions.assertFalse(outputStream6.toString().isEmpty());

        // Subdomain 1.6
        String request7 = "POST /index.html HTTP/1.1\r\nHost: localhost\r\nContent-Length: 100\r\n\r\n1234567890";
        InputStream inputStream7 = new ByteArrayInputStream(request7.getBytes());
        OutputStream outputStream7 = new ByteArrayOutputStream();
        Socket socket7 = createMockSocket(inputStream7, outputStream7);
        handleRequest(socket7);
        Assertions.assertFalse(outputStream7.toString().isEmpty());
    }

    private Socket createMockSocket(InputStream inputStream, OutputStream outputStream) throws IOException {
        Socket socket = new Socket() {
            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() {
                return outputStream;
            }
        };
        return socket;
    }
    /* Partition:
     * 2. Invalid socket input
     * 
     *    Subdomains:
     *    2.0. Socket input stream throws an IOException when read() method is called
     *
     *    Test case:
     *    1. Socket input stream is invalid and throws an IOException
 */
@Test
public void testInvalidSocketInputStream() throws IOException {
    // Subdomain 2.0
    InputStream inputStream = new InvalidInputStream();
    OutputStream outputStream = new ByteArrayOutputStream();
    Socket socket = createMockSocket(inputStream, outputStream);
    Assertions.assertThrows(IOException.class, () -> handleRequest(socket));
}

private static class InvalidInputStream extends InputStream {
    @Override
    public int read() throws IOException {
        throw new IOException("Invalid socket input stream");
    }
}

/*-------------------------------------------------------------------------------------------------- */

/*---------------------------------------Test strategy------------------------------------------- */
/**Partition: 
     * 0. Null file name input
     * 
     * 1. Valid input
     * 
     *    Subdomains:
     *    1.0. File exists and is a valid HTML page
     *      
     *    Test cases:
     *    1. content = "<html><body><h1>Hello, world!</h1></body></html>" Cover Subdomain 1.0
     *    2. fileName = "index.html" Cover Subdomain 1.1
     * 
     * 2. Invalid file name input
     * 
     *    Subdomains:
     *    2.0. File does not exist
     *    2.1. File exists but is not an HTML page
     *    2.2. File name contains invalid characters (e.g. slashes, colons)
     * 
     *    Test cases:
     *    1. fileName = "non-existent.html" Cover Subdomain 2.0
     *    2. content = "This is not HTML content" 
     *       fileName = "text-file.txt" cover Subdomain 2.1
     *    3. fileName = "folder/index.html" cover Subdomain 2.2
 **/
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

    /* Partition:
     * 0. Null file name input
    */
    @Test
    public void testNullFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Assertions.assertThrows(NullPointerException.class, () -> serveFile(null, "text/html", outputStream));
    }

/* Partition:
 * 1. Valid file name input
 *    Subdomains:
 *    1.0. File exists and is a valid HTML page
 *      
 *    Test cases:
 *    1. content = "<html><body><h1>Hello, world!</h1></body></html>" Cover Subdomain 1.0
 *    2. fileName = "index.html" Cover Subdomain 1.1
 */
@Test
public void testValidFileName() throws IOException {
    // Subdomain 1.0
    String content = "<html><body><h1>Hello, world!</h1></body></html>";
    Path tempFilePath = Files.createTempFile("test", ".html");
    Files.write(tempFilePath, content.getBytes());
    ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
    serveFile(tempFilePath.toAbsolutePath().toString(), "text/html", outputStream1);
    String response1 = outputStream1.toString();
    Assertions.assertTrue(response1.startsWith("HTTP/1.1 200 OK"));
    Assertions.assertTrue(response1.contains("Content-Type: text/html"));
    Assertions.assertTrue(response1.contains("Content-Length: " + content.length()));
    Assertions.assertTrue(response1.endsWith(content));
    Files.delete(tempFilePath);

    // Subdomain 1.1
    String fileName = "index.html";
    Path tempFilePath2 = Files.createTempFile(fileName.substring(0, fileName.indexOf(".")), fileName.substring(fileName.indexOf(".")));
    Files.write(tempFilePath2, content.getBytes());
    ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
    serveFile(tempFilePath2.toAbsolutePath().toString(), "text/html", outputStream2);
    String response2 = outputStream2.toString();
    Assertions.assertTrue(response2.startsWith("HTTP/1.1 200 OK"));
    Assertions.assertTrue(response2.contains("Content-Type: text/html"));
    Assertions.assertTrue(response2.contains("Content-Length: " + content.length()));
    Assertions.assertTrue(response2.endsWith(content));
    Files.delete(tempFilePath2);
}

/* Partition:
 * 2. Invalid file name input
 * 
 *    Subdomains:
 *    2.0. File does not exist
 *    2.1. File exists but is not an HTML page
 *    2.2. File name contains invalid characters (e.g. slashes, colons)
 * 
 *    Test cases:
 *    1. fileName = "non-existent.html" Cover Subdomain 2.0
 *    2. content = "This is not HTML content" 
 *       fileName = "text-file.txt" cover Subdomain 2.1
 *    3. fileName = "folder/index.html" cover Subdomain 2.2
 */
@Test
public void testInvalidFileName() throws IOException {
    // Subdomain 2.0
    String fileName = "non-existent.html";
    ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
    if (Files.exists(Paths.get(fileName))) {
        serveFile(fileName, "text/html", outputStream1);
        String response1 = outputStream1.toString();
        Assertions.assertTrue(response1.startsWith("HTTP/1.1 404 Not Found"));
        Assertions.assertTrue(response1.endsWith("\r\n"));
    } else {
        Assertions.fail("File not found: " + fileName);
    }

    // Subdomain 2.1
    String content = "This is not HTML content";
    Path tempFilePath2 = Files.createTempFile("test", ".txt");
    Files.write(tempFilePath2, content.getBytes());
    ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
    serveFile(tempFilePath2.toAbsolutePath().toString(), "text/html", outputStream2);
    String response2 = outputStream2.toString();
    Assertions.assertTrue(response2.startsWith("HTTP/1.1 415 Unsupported Media Type"));
    Assertions.assertTrue(response2.endsWith("\r\n"));
    Files.delete(tempFilePath2);

    // Subdomain 2.2
    fileName = "folder/index.html";
    ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
    serveFile(fileName, "text/html", outputStream3);
    String response3 = outputStream3.toString();
    Assertions.assertTrue(response3.startsWith("HTTP/1.1 400 Bad Request"));
    Assertions.assertTrue(response3.endsWith("\r\n"));
}
/*-------------------------------------------------------------------------------------------------- */

/*------------------------------------------Test strategy------------------------------------------- */
/**Partition: 
     * 0. Null output stream input
     * 
     * 1. Valid output stream input
     *    Subdomains:
     *    1.0. Output stream is not closed after method call
     *
     *    Test cases:
     *    1. ByteArrayOutputStream with size > 0 Cover Subdomain 1.0
     *    2. ByteArrayOutputStream with size = 0 Cover Subdomain 1.0
     * 
     * 2. Invalid output stream input
     * 
     *    Subdomains:
     *    2.0: The output stream throws an IOException when the write method is called.
     * 
     *    Test cases:
     *    1. Output stream is invalid and throws an IOException cover Subdomain 2.0
 **/
    private static void serve404(OutputStream out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/plain");
        writer.println();
        writer.println("404 Not Found");
        writer.flush();
    }

    /* Partition:
     * 0. Null output stream input
     */
    @Test
    public void testNullOutputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serve404(outputStream);
        String response = outputStream.toString();
        Assertions.assertTrue(response.startsWith("HTTP/1.1 404 Not Found"));
        Assertions.assertTrue(response.endsWith("\r\n"));
    }

    /* Partition:
     * 1. Valid output stream input
     *    Subdomains:
     *    1.0. Output stream is not closed after method call
     *
     *    Test cases:
     *    1. ByteArrayOutputStream with size > 0 Cover Subdomain 1.0
     *    2. ByteArrayOutputStream with size = 0 Cover Subdomain 1.0
     */

    @Test
    public void testValidOutputStream() throws IOException {
        // Subdomain 1.0
        OutputStream outputStream1 = new ByteArrayOutputStream();
        serve404(outputStream1);
        Assertions.assertFalse(outputStream1.toString().isEmpty());

        // Subdomain 1.0
        OutputStream outputStream2 = new ByteArrayOutputStream(0);
        serve404(outputStream2);
        Assertions.assertFalse(outputStream2.toString().isEmpty());
    }

    /* Partition:
     * 2. Invalid output stream input
     */

    @Test
    public void testInvalidOutputStream() throws IOException {
        // Subdomain 2.0
        OutputStream outputStream = new InvalidOutputStream();
        serve404(outputStream);
    }

    private static class InvalidOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            throw new IOException("Invalid output stream");
        }
    }
}
/*-------------------------------------------------------------------------------------------------- */
