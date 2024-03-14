/**
 * 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * main class
 * 
 * @author Ruoying He (1407120)
 * @author Thiam Wai Chua (0666146)
 * assignment group 24
 * 
 * assignment copyright Kees Huizing
 */

import javax.swing.SwingUtilities;
//...
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

class PrisonersDilemma {  /* possible extends... */
    //...
    JFrame frame = new JFrame("Prisoners Dilemma");
    
    private int GRID_SIZE = 50;              // grid Size
    private int timerDelay = 1000;
    PlayingField playingField = new PlayingField(GRID_SIZE);


    void buildGUI() {
        SwingUtilities.invokeLater( () -> {
            //...
            //create the frame
            frame.setSize(500, 700);
            frame.setVisible(true);
            frame.add(playingField.getPanelGrid(), BorderLayout.CENTER);
            JPanel functionPanel = new JPanel();
            frame.add(functionPanel, BorderLayout.SOUTH);
            functionPanel.setLayout(new GridLayout(4,2)); 
            functionPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // setting the grid layout from left to right
            
            // create labels for the alpha slider
            JLabel labels = new JLabel(" alpha = " + playingField.getAlpha());
            functionPanel.add(labels); 
            JSlider alphaSlider = new JSlider(0,30,0); //multiply by 10 and divide by 10 later
            //alphaSlider.setMajorTickSpacing(1);
            alphaSlider.setPaintTicks(true);
            alphaSlider.setPaintLabels(true);

            //we use hashtable for labels because jslider can only have integers
            //Hashtable labelTable = new Hashtable(); 
            //labelTable.put(int 0 , new JLabel("0.0") );
            //labelTable.put( 10 , new JLabel("1.0") );
            //labelTable.put( 20, new JLabel("2.0") );
            //labelTable.put( 30, new JLabel("3.0") );
            //alphaSlider.setLabelTable( labelTable );

            //add change listener to get the actual value of the slider
            alphaSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider)e.getSource();
                    playingField.setAlpha(source.getValue()/10.0); // for accuracy
                    labels.setText(" alpha = " + playingField.getAlpha());
                }
            });
            //add label for frequency
            JLabel frequencyLabel = new JLabel("frequency = " + timerDelay);
            functionPanel.add(frequencyLabel);
            functionPanel.add(alphaSlider);
            //use task performer for each step
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    playingField.step();
                }
            };
            Timer timer = new Timer(timerDelay, taskPerformer);
            //create the frequency slieder
            JSlider frequencySlider = new JSlider(0,3000,1000);
            frequencySlider.setMajorTickSpacing(500);
            frequencySlider.setPaintTicks(true);
            frequencySlider.setPaintLabels(true);
            //use change listener to get the value of the frequency
            frequencySlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider)e.getSource();
                    timerDelay = source.getValue();
                    timer.setDelay(timerDelay);
                    frequencyLabel.setText("frequency = " + timerDelay);
                }
            });
            functionPanel.add(frequencySlider);

            //create the 'Go' and 'Pause' button
            JButton goButton = new JButton("Go");
            goButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (timer.isRunning()){ //the timer will stop when it is paused
                        timer.stop();
                    } else {
                        timer.start();
                    }
                    if (goButton.getText().equals("Go")) {
                        goButton.setText("Pause");
                    } else {
                        goButton.setText("Go");
                }
                }
            });
            functionPanel.add(goButton);

            //create the reset button
            JButton resetButton = new JButton("Reset");
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playingField.reset();
                }
            });
            functionPanel.add(resetButton);    
    
        });
        playingField.setMaintainStatus(false);   // set if extention rule is applied
    }
    
    public static void main( String[] a ) {
        (new PrisonersDilemma()).buildGUI();
    }
}
