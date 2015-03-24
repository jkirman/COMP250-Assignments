/*
 * COMP 250 - Assignment 1
 * Completed by: Jeffrey Kirman (2604933698)
 * Professor: Martin Robillard
 * 
 * LOG
 * 17/01/15: Completed assignment.
 * 21/01/15: Removed stop words from retrieved webpage data.
 * 22/01/15 1200: Added comments and cleaned up code.
 * 22/01/15 1220: Added more comments.
 * 22/01/15 1306: Fixed the findMaxDouble method.
 * 22/01/15 1352: Further fixed the findMaxDouble method.
 * 26/01/15: Fixed the removeStopWords method.
 */

package assignment1;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;

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

/**
 * When you run the main method, the program should print the name of the professor
 * that is the best match for the keywords described in the QUERY array using two similarity
 * metrics: the Jaccard index and the relative number of keyword matches. If no result is found,
 * the program should print out: "No result found".
 *  
 *  Complete the code provided as part of the assignment package. 
 *  
 *  Complete the list of professors, but do not remove the ones who are there or their URL.
 *  
 *  You can change the content of the QUERY as you like.
 *  
 *  Do not change any of the function signatures. However, you can write additional helper functions 
 *  and test functions if you want.
 *  
 *  Do not define any new classes.
 *  
 *  Do all the work using arrays. Do not use the Collection classes (List, etc.) The goal of 
 *  this assignment is to develop proficiency in the design of algorithm and the use of basic 
 *  data structures. 
 *  
 *  It is recommended to use the Arrays.sort and Arrays.binarySeach methods.
 *  
 *  Make sure your entire solution is in this file.
 *
 */
public class Assignment1
{
	/**
	 * List of professors to search. Complete the list with all professors in the school
	 * of computer science. Choose the page that has the best description of the professor's
	 * research interests.
	 */
	private static Professor[] PROFESSORS = {
		new Professor("Martin Robillard", "http://www.cs.mcgill.ca/~martin"),
		new Professor("Gregory Dudek", "http://www.cim.mcgill.ca/~dudek/dudek_bio.html"),
		new Professor("Yang Cai", "http://www.cs.mcgill.ca/~cai/"),
		new Professor("Brigitte Pientka", "http://www.cs.mcgill.ca/~bpientka/"),
		new Professor("Xiao-Wen Chang","http://www.cs.mcgill.ca/~chang/"),
		new Professor("David Avis", "http://en.wikipedia.org/wiki/David_Avis"),
		new Professor("Jackie Chi Kit Cheung", "http://cs.mcgill.ca/~jcheung/"),
		new Professor("Claude Crépeau", "http://en.wikipedia.org/wiki/Claude_Cr%C3%A9peau"),
		new Professor("Luc Devroye", "http://luc.devroye.org/"), // Change webpage maybe
		new Professor("Hamed Hatami", "http://www.cs.mcgill.ca/~hatami/"),
		new Professor("Wenbo He", "http://www.cs.mcgill.ca/~wenbohe/"),
		new Professor("Laurie J. Hendren", "http://www.sable.mcgill.ca/~hendren/"),
		new Professor("Bettina Kemme", "http://www.cs.mcgill.ca/~kemme/"),
		new Professor("Jörg Kienzle", "http://www.cs.mcgill.ca/~joerg/Home/Jorgs_Home.html"),
		new Professor("Paul G. Kry", "http://www.cs.mcgill.ca/~kry/"),
		new Professor("Michael Langer", "http://www.cim.mcgill.ca/~langer/"), // Change webpage maybe
		new Professor("Xue (Steve) Liu", "http://www.cs.mcgill.ca/~xueliu/"),
		new Professor("Muthucumaru Maheswaran", "http://www.cs.mcgill.ca/~maheswar/"),
		new Professor("Prakash Panangaden", "http://www.cs.mcgill.ca/~prakash/"),
		new Professor("Joelle Pineau", "http://www.cs.mcgill.ca/~jpineau/research.html"),
		new Professor("Doina Precup", "http://www.cs.mcgill.ca/~dprecup/"),
		new Professor("Bruce Reed", "http://en.wikipedia.org/wiki/Bruce_Reed_%28mathematician%29"),
		new Professor("Derek Ruths", "http://www.derekruths.com/research/"), // This page frequently times out
		new Professor("Kaleem Siddiqi", "http://www.cim.mcgill.ca/~shape/"),
		new Professor("Denis Thérien", "http://www.cs.mcgill.ca/~denis/"),
		new Professor("Carl Tropper", "http://www.cs.mcgill.ca/~carl/"),
		new Professor("Hans Vangheluwe", "http://msdl.cs.mcgill.ca/people/hv"),
		new Professor("Clark Verbrugge", "http://www.sable.mcgill.ca/~clump/research.html"),
		new Professor("Adrian Roshan Vetta", "http://www.math.mcgill.ca/~vetta/"),
		new Professor("Mathieu Blanchette", "http://en.wikipedia.org/wiki/Mathieu_Blanchette_%28computational_biologist%29"),
		new Professor("Nathan Friedman", "https://www.cs.mcgill.ca/people/faculty/profile?uid=nathan"),
		new Professor("Mike Hallett", "http://cancercentre.mcgill.ca/research/index.php?option=com_content&view=article&id=154%3Adr-michael-hallett&catid=30%3Aresearchers-full-members&Itemid=35&lang=en"),
		new Professor("Jérôme Waldispühl", "http://www.cs.mcgill.ca/~jeromew/")
	};
	
	/**
	 * A set of keywords describing an area of interest. Does not have to be sorted, 
	 * but must not contain any duplicates.
	 */
	private static String[] QUERY = {"programming", "engineering", "design"};
	
	/**
	 * Words with low information content that we want to exclude from the similarity
	 * measure.
	 * 
	 * This array should always be sorted. Don't change anything for the assignment submissions,
	 * but afterwards if you want to keep playing with this code, there are some other words
	 * that would obviously be worth adding.
	 */
	private static String[] STOP_WORDS = {"a", "an", "and", "at", "by", "for", "from", "he", "his", 
		"in", "is", "it", "of", "on", "she", "the", "this", "to", "with" };
	
	/**
	 * List of all punctuation that is to be considered not useful for word searching. Includes special characters such as
	 * \t (horizontal tab), \n (line break), \r (line break), \u00A0 (no break space), \u2007 (figure space), \u202F (narrow no break space).
	 */
	private static char[] PUNCTUATION = {',','.','!','?',';',':','"','\'','(',')','[',']','/','·',
		'\t', '\n', '\r', '©','\u00A0', '\u2007', '\u202F'};
	
	/**
	 * Your program starts here. You should not need to do anything here besides
	 * removing the first two statements once you have copied the required statement
	 * and dealing with the case where there are no results.
	 */
	public static void main(String[] args) throws IOException
	{
		Arrays.sort(QUERY);
		String[] queryWithoutStopWords = removeStopWords(QUERY);
		System.out.println("Jaccard: " + bestMatchJaccard(queryWithoutStopWords));
		System.out.println("Relhits: " + bestMatchRelHits(queryWithoutStopWords));
	}
	
	/**
     * Returns the name of the professor that is the best match according to
     * the Jaccard similarity index, or the empty String if there are no such
     * professors. pQuery must not include duplicate or stop
     * words, and must be sorted before being passed into this function.
	 */
	public static String bestMatchJaccard(String[] pQuery) throws IOException
	{
		assert pQuery != null;
		
		double[] jaccardIndex = new double[PROFESSORS.length]; // Array which contains the Jaccard index of each professor
		boolean noMatch = true; // Boolean which is true if all Jaccard indices of professors are 0.
		
		// Obtains words from each professor's webpage, removes stop words from them, and calculates the Jaccard index
		for (int i = 0; i < PROFESSORS.length; i++) {
			jaccardIndex[i] = jaccardIndex(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())), pQuery);
		}
		
		// Check to see if all Jaccard indices are 0
		for (int i = 0; i < jaccardIndex.length; i++) {
			if (jaccardIndex[i] != 0) {
				noMatch = false;
			}
		}
		
		if (noMatch) {
			return "No result found";
		}
		else {
			return PROFESSORS[findMaxDouble(jaccardIndex)].getName();
		}
	}
	
	/**
	 * Returns the size of the intersection between pDocument and pQuery.
	 * pDocument can contain duplicates, pQuery cannot. Both arrays must 
	 * be sorted in alphabetical order before being passed into this function.
	 */
	public static int intersectionSize(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		
		int intersectionCount = 0; // The count of intersections
		
		// Does a binary search for each element in pQuery in pDocument; if there is a result, the count of intersections is incremented
		for (int i = 0; i < pQuery.length; i++) {
			if (Arrays.binarySearch(pDocument, pQuery[i]) >= 0) {
				intersectionCount++;
			}
		}
		
		return intersectionCount;
	}
	
	/**
     * Returns the name of the professor that is the best match according to
     * the RelHits (relative hits) similarity index, computed as numberOfHits/size of the document.
     * Returns the empty string if no professor is found.
     * pQuery must not include duplicate or stop words, and must be sorted before
     * being passed into this function.
	 */
	public static String bestMatchRelHits(String[] pQuery) throws IOException
	{
		assert pQuery != null;
		
		double[] relHits = new double[PROFESSORS.length]; // Array which contains the relative hits index of each professor
		String[] currentPage; // Array which contains the words on a web page of a professor
		boolean noMatch = true; // Boolean which is true if all relative hits of professors are 0.
		
		// Obtains words from each professor's webpage, removes stop words from them, and calculates the relative hits
		for (int i = 0; i < PROFESSORS.length; i++) {
			currentPage = removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl()));
			relHits[i] = ((double)numberOfHits(currentPage, pQuery))/((double)currentPage.length);
		}
		
		// Check to see if all relative hits are 0
		for (int i = 0; i < relHits.length; i++) {
			if (relHits[i] != 0) {
				noMatch = false;
			}
		}
		
		if (noMatch) {
			return "No result found";
		}
		else {
			return PROFESSORS[findMaxDouble(relHits)].getName();
		}
	}
	
	/**
	 * Returns the Jaccard similarityIndex between pDocument and pQuery,
	 * that is, |intersection(pDocument,pQuery)|/|union(pDocument,pQuery)|
	 */
	public static double jaccardIndex(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		return ((double)intersectionSize(pDocument, pQuery))/((double)unionSize(pDocument,pQuery)); // Returns |intersection(pDocument,pQuery)|/|union(pDocument,pQuery)|
	}
	
	/**
	 * Returns the size of the union between pDocument and pQuery. 
	 * pDocument can contain duplicates, pQuery cannot. Both arrays must 
	 * be sorted in alphabetical order before being passed into this 
	 * function.
	 */
	public static int unionSize(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		
		String[] union = new String[pDocument.length + pQuery.length];
		int unionSize = 1;
		
		// Copy a concatenation of pDocument and pQuery into a temporary array
		for (int i = 0; i < pDocument.length; i++) {
			union[i] = pDocument[i];
		}
		for (int i = 0; i < pQuery.length; i++) {
			union[pDocument.length + i] = pQuery[i];
		}
		
		Arrays.sort(union); // Sort the temporary array.
		
		// Count unique values in temporary array
		for (int i = 0; i < union.length - 1; i++) {
			if (!union[i+1].equals(union[i])) {
				unionSize++;
			}
		}
		
		return unionSize; // Union is the simple the size of both arrays
	}
	
	/**
	 * Returns the number of times that any word in pQuery is found in pDocument
	 * for any word, and including repetitions. For example, if pQuery contains 
	 * "design" and "design" is found 3 times in pDocument, this would return 3.
	 * Both pDocument and pQuery should be sorted in alphabetical order before 
	 * being passed into this function.
	 */
	public static int numberOfHits(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		
		int numberOfHits = 0; // Contains the number of hits for all words in the pQuery array
		int index = 0; // Temporary variable for the current index of a search for a word in pQuery in pDocument
		
		// Searches for a word in pQuery in pDocument, if a result is found, then numberOfHits is incremented for every duplicate
		// found in indices next to the search result index (since pDocument has been sorted prior to being passed through
		// this method).
		for (int i = 0; i < pQuery.length; i++) {
			index = Arrays.binarySearch(pDocument, pQuery[i]);
		
			if (index >= 0) {
				for (int j = index; j < pDocument.length; j++) {
					if (pDocument[index].equals(pDocument[j])) {
						numberOfHits++;
					}
					else {
						break;
					}
				}
			}
		}
		
		return numberOfHits;
	}
	
	/**
	 * Returns a new array of words that contains all the words in pKeyWords
	 * that are not in the array of stop words. The order of the original 
	 * array should not be modified except by removing words. If the array is sorted,
	 * the resulting array should also be sorted.
	 * @param pKeyWords The array to trim from stop words
	 * @return A new array without the stop words.
	 */
	public static String[] removeStopWords(String[] pKeyWords)
	{
		assert pKeyWords != null;
		
		// Copies all strings in the pKeyWords array into a new temporary array
		String[] temp = Arrays.copyOf(pKeyWords, pKeyWords.length);
		String[] result = new String[pKeyWords.length];
		int count = 0;
		int index;
		
		// Does a binary search for each stop word and if the stop word exists in the array of strings
		// the corresponding indices for the stop words are replaced with null
		for (int i = 0; i < STOP_WORDS.length; i++) {
			index = Arrays.binarySearch(pKeyWords, STOP_WORDS[i]);
			if (index >= 0) {
				
				for (int j = index; j < pKeyWords.length; j++) {
					if (pKeyWords[index].equals(pKeyWords[j])) {
						temp[j] = null;
					}
					else {
						break;
					}
				}
				
				for (int j = index; j > 0; j--) {
					if (pKeyWords[index].equals(pKeyWords[j])) {
						temp[j] = null;
					}
					else {
						break;
					}
				}
				
			}
			
		}
		
		// Copy the results all Strings that aren't stop words into a new array
		for (int i = 0; i < pKeyWords.length; i++) {
			if (temp[i] != null) {
				result[count] = temp[i];
				count++;
			}
		}
		
		// Returns a truncated copy of the result array (omitting null indices)
		return Arrays.copyOf(result,count);
	}
	
	/**
	 * Obtains all the words in a page (including duplicates), but excluding punctuation and
	 * extraneous whitespaces and tabs. The results should be sorted in alphabetical order
	 * and all be completely in lower case.
	 * Consider using String.replaceAll(...) to complete this method.
	 * @throws IOException if we can't download the page (e.g., you're off-line)
	 */
	public static String[] obtainWordsFromPage(String pUrl) throws IOException
	{
		// The statement below connects to the webpage, parses it,
		// and return the text content into inputString. Consider
		// that this is all the text of the webpage that you need.
		String inputString = Jsoup.connect(pUrl).get().text();
		
		String punctRemoved; // String with punctuation removed
		String whiteRemoved; // String with both punctuation and extraneous whitespace removed
		String lowerCase; // String with punctuation removed, extraneous whitespace removed, and lowercase
		String[] wordsOnPage; // String array of all the words on the page accessed

		
		// Remove punctuation
		punctRemoved = inputString;
		for (int i = 0; i < PUNCTUATION.length; i++) {
			punctRemoved = punctRemoved.replace(PUNCTUATION[i], ' ');
		}
		
		// Find maximum whitespace length
		int whitespaceLength = 0; // Contains the maximum value of length of whitespace characters in a string
		int currentLength = 0; // Temporary variable for the current whitespace length in a pass
		whiteRemoved = punctRemoved;
		for (int i = 0; i < punctRemoved.length(); i++) {
			if (punctRemoved.charAt(i) == ' ') {
				currentLength++;
			}
			else if (currentLength > 0) {
				if (currentLength > whitespaceLength) {
					whitespaceLength = currentLength;
				}
				currentLength = 0;
			}
		}
		
		// Reduce whitespace
		if (whitespaceLength > 1) {
			for (int i = whitespaceLength; i >= 2; i--) {
				whiteRemoved = whiteRemoved.replaceAll(generateSpaceString(i), " ");
			}
		}
		
		// Turn into lower case
		lowerCase = whiteRemoved.toLowerCase();
		
		// Split into different words.
		wordsOnPage = lowerCase.split(" ");
		
		// Sort in alphabetical order
		Arrays.sort(wordsOnPage);
		
        return wordsOnPage;
	}
	
	/**
	 * Generates a String with spaces
	 * @param size The amount of spaces that should be in the string
	 * @return The String with size spaces
	 */
	public static String generateSpaceString(int size) {
		char[] spaces = new char[size];
		for (int i = 0; i < size; i++) {
			spaces [i] = ' ';
		}
		return String.copyValueOf(spaces);
	}
	
	/**
	 * Finds the index of the largest value in an array of doubles
	 * @param array An array of type double.
	 * @return The index of the largest value of the array. Returns -1 if a null array is passed to this method.
	 */
	public static int findMaxDouble(double[] array) {
		
		if (array == null) {
			return -1;
		}
		
		int index = 0;
		double largest = array[0];
		for (int i = 1; i < array.length; i++) {
			if (largest > array[i]) {
				largest = array[i];
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Helper method that prints an array of strings in one line with a space between each string.
	 * @param array An array of type String.
	 */
	public static void printStringArray(String[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();
	}
}

/**
 * This simple class just keeps the information about
 * a professor together. Do not modify this class.
 */
class Professor
{
	private String aName;
	private String aWebPageUrl; 
	
	public Professor(String pName, String pWebpageUrl)
	{
		aName = pName;
		aWebPageUrl = pWebpageUrl;
	}
	
	public String getName()
	{
		return aName;
	}
	
	public String getWebPageUrl()
	{
		return aWebPageUrl;
	}
}
