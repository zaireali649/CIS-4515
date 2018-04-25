package com.templecis.escaperoute.Maze_Stuff;

/**
 * Created by petermontanez on 4/25/18.
 */

// Breath-first  traversal for the path.
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
        //        V = new int[R + 1][C + 1];
//        for (int i = 1; i <= R; i++) {
//            for (int j = 1; j <= C; j++) {
//                V[i][j] = 0;
//            }
//        }
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
    public static boolean CreatePath(Maze maze, int srow, int scol, int erow, int ecol, LinkedList<Point> L) {
        boolean done = false;
        int c = scol;
        int r = srow;
        //        V[srow][scol] = 1;
        V[r][c] = 1;

        //        Point u = new Point(r, c);
//        LinkedList<Point> Q = new LinkedList<Point>();
//        Q.add(u);
//        while ((Q.size() != 0) && (!done)) {

//////////////////        Point u = new Point(r,c);
//////////////////        LinkedList<Point> P = new LinkedList<Point>();
//////////////////
//////////////////        P.add(u);
        //****************************     while((P.size() != 0) && (!done)){
//                if ((r > 1) && (V[r - 1][c] != 1) && (maze.can_go(r, c, 'U'))) {
//                    V[r - 1][c] = 1;
//                    P[(r - 2) * C + c] = u;
//                    Q.add(new Point(r - 1, c));
//                }
        if (!(done) && (r > 1) && (V[r - 1][c] != 1) && maze.can_go(r, c, 'U')) {
            r--;
            done = CreatePath(maze, r, c, erow, ecol, L);
            r++;
        }
//                if ((c < C) && (V[r][c + 1] != 1) && (maze.can_go(r, c, 'R'))) {
//                    V[r][c + 1] = 1;
//                    P[(r - 1) * C + c + 1] = u;
//                    Q.add(new Point(r, c + 1));
//                }
        if (!(done) && (c < C) && (V[r][c + 1] != 1) && maze.can_go(r, c, 'R')) {
            c++;
            done = CreatePath(maze, r, c, erow, ecol, L);
            c--;
        }
//                if ((r < R) && (V[r + 1][c] != 1) && (maze.can_go(r, c, 'D'))) {
//                    V[r + 1][c] = 1;
//                    P[r * C + c] = u;
//                    Q.add(new Point(r + 1, c));
//                }
        if (!(done) && (r < R) && (V[r + 1][c] != 1) && maze.can_go(r, c, 'D')) {
            r++;
            done = CreatePath(maze, r, c, erow, ecol, L);
            r--;
        }
//                if ((c > 1) && (V[r][c - 1] != 1) && (maze.can_go(r, c, 'L'))) {
//                    V[r][c - 1] = 1;
//                    P[(r - 1) * C + c - 1] = u;
//                    Q.add(new Point(r, c - 1));
//                }
        if (!(done) && (c > 1) && (V[r][c - 1] != 1) && maze.can_go(r, c, 'L')) {
            c--;
            done = CreatePath(maze, r, c, erow, ecol, L);
            c++;
        }

        //  ******************************     } // end while
        if (done) {
            L.add(new Point(r, c));
        }
        //            if ((r == erow) && (c == ecol)) {
//                done = true;
//            }
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