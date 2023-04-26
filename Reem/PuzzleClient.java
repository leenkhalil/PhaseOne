
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PuzzleClient extends JFrame implements ActionListener {
    private static final String BLANK_PUZZLE_URL = "http://localhost:8080/blank";
    private static final String SOLVED_PUZZLE_URL = "http://localhost:8080/solved";
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 500;
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 400;
    private static final String TITLE = "Puzzle Pictures";

    private final JLabel puzzleLabel;

    /*
     * Method Name: PuzzleClient()
     * Creates a new PuzzleClient window with buttons for getting a blank or solved
     * puzzle image.
     * Method Parameters: None
     * 
     * @param None
     * 
     * @throws None
     * 
     * @return None
     */
    public PuzzleClient() {
        /*
         * Testing Strategy:
         * Subdomains:
         * 
            * Creating the JFrame window and its properties
            * Creating the container for the JFrame window
            * Creating the label for the puzzle image
            * Creating the button panel for the JFrame window
            * Creating the "Blank Puzzle" button and its properties
            * Creating the "Solved Puzzle" button and its properties
            * Adding the button panel and the puzzle label to the container
         * Test cases:
         * 
            * Test case 1: Verify that the JFrame window is created with the correct title
                * and dimensions.
            * Test case 2: Verify that the container for the JFrame window is created.
            * Test case 3: Verify that the label for the puzzle image is created with the
                * correct dimensions.
            * Test case 4: Verify that the button panel for the JFrame window is created.
            * Test case 5: Verify that the "Blank Puzzle" button is created with the
                * correct text and action command.
            * Test case 6: Verify that the "Solved Puzzle" button is created with the
                * correct text and action command.
            * Test case 7: Verify that the button panel and the puzzle label are added to
                * the container correctly.

         * Coverage:
            * all test cases together covers 100% statement coverage
         */

        setTitle(TITLE);
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        puzzleLabel = new JLabel();
        puzzleLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        JPanel buttonPanel = new JPanel();
        JButton blankButton = new JButton("Blank Puzzle");
        blankButton.setActionCommand("blank");
        blankButton.addActionListener(this);
        buttonPanel.add(blankButton);

        JButton solvedButton = new JButton("Solved Puzzle");
        solvedButton.setActionCommand("solved");
        solvedButton.addActionListener(this);
        buttonPanel.add(solvedButton);

        contentPane.add(buttonPanel, BorderLayout.NORTH);
        contentPane.add(puzzleLabel, BorderLayout.CENTER);
    }

    /*
     * Method Name: actionPerformed()
     * Handles button clicks for getting a blank or solved puzzle image.\
     * parameter type: ActionEvent
     * 
     * @param e represent an ActionEvent object that is generated when the user
     * performs an action(clicking a button).
     * 
     * @throws None
     * 
     * @return None
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /*
         * Testing Strategy:        
         * Partitions:
            * The action event object generated by clicking a button
         * Subdomains:
            * Clicking the "Blank Puzzle" button
            * Clicking the "Solved Puzzle" button
            * HTTP connection to the blank puzzle URL
            * HTTP connection to the solved puzzle URL
         * Test cases:
            * Test case 1: Verify that clicking the "Blank Puzzle" button sets the correct
            * URL and gets the image correctly.
            * Test case 2: Verify that clicking the "Solved Puzzle" button sets the correct
            * URL and gets the image correctly.
            * Test case 3: Verify that the HTTP connection to the blank puzzle URL is
            * established correctly.
            * Test case 4: Verify that the HTTP connection to the solved puzzle URL is
            * established correctly.
            * Test case 5: Verify that the puzzle label icon is set to the image correctly.

         * Coverage:
            * the 5 test cases cover 100% branch coverage 
         */

        String actionCommand = e.getActionCommand();
        String url = null;
        if ("blank".equals(actionCommand)) {
            url = BLANK_PUZZLE_URL;
        } else if ("solved".equals(actionCommand)) {
            url = SOLVED_PUZZLE_URL;
        }

        if (url != null) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                Image image = ImageIO.read(connection.getInputStream());
                puzzleLabel.setIcon(new ImageIcon(image));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
     * Method Name: main()
     * Creates and displays a PuzzleClient window.
     * parameter type: array of string
     * 
     * @param args the command-line arguments (not used)
     * 
     * @throws None
     * 
     * @return None
     */
    public static void main(String[] args) {

        /*
         * Testing Strategy:

         * Subdomains:
            * Creating the PuzzleClient object
            * Making the PuzzleClient object visible
         * Test cases:
            * Test case 1: Verify that the PuzzleClient object is created correctly.
            * Test case 2: Verify that the PuzzleClient object is made visible correctly.
         * Coverage:
            * the two test cases cover 100% statement coverage
         */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PuzzleClient client = new PuzzleClient();
                client.setVisible(true);
            }
        });
    }
}
