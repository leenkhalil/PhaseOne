
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * A class that serves puzzle images via HTTP.
 */
public class PuzzleServer {
    private static final int PORT = 8080;
    private static final String BLANK_PUZZLE_PATH = "puzzle1Unsolved.png";
    private static final String SOLVED_PUZZLE_PATH = "puzzle1Solved.png";



    /*Method Name: main()
     * Starts the HTTP server on the default port and creates contexts for serving blank and solved puzzles.
    * parameter type: array of string
    * @param args command line arguments, not used
    * @throws IOException if an error occurs while starting the HTTP server
    * @return None
    */
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/blank", new PuzzleHandler(new File(BLANK_PUZZLE_PATH)));
        server.createContext("/solved", new PuzzleHandler(new File(SOLVED_PUZZLE_PATH)));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    /**
     * An HTTP handler that serves a puzzle image file in response to a request.
     */  
    static class PuzzleHandler implements HttpHandler {
        private final File puzzleFile;

    /*Method Name: PuzzleHandler
    * Constructs a new PuzzleHandler object with the specified puzzle file.
    * parameter type: File
    * @param puzzleFile the file object representing the puzzle image to be served
    * @throws None
    * @return None
    */
        public PuzzleHandler(File puzzleFile) {
            this.puzzleFile = puzzleFile;
        }
    /*Method Name: handle()
    * Serves the puzzle image file in response to an HTTP request.
    * parameter type: HttpExchange
    * @param exchange the HttpExchange object representing the request and response
    * @throws IOException if an error occurs while reading the file or writing the response
    * @return None
    */
        public void handle(HttpExchange exchange) throws IOException {
            byte[] response = Files.readAllBytes(puzzleFile.toPath());
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        }
    }
}

