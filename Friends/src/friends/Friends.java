package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		// using BFS to FIND shortest path 
		
		// double-checking
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		if (g == null) return null;
		if (p1 == null) return null;
		if (p2 == null) return null;
//		if (p1.equals(p2)) {
//			// return array list with single name
//			ArrayList<String> singlePath = new ArrayList<String>();
//			singlePath.add(p1);
//			return singlePath;
//		}
		
		// resulting chain OF NAMES to represent the shortest path
		ArrayList<String> shortestPath = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		
		// creating q to use as counter and stack to hold path
		Queue<Person> queue = new Queue<Person>();
		Queue<ArrayList<String>> path = new Queue<ArrayList<String>>();

		int i = g.map.get(p1);
		queue.enqueue(g.members[i]);			// mark source as visited
		shortestPath.add(g.members[i].name);
		path.enqueue(shortestPath);
		
		// use BFS to traverse each word and neighbors
		while(!queue.isEmpty()) {
			Person source = queue.dequeue();		// peeking at all adjacent neighbors
			int sourceIndex = g.map.get(source.name);	// get 1st index friend OF source to traverse through
			visited[sourceIndex] = true;		// already visited 1st neighbor
			
			ArrayList<String> temp = path.dequeue();
			
			Friend adj = g.members[g.map.get(source.name)].first;			// 1st friend and acts as 1st element in LL
			
			while (adj != null) {
				if (!(visited[adj.fnum])) {
					ArrayList<String> copy = new ArrayList<String>(temp);
					
					String person = g.members[adj.fnum].name;
					copy.add(person);
					
					if (person.equals(p2)) 
						return copy;
					
					queue.enqueue(g.members[adj.fnum]);
					path.enqueue(copy);
				}
				adj = adj.next;
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		// given that there was no shortestPath
		return null;
	}
	
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		// performing BFS to FIND school cliques
		
		// checking null conditions
		school = school.toLowerCase();
		if (g == null) return null;
		if (school == null) return null;
		if (school.length() == 0) return null;
		
		// allCliques holds schoolCliques | FINAL RESULT
		int len = g.members.length;
		ArrayList<ArrayList<String>> allCliques = new ArrayList<>();
		boolean[] visited = new boolean[len];		// returns whether or not node has been visited
//		checkFalse(visited);
		
		// iterating through all members in graph
		for (int i=0; i<len; i++) {
			// person is a member OF desired school AND has NOT been visited
//			System.out.println("name is " + p.name + " and school is " + p.school);
//			System.out.print(p.school.equals(school) + ", ");
//			System.out.print((visited[g.map.get(p.name)] == false) + ", ");
//			System.out.println(p.school == null);
			Person p = g.members[i];
			
			if (visited[i] == true || p.student == false) 
				continue;
			
			ArrayList<String> temp = new ArrayList<String>();
			helper(g, visited, temp, school, i);
			
			if (temp!=null) {
				if (temp.size()>0) 
					allCliques.add(temp);
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return allCliques;
		
	}
	
	private static void helper(Graph g, boolean[] visited, ArrayList<String> tempClique, String school, int i) {
		Person p = g.members[i];
		
		boolean one = visited[i];		// checking IF visited
//		boolean two = p.student;		// must be a student
//		boolean three = p.school.equals(school);		// checking IF school equals to original param school
		if (one==false && p.student && p.school.equals(school)) 
			tempClique.add(p.name);
		
		visited[g.map.get(p.name)] = true;
		
		Friend ptr = g.members[i].first;
		while (ptr!=null) {
			int num = ptr.fnum;
			Person adj = g.members[num];
			
//			boolean adjOne = visited[num];		//  checking IF visited
//			boolean adjTwo = adj.student;		// checking IF student
//			boolean adjThree = adj.school.equals(school);		// checking IF school equals to original param school
			if (visited[num] == false && adj.student && adj.school.equals(school)) {
				helper(g, visited, tempClique, school, num);
			}
			
			ptr = ptr.next;
		}
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> allConnectors = new ArrayList<String>();
		int memLength = g.members.length;
		
		int[] dfsNums = new int[memLength], bk = new int[memLength];
		boolean[] back = new boolean[memLength];
		boolean[] visited = new boolean[memLength]; 		// need to set to false
		
		for (int i=0; i<memLength; i++) {
			if (visited[i] == false) {
				DFS(g, visited, i, dfsNums, bk, i, allConnectors, back, i);
			}
		}
		
		return allConnectors;
	}
	
	// performing recursive depth first search to call in connectors
	private static void DFS(Graph g, boolean[] visited, int index, int[] dfsNums, 
	int bk [], int pIndex, ArrayList<String> allConnectors, boolean[] back, int in) {
		// double-checking IF already visited
		if (visited[index] == true)
			return;
		
		// marking indexes
		visited[index]=true;
		dfsNums[index] = dfsNums[pIndex] + 1;
		bk[index]=dfsNums[index];		// holds traversed back
		
		Friend ptr = g.members[index].first;
		while (ptr!=null) {
			if (visited[ptr.fnum]) {
				bk[index]=Math.min(bk[index], dfsNums[ptr.fnum]);
			} else {
				DFS(g, visited, ptr.fnum, dfsNums, bk, index, allConnectors, back, in);
				
				boolean condition1 = dfsNums[index] <= bk[ptr.fnum];
				boolean condition2 = allConnectors.contains(g.members[index].name);
				if (condition1==true && condition2==false && (index!=in || back[index])) {
					allConnectors.add(g.members[index].name);
				}
				if (dfsNums[index] > bk[ptr.fnum]) {
					bk[index] = Math.min(bk[index], bk[ptr.fnum]);
				}
				back[index]=true;
			}
			ptr = ptr.next;
		}
	}
}

