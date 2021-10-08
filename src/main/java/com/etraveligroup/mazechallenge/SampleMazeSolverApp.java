package com.etraveligroup.mazechallenge;

import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import com.etraveligroup.mazechallenge.model.maze.MazeBuilder;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileMalformedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SampleMazeSolverApp {

    private static final Logger logger = LogManager.getLogger(SampleMazeSolverApp.class);

    public static void main(String[] args) {

        // Parameter the external maze file path (default path: ".\\files\\maze.txt")
        MazeBuilder mazeBuilder = new MazeBuilder();

        try {
            // Built maze from external file
            Maze maze = null;

            maze = mazeBuilder.builtMaze();

            // Create MazeSolver object to run different maze algorithms
            MazeSolver mazeSolver = new MazeSolver(maze);

            // Create Actor
            Actor actor = new Actor();

            // Execute 'Random mouse' algorithm
            mazeSolver.randomMouse(actor);

            // Execute 'Mark the path' algorithm deterministic version
            mazeSolver.markThePath(actor, false);

            // Execute 'Mark the path' algorithm with randomness version
            mazeSolver.markThePath(actor, true);

            // If no second parameter is passed then the deterministic version is executed by default
            //mazeSolver.markThePath(actor);

        } catch (MazeFileMalformedException | IOException ex) {
            logger.error("Exception occured: " + ex);
        }
    }
}
