import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Yanjia Duan 
 * This class is a template for implementation of 
 * HW1 for CS540 section 2
 */
/**
 * Data structure to store each node.
 */
class Location {
	private int x;
	private int y;
	private Location parent;

	public Location(int x, int y, Location parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Location getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Location) {
			Location loc = (Location) obj;
			return loc.x == x && loc.y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * (hash + x);
		hash = 31 * (hash + y);
		return hash;
	}
}

public class KingsKnightmare {
	//represents the map/board
	private static boolean[][] board;
	//represents the goal node
	private static Location king;
	//represents the start node
	private static Location knight;
	//y dimension of board
	private static int n;
	//x dimension of the board
	private static int m;
	//enum defining different algo types
	enum SearchAlgo{
		BFS, DFS, ASTAR;
	}

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			//loads the input file and populates the data variables
			SearchAlgo algo = loadFile(args[0]);
			if (algo != null) {
				switch (algo) {
					case DFS :
						executeDFS();
						break;
					case BFS :
						executeBFS();
						break;
					case ASTAR :
						executeAStar();
						break;
					default :
						break;
				}
			}
		}
	}

	/**
	 * Implementation of Astar algorithm for the problem
	 */
	private static void executeAStar() {
		int expand = 0, i, j;
		PriorityQ<Location> priorityQ = new PriorityQ<Location>();
		boolean[][] explored = new boolean[n][m];
		boolean[][] Frontier = new boolean[n][m];
		int h[][] = new int [n][m], move[][] = new int [n][m], score[][] = new int [n][m];
		int dir[][] = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
		int[][] path;
		Location node = knight, child;
		for (i = 0; i < n; i++)
			for (j = 0; j < m; j++)
				score[i][j] = h[i][j] = Math.abs(i-king.getY()) + Math.abs(j-king.getX());
		priorityQ.add(knight,score[knight.getY()][knight.getX()]);
		while (!king.equals(node)) {
			if (priorityQ.isEmpty()) {
				System.out.println("NOT REACHABLE");
				System.out.println("Expanded Nodes: " + expand);
				break;
			}
			node = priorityQ.poll().getKey();
			
			if (node.equals(king)) {
				path = new int[expand + 1][2];
				for (j = expand; j > 0; j--) {
					path[j][0] = node.getX();
					path[j][1] = node.getY();
					node = node.getParent();
					if (node == null)
						break;
				}
				path[j][0] = knight.getX();
				path[j][1] = knight.getY();
				for (; j <= expand; j++)
					System.out.println(path[j][0] + " " + path[j][1]);
				System.out.println("Expanded Nodes: " + expand);
				break;
			}
			
			for (i = 0; i < 8; i++) {
				child = new Location(node.getX()+dir[i][0], node.getY()+dir[i][1], node);
				if (0 <= child.getX() && child.getX() < m && 0 <= child.getY() && child.getY() < n && !board[child.getY()][child.getX()]) {
					if (!explored[child.getY()][child.getX()] && !Frontier[child.getY()][child.getX()]) {
						move[child.getY()][child.getX()] = move[node.getY()][node.getX()] + 3;
						score[child.getY()][child.getX()] = h[child.getY()][child.getX()] + move[child.getY()][child.getX()];
						Frontier[child.getY()][child.getX()] = true;
						priorityQ.add(child, score[child.getY()][child.getX()]);
					}
					else if (h[child.getY()][child.getX()] + move[node.getY()][node.getX()] + 3 < score[child.getY()][child.getX()]) {
						priorityQ.remove(child);
						score[child.getY()][child.getX()] = h[child.getY()][child.getX()] + move[node.getY()][node.getX()] + 3;
						priorityQ.add(child, score[child.getY()][child.getX()]);
						Frontier[child.getY()][child.getX()] = true;
					}
				}
			}
			explored[node.getY()][node.getX()] = true;
			expand++;
		}
	}

	/**
	 * Implementation of BFS algorithm
	 */
	private static void executeBFS() {
		int expand = 0, i, j;
		Queue<Location> queue = new LinkedList<Location>();
		boolean[][] explored = new boolean[n][m];
		boolean[][] Frontier = new boolean[n][m];
		int dir[][] = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
		int[][] path;
		Location node = knight, child;
		
		queue.offer(knight);
		while (!king.equals(node)) {
			if (queue.isEmpty()) {
				System.out.println("NOT REACHABLE");
				System.out.println("Expanded Nodes: " + expand);
				break;
			}
			node = (Location)queue.poll();
			explored[node.getY()][node.getX()] = true;
			expand++;
			for (i = 0; i < 8; i++) {
				child = new Location (node.getX() + dir[i][0], node.getY() + dir[i][1], node);
				if(0 <= child.getX() && child.getX() < m && 0 <= child.getY() && child.getY() < n && !board[child.getY()][child.getX()] && !explored[child.getY()][child.getX()] && !Frontier[child.getY()][child.getX()]) {
					if (child.equals(king)) {
						node = child;
						path = new int[expand + 1][2];
						for (j = expand; j > 0; j--) {
							path[j][0] = child.getX();
							path[j][1] = child.getY();
							child = child.getParent();
							if (child == null)
								break;
						}
						path[j][0] = knight.getX();
						path[j][1] = knight.getY();
						for (; j <= expand; j++)
							System.out.println(path[j][0] + " " + path[j][1]);
						System.out.println("Expanded Nodes: " + expand);
						break;
					}
					queue.offer(child);
					Frontier[child.getY()][child.getX()] = true;
				}
			}
			
		}
	}
	
	/**
	 * Implementation of DFS algorithm
	 */
	private static void executeDFS() {
		int expand = 0, i, j;
		Stack<Location> st = new Stack<Location>();
		boolean[][] explored = new boolean[n][m];
		boolean[][] Frontier = new boolean[n][m];
		int dir[][] = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
		int[][] path;
		Location node = knight, child;
		
		st.push(knight);
		while (!king.equals(node)) {
			if (st.empty()) {
				System.out.println("NOT REACHABLE");
				System.out.println("Expanded Nodes: " + expand);
				break;
			}
			node = (Location)st.pop();
			explored[node.getY()][node.getX()] = true;
			expand++;
			for (i = 0; i < 8; i++) {
				child = new Location (node.getX() + dir[i][0], node.getY() + dir[i][1], node);
				if (0 <= child.getX() && child.getX() < m && 0 <= child.getY() && child.getY() < n && !board[child.getY()][child.getX()] && !explored[child.getY()][child.getX()] && !Frontier[child.getY()][child.getX()]) {
					if (child.equals(king)) {
						node = child;
						path = new int[expand + 1][2];
						for (j = expand; j > 0; j--) {
							path[j][0] = child.getX();
							path[j][1] = child.getY();
							child = child.getParent();
							if (child == null)
								break;
						}
						path[j][0] = knight.getX();
						path[j][1] = knight.getY();
						for (; j <= expand; j++)
							System.out.println(path[j][0] + " " + path[j][1]);
						System.out.println("Expanded Nodes: " + expand);
						break;
					}
					st.push(child);
					Frontier[child.getY()][child.getX()] = true;
				}
			}
			
		}
	}
	
	/**
	 * 
	 * @param filename
	 * @return Algo type
	 * This method reads the input file and populates all the 
	 * data variables for further processing
	 */
	private static SearchAlgo loadFile(String filename) {
		File file = new File(filename);
		try {
			Scanner sc = new Scanner(file);
			SearchAlgo algo = SearchAlgo.valueOf(sc.nextLine().trim().toUpperCase());
			n = sc.nextInt();
			m = sc.nextInt();
			sc.nextLine();
			board = new boolean[n][m];
			for (int i = 0; i < n; i++) {
				String line = sc.nextLine();
				for (int j = 0; j < m; j++) {
					if (line.charAt(j) == '1') {
						board[i][j] = true;
					} else if (line.charAt(j) == 'S') {
						knight = new Location(j, i, null);
					} else if (line.charAt(j) == 'G') {
						king = new Location(j, i, null);
					}
				}
			}
			sc.close();
			return algo;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}