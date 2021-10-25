package com.etraveligroup.mazechallenge.model.maze;

public class FileValidator {

    private int startPointCount = 0, endPointCount = 0;

    public boolean validateNextCharacter(Character c) {
        switch (c) {
            case '_':
            case 'X':
                return true;
            case 'S':
                return ++startPointCount <= 1;
            case 'G':
                return ++endPointCount <= 1;
            default:
                return false;
        }
    }

    public boolean startPointExist() {
        return startPointCount != 0;
    }

    public boolean endPointExist() {
        return endPointCount != 0;
    }

}
