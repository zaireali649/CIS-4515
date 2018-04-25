package com.templecis.escaperoute.Maze_Stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.game.objects.MazeTile;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import java.awt.Point;
import java.util.LinkedList;
import javax.swing.JButton;

public class Maze
{
    private int M, N;
    private Intcoll[] C;
    private int[] Line;
    private boolean[] status;
    private int show;
    private LinkedList<Point> Path;
    private LinkedList<Node> L;
    private int start, delta;
//    private Stroke drawingStroke=new BasicStroke(2);+



    public Maze()
    {
        //super("A MAZE");

        M = 10;
        N = 10;
        L = new LinkedList<Node>();

        C=new Intcoll[N*M+1];
        int k;
        for (k=1; k<=M*N; k++)
        {
            C[k]=new Intcoll(M*N+1);
            C[k].insert(k);}

        start=50; delta=15;
        //Top=new Line2D.Double(start+delta, start, start+N*delta, start);
       // Bottom=new Line2D.Double(start, start+M*delta,start+(N-1)*delta, start+M*delta);
       // Left=new Line2D.Double(start, start, start, start+M*delta);
       // Right=new Line2D.Double(start+N*delta, start, start+N*delta, start+M*delta);
        int i=0, j=0;
        int size=M*(N-1)+(M-1)*N+1;
        //Add all internal lines
        Line=new int[size]; status=new boolean[size];
        for (k=1; k<size; k++) {
            Line[k]=k; status[k]=true;
        }
        // Randomly generate the next line to remove if its adjacent
        // cells are not already connected
        Random gen=new Random();
        int L=M*(N-1)+(M-1)*N;
        int S=1;
        int cell1, cell2;
        int temp;
        int count=0, a, b, slot;
        //remove enough lines to generate the maze
        while (count<M*N-1)
        {
            slot=gen.nextInt(L)+1;
            k=Line[slot];
            int D=M*(N-1);
            if (k<=D)
            {
                i=(k-1)/(N-1)+1;
                j=k-(i-1)*(N-1);
                cell1=(i-1)*N+j;
                cell2=cell1+1;
            }
            else
            {
                int K=k-D;
                i=(K-1)/N+1;
                j=K-N*(i-1);
                cell1=(i-1)*N+j;
                cell2=cell1+N;
            }
            //Find a and b - the sets of adjacent cells cell1 and cell2
            a=1;
            while (!C[a].belongs(cell1)) a++;
            b=1;
            while (!C[b].belongs(cell2)) b++;
            //if not in same set - take the union of the sets
            //and remove the line between the two cells
            if (a!=b)
            {
                if (a<b) {
                    union(C[a], C[b], C);
                C[b]=C[M*N-count];
                }
                else {
                    union(C[b], C[a], C);
                C[a]=C[M*N-count];
                }
                status[k]=false; count++;
                Line[slot]=Line[L];
                L--;
            }
        } //end of while

        //Mazepanel mazepanel=new Mazepanel();
       // setLayout(new BorderLayout());
       // add(mazepanel, BorderLayout.CENTER);

      //  mazepanel.addMouseListener(slotlistener);

//        JButton button=new JButton("Show Path");
        //add(button, BorderLayout.NORTH);

      //  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  setSize(1200, 800);
        //setVisible(true);
    }//end of constructor


    public Array<MazeTile> CreatePath(Maze maze, int srow, int scol, int erow, int ecol, LinkedList<Point> L) {
        boolean done = false;
        Array<MazeTile> mazeTile = new Array<MazeTile>();


        for (int r = srow; r < erow; r++){
            for(int c = scol; c < ecol; c++){
                MazeTile mz = new MazeTile();
                Gdx.app.log("THIS IS THE SIZE", "" + mazeTile.size);

                mz.position.x = r * 2;
                mz.position.y = c * 2;
                mz.topWall = !maze.can_go(r, c, 'U');
                mz.rightWall = !maze.can_go(r, c, 'R');
                mz.bottomWall = !maze.can_go(r, c, 'D');
                mz.leftWall = !maze.can_go(r, c, 'L');
                mazeTile.add(mz);

            }


        }
        return mazeTile;
    }
    public boolean can_go(int i, int j, char c)
    {
        int D=M*(N-1);
        boolean result=false;
        int K=N*(M-1)+(N-1)*M;
        if (c=='U') result = status[D+(i-2)*N+j];
        if (c=='R') result = status[(i-1)*(N-1)+j];
        if (c=='D') result = status[D+(i-1)*N+j];
        if (c=='L') result = status[(i-1)*(N-1)+j-1];
        return result;
    }

  /*  public void showPath(LinkedList<Point> P)
    {
        Path=P;
   // repaint();
    }*/

    private void union(Intcoll A, Intcoll B, Intcoll[] C)
    {
        int i=1; int n=B.get_howmany();
        while (n > 0)
        {
            if (B.belongs(i)) {
            A.insert(i);
            n--;}            i++;}
    }

    private class Node
    {
        private Point p; private Color c;

        public Node(int X, int Y, Color color)
        {
            p=new Point(X, Y);
            c=color;}
    }

} //end of class
