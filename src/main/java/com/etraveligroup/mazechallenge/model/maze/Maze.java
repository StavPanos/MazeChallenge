package com.etraveligroup.mazechallenge.model.maze;

import com.etraveligroup.mazechallenge.model.block.Block;
import com.etraveligroup.mazechallenge.model.block.Coordinates;

import java.util.HashMap;
import java.util.Map;

public class Maze {

    public static final int MAX_DIMENSION = Integer.MAX_VALUE;

    private int mazeHeight;

    private int mazeWidth;

    private Block mazeStart;

    private Block mazeEnd;

    private String name;

    private Map<Coordinates, Block> blocks = new HashMap<>();

    protected Maze() {
    }

    public Map<Coordinates, Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(Map<Coordinates, Block> blocks) {
        this.blocks = blocks;
    }

    public Block getMazeStart() {
        return mazeStart;
    }

    public void setMazeStart(Block mazeStart) {
        this.mazeStart = mazeStart;
    }

    public Block getMazeEnd() {
        return mazeEnd;
    }

    public void setMazeEnd(Block mazeEnd) {
        this.mazeEnd = mazeEnd;
    }

    public int getMazeHeight() {
        return mazeHeight;
    }

    public void setMazeHeight(int mazeHeight) {
        this.mazeHeight = mazeHeight;
    }

    public int getMazeWidth() {
        return mazeWidth;
    }

    public void setMazeWidth(int mazeWidth) {
        this.mazeWidth = mazeWidth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int x=1;x<=mazeHeight;x++) {
            for (int y=1;y<=mazeWidth;y++) {
                stringBuilder.append(blocks.get(new Coordinates(x, y)).getBlockType()).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
