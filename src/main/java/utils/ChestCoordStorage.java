package utils;

public class ChestCoordStorage {
    int x;
    int y;
    int z;

    public ChestCoordStorage(int xCoord, int yCoord, int zCoord){
       this.x = xCoord;
       this.y = yCoord;
       this.z = zCoord;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
