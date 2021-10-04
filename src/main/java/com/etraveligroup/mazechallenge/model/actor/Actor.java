package com.etraveligroup.mazechallenge.model.actor;

import com.etraveligroup.mazechallenge.model.block.Coordinates;

public class Actor {

    protected Coordinates currentPosition;

    public Actor() {

    }

    public Actor(Coordinates currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Coordinates move(Directions direction) {
        int x = currentPosition.getX(), y = currentPosition.getY();

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
        currentPosition = new Coordinates(x, y);

        return currentPosition;
    }

    public Coordinates getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Coordinates currentPosition) {
        this.currentPosition = currentPosition;
    }
}
