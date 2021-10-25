package com.etraveligroup.mazechallenge.solver;

import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RandomMouseMazeSolver extends MazeSolver {

    private static final Logger logger = LogManager.getLogger(MazeSolver.class);

    public RandomMouseMazeSolver(Maze maze, Actor actor) {
        this.maze = maze;
        this.actor = actor;
    }

    /**
     * An actor follows the same way until a junction is reached (a point with more than one possible next direction).
     * He then makes a random decision about the next direction to follow. This is a simple algorithm,
     * which can produce different paths due to randomness. For large mazes this algorithm can be extremely slow.
     *
     * @return The Actor's path from start to finish
     */
    @Override
    public List<Block> solveMaze() {
        // Log
        logger.info("For " + maze.getName());
        logger.info("Starting Random mouse algorithm execution...");

        // init
        initSolver();

        // Set actor's current position to the starting point of the maze
        setActorStartingPosition();

        if (maze != null && actor != null) {
            // Initialize
            possibleMoves = getNextPossibleMoves();
            previousDirection = nextMoveDirection = randomlySelectNextMoveDirection();

            // While Actor is not at the end point of the maze
            while (!actor.getCurrentPosition().equals(maze.getMazeEnd().getCoordinates())) {

                // Get all next possible moves from Actor's current location
                possibleMoves = getNextPossibleMoves();

                // If exist more than one next possible directions (junction)
                if (possibleMoves.size() > 1) {
                    // Select randomly one
                    nextMoveDirection = randomlySelectNextMoveDirection();
                } else {
                    // Follow the same direction (until reach junction)
                    nextMoveDirection = possibleMoves.keySet().iterator().next();
                }
                // Move Actor
                actor.move(nextMoveDirection);

                // Keep last direction for the next iteration
                previousDirection = nextMoveDirection;

                // Add this block in Actor's path
                path.add(possibleMoves.get(nextMoveDirection));
            }
            logger.info("Execution completed!");
            printPath();
        }
        return path;
    }

}
