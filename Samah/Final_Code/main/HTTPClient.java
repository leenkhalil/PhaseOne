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

    /** 
    * Send an HTTP GET request to a specified URL.
    * @param url represents the URL to which the GET request will be sent.
    * @throws IOException if there problem in network connectivity issues, connection timeouts.
    * @return returns the response as a String 
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
    /** 
    * Writes the specified content string to a file with the specified file name.
    * @param content represents the content to be written to the file.
    * @param fileName represents the name of the file to which the content will be written.
    * @throws IOException if there problem in writing the content to the file.
    * @return writes the specified content string to a file with the specified file name 
    */
    private static void writeToFile(String content, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    /** 
    * Opens a file in the default web browser
    * @param fileName represents the name of the file to be opened in the web browser.
    * @throws IOException if there problem in  while opening the file in the web browser.
    * @return boolean return True if the file was successfully opened
    *         returned False if an IOException occurred while attempting to open the file.
    */
    private static boolean openInBrowser(String fileName) throws IOException {
        try {
            Desktop.getDesktop().browse(Paths.get(fileName).toUri());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}