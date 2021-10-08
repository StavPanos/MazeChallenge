package com.etraveligroup.mazechallenge.model.block;

import java.util.Objects;

public class Block {
    private Coordinates coordinates;
    private BlockTypes blockType;

    public Block(Coordinates coordinates, BlockTypes blockType) {
        this.coordinates = coordinates;
        this.blockType = blockType;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public BlockTypes getBlockType() {
        return blockType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(coordinates, block.coordinates) && blockType == block.blockType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, blockType);
    }

    @Override
    public String toString() {
        return "(" + coordinates.getX() + ":" + coordinates.getY() +
                (blockType.equals(BlockTypes.START) || blockType.equals(BlockTypes.END) ? " " + blockType: "")
                + ")";
    }
}
