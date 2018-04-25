package com.templecis.escaperoute.Maze_Stuff;

/**
 * Created by petermontanez on 4/25/18.
 */

// Breath-first  traversal for the path.
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.game.objects.MazeTile;

import java.util.*;
import java.awt.Point;
import java.util.LinkedList;

public class YourGraphicMaze {

    private Maze maze;
    private static int R, C;
    //    private static int[][] V;
    private static int[][] V;

    public YourGraphicMaze() {
        // an R rows x C columns maze
        maze = new Maze();
        R = maze.Rows();
        C = maze.Cols();

        V = new int[R + 1][C + 1];
        for (int i = 1; i <= R; i++) {
            for (int j = 1; j < C; j++) {
                V[i][j] = 0;
            }
        }
        // Path holds the cells of the path
        LinkedList<Point> Path = new LinkedList<Point>();
        // Create the path
        CreatePath(maze, 1, 1, R, C, Path);
        // show the path in the maze
        maze.showPath(Path);
    }

    // Creates the path through maze, starting at cell (srow, scol)
    // and ending at cell (erow and ecol),  in L


    public Array<MazeTile> maze_tiles_up;
    public Array<MazeTile> maze_tiles_down;
    public Array<MazeTile> maze_tiles_left;
    public Array<MazeTile> maze_tiles_right;


    //then merge all directions into one array


    public boolean CreatePath(Maze maze, int srow, int scol, int erow, int ecol, LinkedList<Point> L) {
        boolean done = false;
        int c = scol;
        int r = srow;
        //        V[srow][scol] = 1;
        V[r][c] = 1;




        if (!(done) && (r > 1) && (V[r - 1][c] != 1) && maze.can_go(r, c, 'U')) {
            MazeTile mz = new MazeTile();
            mz.position.x = r;
            mz.position.y = c;
            mz.topWall = maze.can_go(r, c, 'U');
            maze_tiles_up.add(mz);

            r--;
            done = CreatePath(maze, r, c, erow, ecol, L);
            r++;
        }

        if (!(done) && (c < C) && (V[r][c + 1] != 1) && maze.can_go(r, c, 'R')) {



            c++;
            done = CreatePath(maze, r, c, erow, ecol, L);
            c--;
        }

        if (!(done) && (r < R) && (V[r + 1][c] != 1) && maze.can_go(r, c, 'D')) {
            r++;
            done = CreatePath(maze, r, c, erow, ecol, L);
            r--;
        }

        if (!(done) && (c > 1) && (V[r][c - 1] != 1) && maze.can_go(r, c, 'L')) {
            c--;
            done = CreatePath(maze, r, c, erow, ecol, L);
            c++;
        }

        if (done) {
            L.add(new Point(r, c));
        }

        if (r == erow && c == ecol) {
            //Doesn't add last point to LinkedList if not added here
            L.add(new Point(r, c));
            done = true;
            return done;
        }
        return done;
    }

    public static void main(String[] args) {
        {
            new YourGraphicMaze();
        }
    }
}