package com.templecis.escaperoute.Maze_Stuff;

public class Maze {

    public enum PLACE {wall, empty, visited};

    private PLACE maze[][] = new PLACE[Const.maze_height][Const.maze_width];

    //public String toString() {
    //    return new MazeStringRenderer(this).toString();
    //}

    public static boolean isValidPlace(int row, int col) {
        return
                ((row >= 0) && (row < Const.maze_height)) &&
                        ((col >= 0) && (col < Const.maze_width));
    }

    public void setPlace(int row, int col, PLACE place) {
        if (isValidPlace(row, col)) {
            maze[row][col] = place;
        }
    }

    public boolean empty(){
        return true;
    }

    public PLACE getPlace(int row, int col) {
        PLACE place = null;

        if (isValidPlace(row, col)) {
            place = maze[row][col];
        }

        return place;
    }

}
