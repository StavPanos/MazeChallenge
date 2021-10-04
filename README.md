# Maze Challenge

## Overview

### The problem

Given a two - dimensional matrix representing a maze with a single arbitrary starting and
ending point, as well as an arbitrary number of wall blocks, you are required to produce an
application that will output an actor’s route in the form of two - dimensional points from start to
finish. The actor cannot possess any knowledge of the route beforehand; it is required to
“discover” the route progressively, being only able to inspect and move to any squares that
are North, South, East and West from its current position.

## Model


 * Maze Class: describes the maze
 * MazeBuilder Class: used to built and validate mazes from external sources
 * Block Class: describes the block units of a Maze
 * BlockTypes Enum: START, WALL, EMPTY, END
 * Coordinates Class: the coordinates on Maze map. Its block has unique coordinates
 * Actor Class: Describes the actor which moves on the Maze
 * Directions Enum: NORTH, SOUTH, WEST, EAST
 
 There is also a structure of custom Exceptions thrown whenever external file validation fails:
 
 * MazeFileMalformedException
 * MazeFileIllegalCharacterException extends MazeFileMalformedException
 * MazeSizeOutOfBoundsException extends MazeFileMalformedException
 * EmptyMazeFileException extends MazeFileMalformedException
 

## Implementation

There is an example Application in SampeMazeSolverApp Class. We may add a maze text file in folder ".\\files\\maze.txt" (default path) of our Java project or pass path of your file as parameter in MazeBuilder constructor

MazeBuilder Class: used to built and validate mazes from external sources:

	    MazeBuilder mazeBuilder = new MazeBuilder(); // Parameter the external maze file path (default path: ".\\files\\maze.txt")

        Maze maze = mazeBuilder.builtMaze() // Throws MazeFileMalformedException
		
MazeSolver Class: provides different algorithms for solving a maze:
		
	    MazeSolver mazeSolver = new MazeSolver(maze); // Parameter: Maze object
		
	    // Create Actor
        Actor actor = new Actor();

        // Execute Random mouse algorithm
        mazeSolver.randomMouse(actor);

        // Execute Mark the path algorithm deterministic version
        mazeSolver.markThePath(actor, false);
		
## Maze-Solving Algorithms

### Random Mouse

An actor follows the same way until a junction is reached (a point with more than one possible next direction).
He then makes a random decision about the next direction to follow. This is a simple algorithm,
which can produce different paths due to randomness. For large mazes this algorithm can be extremely slow.

### Mark the path Algorithm

Similar to <i>Trémaux's algorithm</i>. Is an efficient algorithm to find the way out of a maze by marking all the previous positions.
The actor follows the same way until a junction is reached. On his way "he marks each block passed" (store the count of visits per block in visitsPerBlock field).
Every time a block is passed, actor "marks it" (increase the number of visits for this block by one).</p>

<p>Whenever there is more than one possible directions (junction) the actor selects the least visited block for the next move.
In case there is more than one possible moves with minimum number of visits the Actor chooses his way
either randomly or deterministically (based on the enum Directions ordering). The deterministic version of the algorithm always outputs the same path.
The random version of the algorithm may output different paths</p>
		


