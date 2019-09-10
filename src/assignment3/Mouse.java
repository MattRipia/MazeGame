package assignment3;

// Made by Matt Ripia - 1385931

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Mouse implements Runnable
{
    protected int row;
    protected int col;
    protected Maze maze;
    protected int delay;
    protected Color colour;
    public boolean running;
    protected boolean stopRequested;
    private final Image smartMouseicon = Toolkit.getDefaultToolkit().getImage("mouse3.jpg");
    private final Image randomMouseicon = Toolkit.getDefaultToolkit().getImage("mouse4.jpg");
    private final Image keyboardMouseicon = Toolkit.getDefaultToolkit().getImage("mouse2.png");
    
    public Mouse(Maze maze, int delay, int startRow, int startCol)
    {
        this.maze = maze;
        this.delay = delay;
        this.col = startCol;
        this.row = startRow;
        this.stopRequested = false;
    }
    
    public int getRow(){
        return this.row;
    }
    
    public int getCol(){
        return this.col;
    }
    
    public void requestStop(){
        this.stopRequested = true;
    }
    
    public void updateDelay(int delay){
        this.delay = delay;
    }
    
    protected abstract void move();
    
    public void drawMouse(Graphics g, int worldWidth, int worldHeight)
    {
        if(maze.getNumCols() > 0 && maze.getNumRows() > 0)
        {
            // width/height of each circle/mouse
            int mouseSize = worldWidth / maze.getNumCols();

            // the starting and end positions of each mouse when r = 0 | c = 0
            int xPos = 10;
            int yPos = 10;

            xPos = xPos + (mouseSize * col);
            yPos = yPos + (mouseSize * row);
            
            // draws the different type of mouse depending on what subclass it belongs to
            if(this instanceof SmartMouse)
            {
                g.drawImage(smartMouseicon, xPos + 1, yPos + 1, mouseSize - (mouseSize/10), mouseSize - (mouseSize/10), null);
            }
            else if(this instanceof RandomMouse)
            {
                g.drawImage(randomMouseicon, xPos + 1, yPos + 1, mouseSize - (mouseSize/10), mouseSize - (mouseSize/10), null);
            }
            else if(this instanceof KeyboardMouse)
            {
                g.drawImage(keyboardMouseicon, xPos, yPos, mouseSize, mouseSize, null);
            }
        }
    }
    
    @Override
    public void run() 
    { 
        while(!stopRequested)
        {
            try {
                move();
                Thread.sleep(delay);
                
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (Exception e){
                System.out.println("Something terrible went wrong!" + e);
            }
        }
        
        running = false;
    }
}
