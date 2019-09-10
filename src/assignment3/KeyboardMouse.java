package assignment3;

// Made by Matt Ripia - 1385931

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardMouse extends Mouse implements KeyListener 
{
    public boolean won;
    private int lastKey;
    
    public KeyboardMouse(Maze maze, int delay, int startRow, int startCol) 
    {
        super(maze, delay, startRow, startCol);
        won = false;
        colour = Color.blue;
    }

    @Override
    protected void move()
    {
        if(!won)
        {
            if(lastKey == 37)
            {
                // left
                if(maze.isOpen(row, col, Direction.WEST))
                {
                    this.col -= 1;
                }
            }  

            if(lastKey == 38) 
            {
                // up
                if(maze.isOpen(row, col, Direction.NORTH))
                {
                    this.row -= 1;
                }
            }

            if(lastKey == 39)
            {
                // right
                if(maze.isOpen(row, col, Direction.EAST))
                {
                    this.col += 1;
                }
            }
            
            if(lastKey == 40)
            {
                // south
                if(maze.isOpen(row, col, Direction.SOUTH))
                {
                    this.row += 1;
                }
            }

            if(col > maze.getNumCols() - 1)
            {
                // winner
                won = true;
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) 
    {
        lastKey = e.getKeyCode();
        move();
    }
    
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){}

}
