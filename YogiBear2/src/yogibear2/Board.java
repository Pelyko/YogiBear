package yogibear2;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;
import java.io.IOException;

public class Board extends JPanel{
    
    private Game game;
    private double scale;
    private int scaled_size;
    private final int tile_size = 32;
    private final Image yogi,grass,guard,tree,basket;
    
    public Board(Game g) throws IOException{
        game = g;
        scale = 1.5;
        scaled_size = (int)(scale * tile_size);
        yogi = ResourceLoader.loadImage("res/maci.png");
        guard = ResourceLoader.loadImage("res/vador.png");
        tree = ResourceLoader.loadImage("res/fa.png");
        grass = ResourceLoader.loadImage("res/fu2.png");
        basket = ResourceLoader.loadImage("res/kosar.png");
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        //if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D)g;
        Position p = game.getPlayerPos();
        char[][] li = game.getLevel();
        for (int y = 0; y < game.getRows(); y++){
            for (int x = 0; x < game.getCols(); x++){
                Image img = null;
                switch (li[y][x]){
                    case 'D': img = guard; break;
                    case 'A': img = guard; break;
                    case 'W': img = guard; break;
                    case 'S': img = guard; break;
                    case 'K': img = basket; break;
                    case 'F': img = tree; break;
                    default : img = grass; break;
                }
                if (p.y == x && p.x == y) img = yogi;
                if (img == null) continue;
                gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
            }
        }
    }
}
