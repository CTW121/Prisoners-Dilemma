/**
 * 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * 
 * @author Ruoying He (1407120)
 * @author Thiam Wai Chua (0666146)
 * assignment group 24
 * 
 * assignment copyright Kees Huizing
 */

import java.lang.Math;

class Patch {
    //...
    boolean[] strategy = new boolean[2];
    int current;

    double score;

    int[] neighborID = new int[8];

    // returns true if and only if patch is cooperating
    boolean isCooperating() {
        //...
        return strategy[current]; // CHANGE THIS
    }
    
    // set strategy to C if isC is true and to D if false
    void setCooperating(boolean isC) {
        //...
        strategy[(current + 1) % 2] = isC;
    }
    
    // change strategy from C to D and vice versa
    void toggleStrategy() {
        // ...
        strategy[current] = !strategy[current];
    }
    
    // return score of this patch in current round
    double getScore() {

        return score; // CHANGE THIS
    }

    void setScore(double newScore) {
        
        score = newScore;
    }

    void updateStrategy() {
        // point towards new strategy
        current = (current + 1) % 2;
    }

    void assignID(int id, int size) {
        // assign neighbor's ID
        neighborID[0] = (id + size - 1) % size + (id / size) * size ;                                               // west side
        neighborID[1] = (id + size + 1) % size + (id / size) * size;                                                // east side
        neighborID[2] = (id + size) % (size * size);                                                                // south side
        neighborID[3] = (neighborID[2] + size - 1) % size + (neighborID[2] / size) * size;                          // south west side
        neighborID[4] = (neighborID[2] + size + 1) % size + (neighborID[2] / size) * size;                          // south east side
        neighborID[5] = (id + (size * size) - size) % (size * size);                                                // north side
        neighborID[6] = (neighborID[5] + size - 1) % size + (neighborID[5] / size) * size;                          // north west side
        neighborID[7] = (neighborID[5] + size + 1) % size + (neighborID[5] / size) * size;                          // north east side
    }

    int[] getNeighborID() {

        return neighborID;
    }

}
