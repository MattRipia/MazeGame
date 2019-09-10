package assignment3;

// Made by Matt Ripia - 1385931

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SmartMouse extends Mouse 
{
    public boolean won;
    private Stack<Direction> stack;
    private Set<Point> visited;
    
    public SmartMouse(Maze maze, int delay, int startRow, int startCol) 
    {
        super(maze, delay, startRow, startCol);
        won = false;
        colour = Color.orange;
        stack = new Stack();
        visited = new HashSet();
    }
    
    public synchronized void drawHistory(Graphics g, int worldWidth, int worldHeight) 
    {
        if(maze.getNumCols() > 0 && maze.getNumRows() > 0)
        {
            // width/height of the history boxes
            int boxSize = worldWidth / maze.getNumCols();
            int historySize = boxSize / 3;
            // the starting and end positions of each mouse when r = 0 | c = 0
            int xPos = 10;
            int yPos = 10;
            
            // for each point already visited, draw a trail
            for(Point p : visited)
            {
                g.setColor(Color.red);
                xPos = (p.y * boxSize) + 10;
                yPos = (p.x * boxSize) + 10;
                g.fillOval(xPos + boxSize / 3, yPos + boxSize / 3 , historySize, historySize);
            }
        }
    }
    
    @Override
    protected synchronized void move()
    {
        // the smart mouse needs to move using depth first search
        if(!won)
        {
            // get the current position of the mouse
            Point currPos = new Point(row, col);
            
            // add the current position to a set, so we dont revisit this room
            visited.add(currPos);
            
            // find open doors
            boolean eastOpen = maze.isOpen(row, col, Direction.EAST);
            boolean southOpen = maze.isOpen(row, col, Direction.SOUTH);
            boolean westOpen = maze.isOpen(row, col, Direction.WEST);
            boolean northOpen = maze.isOpen(row, col, Direction.NORTH);
            
            if(eastOpen && !visited.contains(new Point(row, col + 1)))
            {
                // if the east room is open, and has not been visited yet, then search
                this.col += 1; // move east
                stack.add(Direction.EAST);
            }
            
            else if(southOpen && !visited.contains(new Point(row + 1, col)))
            {
                 // if the south room is open, and has not been visited yet, then search
                this.row += 1; // move south
                stack.add(Direction.SOUTH);
            }
           
            else if(northOpen && !visited.contains(new Point(row - 1, col)))
            {
                 // if the north room is open, and has not been visited yet, then search
                this.row -= 1; // move north
                stack.add(Direction.NORTH);
            }
            
            
            else if(westOpen && !visited.contains(new Point(row, col - 1)))
            {
                // if the west room is open, and has not been visited yet, then search
                this.col -= 1; // move west
                stack.add(Direction.WEST);
            }
            
            else
            {
                // if no valid direction is found, pop the stack and go in that direction instead
                if(stack.size() > 0)
                {
                    Direction newDirection = Direction.getOppositeDirection(stack.pop());
                    if(newDirection != null)
                    {
                        switch (newDirection) 
                        {
                            case EAST:
                                 this.col += 1;
                                break;
                            case WEST:
                                this.col -= 1;
                                break;
                            case NORTH:
                                this.row -= 1;
                                break;
                            case SOUTH:
                                this.row += 1;
                                break;
                        }
                    }
                }
                else
                {
                    System.out.println("SmartMouse: Cannot move in any direction! I must be stuck in some wall...");
                }
            }
        }
        
        if(col > maze.getNumCols() - 1)
        {
            won = true;
            this.stopRequested = true;
            System.out.println("The smart mouse won!");
            resetState();
        }
    }

    public void resetState()
    {
        stack.clear();
        visited.clear();
    }

}
