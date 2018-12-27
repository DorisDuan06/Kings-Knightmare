# Kings-Knightmare
This project implements a modified chess problem. We have a modified chessboard that is size n × m. On the chessboard is a white king, a black knight, and several obstacles. Once the game starts, the knight can make a possibly infinite number of moves, and the king does not move. The goal is to find a path for the knight to capture the king, while avoiding all obstacles.
A state will be represented by the current coordinates of the knight, (x, y). The possible moves of a knight are shown in the figure below by the 8 positions containing dark circles. Use the upper-left corner of the map as the origin, with coordinates (0, 0). When expanding a node to create its children nodes, each child’s position is computed from the current state’s position by: (+2, +1), (+1, +2), (-1, +2), (-2, +1), (-2, -1), (-1, -2), (+1, -2), (+2, -1); these positions are numbered, respectively, 1-8 in figure below. The program uses this order for the children! Note that some of these moves may not be legal, i.e., when there is an obstacle at the destination position, or when the destination position is off the map. Note: it does not matter if the positions in-between the current position and destination position in a single move are obstacles.
![image](https://github.com/DorisDuan06/Kings-Knightmare/blob/master/chessboard.jpg)

This program finds a sequence of moves for the knight to capture the king using Depth-First Search (DFS), Breadth-First Search (BFS) and A* Search (AStar). The programming assumptions for each search method are as follows:

(i) DFS: Use a stack for implementing Frontier, pushing child nodes on to the stack in the order 1-8 shown above, and popping them from Frontier in the reverse order of 8-1. Perform the goal test for a node when it is created (i.e., before putting it on Frontier), not when it is removed from Frontier.

(ii) BFS: Insert expanded nodes in Frontier using a queue in the order 1-8 shown above, and hence removing them from Frontier in the same order 1-8. Perform the goal test for a node when it is created (i.e., before putting it on Frontier), not when it is removed from Frontier. 

(iii) AStar: Assume each move of the knight costs 3. Thus, all the moves 1-8 in the figure above will have equal cost of 3. The heuristic function is the Manhattan distance between a node’s current position (x, y) and the goal position (x_goal, y_goal). That is, |x – x_goal| + |y – y_goal|. If two child nodes have the same priority score, the tie must be broken based on the neighbor number shown above. For example, node 1 must be added to Frontier before node 8 in the priority queue if nodes 1 and 8 have same priority score. Use the provided implementation of a priority queue given in PriorityQ. This implementation sorts and maintains the queue in ascending order of score. In case of ties, this priority queue places the node that is inserted first ahead of another node (sibling or non-sibling) with the same score. So, it is important to insert children nodes in the order 1-8, to enable the priority queue to take care of tie-breaking cases automatically. Perform the goal test for a node when it is removed from Frontier. 

For efficiency, this program maintains a set of explored positions to avoid repeated states in all three search methods. The Explored set is implemented simply as a Boolean array indicating if each board position has been previously expanded or not. Each board position needs to be expanded at most once because, for this problem, when a node is expanded, it is guaranteed we have found the shortest or lowest cost path to that node’s position. 

Each test case will be in a separate text file. The first line contains the search method to use, either “bfs”, “dfs” or “astar” (all lower case). The second line will be two integers, n (corresponding to the y coordinate, specifying the number of rows on the board) and m (corresponding to the x coordinate, specifying the number of columns on the board), indicating the size of the chessboard where 5 < n, m < 201. Next will be n lines, each line containing m characters, each being either “S”, ”G”, “0”, or ”1”. “S” is the initial knight position. “G” is the king’s position (i.e., the goal position). “0” is an empty space. “1” is an obstacle.
