package yogibear2;

import java.io.FileNotFoundException;

public class Game {
    
    private int levelcnt;
    private Level level;
    private Position player;
    private int score,prefw,prefh;
    
    public Game() throws FileNotFoundException{
        prefw=870;
        prefh=576;
        score = 0;
        levelcnt=1;
        level = new Level(Integer.toString(levelcnt),3);
        player = level.getPlayerPos();
    }
    
    public int getW(){
        return prefw;
    }
    
    public int getH(){
        return prefh;
    }
    
    public char[][] getLevel(){
        return level.getLevelArray();
    }
    
    public int getRows(){
        return level.getRows();
    }
    
    public int getCols(){
        return level.getCols();
    }
    
    public void nextLevel() throws FileNotFoundException{
        levelcnt++;
        if(levelcnt>5){
            prefw=1060;
            prefh=678;
        }
        level = new Level(Integer.toString(levelcnt),level.getHP());
        player = level.getPlayerPos();
    }
    
    public int getHP(){
        return level.getHP();
    }
    
    public Position getPlayerPos(){
        return level.getPlayerPos();
    }
    
    public int getScore(){
        return score;
    }
    
    public Boolean step(Direction d){
        if(d==null){
            level.moveGuards();
        }
        if(d!=null){
            if(level.movePlayer(d)){
                score++;
            }
            if(level.getBaskets()==0){
                return true;
            }
        }
        return false;
    }
    
}
