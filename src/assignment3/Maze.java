package assignment3;

// Made by Matt Ripia - 1385931

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Maze {
    
    private int numRows;
    private int numCols;
    private Room[][] room;
    private final Image exit = Toolkit.getDefaultToolkit().getImage("exit.png");

    // empty default constructor
    public Maze()
    {
        this.numRows = 0;
        this.numCols = 0;
        this.room = new Room[0][0];
    }
    
    // building a maze constructor
    public Maze(int numRows, int numCols)
    {
        this.numRows = numRows;
        this.numCols = numCols;
        this.room = new Room[numRows][numCols];
        
        // creates the room objects for each row/col
        for(int r = 0; r < numRows; r++)
        {
            for(int c = 0; c < numCols; c++)
            {
                room[r][c] = new Room();
            }
        }
    }
    
    // returns the rows
    public int getNumRows(){
        return numRows;
    }
    
    // returns the columns
    public int getNumCols(){
        return numCols;
    }
    
    // checks if a room a the index has an open door given a certain direction
    public boolean isOpen(int row, int col, Direction door)
    {
        // if the row|col is greater than the grid
        if(row > numRows || col > numCols)
            throw new IllegalArgumentException("column or row is greater than the demension of the grid");
        
        // returns true if the current room has an open door in the particular direction
        return room[row][col].isDoorOpen(door); 
    }
    
    public boolean hasOpenDoor(int row, int col)
    {
        // if the row|col is greater than the grid
        if(row > numRows || col > numCols)
            throw new IllegalArgumentException("column or row is greater than the demension of the grid");
        
        // returns true if the current room has an open door in any direction
        return room[row][col].hasOpenDoor();
    }
    
    public void openDoor(int row, int col, Direction door)
    {
        // if the row|col is greater than the grid
        if(row > numRows || col > numCols)
            throw new IllegalArgumentException("column or row is greater than the demension of the grid");
        
        // opens the door in a given direction
        room[row][col].openDoor(door);
    }
    
    public void drawMaze(Graphics g, int worldWidth ,int worldheight)
    {
        if(numCols > 0 && numRows > 0)
        {
            // width of each room
            int roomWidth = worldWidth / numCols;
            int roomHeight = roomWidth;

            // the starting and end positions of each room
            int xStart = 10;
            int xEnd = xStart + roomWidth;
            int yStart = 10;
            int yEnd = yStart + roomHeight;

            // for each row
            for(int r = 0; r < numRows; r++)
            {
                // for each col
                for(int c = 0; c < numCols; c++)
                {
                    // if the north wall is not open
                    if(!room[r][c].isDoorOpen(Direction.NORTH)){
                        g.drawLine(xStart, yStart, xEnd, yStart);
                    }

                    // if the east wall is not open
                    if(!room[r][c].isDoorOpen(Direction.EAST)){
                        g.drawLine(xEnd, yStart, xEnd, yEnd);
                    }

                    // if the south wall is not open
                    if(!room[r][c].isDoorOpen(Direction.SOUTH)){
                        g.drawLine(xEnd, yEnd, xStart, yEnd);
                    }

                    // if the west wall is not open
                    if(!room[r][c].isDoorOpen(Direction.WEST)){
                        g.drawLine(xStart, yEnd, xStart, yStart);
                    }
    
                    // gets the new starting x points for the next room
                    xStart = xEnd;
                    xEnd = xStart + roomWidth;
                    
                }

                // gets the new starting y points for the next room
                yStart = yEnd;
                yEnd = yStart + roomHeight;

                // resets the x co-ordinates for each new row
                xStart = 10;
                xEnd = xStart + roomWidth;
            }
        }
    }
    
    // draws the exit symbol
    public void drawExit(Graphics g, int worldWidth ,int worldheight)
    {
        // for each row, check if the last column contains the exit
        for(int i = 0 ; i < numRows; i++)
        {
            if(room[i][numCols - 1].isDoorOpen(Direction.EAST))
            {
                int size = worldWidth / numCols;
                int xCircleStart = 10 + (size * (numCols - 1));
                int yCircleStart = 10 + (size * i);

                g.drawImage(exit, xCircleStart + 2, yCircleStart + 2, size - (size/5), size - (size/5), null);
            }
        }
    }

    public boolean isInsideMaze(int adjRow, int adjCol) 
    {
        if(adjRow >= numRows || adjRow < 0){
            return false;
        }
        
        if(adjCol >= numCols || adjCol < 0){
            return false;
        }
        
        return true;
    }
}
