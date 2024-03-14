/**
 * 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 * 
 * @author Ruoying He (1407120)
 * @author Thiam Wai Chua (0666146)
 * assignment group 24
 * 
 * assignment copyright Kees Huizing
 */

import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import javax.swing.border.LineBorder;
import java.awt.BorderLayout;

class PlayingField extends JPanel /* possible implements ... */ {
    
    private Patch[][] grid;
    
    private double alpha; // defection award factor
    
    private Timer timer;
    
    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random random = new Random( SEED );         

    private JPanel panelGrid = new JPanel();

    private int delayTime = 1000; // step time in between [ms]
    private boolean maintainStatus;     // if extention rule is applied

    private Color lightBlue = new Color(51, 153, 255);
    private final int borderWidth = 500;    // to make patch looks round

    
    // constructor, initial random setting and display
    PlayingField(int numSize) {
        grid = new Patch[numSize][numSize];

        for (int row = 0; row < numSize; row++ ) {
            for (int col = 0; col < numSize; col++ ) {
                grid[row][col] = new Patch();
                // initialize neighbor ID
                grid[row][col].assignID(row * numSize + col, numSize);
                // random assign strategy
                grid[row][col].setCooperating(random.nextBoolean());
                grid[row][col].updateStrategy();
            }
        }

        panelGrid.setLayout(new GridLayout(grid.length, grid.length));
        display(getGrid(), getGrid());

        // files step() every x second
        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                step();
            }
        };

        timer = new javax.swing.Timer(delayTime, listener);
        timer.setInitialDelay(delayTime);
    }



    /**
     * calculate and execute one step in the simulation 
     */
    public void step( ) {
        int[] neighborIDs;
        boolean[] neighborStrategy;

        int trueCount;

        boolean ownStrategy;
        double ownScore;

        int[] maxIdx;
        int winnerIdx;

        // store current strategy
        boolean[][] oldStrategies = getGrid();

         // calculate score
        for (int row = 0; row < grid.length; row++ ) {
            for (int col = 0; col < grid[0].length; col++ ) {
                // get its own strategy
                ownStrategy = grid[row][col].isCooperating();

                // get its neighbors strategy
                neighborIDs = grid[row][col].getNeighborID();
                neighborStrategy = new boolean[neighborIDs.length];
                for (int i = 0; i < neighborIDs.length; i++ ) {
                    neighborStrategy[i] = grid[neighborIDs[i] / grid.length][neighborIDs[i] % grid.length].isCooperating();
                }

                // calculate
                trueCount = 0;
                for (int i = 0; i < neighborStrategy.length; i++ ) {
                    if (neighborStrategy[i]) {
                        trueCount++;
                    }
                }
                if (ownStrategy) {      // cooperating, score is the number of the neighbors that cooperated
                    grid[row][col].setScore(trueCount);
                }
                else {
                    grid[row][col].setScore(trueCount * alpha);
                }
            }
        }

        // update strategy
        for (int row = 0; row < grid.length; row++ ) {
            for (int col = 0; col < grid[0].length; col++ ) {
                // get all scores
                ownScore = grid[row][col].getScore();
                neighborIDs = grid[row][col].getNeighborID();
                double[] neighborScores = new double[neighborIDs.length];
                for (int i = 0; i < neighborIDs.length; i++ ) {
                    neighborScores[i] = grid[neighborIDs[i] / grid.length][neighborIDs[i] % grid.length].getScore();
                }

                // find max score
                double maxScore = Arrays.stream(neighborScores).max().getAsDouble();
                maxIdx = IntStream.range(0, neighborScores.length).filter(i -> neighborScores[i] == maxScore).toArray();

                if (ownScore > maxScore) {                                                  // highest is himself
                    grid[row][col].setCooperating(grid[row][col].isCooperating());          // update strategy = maintain strategy
                }
                else if (ownScore == maxScore) {
                        if (maintainStatus) {
                            grid[row][col].setCooperating(grid[row][col].isCooperating());
                        }
                        else {
                            winnerIdx = random.nextInt(maxIdx.length + 1);
                            if (winnerIdx == maxIdx.length) {                                  // random selection choose himself, update strategy = maintain strategy
                                grid[row][col].setCooperating(grid[row][col].isCooperating());
                            }
                            else {                                                             // update strategy = strategy of one of its winning neighbors
                                grid[row][col].setCooperating(grid[neighborIDs[maxIdx[winnerIdx]] / grid.length][neighborIDs[maxIdx[winnerIdx]] % grid.length].isCooperating());
                            }
                        }
                }
                else {                                                                          // update strategy = strategy of one of its winning neighbors
                        winnerIdx = random.nextInt(maxIdx.length);
                        grid[row][col].setCooperating(grid[neighborIDs[maxIdx[winnerIdx]] / grid.length][neighborIDs[maxIdx[winnerIdx]] % grid.length].isCooperating());
                }
            }
        }

        // change pointer towards updated strategy
        for (int row = 0; row < grid.length; row++ ) {
            for (int col = 0; col < grid[0].length; col++ ) {
                grid[row][col].updateStrategy();
            }
        }

        // update display
        display(oldStrategies, getGrid());

    }
    
    public void setAlpha( double alpha ) {
        this.alpha = alpha;
    }
    
    public double getAlpha( ) {
        //...
        return alpha; // CHANGE THIS
    }
    
    // return grid as 2D array of booleans
    // true for cooperators, false for defectors
    // precondition: grid is rectangular, has non-zero size and elements are non-null
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[grid.length][grid[0].length];
        for (int x = 0; x < grid.length; x++ ) {
            for (int y = 0; y < grid[0].length; y++ ) {
                resultGrid[x][y] = grid[x][y].isCooperating();
            }
        }
        
        return resultGrid; 
    }
    
    // sets grid according to parameter inGrid
    // a patch should become cooperating if the corresponding
    // item in inGrid is true
    public void setGrid( boolean[][] inGrid) {
        for (int x = 0; x < inGrid.length; x++ ) {
            for (int y = 0; y < inGrid[0].length; y++ ) {
                grid[x][y].setCooperating(inGrid[x][y]);
            }
        }
    }   

    // set if extention rule is applied
    public void setMaintainStatus(boolean isChecked) {
        maintainStatus = isChecked;
    }

    // return status if extention rule is applied
    public boolean getMaintainStatus() {
        return maintainStatus;
    }

    // update drawing
    private void display(boolean[][] oldStrategies, boolean[][] newStrategies) {
        panelGrid.removeAll();
        for (int i = 0; i < (grid.length * grid.length); i++) {
            JLabel array = new JLabel();
            array.setHorizontalAlignment(JLabel.CENTER);
            LineBorder line;

            if (newStrategies[i / grid.length][i % grid.length]) {

                if (oldStrategies[i / grid.length][i % grid.length]) {
                    line = new LineBorder(Color.blue, borderWidth, true);
                }
                else {
                    line = new LineBorder(lightBlue, borderWidth, true);
                }
                array.setBorder(line);
            }
            else {
                if (oldStrategies[i / grid.length][i % grid.length]) {
                    line = new LineBorder(Color.orange, borderWidth, true);
                }
                else {
                    line = new LineBorder(Color.red, borderWidth, true);
                }
                array.setBorder(line);
            }
            add(array, BorderLayout.CENTER);
            panelGrid.add(array);
            array.setOpaque(false);
        }

        panelGrid.revalidate();
        panelGrid.repaint();
    }

    // return handel of the JPanel
    public JPanel getPanelGrid() {
        return panelGrid;
    }

    // when go is pressed, start looping over step
    public void start() {
        timer.start();
    }

    // when pause is pressed or a patch is clicked, stop looping over step
    public void stop() {
        timer.stop();
    }

    // change update frequency
    public void setSpeed(int speed) {
        delayTime = speed;
        timer.setInitialDelay(delayTime);
        timer.setDelay(delayTime);
        timer.restart();
    }

    public int getSpeed() {
        return delayTime;
    }

    // reset random status
    public void reset() {
        // stop calculation, wait for user click 'GO' again
        timer.stop();

        for (int row = 0; row < grid.length; row++ ) {
            for (int col = 0; col < grid[0].length; col++ ) {
                // random assign strategy
                grid[row][col].setCooperating(random.nextBoolean());
                grid[row][col].updateStrategy();
            }
        }

        display(getGrid(), getGrid());
    }

   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);     // clears the panel
    }

}

