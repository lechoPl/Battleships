package utility;

import enums.GAME_STATE;
import java.io.Serializable;
import java.util.Random;

/**
 * 
 * @author Michal Lech
 */
public class Game implements Serializable {
    private GAME_STATE gameState;
    
    private Player player1;
    private Player player2;
    
    private Computer computer;
    
    /**
     * New game
     * @param humana 
     */
    public Game(Player human) {
        gameState = GAME_STATE.INIT;
        computer = new Computer();
        
        
        player1 = human;
        player2 = computer.initComputerPlayer();
        
        //current player
        Random rand = new Random();
        /*gameState = rand.nextInt(2) == 0 ?
                GAME_STATE.MOVE_P1 :
                GAME_STATE.MOVE_P2;
        */
        
        gameState = GAME_STATE.MOVE_P1;
    }
    
    public GAME_STATE getGameState() {
        return gameState;
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public void changePlayer() {
        if(gameState == GAME_STATE.MOVE_P1 || gameState == GAME_STATE.MOVE_P2) {
            gameState = gameState == GAME_STATE.MOVE_P1 ?
                    GAME_STATE.MOVE_P2 : GAME_STATE.MOVE_P1;
        }
    }
    
    public void setEndGame() {
        gameState = GAME_STATE.END;
    }
    
    public Computer getComputer() {
        return computer;
    }
}