package a4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

/*
 * Assignment 4
 * Jeffrey Kirman (260493368)
 * 
 * Completed 23/03/2015
 */

/* ACADEMIC INTEGRITY STATEMENT
 * 
 * By submitting this file, we state that all group members associated
 * with the assignment understand the meaning and consequences of cheating, 
 * plagiarism and other academic offenses under the Code of Student Conduct 
 * and Disciplinary Procedures (see www.mcgill.ca/students/srr for more information).
 * 
 * By submitting this assignment, we state that the members of the group
 * associated with this assignment claim exclusive credit as the authors of the
 * content of the file (except for the solution skeleton provided).
 * 
 * In particular, this means that no part of the solution originates from:
 * - anyone not in the assignment group
 * - Internet resources of any kind.
 * 
 * This assignment is subject to inspection by plagiarism detection software.
 * 
 * Evidence of plagiarism will be forwarded to the Faculty of Science's disciplinary
 * officer.
 */



/* A Simple Search Engine exploring subnetwork of McGill University's webpages.
 * 	
 *	Complete the code provided as part of the assignment package. Fill in the \\TODO sections
 *  
 *  Do not change any of the function signatures. However, you can write additional helper functions 
 *  and test functions if you want.
 *  
 *  Do not define any new classes. Do not import any data structures. 
 *  
 *  Make sure your entire solution is in this file.
 *  
 *  We have simplified the task of exploring the network. Instead of doing the search online, we've
 *  saved the result of an hour of real-time graph traversal on the McGill network into two csv files.
 *  The first csv file "vertices.csv" contains the vertices (webpages) on the network and the second csv 
 *  file "edges.csv" contains the links between vertices. Note that the links are directed edges.
 *  
 *  An edge (v1,v2) is link from v1 to v2. It is NOT a link from v2 to v1.
 * 
 */

public class Search {

	private ArrayList<Vertex> graph;
	private ArrayList<Vertex> BFS_inspector;
	private ArrayList<Vertex> DFS_inspector;
	private Comparator<SearchResult> comparator = new WordOccurrenceComparator();
	private PriorityQueue<SearchResult> wordOccurrenceQueue;
	
	/**
	 * You don't have to modify the constructor. It only initializes the graph
	 * as an arraylist of Vertex objects
	 */
	public Search(){
		graph = new ArrayList<Vertex>();
	}
	
	/**
     * Used to invoke the command line search interface. You only need to change
     * the 2 filepaths and toggle between "DFS" and "BFS" implementations.
	 */
	public static void main(String[] args) {
		String pathToVertices = "vertices.csv";
		String pathToEdges = "edges.csv";
		
		Search mcgill_network = new Search();
		mcgill_network.loadGraph(pathToVertices, pathToEdges);
		
		Scanner scan = new Scanner(System.in);
		String keyword;
		
		do{
			System.out.println("\nEnter a keyword to search: ");
			keyword = scan.nextLine();
			
			if(keyword.compareToIgnoreCase("EXIT") != 0){
				mcgill_network.search(keyword, "BFS");		//You should be able to change between "BFS" and "DFS"
				mcgill_network.displaySearchResults();
			}

		} while(keyword.compareToIgnoreCase("EXIT") != 0);
		
		System.out.println("\n\nExiting Search...");
		scan.close();
	}
	
	/**
	 * Do not change this method. You don't have to do anything here.
	 * @return
	 */
	public int getGraphSize(){
		return this.graph.size();
	}
	
	/**
	 * This method will either call the BFS or DFS algorithms to explore your graph and search for the
	 * keyword specified. You do not have to implement anything here. Do not change the code.
	 * @param pKeyword
	 * @param pType
	 */
	public void search(String pKeyword, String pType){
			resetVertexVisits();
			wordOccurrenceQueue = new PriorityQueue<SearchResult>(1000, comparator);
			BFS_inspector = new ArrayList<Vertex>();
			DFS_inspector = new ArrayList<Vertex>();
			
			if(pType.compareToIgnoreCase("BFS") == 0){
				Iterative_BFS(pKeyword);
			}
			else{
				Iterative_DFS(pKeyword);
			}
	}
	
	/**
	 * This method is called when a new search will be performed. It resets the visited attribute
	 * of all vertices in your graph. You do not need to do anything here.
	 */
	public void resetVertexVisits(){
		for(Vertex k : graph){
			k.resetVisited();
		}
	}
	
	/**
	 * Do not change the code of this method. This is used for testing purposes. It follows the 
	 * your graph search traversal track to ensure a BFS implementation is performed.
	 * @return
	 */
	public String getBFSInspector(){
		String result = "";
		for(Vertex k : BFS_inspector){
			result = result + "," + k.getURL();
		}
		
		return result;
	}
	
	/**
	 * Do not change the code of this method. This is used for testing purposes. It follows the 
	 * your graph search traversal track to ensure a DFS implementation is performed.
	 * @return
	 */
	public String getDFSInspector(){
		String result = "";
		for(Vertex k : DFS_inspector){
			result = result + "," + k.getURL();
		}
		return result;
	}
	
	/**
	 * This method prints the search results in order of most occurrences. It utilizes
	 * a priority queue (wordOccurrenceQueue). You do not need to change the code.
	 * @return
	 */
	public int displaySearchResults(){
		
		int count = 0;
		while(this.wordOccurrenceQueue.size() > 0){
			SearchResult r = this.wordOccurrenceQueue.remove();
			
			if(r.getOccurrence() > 0){
				System.out.println("Count: " + r.getOccurrence() + ", Page: " + r.getUrl());
				count++;
			}
		}
		
		if(count == 0) System.out.println("No results found for your search query");
		
		return count;
		
	}
	
	/**
	 * This method returns the graph instance. You do not need to change the code.
	 * @return
	 */
	public ArrayList<Vertex> getGraph(){
		return this.graph;
	}
	
	/**
	 * This method takes in the 2 file paths and creates your graph. Each Vertex must be 
	 * added to the graph arraylist. To implement an edge (v1, v2), add v2 to v1.neighbors list
	 * by calling v1.addNeighbor(v2)
	 * @param pVerticesPathFile
	 * @param pEdgesFilePath
	 */
	public void loadGraph(String pVerticesFilePath, String pEdgesFilePath){
		
		// **** LOADING VERTICES ***///
		
		BufferedReader reader = null;
		String current_line = null;
		String delimiter = ",";
		Vertex current_vertex;
		
		try {
			reader = new BufferedReader(new FileReader(pVerticesFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// read the first line
		try {
			current_line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(current_line != null) {
			
			String[] current_line_split = current_line.split(delimiter);
			current_vertex = new Vertex(current_line_split[0]);
			
			// add every word in a line to the vertex
			if (current_line_split.length > 1) {
				for (int i = 1; i < current_line_split.length; i++) {
					current_vertex.addWord(current_line_split[i]);
				}
			}
			
			this.graph.add(current_vertex);
			
			// read the next line
			try {
				current_line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		// **** END LOADING VERTICES ***///
		
		
		
		// **** LOADING EDGES ***///
		
		current_line = "";
		Vertex home_vertex = null;
		Vertex neighbour_vertex = null;
		
		try {
			reader = new BufferedReader(new FileReader(pEdgesFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// read the first line
		try {
			current_line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		while(current_line != null) {
			
			String[] current_line_split = current_line.split(delimiter);
			current_vertex = new Vertex(current_line_split[0]);
			
			for (Vertex vertex : graph) {
				
				// load the two vertices in question into a their respective temporary variables
				if (vertex.getURL().equals(current_line_split[0])) {
					home_vertex = vertex;
				}
				else if (vertex.getURL().equals(current_line_split[1])) {
					neighbour_vertex = vertex;
				}
				
				// if both are found break out of the loop
				if ((home_vertex != null) && (neighbour_vertex != null)) {
					break;
				}
			}
			
			home_vertex.addNeighbor(neighbour_vertex);
			
			// reset the temporary vertices variables to null
			home_vertex = null;
			neighbour_vertex = null;
			
			// read the next line
			try {
				current_line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		// **** END LOADING EDGES ***///
		
	}
	
	
	/**
	 * This method must implement the Iterative Breadth-First Search algorithm. Refer to the lecture
	 * notes for the exact implementation. Fill in the //TODO lines
	 * @param pKeyword
	 */
	public void Iterative_BFS(String pKeyword){
		ArrayList<Vertex> BFSQ = new ArrayList<Vertex>();	//This is your breadth-first search queue.
		Vertex start = graph.get(0);						//We will always start with this vertex in a graph search
				
		start.setVisited();
		BFSQ.add(start);
		BFS_inspector.add(start);
		
		// Add the first search result to the output queue
		SearchResult current_result;
		current_result = new SearchResult(start.getURL(), CalculateWordCount(start.getWords(), pKeyword));
		wordOccurrenceQueue.add(current_result);
		
		// temporary variables for iteration purposes
		Vertex current_vertex;
		ArrayList<Vertex> current_list_of_neighbours;
		
		while(!BFSQ.isEmpty()) {
			
			// dequeue vertex
			current_vertex = BFSQ.get(0);
			BFSQ.remove(0);
			
			current_list_of_neighbours = current_vertex.getNeighbors();
			
			// if a neighbour is not visited its search result is added to the output queue and
			// the neighbour is added to the bfs queue
			for(Vertex neighbour : current_list_of_neighbours) {
				if (!neighbour.getVisited()) {
					current_result = new SearchResult(neighbour.getURL(), CalculateWordCount(neighbour.getWords(), pKeyword));
					wordOccurrenceQueue.add(current_result);
					neighbour.setVisited();
					BFSQ.add(neighbour);
					BFS_inspector.add(neighbour);
				}
			}
			
		}
			
		
	}
	
	/**
	 * This method must implement the Iterative Depth-First Search algorithm. Refer to the lecture
	 * notes for the exact implementation. Fill in the //TODO lines
	 * @param pKeyword
	 */
	public void Iterative_DFS(String pKeyword){
		Stack<Vertex> DFSS = new Stack<Vertex>();	//This is your depth-first search stack.
		Vertex start = graph.get(0);				//We will always start with this vertex in a graph search
		
		// Add the first search result to the output queue
		SearchResult current_result;
		current_result = new SearchResult(start.getURL(), CalculateWordCount(start.getWords(), pKeyword));
		wordOccurrenceQueue.add(current_result);
		
		start.setVisited();
		DFSS.push(start);
		DFS_inspector.add(start);
		
		// temporary variables for iteration purposes
		Vertex current_vertex;
		ArrayList<Vertex> current_list_of_neighbours;
		
		while(!DFSS.isEmpty()) {
			
			current_vertex = DFSS.pop();
			current_list_of_neighbours = current_vertex.getNeighbors();
			
			// if a neighbour is not visited its search result is added to the output queue and
			// the neighbour is pushed to the stack
			for(Vertex neighbour : current_list_of_neighbours) {
				if (!neighbour.getVisited()) {
					current_result = new SearchResult(neighbour.getURL(), CalculateWordCount(neighbour.getWords(), pKeyword));
					wordOccurrenceQueue.add(current_result);
					neighbour.setVisited();
					DFSS.push(neighbour);
					DFS_inspector.add(neighbour);
				}
			}
			
		}
		
	}
	
	/**
	 * Calculates the amount of hits a keyword has in a list of words.
	 * @param list_of_words A list of words to check.
	 * @param pKeyword A keyword.
	 * @return The amount of times pKeyword is found in list_of_words.
	 */
	public int CalculateWordCount(ArrayList<String> list_of_words, String pKeyword) {
		
		int result = 0;
		
		for (String word : list_of_words) {
			if (word.contains(pKeyword)) {
				result++;
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * This simple class just keeps the information about a Vertex together. 
	 * You do not need to modify this class. You only need to understand how it works.
	 */
	public class Vertex{
		private String aUrl;
		private boolean visited;
		private ArrayList<String> aWords;
		private ArrayList<Vertex> neighbors;
		
		public Vertex(String pUrl){
			this.aUrl = pUrl;
			this.visited = false;
			this.neighbors = new ArrayList<Vertex>();
			this.aWords = new ArrayList<String>();
		}
		
		public String getURL(){
			return this.aUrl;
		}
		
		public void setVisited(){
			this.visited = true;
		}
		
		public void resetVisited(){
			this.visited = false;
		}
		
		public boolean getVisited(){
			return this.visited;
		}
			
		public void addWord(String pWord){
			this.aWords.add(pWord);
		}

		public ArrayList<String> getWords(){
			return this.aWords;
		}
		
		public ArrayList<Vertex> getNeighbors(){
			return this.neighbors;
		}
		
		public void addNeighbor(Vertex pVertex){
			this.neighbors.add(pVertex);
		}

	}

	/**
	 * This simple class just keeps the information about a Search Result. It stores
	 * the occurrences of your keyword in a specific page in the graph. You do not need to modify this class. 
	 * You only need to understand how it works.
	 */
	public class SearchResult{
		private String aUrl;
		private int aWordCount;
		
		public SearchResult(String pUrl, int pWordCount){
			this.aUrl = pUrl;
			this.aWordCount = pWordCount;
		}
		
		public int getOccurrence(){
			return this.aWordCount;
		}
		
		public String getUrl(){
			return this.aUrl;
		}
	}
	
	/**
	 * This class enables us to use the PriorityQueue type. The PriorityQueue needs to know how to 
	 * prioritize its elements. This class instructs the PriorityQueue to compare the SearchResult 
	 * elements based on their word occurrence values.
	 * You do not need to modify this class. You only need to understand how it works.
	 */
	public class WordOccurrenceComparator implements Comparator<SearchResult>{
	    @Override
	    public int compare(SearchResult o1, SearchResult o2){
	    	int x = o1.getOccurrence();
	    	int y = o2.getOccurrence();
	    	
	        if (x > y)
	        {
	            return -1;
	        }
	        if (x < y)
	        {
	            return 1;
	        }
	        return 0;
	    }
	}
}
