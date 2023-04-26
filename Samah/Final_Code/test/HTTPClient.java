import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HTTPClient {
    public static void main(String[] args) throws IOException {
        // Set the URL of the resource to be fetched
        String url = "http://localhost:8070/index.html";

        // Send an HTTP GET request to the specified URL and store the response as a string
        String response = sendGetRequest(url);

        // Write the response string to a file called "index.html"
        writeToFile(response, "index.html");

        // Open the "index.html" file in the default web browser
        openInBrowser("index.html");
    }

/*---------------------------------------Test strategy------------------------------------------- */
    /*Partition:
     * 1. Valid URL input
     * 
     *    Subdomains:
     *    1.0. URL is valid and returns a valid HTML page.
     *    1.1. URL is valid but returns a non-HTML page (e.g. image)
     *      
     *    Test cases:
     *    1. url = "http://localhost:8070/index.html" Cover Subdomain 1.0
     *    2. url = "http://localhost:8070/SSudoku.jpg" Cover Subdomain 1.1
     * 
     * 2. Invalid URL input
     * 
     *    Subdomains:
     *    2.0. URL is invalid (e.g. returns 404 error)
     * 
     *    Test cases:
     *    1. url = "http://localhost:8070/home.html" Cover Subdomain 2.0
     */
    private static String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /*
     * Partition: Valid input
     *
     * Subdomains:
     * 1.0. URL is valid and returns a valid HTML page.
     *
     * Test cases:
     * 1. url = "http://localhost:8070/index.html" Cover Subdomain 1.0
     */
    @Test
    public void testSendGetRequestWithValidUrlAndValidHtmlPage() throws IOException {
        // Arrange
        String url = "http://localhost:8070/index.html";

        // Act
        String response = HTTPClient.sendGetRequest(url);

        // Assert
        Assertions.assertTrue(response.contains("<html>"));
    }

    /*
     * Partition: Valid input
     *
     * Subdomains:
     * 1.1. URL is valid but returns a non-HTML page (e.g. image)
     *
     * Test cases:
     * 2. url = "http://localhost:8070/SSudoku.jpg" Cover Subdomain 1.1
     */
    @Test
    void testSendGetRequestWithValidUrlAndNonHtmlPage() throws IOException {
        // Arrange
        String url = "http://localhost:8070/SSudoku.jpg";

        // Act
        String response = HTTPClient.sendGetRequest(url);

        // Assert
        Assertions.assertFalse(response.contains("<html>"));
    }

    /*
     * Partition: Invalid input
     *
     * Subdomains:
     * 2.0. URL is invalid (e.g. returns 404 error)
     *
     * Test cases:
     *  1. url = "http://localhost:8070/home.html" Cover Subdomain 2.0
     */
    @Test
    void testSendGetRequestWithInvalidUrl() {
        // Arrange
        String url = "http://localhost:8070/home.html";

        // Act and assert
        Assertions.assertThrows(IOException.class, () -> HTTPClient.sendGetRequest(url));
    }
/*-------------------------------------------------------------------------------------------------- */


/*---------------------------------------Testing strategy------------------------------------------- */
    /*Partition:
     * 1. Valid input
     * 
     *    Subdomains:
     *    1.0. Content and file name are not null
     *    1.1. Content contains non-ASCII characters
     *    1.2. File name contains valid characters
     *      
     *    Test cases:
     *    1. File name ="index.html" Cover Subdomains 1.0 & 1.2
     *    2. This file "index.html" not contains any non-ASCII characters Cover Subdomain 1.1
     * 
     * 2. Invalid input
     * 
     *    Subdomains:
     *    2.0. Content is null
     *    2.1. File name is null
     *    2.2. File name contains too many characters > 255
     *    2.3. File name contains invalid characters (e.g. slashes, colons)
     * 
     *    Test cases:
     *    1. content = null cover Subdomain 2.0
     *    2. fileName = null cover Subdomain 2.1
     *    3. fileName = "this_is_a_really_long_file_name_that_is_over_255_characters_and_should_fail.html" cover Subdomain 2.2
     *    4. fileName = "test/invalid.html" cover Subdomain 2.3
     */
    private static void writeToFile(String content, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    /*
     * Partition: Valid input
     *
     * Subdomains:
     * 1.0. Content and file name are not null
     * 1.2. File name contains valid characters
     *
     * Test cases:
     * 1. File name ="index.html" Cover Subdomains 1.0 & 1.2
     */
    @Test
    public void testWriteToFileWithValidInput() throws IOException {
        // Arrange
        String content = "Hello, world!";
        String fileName = "index.html";

        // Act
        HTTPClient.writeToFile(content, fileName);

        // Assert that the file was written correctly
        Path path = Paths.get(fileName);
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals(content, Files.readString(path));
    }

    /*
     * Partition: Valid input
     *
     * Subdomains:
     * 1.1. Content contains non-ASCII characters
     *
     * Test cases:
     * 2. This file "index.html" not contains any non-ASCII characters Cover Subdomain 1.1
     */
    @Test
    public void testWriteToFileWithValidInputContainingNonASCIISubdomain() throws IOException {
        // Arrange
        String content = "こんにちは、世界！";
        String fileName = "index.html";

        // Act
        HTTPClient.writeToFile(content, fileName);

        // Assert that the file was written correctly
        Path path = Paths.get(fileName);
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals(content, Files.readString(path, StandardCharsets.UTF_8));
    }

    /*
     * Partition: Invalid input
     *
     * Subdomains:
     * 2.0. Content is null
     *
     * Test cases:
     * 1. content = null cover Subdomain 2.0
     */
    @Test
    public void testWriteToFileWithNullContentSubdomain() {
        // Arrange
        String content = null;
        String fileName = "index.html";

        // Act & Assert
        Assertions.assertThrows(NullPointerException.class, () -> HTTPClient.writeToFile(content, fileName));
    }

    /*Partition: Invalid input
     * 
     *    Subdomains:
     *    2.1. File name is null
     * 
     *    Test cases:
     *    2. fileName = null cover Subdomain 2.1
     */
    @Test
    public void testWriteToFileWithNullFileNameSubdomain() {
        // Arrange
        String content = "Hello, world!";
        String fileName = null;

        // Act & Assert
        Assertions.assertThrows(NullPointerException.class, () -> HTTPClient.writeToFile(content, fileName));
    }

    /*
     * Partition: Invalid input
     *
     * Subdomains:
     * 2.2. File name contains too many characters > 255
     *
     * Test cases:
     * 3. fileName = "this_is_a_really_long_file_name_that_is_over_255_characters_and_should_fail.html" cover Subdomain 2.2
     */
    @Test
    public void testWriteToFileWithTooLongFileNameSubdomain() {
        // Arrange
        String content = "Hello, world!";
        String fileName = "this_is_a_really_long_file_name_that_is_over_255_characters_and_should_fail.html";

        // Act & Assert
        Assertions.assertThrows(IOException.class, () -> HTTPClient.writeToFile(content, fileName));
    }

    /*
     * Partition: Invalid input
     *
     * Subdomains:
     * 2.3. File name contains invalid characters (e.g. slashes, colons)
     *
     * Test cases:
     * 4. fileName = "test/invalid.html" cover Subdomain 2.3
     */
    @Test
    void testWriteToFileWithFileNameContainingSlashes() {
        // Arrange
        String content = "Hello, world!";
        String fileName = "test/invalid.html";

        // Act and assert that an exception is thrown
        Assertions.assertThrows(IOException.class, () -> HTTPClient.writeToFile(content, fileName));
    }
/*--------------------------------------------------------------------------------------------------- */

/*---------------------------------------Testing strategy ------------------------------------------- */
    /*Partition:
     * 0. Null file name input
     * 
     * 1. Valid file name input
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
     *    1. fileName = "non-existent.html Cover Subdomain 2.0
     *    2. content = "This is not HTML content" 
     *       fileName = "text-file.txt" cover Subdomain 2.1
     *    3. fileName = "folder/index.html" cover Subdomain 2.2
     */
    private static void openInBrowser(String fileName) throws IOException{
        Desktop.getDesktop().browse(Paths.get(fileName).toUri());
    }
    /* 
    * 0. Null file name input
    */
    @Test
    public void testOpenInBrowserNullFileName() {
        // Call the openInBrowser method with a null file name input
        Assertions.assertThrows(NullPointerException.class, () -> {
            HTTPClient.openInBrowser(null);
        });
    }

    /* 
    1. Valid file name input
     *    Subdomains:
     *    1.0. File exists and is a valid HTML page
    */
    @Test
    public void testOpenInBrowserValidFileNameWithHTMLFileType() throws IOException {
        String content = "<html><body><h1>Hello, world!</h1></body></html>";
        String fileName = "test.html";

        HTTPClient.writeToFile(content, fileName);

        Assertions.assertDoesNotThrow(() -> {
            HTTPClient.openInBrowser(fileName);
        });
    }
    /* 
    * 2. Invalid file name input
    *       Subdomains:
    *       2.0. File does not exist
    */
    @Test
    public void testOpenInBrowserFileDoesNotExist() {
        // Call the openInBrowser method with a file name that does not exist
        Assertions.assertThrows(IOException.class, () -> {
            HTTPClient.openInBrowser("nonexistent.html");
        });
    }

    /* 
    * 2. Invalid file name input
    *       Subdomains:
    *       2.1. File exists but is not an HTML page
    */
    @Test
    public void testOpenInBrowserFileExistsButIsNotHTML() throws IOException {
        // Create a test text content string and file name
        String content = "This is a text file";
        String fileName = "test.txt";
        // Write the content to a file using the writeToFile method
        HTTPClient.writeToFile(content, fileName);
        // Call the openInBrowser method with the file name
        Assertions.assertThrows(IOException.class, () -> {
            HTTPClient.openInBrowser(fileName);
        });
    }
    /* 
    * 2. Invalid file name input
    *       Subdomains:
    *       2.2. File name contains invalid characters (e.g. slashes, colons)
    */
    @Test
    public void testOpenInBrowserInvalidFileNameWithInvalidCharacters() {
        // Call the openInBrowser method with an invalid file name input containing slashes and colons
        Assertions.assertThrows(IOException.class, () -> {
            HTTPClient.openInBrowser("C:/test.html");
        });
    }
}
/*--------------------------------------------------------------------------------------------------- */
