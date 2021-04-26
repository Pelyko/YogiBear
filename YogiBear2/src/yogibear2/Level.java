package yogibear2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.InputStream;
import java.lang.String;
import java.util.ArrayList;

public class Level {
    private final String url;
    private char[][] map;
    private Position player;
    private int cols,rows,baskets;
    private Boolean dead=false;
    private int HP;
    private ArrayList<Position> basketPos;
    
    public Level(String url,int hp) throws FileNotFoundException{
        HP=hp;
        basketPos = new ArrayList<Position>();
        this.url = url;
        InputStream is;
        is = ResourceLoader.loadResource("res/"+url+".txt");
        Scanner reader = new Scanner(is);
        int maxLength=Integer.parseInt(reader.nextLine());
        rows=maxLength;
        int maxWidth=Integer.parseInt(reader.nextLine());
        cols=maxWidth;
        map = new char[rows][cols];
        int i=0;
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            map[i]=data.toCharArray();
            i++;
        }
        player = getPlayerPos();
        baskets=0;
        for(int k=0; k<rows; k++){
            for(int j=0; j<cols; j++){
                if(map[k][j]=='K'){
                    baskets++;
                }
            }
        }
        
    }
    
    public int getCols(){
        return cols;
    }
    
    public int getRows(){
        return rows;
    }
    
    public int getHP(){
        return HP;
    }
    
    
    public Position getPlayerPos(){
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                if(map[i][j]=='M'){
                    return new Position(i,j);
                }
            }
        }
        return null;
    }
    
    public void refreshBaskets(){
        int i=0;
        while(!basketPos.isEmpty() && i<basketPos.size()){
            if(map[basketPos.get(i).x][basketPos.get(i).y]=='U'){
                map[basketPos.get(i).x][basketPos.get(i).y]='K';
                basketPos.remove(basketPos.get(i));
            }else{
                i++;
            }
        }
    }
    
    public void refreshDown(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                if(map[i][j]=='Z'){
                    map[i][j]='S';
                }
            }
        }
    }
    
    public void moveGuards(){
        Boolean down = true;
        for(int i=0; i<rows; i++){
            down = true;
            for(int j=0; j<cols; j++){
                if(map[i][j]=='W' || map[i][j]=='D' || map[i][j]=='A' || map[i][j]=='S'){
                    if(map[i][j]=='D'){
                        j++;
                        moveGuard(i,j-1);
                    }else{
                        moveGuard(i,j);
                    }
                }
            }
        }
        if(isNextToAGuard(player)){
            HP--;
            map[player.x][player.y]='U';
            map[0][0]='M';
            player = new Position(0,0);
        }
        refreshBaskets();
        refreshDown();
    }
    
    public void moveGuard(int i, int j){
        if(map[i][j]=='W'){
            Position current = new Position(i,j);
            Position next = current.dir(Direction.UP);
            if(isFree(next)){
                if(map[next.x][next.y]=='K'){
                    basketPos.add(new Position(next.x,next.y));
                }
                map[current.x][current.y]='U';
                map[next.x][next.y]='W';
            }else{
                map[current.x][current.y]='S';
            }
        }else{
            if(map[i][j]=='S'){
                Position current = new Position(i,j);
                Position next = current.dir(Direction.DOWN);
                if(isFree(next)){
                    if(map[next.x][next.y]=='K'){
                    basketPos.add(new Position(next.x,next.y));
                }
                    map[current.x][current.y]='U';
                    map[next.x][next.y]='Z';
                }else{
                    map[current.x][current.y]='W';
                }
            }
        }
        if(map[i][j]=='D'){
            Position current = new Position(i,j);
            Position next = current.dir(Direction.RIGHT);
            if(isFree(next)){
                if(map[next.x][next.y]=='K'){
                    basketPos.add(new Position(next.x,next.y));
                }
                map[current.x][current.y]='U';
                map[next.x][next.y]='D';
            }else{
                map[current.x][current.y]='A';
            }
        }else{
            if(map[i][j]=='A'){
                Position current = new Position(i,j);
                Position next = current.dir(Direction.LEFT);
                if(isFree(next)){
                    if(map[next.x][next.y]=='K'){
                    Position basket = new Position(next.x,next.y);
                    basketPos.add(basket);
                }
                    map[current.x][current.y]='U';
                    map[next.x][next.y]='A';
                }else{
                    map[current.x][current.y]='D';
                }
            }
        }
    }
    
    public Boolean movePlayer(Direction d){
        Boolean score = false;
        Position current = player;
        Position next = player.dir(d);
        if(isFree(next)){
            if(map[next.x][next.y]=='K'){
                baskets--;
                score = true;
            }
            if(isNextToAGuard(next)){
                HP--;
                next = new Position(0,0);
            }
                map[current.x][current.y]='U';
                map[next.x][next.y]='M';
                player = next;
        }
        return score;
    }
    
    public int getBaskets(){
        return baskets;
    }
    
    public Boolean isFree(Position p){
        if(p.x<0 || p.y<0 || p.x == rows || p.y == cols ||map[p.x][p.y]=='F'){
            return false;
        }
        return true;
    }
    
    public Boolean isNextToAGuard(Position p){
        if(isFree(new Position(p.x,p.y+1))){
            if(map[p.x][p.y+1]=='W' || map[p.x][p.y+1]=='A' || map[p.x][p.y+1]=='S' || map[p.x][p.y+1]=='D'){
                return true;
            }
        }
        if(isFree(new Position(p.x,p.y-1))){
            if(map[p.x][p.y-1]=='W' || map[p.x][p.y-1]=='A' || map[p.x][p.y-1]=='S' || map[p.x][p.y-1]=='D'){
                return true;
            }
        }
        if(isFree(new Position(p.x+1,p.y))){
            if(map[p.x+1][p.y]=='W' || map[p.x+1][p.y]=='A' || map[p.x+1][p.y]=='S' || map[p.x+1][p.y]=='D'){
                return true;
            }
        }
        if(isFree(new Position(p.x-1,p.y+1))){
            if(map[p.x-1][p.y]=='W' || map[p.x-1][p.y]=='A' || map[p.x-1][p.y]=='S' || map[p.x-1][p.y]=='D'){
                return true;
            }
        }
        return false;
    }
    
        
    public String getLevel(){
        return url;
    }
    
    public char[][] getLevelArray(){
        return this.map;
    }

}
