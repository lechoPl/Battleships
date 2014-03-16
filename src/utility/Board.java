package utility;

import enums.DIR;
import java.io.Serializable;

/**
 *
 * @author Michal Lech
 */
public class Board implements Serializable {
    public final int size = 10;
    protected int board[][];
    private boolean bounds = false;
    
    public Board() {
        board = new int[10][10];
        
        for(int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                board[x][y] = 0;
            }
        }
    }
    
    public int getField(Point p) throws ArrayIndexOutOfBoundsException {
        try {
            return board[p.x][p.y];
        }catch(ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
    }
    
    public void setField(Point p, int val) throws ArrayIndexOutOfBoundsException {
        try {
            board[p.x][p.y] = val;
        }catch(ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
    }
    
    /**
     * set ship on board
     * @param ship ship to add on board
     * @return true if ship is set
     */
    public boolean setShip(Ship ship) {
        //cheack position of ship
        for(int i=0; i<ship.length; i++) {
            try{
                if( getField(ship.getPart(i)) != 0 )
                    return false;
                
                //-----------------------
                if(bounds) {
                    for(int dir=0; dir<DIR.values().length; dir++) {
                        Point p = genNextPoint(
                                ship.getPart(i),
                                DIR.values()[dir],
                                size);
                        
                        if ( p == null ) continue;
                        
                        
                         
                        int val = getField( p );
                        
                        if(val != 0) {
                            if(val == ship.id)
                                continue;
                            
                            return false;
                        }
                        
                    }
                }
                //-----------------------
                
            }catch(ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
        
        for(int i=0; i<ship.length; i++) {
            try{
                setField(ship.getPart(i), ship.id);
            }catch(Exception ex) {
                System.out.println("Wrong check of set ship");
                System.exit(1);
            }
        }
        
        return true;
    }
    
    public void removeShip(Ship ship) {
        for(int i=0; i<ship.getNumberOfParts(); i++) {
            try{
                setField(ship.getPart(i), 0);
            }catch(Exception ex) {
                System.out.println("remove ship error");
                System.exit(1);
            }
        }
    }
    
    private Point genNextPoint(Point p, DIR d, int size) {
        int next_x, next_y;
        
        switch(d){
            case UP:
                next_y = p.y - 1;
                next_x = p.x;
                break;
                
            case UP_LEFT:
                next_y = p.y - 1;
                next_x = p.x - 1;
                break;
                
            case UP_RIGHT:
                next_y = p.y - 1;
                next_x = p.x + 1;
                break;
               
            case RIGHT:
                next_y = p.y;
                next_x = p.x + 1;
                break;
                
            case DOWN: 
                next_y = p.y + 1;
                next_x = p.x;
                break;
                
            case DOWN_LEFT: 
                next_y = p.y + 1;
                next_x = p.x - 1;
                break;
                
            case DOWN_RIGHT: 
                next_y = p.y + 1;
                next_x = p.x + 1;
                break;
            
            case LEFT: 
                next_y = p.y;
                next_x = p.x - 1;
                break;
                
            default:
                return null;
        }
        
        if(next_y < 0 || next_y >= size) return null;
        if(next_x < 0 || next_x >= size) return null;
        
        return new Point(next_x,next_y);
    }
}
