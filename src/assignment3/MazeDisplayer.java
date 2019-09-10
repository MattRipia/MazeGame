package assignment3;

// Made by Matt Ripia - 1385931

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class MazeDisplayer extends JPanel implements ActionListener
{
    // model
    private Maze maze;
    private KeyboardMouse playerMouse;
    private RandomMouse[] randomMice;
    private SmartMouse    smartMouse;
    
    //view
    private DrawPanel drawPanel;
    private JButton createRandomMazeButton, loadMazeButton, infoButton;
    private Timer timer;
    private String[] choices = {  "large", "enormous", "medium", "small", "tiny"};
    private JSlider randomMouseSpeed, smartMouseSpeed;
    private JCheckBox showHistory;
    
    public MazeDisplayer()
    {
        super(new BorderLayout());
        drawPanel = new DrawPanel();
        timer = new Timer(15, this);
        
        // model initialization
        maze = new Maze(0, 0);
        playerMouse = new KeyboardMouse(maze, 0, 0, 0);
        smartMouse =  new SmartMouse(maze, 0, 0, 0);
        randomMice = new RandomMouse[1000];

        // Speed Slider
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(50, new JLabel("Fast"));
        labels.put(275, new JLabel("Medium"));
        labels.put(500, new JLabel("Slow"));
        
        randomMouseSpeed = new JSlider(JSlider.VERTICAL, 50, 500, 300);
        randomMouseSpeed.setInverted(true);
        randomMouseSpeed.setPaintTrack(false);
        randomMouseSpeed.setBorder(BorderFactory.createTitledBorder("Random Mice Speed"));
        randomMouseSpeed.setMinorTickSpacing(50);
        randomMouseSpeed.setMajorTickSpacing(100);
        randomMouseSpeed.setPaintLabels(true);  
        randomMouseSpeed.setPaintTicks(true);  
        randomMouseSpeed.addChangeListener(e -> updateSpeed());
        randomMouseSpeed.setLabelTable(labels);
        randomMouseSpeed.setBackground(new Color(240, 240, 240));
        
        smartMouseSpeed = new JSlider(JSlider.VERTICAL, 50, 500, 300);
        smartMouseSpeed.setInverted(true);
        smartMouseSpeed.setPaintTrack(false);
        smartMouseSpeed.setSize(200, 200);
        smartMouseSpeed.setBorder(BorderFactory.createTitledBorder("Smart Mouse Speed"));
        smartMouseSpeed.setMinorTickSpacing(50);
        smartMouseSpeed.setMajorTickSpacing(100);
        smartMouseSpeed.setPaintLabels(true);  
        smartMouseSpeed.setPaintTicks(true);  
        smartMouseSpeed.addChangeListener(e -> updateSpeed());
        smartMouseSpeed.setLabelTable(labels);
        smartMouseSpeed.setBackground(new Color(240, 240, 240));
        
        // Load Maze button
        loadMazeButton = new JButton("Load Maze from DB");
        loadMazeButton.addActionListener(this);
        
        // Build path button
        createRandomMazeButton = new JButton("Create Random Maze");
        createRandomMazeButton.addActionListener(this);
        
        // info button
        infoButton = new JButton("Info");
        infoButton.addActionListener(this);
        
        // history button
        showHistory = new JCheckBox("Show Smart Mouse Path");
        showHistory.setSelected(true);

        
        // the button panel at the bottom
        JPanel insideWestPanel = new JPanel(new GridLayout(7,1));
        insideWestPanel.add(new JLabel("                                               "));
        insideWestPanel.add(new JLabel("                                               "));
        insideWestPanel.add(infoButton);
        insideWestPanel.add(createRandomMazeButton);
        insideWestPanel.add(loadMazeButton);
        insideWestPanel.add(showHistory);
        insideWestPanel.setBackground(new Color(230, 230, 230));
        
        // the slider panel on the left
        JPanel westPanel = new JPanel(new GridLayout(4,1));
        westPanel.setBackground(new Color(230, 230, 230));
        westPanel.add(insideWestPanel);
        westPanel.add(randomMouseSpeed);
        westPanel.add(smartMouseSpeed);
        westPanel.add(new JLabel("                                               "));
        
        drawPanel.addKeyListener(playerMouse);
        add(westPanel,BorderLayout.WEST);
        add(drawPanel,BorderLayout.CENTER);
        timer.start();
    }

    // this method is caled whenever the speed slider is changed and updates the speed of the mice
    private void updateSpeed() 
    {
        for(RandomMouse m : randomMice)
        {
            if(m != null)
            {
                m.updateDelay(randomMouseSpeed.getValue()) ;
            }
        }
        
        smartMouse.updateDelay(smartMouseSpeed.getValue());
    }

    private class DrawPanel extends JPanel
    {
        public DrawPanel()
        {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(800,800));      
        }
        
        //draws the maze and draws the different mouses in the maze
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            // draws the maze
            if(maze != null)
            {
                maze.drawMaze(g, getWidth() - 10, getHeight() - 10); 
                maze.drawExit(g, getWidth() - 10, getHeight() - 10); 
            }

            // draws the smart mouse's history dots
            if(smartMouse != null && showHistory.isSelected())
            {
                smartMouse.drawHistory(g, getWidth() - 10, getHeight() - 10);
            }
            
            // draws the random mouse
            for(RandomMouse m : randomMice)
            {
                if(m != null)
                {
                    m.drawMouse(g, getWidth() - 10, getHeight() - 10); 
                }
            }
            
            // draws the smart mouse
            if(smartMouse != null)
            {
                smartMouse.drawMouse(g, getWidth() - 10, getHeight() - 10);
            }
            
            // draws the player mouse
            if(playerMouse != null)
            {
                playerMouse.drawMouse(g, getWidth() - 10, getHeight() - 10); 
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        Object source = e.getSource();

        if(source == infoButton)
        {
            String message = "This maze game was made by Matthew Ripia - 1385931\n\nThere are 3 different types of mice in this game\n\n  - The first is a 'random mouse' which "
                    + "will just wander in random directions till it reaches the exit.\n  - The second is a 'smart mouse' which will use a depth-first-search algorithm to"
                    + "navigate to the exit.\n  - Lastly there is the player controlled mouse which can be oved using the arrow keys!\n\n"
                    + "There are two ways to create a maze, the first of which takes information from the Raptor2 database from AUT\n"
                    + "The second is a randomly generated maze which is unique and based off the user's inputs\n\n"
                    + "For each maze generated, the user can also choose how many random mice to create as well as control the speed of both the random and smart mice!";
            JOptionPane.showMessageDialog(drawPanel, message, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        
        if(source == loadMazeButton)
        {
            JTextField miceNo = new JTextField("10");
            JComboBox selectionList = new JComboBox(choices);
            Object[] fields = { "Database Maze: ", selectionList, "Number of Mice: (0 - 1000)", miceNo};
           
            int choice = JOptionPane.showConfirmDialog(null, fields,
                   "The Choice of a Lifetime", JOptionPane.OK_CANCEL_OPTION);

            // start
            if(choice == 0)
            {
                int numMice = 0;
                String selectedTable = (String)selectionList.getSelectedItem();
                
                try
                {
                    numMice = Integer.valueOf(miceNo.getText());
                } 
                catch(NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(null, "You didnt enter a number of mice!", "Error", JOptionPane.OK_OPTION);
                    loadMazeButton.doClick();
                }
                
                if(numMice <= 1000 && numMice >= 0)
                {
                    // create the mice, set the rest to null
                    for(int i = 0; i < 1000; i++)
                    {
                        if(i < numMice)
                        {
                            randomMice[i] = new RandomMouse(maze, 500, 0, 0);  
                        }
                        else
                        {
                            randomMice[i] = null;
                        }
                    }

                     // get a new maze from the database
                    maze = MazeMaker.loadMazeFromDatabase(selectedTable, "student", "fpn871");
                    startGame();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Please enter between 0 and 1000 mice!", "Error", JOptionPane.OK_OPTION);
                    loadMazeButton.doClick();
                }
            }
        }
        
        if(source == createRandomMazeButton)
        {
            int totalRows = 0, totalColumns = 0, totalMice = 0;
            
            JTextField miceNo = new JTextField("10");
            JTextField rows = new JTextField("20");
            JTextField columns = new JTextField("20");

            Object[] fields = { "Rows: (10 - 70)", rows, "Columns: (10 - 70)", columns, "Number of Mice: (0 - 1000)", miceNo};
           
            int choice = JOptionPane.showConfirmDialog(null, fields,
                   "The Choice of a Lifetime", JOptionPane.OK_CANCEL_OPTION);
            
            // ok pressed
            if(choice == 0)
            {
                try
                {
                    // ensures the inputs are integers
                    totalMice = Integer.valueOf(miceNo.getText());
                    totalRows = Integer.valueOf(rows.getText());
                    totalColumns = Integer.valueOf(columns.getText());
                } 
                catch(NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(null, "All fields need to contain numbers!", "Error", JOptionPane.OK_OPTION);
                    createRandomMazeButton.doClick();
                }
                
                // check values are in the correct range
                if(totalRows > 70 || totalColumns > 70 || totalRows < 10 || totalColumns < 10 || totalMice < 0 || totalMice > 1000)
                {
                    if(totalRows > 70 || totalColumns > 70)
                    {
                        JOptionPane.showMessageDialog(null, "Cannot generate a maze with greater than 70 rows or columns!", "Error", JOptionPane.OK_OPTION);
                    }
                    
                    if(totalRows < 10 || totalColumns < 10)
                    {
                        JOptionPane.showMessageDialog(null, "Cannot generate a maze with less than 10 rows or columns!", "Error", JOptionPane.OK_OPTION);
                    }
                    
                    if(totalMice < 0 || totalMice > 1000)
                    {
                        JOptionPane.showMessageDialog(null, "Please enter between 0 and 1000 mice!", "Error", JOptionPane.OK_OPTION);
                    }
                    
                    createRandomMazeButton.doClick();
                }
                else
                {
                    for(int i = 0; i < 1000; i++)
                    {
                        if(i < totalMice)
                        {
                            randomMice[i] = new RandomMouse(maze, 500, 0, 0);  
                        }
                        else
                        {
                            randomMice[i] = null;
                        }
                    }
                    
                    maze = new Maze(totalRows, totalColumns);
                    MazeMaker.createMazePathsInThread(maze);
                    startGame();
                }
            }
            else
            {
                System.out.println("cancelled");
            }
        }
        
        // checks the current frame to see if there are any winners
        checkWinner();
        
        // ensures the panel that has the key listener is in focus
        drawPanel.setFocusable(true);
        drawPanel.requestFocusInWindow();
        
        // redraws the maze every 15ms
        drawPanel.repaint();
    }

    public void startGame()
    {
        // sets the player mouse variables
        Random rand = new Random();
        int ranCol = maze.getNumCols() / 2;
        int ranRow = maze.getNumRows() / 2;
        playerMouse.maze = maze;
        playerMouse.col = rand.nextInt(ranCol);
        playerMouse.row = rand.nextInt(ranRow);
        
        // sets the smart mouse variables and starts the thread
        smartMouse.maze = maze;
        smartMouse.won = false;
        smartMouse.resetState();
        smartMouse.stopRequested = false;
        smartMouse.delay = smartMouseSpeed.getValue();
        smartMouse.col = rand.nextInt(ranCol);
        smartMouse.row = rand.nextInt(ranRow);
        
        // start the threads if not already
        if(!smartMouse.running)
        {
            Thread smartMouseThread = new Thread(smartMouse);
            smartMouseThread.start();
            smartMouse.running = true;
        }
        
        // for each mouse in the array
        for(RandomMouse m : randomMice)
        {
            if(m != null)
            {
                m.delay = randomMouseSpeed.getValue();
                m.stopRequested = false;
                m.won = false;
                m.maze = maze;
                m.row = rand.nextInt(ranRow);
                m.col = rand.nextInt(ranCol);
                
                // start the threads if not already
                if(!m.running)
                {
                    Thread mouseThread = new Thread(m);
                    mouseThread.start();
                    m.running = true;
                }
            }
        }
    }
    
    private void checkWinner() 
    {
        if(playerMouse.won || smartMouse.won)
        {
            if(playerMouse.won)
            {
                JOptionPane.showMessageDialog(drawPanel, "You won!", "Winner Winner Chicken Dinner", JOptionPane.INFORMATION_MESSAGE);
                playerMouse.won = false;
            }
            else if(smartMouse.won)
            {
                JOptionPane.showMessageDialog(drawPanel, "The Smart Mouse Won...", "Loser", JOptionPane.OK_OPTION);
                smartMouse.won = false;
            }
            
            // end game
            maze = new Maze(0,0);
            resetStats(); 
        }
        
        // for each mouse in the array
        for(RandomMouse m : randomMice)
        {
            if(m != null)
            {
                if(m.won)
                {
                    JOptionPane.showMessageDialog(drawPanel, "A Random Mouse Won...", "Loser", JOptionPane.OK_OPTION);
                    m.won = false;

                    // end game
                    maze = new Maze(0,0);
                    resetStats();
                }
            }
        }
    }
    
    private void resetStats()
    {
        // reset the player varibles
        playerMouse.col = 0;
        playerMouse.row = 0;
        playerMouse.maze = maze;

        // reset the smart mouse varibles
        smartMouse.col = 0;
        smartMouse.row = 0;
        smartMouse.won = false;
        smartMouse.maze = maze;
        smartMouse.resetState();
        smartMouse.stopRequested = true;
        
        // for each mouse in the array
        for(RandomMouse m : randomMice)
        {
            if(m != null)
            {
                m.won = false;
                m.stopRequested = true;
                m.row = 0;
                m.col = 0;
                m.maze = maze;
            }
        } 
    }

    //main method to test this game
    public static void main(String[] args)
    { 
        JFrame frame = new JFrame("Maze Maker - Matt Ripia - 1385931");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MazeDisplayer());
        frame.pack();
        
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width-frameDimension.width)/2,
            (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
    }
}