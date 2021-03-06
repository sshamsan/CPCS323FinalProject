/** Prim's Algorithm 
 * CPCS324 Project Phase 1 - DAR
 * 
 * Shahad Shamsan
 * Suha Shafi
 * Safiya Al jahdali
 */
package floydsphase1;
public class FloydsPhase1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
      /**
         * choose to use 10000 to represent infinity
         * 10000 is a large number compared to the Weights in the graph 
         * using 10000 or the largest value (MAX.value) 
         * will get the results for the matrix
         */
      
        int inf = 10000;
        
        int[][] weightedMatrix = {
        //    A     B     C      D     E    F     G      H     I    J
            {0   , 10  , inf , inf , inf ,  5  , inf , inf , inf , inf}, // A
            {inf ,  0  ,  3  , inf ,  3  , inf , inf , inf , inf , inf}, // B  
            {inf , inf ,  0  ,   4 , inf , inf , inf ,  5  , inf , inf}, // C
            {inf , inf , inf ,   0 , inf , inf , inf , inf ,   4 , inf}, // D
            {inf , inf ,  4  , inf ,  0  , inf ,  2  , inf , inf , inf}, // E
            {inf ,  3  , inf , inf , inf ,   0 , inf , inf , inf ,   2}, // F
            {inf , inf , inf ,   7 , inf , inf ,  0  , inf , inf , inf}, // G
            {inf , inf , inf ,   4 , inf , inf ,  0  ,  0  ,  3  , inf}, // H
            {inf , inf , inf , inf , inf , inf , inf , inf ,  0  , inf}, // I
            {inf ,  6  , inf , inf , inf , inf ,  8  , inf , inf ,   0}, // J 
        };     
         
        
        
        // PRINT R0 (THE WEIGHT MATRIX) 
        System.out.println("The Weight Matrix is (D0): \n");
        
        // Print the letters (A-J) for the columns
        char letters = 65;
        for (int i = 0; i < 10; i++) {
            System.out.printf("\t%s", letters);
            letters++;
        }
        System.out.println("\n");
        
        // RESET letters to label each row (A-J)
        letters = 65;
        for (int[] wm : weightedMatrix) {
            // Letter of each row
            System.out.printf("%s  ", letters);
            
            // print each element
            for (int j = 0; j < weightedMatrix.length; j++) {
                if(wm[j] == 10000){
                    // infinity
                    System.out.print("\t∞");
                }else{
                    System.out.printf("\t%d",wm[j]);
                }
            }
            System.out.println("");
            letters++; 
        }
      
        System.out.println("\n-------------------------------------------------"
                + "----------------------------------\n");
        
        
        // FLOYD's ALGORITHM
        // fill the distance matrix
        // first step: D <- W
        int[][] distanceMatrix = weightedMatrix.clone();
        
        
        for (int k = 0; k < distanceMatrix.length; k++) {
            
            // PRINT THE DISTANCE MATRIX AT EACH STAGE // 
            System.out.println("D(" + (k + 1) + "): ");
            
            // Header for the final matrix
            if ((k+1) == 10){
                System.out.println("The final Distance Matrix\n");
            }
            
            // Label the columns (A-J)
            letters = 65; 
            for (int i = 0; i < 10; i++) {
                System.out.printf("\t%s", letters);
                letters++;
            }
            System.out.println("");
            
            // RESET letters to label each row (A-J)
            letters = 65;
            
            for (int i = 0; i < distanceMatrix.length; i++) {
                // start each row
                System.out.printf("%s [", letters);
                
                for (int j = 0; j < distanceMatrix.length; j++) {
                    // assign the minimum value for each elemnt
                    int min = Math.min(distanceMatrix[i][j], (distanceMatrix[i][k] + distanceMatrix[k][j]));
                    distanceMatrix[i][j] = min;
                    
                    // print the elements of each row
                    if (distanceMatrix[i][j] == 10000) {
                        // infinity 
                        System.out.print("\t∞");
                    } else {
                        System.out.print("\t"+distanceMatrix[i][j]);
                    } 
                }
                
                //close and seperate each row
                System.out.print("\t]\n"); 
                letters++;
            }
            // Space between matrices
            System.out.println(""); 
        }
        
        // find the total weight for the minimum spanning tree
        int total =0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (distanceMatrix[i][j] != 10000) {
                    total+= distanceMatrix[i][j];
                }
            }
        }
        System.out.println("Total weight for D(10) is: " + total+"\n");
    }
    
}
