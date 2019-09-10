package assignment3;

// Made by Matt Ripia - 1385931

import java.awt.Color;
import java.util.Random;

public class RandomMouse extends Mouse 
{
    public boolean won;
    private final Random rand;
    
    public RandomMouse(Maze maze, int delay, int startRow, int startCol) 
    {
        super(maze, delay, startRow, startCol);
        rand = new Random();
        won = false;
        colour = Color.pink;
    }
    
    @Override
    protected synchronized void move()
    {
        if(!won)
        {
            boolean moved = false;
            while(!moved)
            {
                int num = rand.nextInt(4);
                switch(num)
                {
                    case 0:
                         // prioritise go west
                         if(maze.isOpen(row, col, Direction.WEST)){
                             this.col -= 1;
                             moved = true;
                         }
                        break;
                    case 1:
                         // prioritise go north
                         if(maze.isOpen(row, col, Direction.NORTH)){
                             this.row -= 1;
                             moved = true;
                         }
                        break;
                    case 2:
                         // prioritise go east
                         if(maze.isOpen(row, col, Direction.EAST)){
                             this.col += 1;
                             moved = true;
                         }
                        break;
                    case 3:
                         // prioritise go south
                         if(maze.isOpen(row, col, Direction.SOUTH)){
                             this.row += 1;
                             moved = true;
                         }
                        break;
                }
            }
            
            if(col > maze.getNumCols() - 1)
            {
                won = true;
                this.stopRequested = true;
                System.out.println("The random mouse won!");
            }
        }
    }
}
