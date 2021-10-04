import com.etraveligroup.mazechallenge.MazeSolver;
import com.etraveligroup.mazechallenge.model.actor.Actor;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileMalformedException;
import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.block.Coordinates;
import com.etraveligroup.mazechallenge.model.maze.Maze;
import com.etraveligroup.mazechallenge.model.maze.MazeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static com.etraveligroup.mazechallenge.model.block.BlockTypes.START;
import static com.etraveligroup.mazechallenge.model.block.BlockTypes.END;
import static com.etraveligroup.mazechallenge.model.block.BlockTypes.EMPTY;

public class MazeSolverTest {

    private MazeSolver mazeSolver;
    private MazeSolver mazeSolver2;

    private MazeBuilder mazeBuilder = new MazeBuilder(".\\src\\test\\files\\mazeSolverTest\\simple_maze.txt");
    private MazeBuilder largeMazeBuilder = new MazeBuilder(".\\src\\test\\files\\mazeSolverTest\\large_maze.txt");

    private Maze maze;
    private Maze largeMaze;

    private Actor actor;

    private Block block1 = new Block(new Coordinates(1, 1), START);
    private Block block2 = new Block(new Coordinates(1, 2), EMPTY);
    private Block block3 = new Block(new Coordinates(1, 3), EMPTY);
    private Block block4 = new Block(new Coordinates(2, 3), EMPTY);
    private Block block5 = new Block(new Coordinates(3, 3), EMPTY);
    private Block block6 = new Block(new Coordinates(3, 2), EMPTY);
    private Block block7 = new Block(new Coordinates(3, 1), END);

    @Before
    public void setUp() throws MazeFileMalformedException {
        // Built maze
        maze = mazeBuilder.builtMaze();
        // Create actor
        actor = new Actor();

        // Create maze solver
        mazeSolver = new MazeSolver(maze);

        // Built large maze
        largeMaze = largeMazeBuilder.builtMaze();
        // Create maze solver
        mazeSolver2 = new MazeSolver(largeMaze);
    }

    // Simple maze
    @Test
    public void deterministicMarkThePathAlgorithmSimpleMaze() {
        // when:
        mazeSolver.markThePath(actor, false);
        // then:
        assertEquals(mazeSolver.getPath(), new ArrayList<Block>(List.of(block1, block2, block3, block4, block5, block6, block7)));
    }

    // Simple maze
    @Test
    public void randomnessMarkThePathAlgorithmSimpleMaze() {
        // when:
        mazeSolver.markThePath(actor, true);
        // then:
        assertEquals(mazeSolver.getPath(), new ArrayList<Block>(List.of(block1, block2, block3, block4, block5, block6, block7)));
    }

    // Simple maze
    @Test
    public void randomMouseAlgorithmSimpleMaze() {
        // when:
        mazeSolver.randomMouse(actor);
        // then:
        assertEquals(mazeSolver.getPath(), new ArrayList<Block>(List.of(block1, block2, block3, block4, block5, block6, block7)));
    }

    // Large maze
    @Test
    public void deterministicMarkThePathAlgorithmLargeMaze() {
        // when:
        mazeSolver2.markThePath(actor, false);

        int lastBlockIndex = mazeSolver2.getPath().size() - 1;

        // then:
        assertEquals(mazeSolver2.getPath().get(lastBlockIndex).getBlockType(), END);
    }

    // Large maze
    @Test
    public void randomnessMarkThePathAlgorithmLargeMaze() {
        // when:
        mazeSolver2.markThePath(actor, true);

        int lastBlockIndex = mazeSolver2.getPath().size() - 1;

        // then:
        assertEquals(mazeSolver2.getPath().get(lastBlockIndex).getBlockType(), END);
    }

    // Large maze
    @Test
    public void randomMouseAlgorithmLargeMaze() {
        // when:
        mazeSolver2.randomMouse(actor);

        int lastBlockIndex = mazeSolver2.getPath().size() - 1;

        // then:
        assertEquals(mazeSolver2.getPath().get(lastBlockIndex).getBlockType(), END);
    }

}
