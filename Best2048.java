package org.cis120.Best2048;

/**
 * CIS 120 HW09 - Best2048 game
 * University of Pennsylvania
 * Created by Alex Huang
 */

import java.util.ArrayList;

public class Best2048 {

    private int[][] board;
    private int score = 0;

    /**
     * Constructor sets up game state.
     */
    public Best2048() {
        board = new int[4][4];
        reset();
    }

    /**
     * getBoard returns the 2D array that represents the game board
     * 
     * @return the board
     */
    public int[][] getBoard() {
        return this.board;
    }

    /**
     * getScore returns current score of the game
     * 
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * runEachTurn runs each turn of the game
     * 
     * @param key int that represents the key pressed
     */
    public void runEachTurn(int key) {
        if (!checkGameOver()) {
            if (key == 38) {
                swipeLeft();
            } else if (key == 40) {
                swipeRight();
            } else if (key == 37) {
                swipeUp();
            } else if (key == 39) {
                swipeDown();
            }
            spawnNewTiles();
        }
    }

    /**
     * reset (re)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[4][4];
        spawnBoard();
        spawnBoard();
        score = 0;
    }

    /**
     * spawnBoard spawns the first or second tile to start the game.
     */
    public void spawnBoard() {
        // randomize two tiles and make sure the
        // second one isn't in the same spot as the first.
        // If it is, recursively do the method again
        int row = (int) (Math.random() * 3);
        int col = (int) (Math.random() * 3);
        if (board[row][col] == 0) {
            board[row][col] = twoOrFour();
        } else {
            spawnBoard();
        }
    }

    /**
     * spawnNewTiles adds a new tile to the board each time the
     * user makes a move.
     */
    public void spawnNewTiles() {
        // use array lists to keep track of x and y coordinates
        // of tiles that are empty to be possible tiles for spawning
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }
        int row = (int) (Math.random() * rows.size());
        int col = (int) (Math.random() * cols.size());
        board[rows.get(row)][cols.get(col)] = twoOrFour();
    }

    /**
     * checkGameOver checks the board to see if it's full
     * and there are no possible moves the user can make
     */
    public boolean checkGameOver() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    break;
                }
                try {
                    if (board[i][j] == board[i - 1][j - 1]
                            || board[i][j] == board[i - 1][j + 1]
                            || board[i][j] == board[i + 1][j - 1]
                            || board[i][j] == board[i + 1][j + 1]) {
                        return true;
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    /**
     * swipeLeft updates the board in the score when the user
     * swipes to the left, using helper function moveTilesLeft
     */
    public void swipeLeft() {
        // move each element to the left if there's room
        for (int i = 0; i < 4; i++) {
            // (re)set a boolean to false that will be used constantly
            // through this function and the helper function representing
            // whether any collisions have happened so only one happens
            // (except for the special case)
            boolean check = false;
            // deal with special case when 2 collisions have to occur at once
            // with a full row of filled tiles containing two adjacent pairs
            if (board[i][0] == board[i][1] && board[i][2] == board[i][3]
                    && board[i][0] != 0 && board[i][3] != 0) {
                board[i][0] *= 2;
                board[i][1] = 2 * board[i][2];
                board[i][2] = 0;
                board[i][3] = 0;
                score += board[i][0] + board[i][1];
            } else {
                for (int j = 1; j < 4; j++) {
                    // check if there's an immediate collision
                    if (board[i][j - 1] == board[i][j] && !check
                            && board[i][j] != 0) {
                        board[i][j - 1] *= 2;
                        board[i][j] = 0;
                        score += board[i][j - 1];
                        check = true;
                    }
                    // run a recursive method to deal
                    // with moving tiles to the left
                    check = moveTilesLeft(i, j, check);
                }
            }
        }
    }

    /**
     * moveTilesLeft recursively calls itself until a specific
     * tile can no longer move left or collide
     * 
     * @param i the row number for the specific tile
     * @param j the col number for the specific tile
     * @return check the boolean that's making sure multiple collisions
     *         don't happen
     */
    public boolean moveTilesLeft(int i, int j, boolean check) {
        // if there's space to the left move it
        if (j > 0 && board[i][j - 1] == 0) {
            board[i][j - 1] = board[i][j];
            board[i][j] = 0;
            // if it's not on the far left check for a collision
            if (j - 1 > 0 && !check) {
                if (board[i][j - 2] == board[i][j - 1]) {
                    board[i][j - 2] *= 2;
                    board[i][j - 1] = 0;
                    score += board[i][j - 2];
                    check = true;
                }
            }
            // run it again in case there's more room to the left
            moveTilesLeft(i, j - 1, check);
            return check;
        }
        return check;
    }

    /**
     * swipeRight updates the board in the score when the user
     * swipes to the right, using helper function moveTilesRight
     */
    public void swipeRight() {
        // move each element to the right if there's room
        for (int i = 0; i < 4; i++) {
            // (re)set a boolean to false that will be used constantly
            // through this function and the helper function representing
            // whether any collisions have happened so only one happens
            // (except for the special case)
            boolean check = false;
            // deal with special case when 2 collisions have to occur at once
            // with a full row of filled tiles containing two adjacent pairs
            if (board[i][0] == board[i][1] && board[i][2] == board[i][3]
                    && board[i][0] != 0 && board[i][3] != 0) {
                board[i][3] *= 2;
                board[i][2] = 2 * board[i][1];
                board[i][1] = 0;
                board[i][0] = 0;
                score += board[i][2] + board[i][3];
            } else {
                for (int j = 2; j > -1; j--) {
                    // check if there's an immediate collision and
                    // update boolean, so collision doesn't need to be checked later
                    if (board[i][j + 1] == board[i][j] && !check
                            && board[i][j] != 0) {
                        board[i][j + 1] *= 2;
                        board[i][j] = 0;
                        score += board[i][j + 1];
                        check = true;
                    }
                    // run a recursive method to deal
                    // with moving tiles to the right
                    check = moveTilesRight(i, j, check);
                }
            }
        }
    }

    /**
     * moveTilesRight recursively calls itself until a specific
     * tile can no longer move right or collide
     * 
     * @param i the row number for the specific tile
     * @param j the col number for the specific tile
     * @return check the boolean that's making sure multiple collisions
     *         * don't happen
     */
    public boolean moveTilesRight(int i, int j, boolean check) {
        // if there's space to the right move it
        if (j < 3 && board[i][j + 1] == 0) {
            board[i][j + 1] = board[i][j];
            board[i][j] = 0;
            // if it's not on the far right check for a collision
            if (j + 1 < 3 && !check) {
                if (board[i][j + 2] == board[i][j + 1]) {
                    board[i][j + 2] *= 2;
                    board[i][j + 1] = 0;
                    score += board[i][j + 2];
                    check = true;
                }
            }
            // run it again in case there's more room to the right
            check = moveTilesRight(i, j + 1, check);
            return check;
        }
        return check;
    }

    /**
     * swipeUp updates the board in the score when the user
     * swipes to the up, using helper function moveTilesUp
     */
    public void swipeUp() {
        // move each element to the up if there's room
        for (int i = 0; i < 4; i++) {
            // (re)set a boolean to false that will be used constantly
            // through this function and the helper function representing
            // whether any collisions have happened so only one happens
            // (except for the special case)
            boolean check = false;
            // deal with special case when 2 collisions have to occur at once
            // with a full row of filled tiles containing two adjacent pairs
            if (board[0][i] == board[1][i] && board[2][i] == board[3][i]
                    && board[0][i] != 0 && board[3][i] != 0) {
                board[0][i] *= 2;
                board[1][i] = 2 * board[2][i];
                board[2][i] = 0;
                board[3][i] = 0;
                score += board[0][i] + board[1][i];
            } else {
                for (int j = 1; j < 4; j++) {
                    // check if there's an immediate collision
                    if (board[j - 1][i] == board[j][i] && !check
                            && board[j][i] != 0) {
                        board[j - 1][i] *= 2;
                        board[j][i] = 0;
                        score += board[j - 1][i];
                        check = true;
                    }
                    // run a recursive method to deal
                    // with moving tiles up
                    check = moveTilesUp(i, j, check);
                }
            }
        }
    }

    /**
     * moveTilesUp recursively calls itself until a specific
     * tile can no longer move up or collide
     * 
     * @param i the row number for the specific tile
     * @param j the col number for the specific tile
     * @return check the boolean that's making sure multiple collisions
     *         don't happen
     */
    public boolean moveTilesUp(int i, int j, boolean check) {
        // if there's space up move it
        if (j > 0 && board[i][j - 1] == 0) {
            board[j - 1][i] = board[j][i];
            board[j][i] = 0;
            // if it's not on the far up check for a collision
            if (j - 1 > 0 && !check) {
                if (board[j - 2][i] == board[j - 1][i]) {
                    board[j - 2][i] *= 2;
                    board[j - 1][i] = 0;
                    score += board[j - 2][i];
                    check = true;
                }
            }
            // run it again in case there's more room up
            moveTilesUp(i, j - 1, check);
            return check;
        }
        return check;
    }

    /**
     * swipeDown updates the board in the score when the user
     * swipes to the down, using helper function moveTilesDown
     */
    public void swipeDown() {
        // move each element down if there's room
        for (int i = 0; i < 4; i++) {
            // (re)set a boolean to false that will be used constantly
            // through this function and the helper function representing
            // whether any collisions have happened so only one happens
            // (except for the special case)
            boolean check = false;
            // deal with special case when 2 collisions have to occur at once
            // with a full row of filled tiles containing two adjacent pairs
            if (board[0][i] == board[1][i] && board[2][i] == board[3][i]
                    && board[i][0] != 0 && board[i][3] != 0) {
                board[3][i] *= 2;
                board[2][i] = 2 * board[i][1];
                board[1][i] = 0;
                board[0][i] = 0;
                score += board[i][2] + board[i][3];
            } else {
                for (int j = 2; j > -1; j--) {
                    // check if there's an immediate collision and
                    // update boolean, so collision doesn't need to be checked later
                    if (board[j + 1][i] == board[j][i] && !check
                            && board[j][i] != 0) {
                        board[j + 1][i] *= 2;
                        board[j][i] = 0;
                        score += board[j + 1][i];
                        check = true;
                    }
                    // run a recursive method to deal
                    // with moving tiles down
                    check = moveTilesDown(i, j, check);
                }
            }
        }
    }

    /**
     * moveTilesDown recursively calls itself until a specific
     * tile can no longer move down or collide
     * 
     * @param i the row number for the specific tile
     * @param j the col number for the specific tile
     * @return check the boolean that's making sure multiple collisions
     *         * don't happen
     */
    public boolean moveTilesDown(int i, int j, boolean check) {
        // if there's space down move it
        if (j < 3 && board[j + 1][i] == 0) {
            board[j + 1][i] = board[j][i];
            board[j][i] = 0;
            // if it's not on the far down check for a collision
            if (j + 1 < 3 && !check) {
                if (board[j + 2][i] == board[j + 1][i]) {
                    board[j + 2][i] *= 2;
                    board[j + 1][i] = 0;
                    score += board[j + 2][i];
                    check = true;
                }
            }
            // run it again in case there's more room to the down
            check = moveTilesDown(i, j + 1, check);
            return check;
        }
        return check;
    }

    /**
     * twoOrFour decides whether a two or four tile will be spawned.
     * 
     * @return the int 2 or 4 to be spawned
     */
    public int twoOrFour() {
        // 1 in 10 chance of being a 4
        if (Math.random() >= 0.9) {
            return 4;
        }
        return 2;
    }
}
