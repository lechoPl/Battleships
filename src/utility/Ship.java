package utility;

import enums.POSITION;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Michal Lech
 */
public class Ship implements Serializable {
    public final int length;
    
    private final POSITION position;
    private ArrayList<Point> parts;
    private ArrayList<Point> drownParts;
    
    
    private static int count = 2;
    public final int id; // pierwszy statek ma id = 3
    
    /**
     * 
     * @param point top left point of the ship
     * @param length length of ship
     * @param position 
     */
    public Ship(Point point, int length, POSITION position) {
        this.length = length;
        this.position = position;
        
        ++count;
        this.id = count;
        
        //dodatenie kolejnych czesci
        parts = new ArrayList<Point>();
        drownParts = new ArrayList<>();
        
        for(int i=0; i<length; i++) {
            Point temp_point;
            
            switch(position) {
                case HORIZONTAL:
                    temp_point = new Point(point.x + i, point.y);
                    break;
                    
                case VERTICAL:
                    temp_point = new Point(point.x, point.y + i);
                    break;
                default:
                    throw new IllegalArgumentException("Wrong position");
            }
            
            parts.add(temp_point);
        }
    }
    
    public Point getPart(int id) throws ArrayIndexOutOfBoundsException {
        if(id < 0 || id > parts.size())
            throw new ArrayIndexOutOfBoundsException();
        
        return parts.get(id);
    }
    
    /**
     * To remove one part of ship
     * @param p position of parts
     * @return true if succes, false if p dosen't belong to ship
     */
    public boolean drown_part(Point p) {
        Point temp;
        
        for(int i=0; i<parts.size(); i++) {
            temp = parts.get(i);
            if(temp.x == p.x && temp.y == p.y) {
                parts.remove(i);
                drownParts.add(p);
                return true;
            }
        }
        
        return false;
    }
    
    public boolean is_sunk() {
        return parts.size() == 0;
    }
    
    public int getNumberOfParts() {
        return parts.size();
    }
    
    public ArrayList<Point> getDrownParts() {
        return drownParts;
    }
}
