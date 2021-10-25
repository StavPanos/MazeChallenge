import com.etraveligroup.mazechallenge.solver.MarkThePathMazeSolver;
import com.etraveligroup.mazechallenge.solver.MazeSolver;
import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileMalformedException;
import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.block.Coordinates;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import com.etraveligroup.mazechallenge.model.maze.MazeBuilder;
import com.etraveligroup.mazechallenge.solver.RandomMouseMazeSolver;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static com.etraveligroup.mazechallenge.model.block.BlockTypes.START;
import static com.etraveligroup.mazechallenge.model.block.BlockTypes.END;
import static com.etraveligroup.mazechallenge.model.block.BlockTypes.EMPTY;

public class MazeSolverTest {

    private static RandomMouseMazeSolver randomMouse;
    private static RandomMouseMazeSolver randomMouseLarge;
    private static MarkThePathMazeSolver markThePath;
    private static MarkThePathMazeSolver markThePathLarge;

    private static MazeBuilder mazeBuilder = new MazeBuilder(".\\src\\test\\files\\mazeSolverTest\\simple_maze.txt");
    private static MazeBuilder largeMazeBuilder = new MazeBuilder(".\\src\\test\\files\\mazeSolverTest\\large_maze.txt");

    private static Maze maze;
    private static Maze largeMaze;

    private static Actor actor;

    private Block block1 = new Block(new Coordinates(1, 1), START);
    private Block block2 = new Block(new Coordinates(1, 2), EMPTY);
    private Block block3 = new Block(new Coordinates(1, 3), EMPTY);
    private Block block4 = new Block(new Coordinates(2, 3), EMPTY);
    private Block block5 = new Block(new Coordinates(3, 3), EMPTY);
    private Block block6 = new Block(new Coordinates(3, 2), EMPTY);
    private Block block7 = new Block(new Coordinates(3, 1), END);

    @BeforeClass
    public static void setUp() throws MazeFileMalformedException, IOException {
        // Built maze
        maze = mazeBuilder.builtMaze();
        // Create actor
        actor = new Actor();

        // Create maze solvers
        randomMouse = new RandomMouseMazeSolver(maze, actor);
        markThePath = new MarkThePathMazeSolver(maze, actor);

        // Built large maze
        largeMaze = largeMazeBuilder.builtMaze();

        // Create maze solvers
        randomMouseLarge = new RandomMouseMazeSolver(largeMaze, actor);
        markThePathLarge = new MarkThePathMazeSolver(largeMaze, actor);
    }

    // Simple maze
    @Test
    public void deterministicMarkThePathAlgorithmSimpleMaze() {
        // when:
        markThePath.solveMaze(false);
        // then:
        assertEquals(markThePath.getPath(), new ArrayList<>(List.of(block1, block2, block3, block4, block5, block6, block7)));
    }

    // Simple maze
    @Test
    public void randomnessMarkThePathAlgorithmSimpleMaze() {
        // when:
        markThePath.solveMaze(true);
        // then:
        assertEquals(markThePath.getPath(), new ArrayList<>(List.of(block1, block2, block3, block4, block5, block6, block7)));
    }

    // Simple maze
    @Test
    public void randomMouseAlgorithmSimpleMaze() {
        // when:
        randomMouse.solveMaze();
        // then:
        assertEquals(randomMouse.getPath(), new ArrayList<>(List.of(block1, block2, block3, block4, block5, block6, block7)));
    }

    // Large maze
    @Test
    public void deterministicMarkThePathAlgorithmLargeMaze() {
        // when:
        markThePathLarge.solveMaze(false);

        int lastBlockIndex = markThePathLarge.getPath().size() - 1;

        // then:
        assertEquals(markThePathLarge.getPath().get(lastBlockIndex).getBlockType(), END);
    }

    // Large maze
    @Test
    public void randomnessMarkThePathAlgorithmLargeMaze() {
        // when:
        markThePathLarge.solveMaze();

        int lastBlockIndex = markThePathLarge.getPath().size() - 1;

        // then:
        assertEquals(markThePathLarge.getPath().get(lastBlockIndex).getBlockType(), END);
    }

    // Large maze
    @Test
    public void randomMouseAlgorithmLargeMaze() {
        // when:
        randomMouseLarge.solveMaze();

        int lastBlockIndex = randomMouseLarge.getPath().size() - 1;

        // then:
        assertEquals(randomMouseLarge.getPath().get(lastBlockIndex).getBlockType(), END);
    }

}
