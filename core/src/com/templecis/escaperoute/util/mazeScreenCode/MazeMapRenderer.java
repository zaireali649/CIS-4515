package com.templecis.escaperoute.util.mazeScreenCode;


public class MazeMapRenderer extends MazeTileRenderer {

	public MazeMapRenderer(Maze maze, String wallFile, int magnify) {
		super(maze, wallFile);
		this.mazeMagnifyToWorld = magnify;
	}
}
