package yogibear2;

public enum Direction {
    DOWN(1, 0), LEFT(0, -1), UP(-1, 0), RIGHT(0, 1), NONE(0,0);
    
    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }
    public final int x, y;
}