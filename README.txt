COVID
A project that simulates the spread of contagious virus using a relationship graph.


Input:
java StopContagion [-d] [-r RADIUS] num_nodes input_file,
where [-d] and [-r] are optional parameters. And we consider the examples provided by the handout, here's a few points:
1. PLEASE type in [-d] when testing removeByDegree();
2. PLEASE type in [-r RADIUS] if the radius is not 2, eg. [-r 2];
3. PLEASE type in a space between every value and no space at the end of the input;
4. The radius MUST be placed in front of the number of removals;
5. the file name MUST be entered at the end of entire input
6. type in [-t] somewhere in the input(in front of file name) to test to bonus trace function
SAMPLE INPUT: java StopContagion [-d] [-t] [-r 3] 5 a.txt

The class Graph.java contains the basic property of a graph which allows us the add/remove vertices and edges. The
getters and setters also include calculating degree and collective influence of a vertex. Some important methods here
are getAllVertices(), getNeighbors(), and organizePath(). Method getAllVertices() and getNeighbors() are self
explanatory which return an Iterable set contains integer that represents the vertices. Method organizePath() serves
as the relaxation algorithm for the graph. We uses the Dijkstra algorithm here. First, all path have infinite
distance, and this distance is replaced once a shorter distance is found. Since we have an unweighted graph, all
edges have the weight of 1.


In the main method of StopContagion(), a scanner takes in the user input and extract the digits from it. The last
digit, or the only digit if radius is default to 2, should be the number of vertices needed to be removed. The
program takes this number as numRemoved, and check if we have another digit before it. If there is, it would be
radius, so the radius would be changed from default value(which is 2) to the given value. Then the program extracts
the file name at the end of the input and use buildGraph() method the produce the graph. Then it checks if "-d"
is contained in the input. If yes, the program calls removedByDegree(); if not, the program calls removedByInfluence().

BONUS:
We achieve the extra credit which required us to print the connected vertices after each removal. This function is
achieved by printConnected() and DFS() methods in the StopContagion.java class. Using these methods, we simply
print all unvisited vertices that are neighbors(connected) until we finish a neighborhood(group). In the main method,
the program also checks if "-t"(trace) is contained in the input. If yes, the printConnected() function is invoked
and printing all connected groups after each removal.

BONUS 2:
Produce a graph that indicate the all vertices(random coordinates) and edges. Isolated vertices are in different color.
Due to unknown issue, the lower part of the JPanel might not be visible on samller screen, but it does not affect
the construction of entire graph.
