package com.etraveligroup.mazechallenge;

import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.actor.Directions;
import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.block.BlockTypes;
import com.etraveligroup.mazechallenge.model.block.Coordinates;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * {@code MazeSolver} class provides algorithms for solving the maze challenge. There is 2 different algorithms implemented.
 * <p>1.) Random mouse algorithm. A naive way for solving the maze challenge. For large mazes it can be extremely slow</p>
 * <p>2.) Mark the path algorithm, an altered version of Trémaux's algorithm. For the second we may choose between Random implementation
 * (may produce different outputs cause of randomness) or Deterministic implementation (always same output).</p>
 */
public class MazeSolver {

    private static final Logger logger = LogManager.getLogger(MazeSolver.class);

    private Maze maze;

    private Actor actor;

    /**
     * <p>{@code Key}: all the next directions the Actor is able to move based on his current location</p>
     * <p>{@code Value}: the next accessible block for this direction (key)</p>
     */
    Map<Directions, Block> possibleMoves;

    /**
     * Used from 'Mark the path' algorithm to store information for previous visited blocks (visits per block).
     */
    private Map<Block, Integer> visitsPerBlock = new HashMap<>();

    Directions nextMoveDirection = null;

    List<Block> path = new ArrayList<>();

    Directions previousDirection = null;

    public MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public void initSolver() {
        nextMoveDirection = null;
        path = new ArrayList<>();
        path.add(maze.getMazeStart());
        visitsPerBlock = new HashMap<>();
    }

    /**
     * An actor follows the same way until a junction is reached (a point with more than one possible next direction).
     * He then makes a random decision about the next direction to follow. This is a simple algorithm,
     * which can produce different paths due to randomness. For large mazes this algorithm can be extremely slow.
     *
     * @param actor An Actor object
     * @return The Actor's path from start to finish
     */
    public List<Block> randomMouse(Actor actor) {
        // Log
        logger.info("For " + maze.getName());
        logger.info("Starting Random mouse algorithm execution...");

        // init
        initSolver();
        this.actor = actor;

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
     * @param actor          An caching actor
     * @param withRandomness True for random version & False for deterministic version of the algorithm
     * @return The Actor's path from start to finish
     */
    public List<Block> markThePath(Actor actor, boolean withRandomness) {
        // Log
        logger.info("For " + maze.getName());
        logger.info("Starting Mark The Path algorithm" + (withRandomness ? " Randomness" : " Deterministic") + " version execution...");

        // init
        initSolver();
        this.actor = actor;

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

    /** Default withRandomness param is false
     */
    public List<Block> markThePath(Actor actor) {
        return markThePath(actor, false);
    }

    /**
     * Mark the previous visited block
     */
    public void updateVisitsPerBlock(Block newPosition) {
        visitsPerBlock.put(newPosition, visitsPerBlock.get(newPosition) != null ? visitsPerBlock.get(newPosition).intValue() + 1 : Integer.valueOf(1));
    }

    /**
     * Actor selects the block with the minimum number of visits. In case there is more than one blocks with minimum number of visits
     * then selects deterministically one of those blocks based on {@enum Directions} ordering.
     *
     * @return The next direction deterministically selected from all possible next directions
     */
    public Directions deterministicSelectNextMoveDirectionWithMinVisits() {
        // Find the minimum of visits
        int minVisits = findMinimumVisits();

        for (Directions direction : Directions.values()) {
            if (possibleMoves.get(direction) != null
                    && (visitsPerBlock.get(possibleMoves.get(direction)) == null || visitsPerBlock.get(possibleMoves.get(direction)) == minVisits)) {
                return direction;
            }
        }
        return Directions.NORTH;
    }

    /**
     * Actor selects the block with the minimum number of visits. If there is more than one blocks with minimum number of visits
     * then selects randomly one of those blocks.
     *
     * @return The next direction randomly selected from all possible next directions
     */
    public Directions randomlySelectNextMoveDirectionWithMinVisits() {
        // Find the minimum of visits
        int minVisits = findMinimumVisits();

        // Shuffle all next possible directions
        List<Directions> shuffledPossibleDirections = new ArrayList(Arrays.asList(possibleMoves.keySet().toArray(new Directions[0])));
        Collections.shuffle(shuffledPossibleDirections);

        for (Directions direction : shuffledPossibleDirections) {
            if (possibleMoves.get(direction) != null
                    && (visitsPerBlock.get(possibleMoves.get(direction)) == null || visitsPerBlock.get(possibleMoves.get(direction)) == minVisits)) {
                return direction;
            }
        }
        return Directions.NORTH;
    }

    /**
     * Randomly selects one of the next possible directions
     *
     * @return The next direction randomly selected
     */
    public Directions randomlySelectNextMoveDirection() {
        // Shuffle all next possible directions
        List<Directions> shuffledPossibleDirections = new ArrayList(Arrays.asList(possibleMoves.keySet().toArray(new Directions[0])));
        Collections.shuffle(shuffledPossibleDirections);

        // If already has a direction (after starting point)
        if (previousDirection != null)
            // Remove the opposite direction of the current direction from the list to avoid follow the same way back
            shuffledPossibleDirections.remove(getOppositeDirectionBlock(previousDirection));

        // The list of direction is shuffled. We return the first element of the list which is randomly placed in this position
        return shuffledPossibleDirections.get(0);
    }

    /**
     * Returns all the next possible moves of actor
     *
     * @return The next possible moves
     */
    private Map<Directions, Block> getNextPossibleMoves() {
        Map<Directions, Block> possibleMoves = new HashMap<>();

        // For each direction (NORTH, SOUTH, EAST, WEST) check block accessibility
        for (Directions direction : Directions.values()) {
            Block nextBlock = getNextBlock(direction);
            // If next block is accessible
            if (checkIfBlockIsAccessible(nextBlock)) {
                // Add the direction
                possibleMoves.put(direction, nextBlock);
            }
        }
        return possibleMoves;
    }

    /**
     * <p>Check if a maze block is accessible, given its coordinates on map. An actor may access a block if it is withing the bounds of the maze
     * and if it is not a wall {@code BlockType.WALL}</p>
     *
     * @param block The location of the block on the maze map
     * @return True if the given block is accessible
     */
    private boolean checkIfBlockIsAccessible(Block block) {
        if (block != null && !block.getBlockType().equals(BlockTypes.WALL)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the neighboring block of the given direction
     *
     * @param direction The direction of the next neighboring block
     * @return The neighboring block of the given direction
     */
    private Block getNextBlock(Directions direction) {
        int x = actor.getCurrentPosition().getX(), y = actor.getCurrentPosition().getY();

        switch (direction) {
            case EAST:
                y++;
                break;
            case WEST:
                y--;
                break;
            case NORTH:
                x--;
                break;
            case SOUTH:
                x++;
                break;
        }
        return maze.getBlocks().get(new Coordinates(x, y));
    }

    /**
     * Returns the opposite direction of the given direction
     *
     * @param direction The given direction
     * @return The opposite direction of the given one
     */
    private Directions getOppositeDirectionBlock(Directions direction) {
        switch (direction) {
            case EAST:
                return Directions.WEST;
            case WEST:
                return Directions.EAST;
            case NORTH:
                return Directions.SOUTH;
            default:
                return Directions.NORTH;
        }
    }

    /**
     * Throws an exception if actors current position is not the starting block ({@code BlockTypes.START}) of the maze
     */
    private void checkActorsStartingPosition() {
        if (!actor.getCurrentPosition().equals(maze.getMazeStart().getCoordinates()))
            throw new IllegalStateException("Actor should start from the starting point of the maze");
    }

    /**
     * Set actor's current position to the starting point of the maze
     */
    private void setActorStartingPosition() {
        actor.setCurrentPosition(maze.getMazeStart().getCoordinates());
    }

    /**
     * Calculates the minimum number of visits from the field {@code MazeSolver.visitsPerBlock}
     *
     * @return The minimum number of visits for the next accessible blocks
     */
    private int findMinimumVisits() {
        int min = Integer.MAX_VALUE;
        for (Block block : possibleMoves.values()) {
            min = visitsPerBlock.get(block) == null ? 0 : visitsPerBlock.get(block) < min ? visitsPerBlock.get(block) : min;
        }
        return min;
    }

    /**
     * Prints Actor's path
     */
    public void printPath() {

        StringBuilder outputPath = new StringBuilder("");
        // Build the path as string
        path.forEach(block -> outputPath.append(block.toString() + ", "));
        // Remove the last 2 characters (comma and blank)
        logger.info(outputPath.substring(0, outputPath.length() - 2) + "\n");
    }

    public List<Block> getPath() {
        return path;
    }

}
