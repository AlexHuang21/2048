package org.cis120.Best2048;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Alex Huang
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class instantiates a 2048 object, which is the model for the game.
 * As the user presses an arrow key, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Best2048 game; // model for the game
    private JLabel status; // current status text
    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        game = new Best2048(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        /*
         * Listens for key clicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent k) {
                game.runEachTurn(k.getKeyCode());
                updateStatus();
                repaint();
            }
        });
    }

    private void updateStatus() {
        if (!game.checkGameOver()) {
            status.setText("game over");
        }
        status.setText("Score = " + Integer.toString(game.getScore()));
    }

    /**
     * (Re)sets the game to its initial state.
     */
    public void reset() {
        game.reset();
        updateStatus();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        // Draws board grid
        g.drawLine(100, 0, 100, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 300, 400, 300);

        // Draws in tiles
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.drawString((game.getBoard()[i][j]) + "", i * 100 + 50, j * 100 + 50);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
