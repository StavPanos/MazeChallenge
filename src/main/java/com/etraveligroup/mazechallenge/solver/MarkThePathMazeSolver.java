package com.etraveligroup.mazechallenge.solver;

import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MarkThePathMazeSolver extends MazeSolver {

    private static final Logger logger = LogManager.getLogger(MazeSolver.class);

    public MarkThePathMazeSolver(Maze maze, Actor actor) {
        this.maze = maze;
        this.actor = actor;
    }

    boolean withRandomness = true;

    public List<Block> solveMaze(boolean withRandomness) {
        this.withRandomness = withRandomness;
        return solveMaze();
    }

    /**
     * <p>Similar to <i>Trémaux's algorithm</i>. Is an efficient algorithm to find the way out of a maze by marking all the previous positions.
     * The actor follows the same way until a junction is reached. On his way "he marks each block passed" (store the count of visits per block in visitsPerBlock field).
     * Every time a block is passed, actor "marks it" (increase the number of visits for this block by one).</p>
     *
     * <p>Whenever there is more than one possible directions (junction) the actor selects the least visited block for the next move.
     * In case there is more than one possible moves with minimum number of visits the Actor chooses his way
     * either randomly or deterministically (based on the enum Directions ordering). The deterministic version of the algorithm always outputs the same path.
     * The random version of the algorithm may output different paths</p>
     *
     * <p>Ambiguous next moves are solved randomly in case parameter {@code withRandomness == true}
     * or deterministically in case parameter {@code withRandomness == false}</p>
     *
     * @return The Actor's path from start to finish
     */
    @Override
    public List<Block> solveMaze() {
        // Log
        logger.info("For " + maze.getName());
        logger.info("Starting Mark The Path algorithm" + (withRandomness ? " Randomness" : " Deterministic") + " version execution...");

        // init
        initSolver();

        // Set actor's current position to the starting point of the maze
        setActorStartingPosition();
        // Add starting position to visitsPerBlock
        updateVisitsPerBlock(maze.getMazeStart());

        if (maze != null && actor != null) {
            // For random version only we need to keep the last direction moved (previousDirection)
            if (withRandomness) {
                // Initialize
                possibleMoves = getNextPossibleMoves();
                previousDirection = nextMoveDirection = randomlySelectNextMoveDirection();
            }

            // While Actor is not at the end point of the maze
            while (!actor.getCurrentPosition().equals(maze.getMazeEnd().getCoordinates())) {

                // Get all next possible moves from Actor's current location
                possibleMoves = getNextPossibleMoves();

                // Select Actor's next move either randomly or deterministically
                if (withRandomness) {
                    // Solving ambiguous situations with randomness
                    nextMoveDirection = randomlySelectNextMoveDirectionWithMinVisits();

                    // Keep last direction for the next iteration
                    previousDirection = nextMoveDirection;
                } else {
                    // Solving ambiguous situations deterministically
                    nextMoveDirection = deterministicSelectNextMoveDirectionWithMinVisits();
                }

                // Move Actor
                actor.move(nextMoveDirection);

                // Update number of visits for the current position
                updateVisitsPerBlock(possibleMoves.get(nextMoveDirection));

                // Add this block in Actor's path
                path.add(possibleMoves.get(nextMoveDirection));
            }
            logger.info("Execution completed");
            printPath();
        }
        return path;
    }


}
