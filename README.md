# Project Labirynt

> _ignore this readme, if you just want to see the code_

## Game Map (with keys)
Numbers in the rectangles, are linked to the code.
``` java:
WallsData matrix[][] -> this matrix contains all the values for the walls WIDTH and HEIGHT
  ->  matrix [n][0] => width
      matrix [n][1] => height
```
Here, 'n' is a number between 0 and 15 (maximum types of walls)

The 1st number in the format N:N represents the wall type.
The 2nd number, represents the wall id (which is a number between 1 and 36)

<div align="center">
  <img width="600" src="./images/MapKeysNumbered.png" alt="mapKeys">
</div>
