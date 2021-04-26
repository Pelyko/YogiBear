package yogibear2;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import javax.swing.Timer;
import java.sql.*;

public class Window extends JFrame{
    
    private Game game;
    private Board board;
    public  static int ONE_SECOND=500;
    private int second=0;
    private JLabel stat;
    private Scores scores;
    private Timer timer;
    private String name;
    
    public Window() throws FileNotFoundException,SQLException{
        game = new Game();
        name="NA";
        
        
        scores = new Scores();
        
        URL url = Window.class.getClassLoader().getResource("res/yogiHead.jpg");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        setTitle("Yogi Bear");
        setSize(870,576);
        setResizable(false);
        
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        
        JMenuBar menuBar = new JMenuBar();
        JMenu ujJatek = new JMenu("Uj jatek");
        JMenu scoreBoard = new JMenu("Ranglista");
        JMenu difficulty = new JMenu("Nehézségi szint");
        
        JMenuItem easy = new JMenuItem("Könnyű");
        easy.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ONE_SECOND=1000;
                System.out.println("C");
            }
        }
        
        );
        JMenuItem medium = new JMenuItem("Közepes");
        JMenuItem hard = new JMenuItem("Nehéz");
        
        difficulty.add(easy);
        difficulty.add(medium);
        difficulty.add(hard);
        
        JMenuItem ujJatekKezd = new JMenuItem("Uj játék kezdése");
        ujJatekKezd.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                        new Window();
                        dispose();
                        timer.stop();
                    }catch(Exception FileNotFoundException){}
            }});
        ujJatek.add(ujJatekKezd);
        
        JMenuItem toplista = new JMenuItem("Első 10 helyezett");
        toplista.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String top10="";
                    String row;
                    for(int i=0; i<scores.getScores().size(); i++){
                        row=scores.getScores().get(i);
                        top10+=row+"\n";
                    }
                    JOptionPane.showMessageDialog(null,top10);
                }catch(Exception SQLException){}
            }
        });
        scoreBoard.add(toplista);
        
        menuBar.add(ujJatek);
        menuBar.add(scoreBoard);
        menuBar.add(difficulty);
        
        stat = new JLabel();
        stat.setFont(stat.getFont().deriveFont(32.0f));
        
        add(menuBar,BorderLayout.NORTH);
        add(stat,BorderLayout.SOUTH);
        try { add(board = new Board(game),BorderLayout.CENTER); } catch (IOException ex){}
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke); 
                int kk = ke.getKeyCode();
                Direction d = null;
                switch (kk){
                    case KeyEvent.VK_A:  d = Direction.LEFT; break;
                    case KeyEvent.VK_D: d = Direction.RIGHT; break;
                    case KeyEvent.VK_W:    d = Direction.UP; break;
                    case KeyEvent.VK_S:  d = Direction.DOWN; break;
                    default: d = Direction.NONE; break;
                }
                board.repaint();
                if(game.step(d)){
                    try{
                        game.nextLevel();
                        setSize(game.getW(),game.getH());
                    } catch (Exception FileNotFoundException) {name = JOptionPane.showInputDialog(null, "Gratulálok végigvitted a játékot! Add meg a neved:");
                    try{
                        scores.add(name,game.getScore(),second*2);
                        new Window();
                        dispose();
                        timer.stop();
                    }catch(Exception FileNotFoundException2){}}
                }
            }
        });
        
        timer = new Timer(ONE_SECOND, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                second++;
                refreshStat();
                game.step(null);
                board.repaint();
                if(game.getHP()<=0){
                    name = JOptionPane.showInputDialog(null, "Game Over! Add meg a neved:");
                    try{
                        scores.add(name,game.getScore(),second*2);
                        new Window();
                        dispose();
                        ((Timer)evt.getSource()).stop();
                    }catch(Exception FileNotFoundException){}
                }
            }});
        timer.start();
        
        setVisible(true);
    }
    
    public void refreshStat(){
        int masodperc = second/2;
        int minute = (masodperc-masodperc%60)/60;
        int mp = masodperc%60;
        stat.setText("Életek száma: "+game.getHP()+" Pontok száma: "+game.getScore()+" Eltelt idő: "+minute+":"+mp);
    }
}
