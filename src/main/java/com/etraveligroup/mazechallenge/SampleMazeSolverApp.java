package com.etraveligroup.mazechallenge;

import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import com.etraveligroup.mazechallenge.model.maze.MazeBuilder;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileMalformedException;
import com.etraveligroup.mazechallenge.solver.MarkThePathMazeSolver;
import com.etraveligroup.mazechallenge.solver.RandomMouseMazeSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SampleMazeSolverApp {

    private static final Logger logger = LogManager.getLogger(SampleMazeSolverApp.class);

    public static void main(String[] args) {

        // Parameter the external maze file path (default path: ".\\files\\maze.txt")
        MazeBuilder mazeBuilder = new MazeBuilder(".\\files\\simple_maze.txt");

        try {
            // Built maze from external file
            Maze maze = mazeBuilder.builtMaze();

            // Create Actor
            Actor actor = new Actor();

            // Create MazeSolver object to run different maze algorithms
            RandomMouseMazeSolver randomMouse = new RandomMouseMazeSolver(maze, actor);
            MarkThePathMazeSolver markThePath = new MarkThePathMazeSolver(maze, actor);

            // Execute 'Random mouse' algorithm
            randomMouse.solveMaze();

            // Execute 'Mark the path' algorithm deterministic version
            markThePath.solveMaze(false);

            // Execute 'Mark the path' algorithm with randomness version
            markThePath.solveMaze(true);

            // If no second parameter is passed then the deterministic version is executed by default
            //mazeSolver.markThePath(actor);

        } catch (MazeFileMalformedException | IOException ex) {
            logger.error("Exception occured: " + ex);
        }
    }
}
