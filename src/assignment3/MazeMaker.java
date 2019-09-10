package assignment3;

// Made by Matt Ripia - 1385931

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MazeMaker
{
    private static Database db;
    private static final Random generator = new Random();
    private static final String DRIVER="com.mysql.jdbc.Driver";
    private static final String DB_URL="jdbc:mysql://raptor2.aut.ac.nz:3306/mazes";
    
    public static void createMazePathsInThread(Maze maze)
    {   
        Thread t = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                int numRows = maze.getNumRows();
                int numCols = maze.getNumCols();
                int startRow = generator.nextInt(numRows);
                visitRoom(maze, startRow, 0);
                
                // randomly open one door along the eastern wall of maze
                int exitRow = generator.nextInt(numRows);
                maze.openDoor(exitRow, numCols-1, Direction.EAST);
        }});
        
        t.start();
    }

    public static Maze loadMazeFromDatabase(String mazeName, String username, String password)
    {   
        Maze maze = null;
        db = new Database(username, password, DRIVER, DB_URL);
        
        if(db.conn != null)
        {
            try 
            {
                String query = "select * from " + mazeName;
                ResultSet rs = db.queryDB(query);
               
                if(rs != null)
                {
                    // 1: Row | 2: Col | 3: North | 4: East | 5: South | 6: West
                    // gets the size of the maze
                    rs.last();
                    int rows = rs.getInt(1) + 1;
                    int cols = rs.getInt(2) + 1;
                    
                    System.out.println("r: " + rows);
                    System.out.println("c: " + cols);
                    
                    Maze m = new Maze(rows, cols);

                    // resets the cursor back to the start of the result set
                    rs = db.queryDB(query);
                    while(rs.next())
                    {
                        int cRow = rs.getInt(1);
                        int cCol = rs.getInt(2);
                        String north = rs.getString(3);
                        String east = rs.getString(4);
                        String south = rs.getString(5);
                        String west = rs.getString(6);
                        
                        
                        if(north.equals("Y"))
                        {
                            m.openDoor(cRow, cCol, Direction.NORTH);
                        }
                        
                        if(east.equals("Y"))
                        {
                            m.openDoor(cRow, cCol, Direction.EAST);
                        }
                        
                        if(south.equals("Y"))
                        {
                            m.openDoor(cRow, cCol, Direction.SOUTH);
                        }
                                                
                        if(west.equals("Y"))
                        {
                            m.openDoor(cRow, cCol, Direction.WEST);
                        }
                    }
                    
                    return m;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(MazeMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            System.out.println("not connected");
            return null;
        }

        return maze;
    }

    // prepares a maze of the specified direction that hsa a single
    // exit somewhere along the eastern wall
    public static void createMazePaths(Maze maze)
    {  
        // prepare a maze whose doors are all initially closed
        int numRows = maze.getNumRows();
        int numCols = maze.getNumCols();
        int startRow = generator.nextInt(numRows);
        visitRoom(maze, startRow, 0);
        
        // randomly open one door along the eastern wall of maze
        int exitRow = generator.nextInt(numRows);
        maze.openDoor(exitRow, numCols-1, Direction.EAST);
    }

    // recursive helper method which uses a depth first search of maze
    // opening doors as it moves from room to room
    private static void visitRoom(Maze maze, int row, int col)
    {  
        // randomize the order in which directions will be moved
        List<Direction> directionList = new ArrayList<>(4);
        
        for (Direction direction : Direction.values())
            directionList.add(direction);
        
        Collections.shuffle(directionList);
        Iterator<Direction> iterator = directionList.iterator();
        while (iterator.hasNext())
        {  
            Direction direction = iterator.next();
            
            // determine row and column of adjacent room
            int adjRow = row, adjCol = col;
            
            switch (direction)
            {  
                case NORTH : adjRow--; break;
                case EAST : adjCol++; break;
                case SOUTH : adjRow++; break;
                case WEST : adjCol--; break;
            }

            // determine whether the adjacent room should be visited
            if (maze.isInsideMaze(adjRow, adjCol) && !maze.hasOpenDoor(adjRow, adjCol))
            {  
                //System.out.println("opening r:" + row + " c: " + col + " d: " + direction);
                Direction newDir = null;
                
                // opens the adjacent door also
                switch(direction)
                {
                    case EAST:
                        newDir = Direction.WEST;
                        break;
                     case WEST:
                        newDir = Direction.EAST;
                        break;
                    case NORTH:
                        newDir = Direction.SOUTH;
                        break;
                    case SOUTH:
                        newDir = Direction.NORTH;
                        break;
                }
                
                maze.openDoor(row, col, direction);
                maze.openDoor(adjRow, adjCol, newDir);
                visitRoom(maze, adjRow, adjCol);
            }
        }
    }
}