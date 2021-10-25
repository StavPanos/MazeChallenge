package com.etraveligroup.mazechallenge.model.maze;

import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.block.BlockTypes;
import com.etraveligroup.mazechallenge.model.block.Coordinates;
import com.etraveligroup.mazechallenge.model.maze.throwable.EmptyMazeFileException;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileIllegalCharacterException;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeFileMalformedException;
import com.etraveligroup.mazechallenge.model.maze.throwable.MazeSizeOutOfBoundsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Maze Builder class is used for building mazes from external sources. Also provides validation while building the maze.
 **/
public class MazeBuilder {

    private static final Logger logger = LogManager.getLogger(MazeBuilder.class);

    /**
     * The system path for the directory of the maze text file
     **/
    private final String FILE_PATH;

    public MazeBuilder() {
        FILE_PATH = ".\\files\\maze.txt";
    }

    public MazeBuilder(String filePath) {
        FILE_PATH = filePath;
    }

    private Maze maze = new Maze();
    private FileValidator validator = new FileValidator();

    public Maze builtMaze() throws MazeFileMalformedException, IOException {
        Map<Coordinates, Block> blocks = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH))) {
            logger.info("Maze built started...");
            logger.info("Reading from file: " + FILE_PATH);

            String line;
            int lineCount = 0, x = 1, y = 1;

            while ((line = bufferedReader.readLine()) != null) {
                try {
                    lineCount++;
                    int lineLentgh = line.length();
                } catch (ArithmeticException ex) {
                    throw new MazeSizeOutOfBoundsException("Maze too large!");
                }

                char[] tempArr = line.toCharArray();
                y = 1;
                for (char ch : tempArr) {
                    if (validator.validateNextCharacter(ch)) {
                        Coordinates cor = new Coordinates(x, y++);
                        blocks.put(cor, new Block(cor, mapBlockType(cor, ch)));
                    } else {
                        throw new MazeFileIllegalCharacterException("Not acceptable character!");
                    }
                }
                x++;
            }
            maze.setMazeHeight(lineCount);
            maze.setMazeWidth(y - 1);

            if (lineCount == 0) {
                throw new EmptyMazeFileException("Empty maze!");
            } else if (!validator.startPointExist()) {
                throw new MazeFileMalformedException("Maze should always have 1 start point");
            } else if (!validator.endPointExist()) {
                throw new MazeFileMalformedException("Maze should always have 1 end point");
            }
            logger.info("File :" + FILE_PATH + " reading completed!");
            logger.info("Maze built competed!\n");

        } catch (FileNotFoundException f) {
            throw new MazeFileMalformedException("Error! File does not exist");
        } catch (IOException e) {
            logger.error("Error! Unexpacted problem while reading file");
            throw e;
        }
        maze.setBlocks(blocks);
        maze.setName("Maze :" + FILE_PATH);

        return maze;
    }

    private BlockTypes mapBlockType(Coordinates coordinates, char c) {
        switch (c) {
            case '_':
                return BlockTypes.EMPTY;
            case 'X':
                return BlockTypes.WALL;
            case 'S':
                maze.setMazeStart(new Block(coordinates, BlockTypes.START));
                return BlockTypes.START;
            default:
                maze.setMazeEnd(new Block(coordinates, BlockTypes.END));
                return BlockTypes.END;
        }
    }

}
