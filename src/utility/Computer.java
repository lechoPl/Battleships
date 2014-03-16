package utility;

import enums.DIR;
import enums.GAME_STATE;
import enums.POSITION;
import java.io.Serializable;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Michal Lech
 */
public class Computer implements Serializable {
    private Random rand = new Random();
    
    public Player initComputerPlayer() {
        
        Player tempPlayer = new Player();
        
        int size = tempPlayer.getOwnBoard().size;
        
        for(int i=1; i<=4; i++) {
            int addShips = 0;
            
            do {
                Ship ship = new Ship(
                        new Point(rand.nextInt(size), rand.nextInt(size)),
                        4 - (i-1),
                        POSITION.values()[rand.nextInt(2)]);
                
                if(tempPlayer.addShip(ship))
                    addShips++;
                
            }while(addShips != i);
        }
        
        return tempPlayer;
    }
    
    private Stack<Point> previousHits = new Stack<>();
    private boolean isGoodDir = false;
    private DIR dir = null;
    
    private int sleepTime = 500; 
    
    public void Move(Game game) {
        if(game.getGameState() != GAME_STATE.MOVE_P2) return;
        
        int size = game.getPlayer2().getShotBoard().size;
        int val = 0;
        
        Point point = null;
        
        if( !previousHits.isEmpty() ) {
            int temp = 0;
            do{
                temp ++;
                if(temp > 1000) {
                    if(!previousHits.isEmpty()) previousHits.pop();
                }
                
                if( !isGoodDir || dir == null) {
                    dir = DIR.values()[rand.nextInt(DIR.values().length)];
                }
                
                if(previousHits.isEmpty()) {
                    point = new Point(rand.nextInt(size), rand.nextInt(size));
                }else {
                    point = genNextPoint(previousHits.peek(), dir, size);
                    if(point == null ) continue;
                }
                
                val = game.getPlayer1().hit(point);
                
                if( val == -1) {
                    game.changePlayer();
                    isGoodDir = false;
                    dir = null;
                }
                
                if( val == -2 ) {
                    isGoodDir = false;
                    dir = null;
                }
                
                if( val < -2) {
                    isGoodDir = true;
                    previousHits.push(point);
                }
            }while(val == 0);
            
        }else {
            do {
                point = new Point(rand.nextInt(size), rand.nextInt(size));
                
                val = game.getPlayer1().hit(point);
                
                if( val == -1) {
                    game.changePlayer();
                    isGoodDir = false;
                    dir = null;
                }
                
                if( val == -2 ) {
                    isGoodDir = false;
                    dir = null;
                }
                
                if( val < -2) {
                    isGoodDir = false;
                    dir = null;
                    previousHits.push(point);
                }
                
            }while( val == 0);
        }
        
        game.getPlayer2().shot(point, val);
        
        if(val < -1) {
            //System.out.println("hit " + previousHit.x + " " + previousHit.y);
            Move(game);
        }
    }
    
    private Point genNextPoint(Point p, DIR d, int size) {
        int next_x, next_y;
        
        switch(d){
            case UP:
                next_y = p.y - 1;
                next_x = p.x;
                break;
               
            case RIGHT:
                next_y = p.y;
                next_x = p.x + 1;
                break;
                
            case DOWN: 
                next_y = p.y + 1;
                next_x = p.x;
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
