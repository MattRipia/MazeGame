package assignment3;

// Made by Matt Ripia - 1385931

public class Room {
    
    private boolean northDoorOpen;
    private boolean eastDoorOpen;
    private boolean southDoorOpen;
    private boolean westDoorOpen;
    
    public Room()
    {
        this.northDoorOpen = false;
        this.eastDoorOpen = false;
        this.southDoorOpen = false;
        this.westDoorOpen = false;
    }
    
    public void openDoor(Direction door)
    {
        switch(door)
        {
            case NORTH:
                this.northDoorOpen = true;
                break;
            case EAST:
                this.eastDoorOpen = true;
                break;
            case SOUTH:
                this.southDoorOpen = true;
                break;
            case WEST:
                this.westDoorOpen = true;
                break;           
        }
    }
    
    public boolean hasOpenDoor()
    {
        return (this.northDoorOpen || this.eastDoorOpen ||
                this.southDoorOpen || this.westDoorOpen);
    }
    
    public boolean isDoorOpen(Direction door)
    {
        switch(door)
        {
            case NORTH:
                return this.northDoorOpen;
            case EAST:
                return this.eastDoorOpen;
            case SOUTH:
                return this.southDoorOpen;
            case WEST:
                return this.westDoorOpen;
            default:
                return false;
        }
    }
    
    @Override
    public String toString()
    {
        return "North: " + this.northDoorOpen + "\n" + 
               "East: " + this.eastDoorOpen + "\n" + 
               "South: " + this.southDoorOpen + "\n" + 
               "West: " + this.westDoorOpen;
    }
}
