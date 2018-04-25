package com.templecis.escaperoute.Maze_Stuff;

//import bp.gdx.maze.Maze.PLACE;
import com.templecis.escaperoute.Maze_Stuff.Maze.PLACE;

public class MazeStringRenderer implements StringRenderer {

	private final Maze maze;

	public MazeStringRenderer(Maze maze) {
		this.maze = maze;
	}

	public String toString() {
		char carr[] = new char[(Const.maze_width + 1) * Const.maze_height];

		for (int r = 0; r < Const.maze_height; r++) {
			for (int c = 0; c < Const.maze_width; c++) {
				char ch = (maze.getPlace(r, c) == PLACE.wall) ? WALL : NOTWALL;
				carr[ (Const.maze_height - r - 1) * (Const.maze_width + 1) + c] = ch;
			}
			carr[ (r + 1) * (Const.maze_width + 1) - 1 ] = '\n';
		}

		return new String(carr);
	}

	public void render() {
		this.toString();
	}

}
