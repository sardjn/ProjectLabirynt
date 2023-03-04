/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettolabirynt;

/**
 *
 * @author HP
 */
public class WallsData {
    
    private double wallsData[][] = new double[15][2]; // [0] = width, [1] = height
    
    public WallsData(double multiplier){
        init(multiplier);
    }
    
    private void init(double multiplier){
        
        wallsData[0][0] = 22.0;
        wallsData[0][1] = 22.0;
        
        wallsData[1][0] = 22.0;
        wallsData[1][1] = 14.0;
        
        wallsData[2][0] = 38.0;
        wallsData[2][1] = 14.0;
        
        wallsData[3][0] = 6.0;
        wallsData[3][1] = 17.0;
        
        wallsData[4][0] = 38.0;
        wallsData[4][1] = 30.0;
        
        wallsData[5][0] = 6.0;
        wallsData[5][1] = 32.0;
        
        wallsData[6][0] = 6.0;
        wallsData[6][1] = 54.0;
        
        wallsData[7][0] = 15.0;
        wallsData[7][1] = 6.0;
        
        wallsData[8][0] = 6.0;
        wallsData[8][1] = 28.0;
        
        wallsData[9][0] = 38.0;
        wallsData[9][1] = 14.0;
        
        wallsData[10][0] = 19.0;
        wallsData[10][1] = 14.0;
        
        wallsData[11][0] = 20.0;
        wallsData[11][1] = 6.0;
        
        wallsData[12][0] = 52.0;
        wallsData[12][1] = 6.0;
        
        wallsData[13][0] = 6.0;
        wallsData[13][1] = 25.0;
        
        wallsData[14][0] = 20.0;
        wallsData[14][1] = 6.0;
        
        
        // now multipley the data by the multiplier
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 2; j++){
                wallsData[i][j] *= multiplier;
            }
        }
        
    }
    
    public double[][] getDataFromMatrix(){
        return this.wallsData;
    }
}
