package gui;

import enums.GAME_STATE;
import enums.POSITION;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import utility.Game;
import utility.Player;
import utility.Point;
import utility.Ship;

/**
 *
 * @author Michal Lech
 */
public class MainAplet extends JApplet {
    public final int _WIDTH = 600;
    public final int _HEIGHT = 300;
    public final int _BORDER = 5;
    
    private JMenuBar menuBar;
    
    private JPanel mainPanel;
    private JPanel gamePanel;
    
    private JPanel boardsPanel;
    private BoardPanel board1Panel;
    private BoardPanel board2Panel;
    
    private JLabel infoBar;
    
    private Game game;
    private boolean newGame = true;
    
    private String infoPlaceShip(int length) {
        return "Ustaw " + length + " masztowiec. ";
    }
    
    private String infoCurrentPlayer() {
        return "Ruch wykonuje: " + (game.getGameState() == GAME_STATE.MOVE_P1 ? 
                "Gracz" : "Komputer");
        
    }
    
    private POSITION _shipPoz = POSITION.HORIZONTAL;
    private int _length = 4;
    private int _addShips = 0;
    
    private void resetPlaceShips() {
        _shipPoz = POSITION.HORIZONTAL;
        _length = 4;
        _addShips = 0;
    }
    
    private MouseInputListener placeShipsList = new MouseInputAdapter() {
        
        @Override
        public void mousePressed(MouseEvent e) {
            if(!newGame) return;
            
            if(e.getButton() == MouseEvent.BUTTON1) {
                Point point = (( BoardPanel )( e.getSource() )).getField( e.getX(), e.getY() );

                if(point == null) return;
                
                Ship ship = new Ship(
                        point,
                        _length,
                        _shipPoz);

                if(game.getPlayer1().addShip(ship))
                    _addShips++;
                    
                if(_addShips == game.getPlayer1().MaxNumbersOfShips[_length-1]) {
                    _addShips = 0;
                    _length--;
                }
                
                if(_length == 0) {
                    infoBar.setText("Ustawiono wszystki statki.");
                    infoBar.setText(infoCurrentPlayer());
                    board1Panel.removeMouseListener(this);
                    
                    board2Panel.addMouseListener(shotList);
                    newGame = false;
                }else {
                    String temp = "Poz: " + (_shipPoz == POSITION.HORIZONTAL ? 
                        "Poziomo" : "Pionowo") + ".";
                    
                    infoBar.setText(infoPlaceShip(_length)+ temp);
                }
                        
                repaintBoard1();
            }
            
            if(e.getButton() == MouseEvent.BUTTON3) {
                _shipPoz = _shipPoz == POSITION.HORIZONTAL ?
                       POSITION.VERTICAL :
                       POSITION.HORIZONTAL;
                
                String temp = "Poz: " + (_shipPoz == POSITION.HORIZONTAL ? 
                        "Poziomo" : "Pionowo") + ".";
                infoBar.setText(infoPlaceShip(_length)+ temp);
            }
        }
    };
    
     private MouseInputListener shotList = new MouseInputAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
             if(game.getGameState() != GAME_STATE.MOVE_P1) return;
                          
             if(e.getButton() == MouseEvent.BUTTON1) {
                Point point = (( BoardPanel )( e.getSource() )).getField( e.getX(), e.getY() );

                if(point == null) return;
                
                int val = game.getPlayer2().hit(point);
                
                if(val == 0) return;
                
                if(val == -2) {
                    ArrayList<Point> temp = 
                            game.getPlayer2().getSunkShip().getDrownParts();
                    
                    for(int i=0; i<temp.size(); i++) {
                        game.getPlayer1().shot(temp.get(i), val);
                    }
                }else {
                    game.getPlayer1().shot(point, val);
                }
                
                repaintBoard2();
                
                if(!game.getPlayer2().hasAnyShips()) {
                    infoBar.setText("Wygrywa: Gracz");
                    game.setEndGame();
                    board2Panel.removeMouseListener(this);
                    return;
                }
                
                
                if(val == -1) {
                    game.changePlayer();
                    
                    //---------------------
                    infoBar.setText(infoCurrentPlayer());
                    game.getComputer().Move(game);
                    
                    repaintBoard1();
                    
                    //game.changePlayer();
                    infoBar.setText(infoCurrentPlayer());
                    
                    if(!game.getPlayer1().hasAnyShips()) {
                        infoBar.setText("Wygrywa: Komputer");
                        game.setEndGame();
                        board2Panel.removeMouseListener(this);
                        return;
                    }
                }
             }
         }
     };
     
    private ActionListener newGameList = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            game = new Game(new Player());
            newGame = true;
            board1Panel.removeMouseListener(placeShipsList);
            resetPlaceShips();
            board2Panel.removeMouseListener(shotList);
            
            board1Panel.setBoard(game.getPlayer1().getOwnBoard());
            board2Panel.setBoard(game.getPlayer1().getShotBoard());
            
            board1Panel.addMouseListener(placeShipsList);
                        
            repaintBoard1();
            repaintBoard2();
            
            infoBar.setText(infoPlaceShip(4)+ "Poz: Poziomo.");
        }
    };
    
    private void repaintBoard1() {
        board1Panel.repaint();
    }
    
    private void repaintBoard2() {
        board2Panel.repaint();
    }
    
    @Override
    public void init()
    {        
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
          fis = new FileInputStream("save.ser"); //utworzenie strumienia wejściowego  
          ois = new ObjectInputStream(fis); //utworzenie obiektu odczytującego obiekty ze strumienia

          game = (Game) ois.readObject(); //deserializacja obiektu
          newGame = false;
          
        } catch (Exception e) {
          game = new Game(new Player());
        } finally {
          // zasoby zwalniamy w finally
          try {
            if (ois != null) ois.close();
          } catch (IOException e) {}
          try {
            if (fis != null) fis.close();
          } catch (IOException e) {}
          
          File file = new File("save.ser");
          file.delete();
        }
        
        //---------------------
        
        this.setSize(_WIDTH, _HEIGHT);
        this.setLayout(new BorderLayout());
        
        menuBar = new JMenuBar();
        this.add(menuBar, BorderLayout.PAGE_START);
        JMenu menu = new JMenu("Gra");
        menuBar.add(menu);
        
        JMenuItem newGameMI = new JMenuItem("Nowa gra");
        newGameMI.addActionListener(newGameList);
        menu.add(newGameMI);
        
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                _BORDER,
                _BORDER,
                _BORDER,
                _BORDER));
        this.add(mainPanel, BorderLayout.CENTER);
        
        
        gamePanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        
        boardsPanel = new JPanel(new GridLayout(0,2));
        gamePanel.add(boardsPanel, BorderLayout.CENTER);
        
        board1Panel = new BoardPanel(game.getPlayer1().getOwnBoard());
        if(newGame) {
            board1Panel.addMouseListener(placeShipsList);
        }
        boardsPanel.add(board1Panel);
        
        board2Panel = new BoardPanel(game.getPlayer1().getShotBoard());
        if(!newGame) {
            board2Panel.addMouseListener(shotList);
        }
        boardsPanel.add(board2Panel);        
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.PAGE_END);
        
        infoBar = new JLabel("Init game.");
        infoPanel.add(infoBar);
        infoBar.setText(infoCurrentPlayer());
        if(newGame) {
            infoBar.setText(infoPlaceShip(4)+ "Poz: Poziomo.");
        }
        
    }
    /*
    @Override
    public void start()
    {
        
    }
    */
    @Override
    public void stop()
    {
        if(game.getGameState() == GAME_STATE.MOVE_P1) {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            /*
             * Zapis do strumienia (plikowego, ale może być dowolne)
             */
            try {
              fos= new FileOutputStream("save.ser"); //utworzenie strumienia wyjściowego
              oos = new ObjectOutputStream(fos);  //utworzenie obiektu zapisującego do strumienia

              oos.writeObject(game); //serializacja obiektu

            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              // zamykamy strumienie w finally
              try {
                if (oos != null) oos.close();
              } catch (IOException e) {}
              try {
                if (fos != null) fos.close();
              } catch (IOException e) {}
            }
        }
        if(newGame){
            File file = new File("save.ser");
            file.delete();
        }
    }
/*
    @Override
    public void destroy()
    {
    }
*/
}
