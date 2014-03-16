package utility;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Michal Lech
 */
public class Player implements Serializable {
    //private static int cout = 0;
    //public final int id;
    
    //index + 1 = type; val = numbers of ships
    public final int[] MaxNumbersOfShips = {4, 3, 2, 1};
    private int[] NumbersOfShips;
    
    private ArrayList<Ship> ships;
    private Board ownBoard;
    private Board shotBoard;
    
    private Ship sunkShip;
    
    public Player() {        
        NumbersOfShips = new int[MaxNumbersOfShips.length];
        for(int i=0; i< MaxNumbersOfShips.length; i++) {
            NumbersOfShips[i] = 0;
        }
        
        ships = new ArrayList<>();
        ownBoard = new Board();
        shotBoard = new Board();
    }
    
    public boolean hasAnyShips() {
        return ships.size() != 0;
    }
    
    public boolean addShip(Ship ship) {
        if(NumbersOfShips[ship.length-1] == MaxNumbersOfShips[ship.length-1])
            return false;
        
        if( !ownBoard.setShip(ship) ) return false;
        
        NumbersOfShips[ship.length-1]++;
        
        ships.add(ship);
        
        return true;
    }
    
    private boolean removeShip(int id) {
        for(int i=0; i<ships.size(); i++) {
            if( ships.get(i).id == id ) {
                sunkShip = ships.get(i);
                
                ships.remove(i);
                return true;
            }
        }
        
        return false;
    }
    
    public Ship getShip(int id) {
       for(int i=0; i<ships.size(); i++) {
            if( ships.get(i).id == id ) {
                return ships.get(i);
            }
        }
        
        return null; 
    }
    
    public Board getOwnBoard() {
        return ownBoard;
    }
    
    public Board getShotBoard() {
        return shotBoard;
    }
    
    public void shot(Point p, int val) {
        shotBoard.setField(p, val);
    }
    
    /**
     * 
     * @param p
     * @return 0 - field is use; -1 - miss; -2 - sunk; <-2 - hit;
     */
    public int hit(Point p) {
        int val = ownBoard.getField(p);
        
        //field was shoted
        if(val < 0) return 0;
        
        //shot in to ship
        if(val > 0) {
            if(!getShip(val).drown_part(p)) {
                System.out.print("error drown_part");
                System.exit(1);
            }
                        
            if(getShip(val).is_sunk()) {
                ArrayList<Point> temp = getShip(val).getDrownParts();
                
                for(int i=0; i<temp.size(); i++) {
                    ownBoard.setField(temp.get(i), -2);
                }
                this.removeShip(val);
                return -2;
            }; 
            
            val = - val;
            
        }
        
        if(val == 0) val = -1;
        
        ownBoard.setField(p, val);
        
        return val;
    }
    
    public Ship getSunkShip() {
        return sunkShip;
    }
}
