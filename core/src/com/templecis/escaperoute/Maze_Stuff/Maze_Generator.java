package com.templecis.escaperoute.Maze_Stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Maze_Generator {

    private Maze maze;

    public void MazeCreator() {
        maze = new Maze();
        createMaze();
        log();
    }

    public Maze getMaze() {
        return maze;
    }

    private void fillWithWalls() {
        for (int r = 0; r < Const.maze_height; r++) {
            for (int c = 0; c < Const.maze_width; c++) {
                maze.setPlace(r, c, Maze.PLACE.wall);
            }
        }
    }

    private void createDefaultRooms() {
        for (int r = 1; r < Const.maze_height; r += 2) {
            for (int c = 1; c < Const.maze_width; c += 2) {
                maze.setPlace(r, c, Maze.PLACE.empty);
            }
        }
    }



    private Place createExit() {
        // exit should be on the left or right side
        int c = (Math.random() < 0.5) ? 0 : Const.maze_width - 1;
        // exit should be on odd numbers
        int r = MathUtils.random(Const.maze_height / 2) * 2 + 1;
        return new Place(r, c);
    }

    private void log() {
        //MazeStringRenderer mazeStringRenderer = new MazeStringRenderer(this.maze);
        Gdx.app.log("MazeCreator", "\n" + maze.toString());
    }

    private void createMaze() {
        fillWithWalls();

        createDefaultRooms();

        Place exit = createExit();

        maze.setPlace(exit.row, exit.col, Maze.PLACE.empty);

        Place nextRoom =
                new Place(
                        exit.row, (exit.col == 0) ? 1 : exit.col -1);

        createRoom(nextRoom);
    }

    private void createRoom(Place room) {
        // "Mark the current cell as visited"
        maze.setPlace(room.row, room.col, Maze.PLACE.visited);

        // "and get a list of its neighbors"
        // it may be a bit faster if I get only the
        // not visited neighbors, but definitely consumes
        // less memory than if I use a list with all
        // neighbor rooms
        Array<Place> neighbours = getNotVisitedNeighbours(room);

        // no neighbors, nothing to do, exit
        if (neighbours.size == 0) {
            return;
        }

        // "starting with a randomly selected neighbor"
        neighbours.shuffle();

        // "For each neighbor"
        while (neighbours.size > 0) {
            Place nextRoom = neighbours.pop();

            // "If that neighbor hasn't been visited,"
            // it must be checked here also
            // (getNotVisitedNeighbours is not enough)
            // because as time goes by algorithm visits
            // places
            if (!isVisitedOrOutOfWorld(nextRoom)) {
                // "remove the wall between this cell and that neighbor"
                maze.setPlace(
                        (room.row + nextRoom.row) / 2,
                        (room.col + nextRoom.col) / 2,
                        Maze.PLACE.empty);

                // "and then recur with that neighbor as the current cell"
                createRoom(nextRoom);
            }
        }
    }

    private boolean isVisitedOrOutOfWorld(Place place) {
        int row = place.row;
        int col = place.col;

        return
                !Maze.isValidPlace(row, col) ||
                        (maze.getPlace(row, col) == Maze.PLACE.visited);
    }

    private Array<Place> getNotVisitedNeighbours(Place room) {
        Array<Place> notVisited = new Array<Place>(true, 4);

        int r = room.row;
        int c = room.col;

        // there are WALLS or DOORS at (r,c-1),(r,c+1),...
        // because the walls are also consumes 1 place
        Place[] neighbours = {
                new Place(r, c - 2), new Place(r, c + 2),
                new Place(r - 2, c), new Place(r + 2, c)
        };

        for (Place p : neighbours) {
            if (!isVisitedOrOutOfWorld(p)) notVisited.add(p);
        }

        return notVisited;
    }

    class Place{
        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        int row;
        int col;

        public Place(int row, int col) {
            super();
            this.row = row;
            this.col = col;
        }

    }


}
