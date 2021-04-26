package yogibear2;

import java.sql.*;
import java.util.ArrayList;

public class Scores {
    
    private Connection connection;
    
    public Scores() throws SQLException{
        
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/Highscores","Pelyko","psw123");
        
    }
    
    public void add(String name, int score, int second) throws SQLException{
        Statement stmt = connection.createStatement();
        String sqlInsert = "insert into PELYKO.HS values ('"+name+"',"+score+","+second+")";
        int countInserted = stmt.executeUpdate(sqlInsert);
    }
    
    public ArrayList<String> getScores() throws SQLException{
        ArrayList<String> data = new ArrayList<String>();
        Statement stmt = connection.createStatement();
        String strSelect = "select * from HS ORDER BY SCORE DESC";
        ResultSet rset = stmt.executeQuery(strSelect);
        int i=0;
        while(rset.next() && i<10){
            i++;
            String name = rset.getString("NAME");
            int score = rset.getInt("SCORE");
            int time = rset.getInt("TIME");
            String row = i+". Név: "+name+" Pont: "+score+" Másodperc: "+time;
            data.add(row);
        }
        return data;
    }
}
