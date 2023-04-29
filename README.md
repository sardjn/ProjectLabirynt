# Project Labirynt

> _ignore this readme, if you just want to see the code_


## Game Map (with keys)
❗️Numbers in the rectangles, are linked to the code.
``` java:
WallsData matrix[][] -> this matrix contains all the values for the walls WIDTH and HEIGHT
  ->  matrix [n][0] => width
      matrix [n][1] => height
```
Here, 'n' is a number between 0 and 15 (maximum types of walls)

The 1st number in the format N:N represents the wall type.
The 2nd number, represents the wall id (which is a number between 1 and 36)

<div align="center">
  <p>
    <img width="400" src="./images/MapKeysNumbered.png" alt="mapKeys">
    <img width="400" src="./images/MapNoKeys.png" alt="mapNoKeys">
  </p>
</div>

### Notes
#### Original Java code
``` java
...

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
wallsData[8][1] = 30.0;
        
wallsData[9][0] = 38.0;
wallsData[9][1] = 14.0;

wallsData[10][0] = 22.0;
wallsData[10][1] = 14.0;

wallsData[11][0] = 20.0;
wallsData[11][1] = 6.0;

wallsData[12][0] = 52.0;
wallsData[12][1] = 6.0;

wallsData[13][0] = 6.0;
wallsData[13][1] = 25.0;

wallsData[14][0] = 22.0;
wallsData[14][1] = 6.0;

...
```
You can find this code in the Java class 'WallsData'.
There are a few more methods in it, if you want to checkout them, visit the code in WallsData.java

#### Other walls
The other walls, such as the bounds ones, don't have a wall id, because they have been treated in a different way.
Checkout ProgettoLabirynt.java for more info.
```java
    ...
    
    private Rectangle WALL_UP;
    private Rectangle WALL_LOW;
    private Rectangle WALL_LEFT_UP;
    private Rectangle WALL_RIGHT_UP;
    private Rectangle WALL_LEFT_DOWN;
    private Rectangle WALL_RIGHT_DOWN;
    
    ... method that initialize the walls ...
    
        WALL_UP = new Rectangle(
                bordersX+multiplier, 
                bordersY+multiplier, 
                ex, 
                ey
                );
                ...
                
        WALL_LOW = new Rectangle(
                bordersX+multiplier, 
                height-bordersY-spessore-multiplier, 
                ex, 
                ey
                );
                ...
                
        ex = spessore;
        ey = 77*multiplier;
        WALL_LEFT_UP = new Rectangle(
                bordersX + multiplier, 
                bordersY + multiplier, 
                ex, 
                ey
                );
                ...
                
        WALL_RIGHT_UP = new Rectangle(
                width - bordersX - spessore - multiplier, 
                bordersY + multiplier, 
                ex, 
                ey
                );
                ...
                
        WALL_LEFT_DOWN = new Rectangle(
                bordersX + multiplier, 
                142*multiplier, 
                ex, 
                ey
                );
                ...
                
        WALL_RIGHT_DOWN = new Rectangle(
                width - bordersX - spessore - multiplier, 
                142*multiplier, 
                ex, 
                ey
                );
                ...
                
        ...
```
