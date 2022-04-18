package com.templecis.escaperoute.Maze_Stuff;

import java.util.Random;

/**
 * Created by Ziggy on 4/25/2018.
 */

public class MazeGenerator {
    private int maze_size;
    private int n;
    private int m;
    // dimension of maze
    public boolean[][] north;     // is there a wall to north of cell i, j
    public boolean[][] east;
    public boolean[][] south;
    public boolean[][] west;
    public boolean[][] visited;
    private boolean done = false;

    public MazeGenerator(int n) {
        this.n = n;
        this.m = n * 4;
        this.maze_size = n;
        init();
        generate();
    }

    public int return_size(){
        return maze_size;
    }


    private void init() {
        // initialize border cells as already visited
        visited = new boolean[n+2][m +2];
        for (int x = 0; x < n+2; x++) {
            visited[x][0] = true;
            visited[x][m+1] = true;
        }
        for (int y = 0; y < m+2; y++) {
            visited[0][y] = true;
            visited[n+1][y] = true;
        }


        // initialze all walls as present
        north = new boolean[n+2][m+2];
        east  = new boolean[n+2][m+2];
        south = new boolean[n+2][m+2];
        west  = new boolean[n+2][m+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < m+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }
    }


    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                Random ran = new Random();
                double r = ran.nextInt(4);
                if (r == 0 && !visited[x][y+1]) {
                    north[x][y] = false;
                    south[x][y+1] = false;
                    generate(x, y + 1);
                    break;
                }
                else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !visited[x][y-1]) {
                    south[x][y] = false;
                    north[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);

/*
        // delete some random walls
        for (int i = 0; i < n; i++) {
            int x = 1 + StdRandom.uniform(n-1);
            int y = 1 + StdRandom.uniform(n-1);
            north[x][y] = south[x][y+1] = false;
        }

        // add some random walls
        for (int i = 0; i < 10; i++) {
            int x = n/2 + StdRandom.uniform(n/2);
            int y = n/2 + StdRandom.uniform(n/2);
            east[x][y] = west[x+1][y] = true;
        }
*/

    }



    // solve the maze using depth-first search
    private void solve(int x, int y) {
        if (x == 0 || y == 0 || x == n+1 || y == n+1) return;
        if (done || visited[x][y]) return;
        visited[x][y] = true;



        // reached middle
        if (x == n/2 && y == n/2) done = true;

        if (!north[x][y]) solve(x, y + 1);
        if (!east[x][y])  solve(x + 1, y);
        if (!south[x][y]) solve(x, y - 1);
        if (!west[x][y])  solve(x - 1, y);

        if (done) return;


    }

    // solve the maze starting from the start state
    public void solve() {
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                visited[x][y] = false;
        done = false;
        solve(1, 1);
    }





/*    // a test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Maze maze = new Maze(n);
        maze.draw();
        maze.solve();
    }*/

}