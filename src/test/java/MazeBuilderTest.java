import com.etraveligroup.mazechallenge.model.maze.throwable.EmptyMazeFileException;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileIllegalCharacterException;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileMalformedException;
import com.etraveligroup.mazechallenge.model.maze.MazeBuilder;
import org.junit.Test;

public class MazeBuilderTest {

    private MazeBuilder mazeBuilderEmptyFile = new MazeBuilder(".\\src\\test\\files\\mazeBuilderTest\\empty_maze.txt");
    private MazeBuilder mazeBuilderIllegalCharacter = new MazeBuilder(".\\src\\test\\files\\mazeBuilderTest\\illegal_maze.txt");
    private MazeBuilder mazeBuilderMalformedNoStartBlock = new MazeBuilder(".\\src\\test\\files\\mazeBuilderTest\\no_start_maze.txt");
    private MazeBuilder mazeBuilderMalformedNoEndBlock = new MazeBuilder(".\\src\\test\\files\\mazeBuilderTest\\no_end_maze.txt");
    private MazeBuilder mazeBuilderMalformedMoreThanOneStarts = new MazeBuilder(".\\src\\test\\files\\mazeBuilderTest\\two_starts_maze.txt");
    private MazeBuilder mazeBuilderMalformedMoreThanOneEnds = new MazeBuilder(".\\src\\test\\files\\mazeBuilderTest\\two_ends_maze.txt");

    // Empty file
    @Test(expected = EmptyMazeFileException.class)
    public void testEmptyMazeException() throws MazeFileMalformedException {
        mazeBuilderEmptyFile.builtMaze();
    }

    // Maze file contains character other than ('S', '_', 'X', 'G')
    @Test(expected = MazeFileIllegalCharacterException.class)
    public void testIllegalCharacterException() throws MazeFileMalformedException {
    mazeBuilderIllegalCharacter.builtMaze();
    }

    // Starting Block does not exist
    @Test(expected = MazeFileMalformedException.class)
    public void testMalformedMazeNoStartBlock() throws MazeFileMalformedException {
        mazeBuilderMalformedNoStartBlock.builtMaze();
    }

    // Ending Block does not exist
    @Test(expected = MazeFileMalformedException.class)
    public void testMalformedMazeNoEndBlock() throws MazeFileMalformedException {
        mazeBuilderMalformedNoEndBlock.builtMaze();
    }

    // More than 1 Starting blocks > 1
    @Test(expected = MazeFileMalformedException.class)
    public void testMalformedMazeMoreThanOneStartPoints() throws MazeFileMalformedException {
        mazeBuilderMalformedMoreThanOneStarts.builtMaze();
    }

    // More than 1 Ending blocks > 1
    @Test(expected = MazeFileMalformedException.class)
    public void testMalformedMazeMoreThanOneEndPoints() throws MazeFileMalformedException {
        mazeBuilderMalformedMoreThanOneEnds.builtMaze();
    }

}
