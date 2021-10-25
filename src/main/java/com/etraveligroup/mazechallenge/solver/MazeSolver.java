package com.etraveligroup.mazechallenge.solver;

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
public abstract class MazeSolver {

    private static final Logger logger = LogManager.getLogger(MazeSolver.class);

    protected Maze maze;

    protected Actor actor;

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

    public MazeSolver() {
    }

    public void initSolver() {
        nextMoveDirection = null;
        path = new ArrayList<>();
        path.add(maze.getMazeStart());
        visitsPerBlock = new HashMap<>();
    }

    public abstract List<Block> solveMaze();

    /**
     * Randomly selects one of the next possible directions
     *
     * @return The next direction randomly selected
     */
    protected Directions randomlySelectNextMoveDirection() {
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
     * Mark the previous visited block
     */
    protected void updateVisitsPerBlock(Block newPosition) {
        visitsPerBlock.put(newPosition, visitsPerBlock.get(newPosition) != null ? visitsPerBlock.get(newPosition) + 1 : 1);
    }

    /**
     * Actor selects the block with the minimum number of visits. In case there is more than one blocks with minimum number of visits
     * then selects deterministically one of those blocks based on enum Directions ordering.
     *
     * @return The next direction deterministically selected from all possible next directions
     */
    protected Directions deterministicSelectNextMoveDirectionWithMinVisits() {
        // Find the minimum of visits for the next possible moves
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
        // Find the minimum of visits for the next possible moves
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
     * Returns all the next possible moves of actor
     *
     * @return The next possible moves
     */
    protected Map<Directions, Block> getNextPossibleMoves() {
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
    protected boolean checkIfBlockIsAccessible(Block block) {
        return block != null && !block.getBlockType().equals(BlockTypes.WALL);
    }

    /**
     * Returns the neighboring block of the given direction
     *
     * @param direction The direction of the next neighboring block
     * @return The neighboring block of the given direction
     */
    protected Block getNextBlock(Directions direction) {
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
    protected Directions getOppositeDirectionBlock(Directions direction) {
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
     * Set actor's current position to the starting point of the maze
     */
    protected void setActorStartingPosition() {
        actor.setCurrentPosition(maze.getMazeStart().getCoordinates());
    }

    /**
     * For the next possible moves, calculates the minimum number of visits. If exist a block that is not yet visited then minimum number of visits is zero
     *
     * @return The minimum number of visits from the next accessible blocks
     */
    protected int findMinimumVisits() {
        // Previous visits for the next possible moves
        Map<Block, Integer> possibleMovesVisits = new HashMap<>();

        // For each next possible move
        for (Block nextMove : possibleMoves.values()) {
            // If there is any block that is not yet visited then minimum previous visits is zero
            if (visitsPerBlock.get(nextMove) == null) {
                return 0;
            }
            // Add the previous visits to map
            possibleMovesVisits.put(nextMove, visitsPerBlock.get(nextMove) == null ? 0 : visitsPerBlock.get(nextMove));
        }
        // Return the next move with minimum number of previous visits
        return Collections.min(possibleMovesVisits.values());
    }

    /**
     * Prints Actor's path
     */
    public void printPath() {
        StringBuilder outputPath = new StringBuilder();
        // Build the path as string
        path.forEach(block -> outputPath.append(block.toString()).append(", "));

        // Remove the last 2 characters (comma and blank)
        logger.info(outputPath.substring(0, outputPath.length() - 2) + "\n");
    }

    public List<Block> getPath() {
        return path;
    }

}
