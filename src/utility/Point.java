package utility;

import java.io.Serializable;

/**
 *
 * @author Michal Lech
 */
public class Point implements Serializable {
    public final int x;
    public final int y;
    
    public Point(Point p) {
        x = p.x;
        y = p.y;
    }
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Point temp;
        
        try{
            temp = (Point) obj;
        }catch(Exception ex) {
            throw new IllegalArgumentException();
        }
        
        return temp.x == x && temp.y == y;
    }
}
