package yogibear2;

public class Position {
    public int x,y;
    
    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }
    
    public Position dir(Direction d){
        return new Position(x+d.x,y+d.y);
    }
}
