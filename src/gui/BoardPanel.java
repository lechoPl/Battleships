package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import utility.Board;
import utility.Point;

/**
 *
 * @author Michal Lech
 */
public class BoardPanel extends JPanel {

private Board board;
    
    private int sizeSquare_x;
    private int sizeSquare_y;
    
    private int positionX = 0;
    private int positionY = 0;
    
    private int NumbersOfFields = 0;

    private Color CLine = Color.DARK_GRAY;
    private Color CMarker = Color.black;
    private Color CBGMarker = Color.LIGHT_GRAY;
    private Color CBoard = Color.WHITE;
    private Color CMiss = Color.BLACK;
    private Color CShip = Color.BLACK;
    private Color CHit = Color.ORANGE;
    private Color CSunk = Color.RED;
    
    
    public BoardPanel(Board board) {
        this.board = board;
                
        this.setFocusable(true);
    }
    
    public Point getField(int x, int y){
        if(x < positionX + sizeSquare_x ||
           y < positionY + sizeSquare_y)
            return null;
        
        x = (x - positionX) / sizeSquare_x;
        y = (y - positionY) / sizeSquare_y;
        
        if(x >= NumbersOfFields || y >= NumbersOfFields)
            return null;
        
        return new Point(x - 1, y - 1);
    }
    
    
 
    @Override
    public void paint(Graphics graphics){
        NumbersOfFields = board.size+1;
        
        sizeSquare_x = (this.getSize().width < this.getSize().height ?
                this.getSize().width : this.getSize().height)
                / NumbersOfFields;
        
        
        sizeSquare_y = (this.getSize().width < this.getSize().height ?
                this.getSize().width : this.getSize().height)
                / NumbersOfFields;
        
        
        positionX  = (this.getSize().width - NumbersOfFields * sizeSquare_x)/2;
        positionY  = (this.getSize().height - NumbersOfFields * sizeSquare_x)/2;
        
        super.paint(graphics);
        
        drawBoard(graphics);
        drawShipsAndHits(graphics);
    }
   
    void drawShipsAndHits(Graphics graphics){
        Graphics2D g2d = (Graphics2D) graphics;
        
        int brushSize = (int)(sizeSquare_x*0.15);
        BasicStroke stroke = new BasicStroke(                        
                        brushSize,
                        BasicStroke.JOIN_ROUND,
                        BasicStroke.JOIN_BEVEL);
                        
        g2d.setColor(Color.black);
        g2d.setStroke(stroke);
        
        for(int x = 1; x < board.size + 1; ++x){
            for(int y = 1; y < board.size + 1; ++y){
                switch( board.getField(new Point(x-1,y-1)) ) {
                    case 0:
                        break;
                        
                    case -1:
                        g2d.setColor(CMiss);
                        g2d.fillOval(
                                positionX + sizeSquare_x * x + (int)(sizeSquare_x/2.5),
                                positionY + sizeSquare_y * y + (int)(sizeSquare_y/2.5),
                                (int)(sizeSquare_x*0.3),
                                (int)(sizeSquare_y*0.3));
                        break;
                        
                    case -2:
                        g2d.setColor(CSunk);
                        g2d.drawRoundRect(
                                positionX + sizeSquare_x * x,
                                positionY + sizeSquare_y * y,
                                (int)(sizeSquare_x),
                                (int)(sizeSquare_y),
                                4,
                                4);
                        g2d.drawLine(
                                positionX + sizeSquare_x * x,
                                positionY + sizeSquare_y * y,
                                positionX + sizeSquare_x * (x+1),
                                positionX + sizeSquare_y * y);
                        
                        g2d.drawLine(
                                positionX + sizeSquare_x * (x+1),
                                positionY + sizeSquare_y * y,
                                positionX + sizeSquare_x * (x),
                                positionX + sizeSquare_y * y);
                        break;                        
                    default:
                        if(board.getField(new Point(x-1,y-1)) > 0) {
                            g2d.setColor(CShip);
                            g2d.drawRoundRect(
                                    positionX + sizeSquare_x * x,
                                    positionY + sizeSquare_y * y,
                                    (int)(sizeSquare_x),
                                    (int)(sizeSquare_y),
                                    4,
                                    4);
                        } else {
                            g2d.setColor(CHit);
                            g2d.drawRoundRect(
                                    positionX + sizeSquare_x * x,
                                    positionY + sizeSquare_y * y,
                                    (int)(sizeSquare_x),
                                    (int)(sizeSquare_y),
                                    4,
                                    4);
                            g2d.drawLine(
                                    positionX + sizeSquare_x * x,
                                    positionY + sizeSquare_y * y,
                                    positionX + sizeSquare_x * (x+1),
                                    positionX + sizeSquare_y * y);

                            g2d.drawLine(
                                    positionX + sizeSquare_x * (x+1),
                                    positionY + sizeSquare_y * y,
                                    positionX + sizeSquare_x * (x),
                                    positionX + sizeSquare_y * y);
                        }
                        break;
                }          
            }    
        }        
    }

    void drawBoard(Graphics graphics){
        //super.paint(graphics);
        
        graphics.setColor(CBGMarker);
        graphics.fillRect(
                positionX,
                positionY, 
                sizeSquare_x*NumbersOfFields,
                sizeSquare_y*NumbersOfFields);
        
        graphics.setColor(CBoard);
        graphics.fillRect(
                positionX + sizeSquare_x,
                positionY + sizeSquare_y, 
                sizeSquare_x*(NumbersOfFields-1),
                sizeSquare_y*(NumbersOfFields-1));
        
        
        graphics.setColor(CLine);
        graphics.drawRect(
                positionX,
                positionY, 
                sizeSquare_x*NumbersOfFields,
                sizeSquare_y*NumbersOfFields);
        
        //Podpisywanie osi x i y
        for(int i = 1; i < NumbersOfFields; i++){
            graphics.setColor(CMarker);
            
            int size = (int)(sizeSquare_x*0.5);
            
            graphics.setFont(new Font("Dialog", Font.PLAIN, size));

            /*
            int shift_y = 0;
            if(i < 10) shift_y = (int)(sizeSquare_x * 0.5);
            else if( i < 100) shift_y = (int)(sizeSquare_x * (1.0/4.0));
            
            graphics.drawString( String.valueOf(i),
                    positionX + sizeSquare_x * i + shift_y,
                    positionY + (sizeSquare_y+size)/2);

            
            int shift = (sizeSquare_x + size)/2;
            char letter = (char) ((int)('A')+(i-1));
            graphics.drawString(String.valueOf(letter), positionX + sizeSquare_x/3,
                    positionY + sizeSquare_y * i + shift);
            */
            int shift = sizeSquare_x - size;
            char letter = (char) ((int)('A')+(i-1));
            graphics.drawString( String.valueOf(letter),
                    positionX + sizeSquare_x * i + shift,
                    positionY + (sizeSquare_y+size)/2);

            int shift_x = 0;
            if(i < 10) shift_x = (int)(sizeSquare_x * 0.5);
            else if(i < 100) shift_x = (int)(sizeSquare_x * (1.0/3.0));

            graphics.drawString(String.valueOf(i), positionX + shift_x,
                    positionY + sizeSquare_y * i + (sizeSquare_x + size)/2);
        }    

        
        for(int i = 0; i < NumbersOfFields; i++){
            graphics.setColor(CLine);
            
            //draw line x
            int x1 = positionX + i * sizeSquare_x;
            int x2 = x1;
            int y1 = positionY;
            int y2 = positionY +(sizeSquare_y * NumbersOfFields);
            graphics.drawLine(x1, y1, x2, y2);
            
            //draw line y
            x1 = positionX;
            x2 = positionX + (sizeSquare_x * NumbersOfFields);
            y1 = positionY + i * sizeSquare_y;
            y2 = y1;
            graphics.drawLine(x1, y1, x2, y2);
            
        }
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
}