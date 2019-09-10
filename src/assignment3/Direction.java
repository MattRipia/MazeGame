package assignment3;

// Made by Matt Ripia - 1385931

public enum Direction {
    
    NORTH, EAST, SOUTH, WEST;
    
    public static Direction getOppositeDirection(Direction d)
    {
        switch(d)
        {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST:  return WEST;
            case WEST:  return EAST;
        }
        
        return null;
    }
}
