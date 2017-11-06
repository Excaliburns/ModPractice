package utils;

public class ChestCoordStorage
{
    private int x;
    private int y;
    private int z;

    public ChestCoordStorage(int xCoord, int yCoord, int zCoord)
    {
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

}
