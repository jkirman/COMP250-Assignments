package assignment1;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class Assignment1Test {

	private static String[] easy_test = { "engineering", "and", "software",
			"computer", "artificial", "intelligence", "games" };
	private static String[] hard_test = { "an", "engineering", "and",
			"software", "computer", "artificial", "intelligence", "games",
			"she", "the", "this", "to", "with" };
	private static String[] expected = { "engineering", "software", "computer",
			"artificial", "intelligence", "games" };
	private static String[] pDocument = { "engineering", "software",
			"computer", "artificial", "intelligence", "games" };
	private static String[] pQuery = { "engineering", "artificial" };
	private static String[] pDocEmpty = {};
	private static String[] pQueryEmpty = {};

	@Test
	public void testUnionSizeNormal() {

		Arrays.sort(pDocument);
		Arrays.sort(pQuery);
		int testUnionSize = Assignment1.unionSize(pDocument, pQuery);
		int expected = 6;
		assertEquals("Fail - UnionSize::Normal", expected, testUnionSize);
	}

	@Test
	public void testUnionSizeHard() {

		Arrays.sort(pDocEmpty);
		Arrays.sort(pQuery);
		int testUnionSize = Assignment1.unionSize(pDocEmpty, pQuery);
		int expected = 2;
		assertEquals("Fail - UnionSize::Hard", expected, testUnionSize);
	}

	@Test
	public void testNumberOfHitsNormal() {
		Arrays.sort(pDocument);
		Arrays.sort(pQuery);
		int testNumberofHits = Assignment1.numberOfHits(pDocument, pQuery);
		int expected = 2;
		assertEquals("Fail - NumberOfHits::Normal", expected, testNumberofHits);
	}

	@Test
	public void testNumberOfHitsHard() {
		Arrays.sort(pDocument);
		Arrays.sort(pQueryEmpty);
		int testNumberofHits = Assignment1.numberOfHits(pDocument, pQueryEmpty);
		int expected = 0;
		assertEquals("Fail - NumberOfHits::Hard", expected, testNumberofHits);
	}

	@Test
	public void testRemoveStopWordsNormal() {
		Arrays.sort(easy_test);
		String[] actual = Assignment1.removeStopWords(easy_test);
		Arrays.sort(expected);
		Arrays.sort(actual);
		assertArrayEquals("FAIL - removeStopWord::Normal", expected, actual); // compare
																				// arrays
	}

	@Test
	public void testRemoveStopWordsHard() {
		Arrays.sort(hard_test);
		String[] actual = Assignment1.removeStopWords(hard_test);
		Arrays.sort(expected); // sort expected and actual String arrays
		Arrays.sort(actual);
		assertArrayEquals("FAIL - removeStopWords::Hard", expected, actual); // compare
																				// arrays
	}

	@Test
	public void testIntersectionSizeNormal() {
		Arrays.sort(pDocument);
		Arrays.sort(pQuery);
		int actual = 2;
		int test = Assignment1.intersectionSize(pDocument, pQuery);
		assertEquals("Fail - intersectionSize:Normal", test, actual);
	}

	@Test
	public void testIntersectionSizeHard() {
		// both are empty
		int test = Assignment1.intersectionSize(pDocEmpty, pQueryEmpty);
		assertEquals("Fail - intersectionSize::Hard", test, 0);
	}

	@Test
	public void testobtainWordsFromPageNormal() {

		// check if it's able to obtain words from a valid page
		String[] sa1 = null;
		try {
			sa1 = Assignment1
					.obtainWordsFromPage("http://www.cs.mcgill.ca/~martin");
		} catch (IOException e) {
			assertTrue(
					"Fail- you might be offline, or else ObtainWordsFromPage failed",
					false);
			return;
		}

		// check if array is sorted
		boolean flag = true;
		for (int i = 0; i < sa1.length - 1; i++) {

			if (sa1[i].compareToIgnoreCase(sa1[i + 1]) > 0) {

				flag = false;
			}
		}
		assertTrue(
				"Fail- returned string array not sorted: ObtainWordsFromPage failed",
				flag);

	}

	@Test
	public void testobtainWordsFromPageHard() {

		// check if it's able to obtain words from a valid page
		String[] sa1 = null;

		try {
			sa1 = Assignment1
					.obtainWordsFromPage("http://www.cs.mcgill.ca/~martin");
		} catch (IOException e) {
			assertTrue(
					"Fail- you might be offline, or else ObtainWordsFromPage failed",
					false);
			return;
		}

		boolean flag = true;
		boolean caps = true;
		// check for punctuation and spaces and capital letters
		String punctutations = ".,:;!_-?'/";
		for (int i = 0; i < sa1.length; i++) {
			if (sa1[i].contains(".") || sa1[i].contains(",")
					|| sa1[i].contains(":") || sa1[i].contains(";")
					|| sa1[i].contains("!") || sa1[i].contains("_")
					|| sa1[i].contains("-") || sa1[i].contains("?")
					|| sa1[i].contains("'") || sa1[i].contains("/")
					|| sa1[i].contains("  ")) {
				flag = false;
				System.out.println("This word has punctuation or spaces "
						+ sa1[i]);
			}
			if (!sa1[i].equals(sa1[i].toLowerCase()))
				caps = false;
		}
		assertTrue(
				"Fail- returned string contains words with punctuation or spaces: ObtainWordsFromPage failed",
				flag);

		assertTrue(
				"Fail- returned string contains words not in lowercase: ObtainWordsFromPage failed",
				caps);
		try {
			sa1 = Assignment1
					.obtainWordsFromPage("http://www.cs.mcgill.ca/~martin");
		} catch (IOException e) {
			assertTrue(
					"Fail- you might be offline, or else ObtainWordsFromPage failed",
					false);

		}

	}

	@Test
	public void testbestMatchJaccardNormal() {
		String[] query;
		try {

			query = new String[] {};

			// Standard Case
			query = new String[] { "programming", "engineering", "design" };
			assertEquals("Fail - Default query should return Martin Robillard",
					Assignment1.bestMatchJaccard(query), "Martin Robillard");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testbestMatchJaccardHard() {
		String[] query;

		query = new String[] { "", "", "" };
		try {
			assertEquals(
					"Fail - Empty strings queries should return emptry string",
					Assignment1.bestMatchJaccard(query), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testbestMatchRelHitsNormal() throws IOException {
		String[] inputQueries = { "pedology" };
		inputQueries = new String[] { "machine", "robotics", "vision" };
		assertEquals("Fail - testbestMatchRelHitsNormal",
				Assignment1.bestMatchRelHits(inputQueries), "Gregory Dudek");

	}

	@Test
	public void testbestMatchRelHitsHard() throws IOException {
		String[] inputQueries = { "" };
		inputQueries = new String[] { "", "", "" };
		assertEquals("Fail - testbestMatchRelHitsHard",
				Assignment1.bestMatchRelHits(inputQueries), "");

	}

	@Test
	public void testjaccardIndexNormal() {

		Arrays.sort(pDocument);
		Arrays.sort(pQuery);
		double jaccardIndex = Assignment1.jaccardIndex(pDocument, pQuery);
		double expected = 0.3333333333;
		assertEquals("Fail - jaccardIndex::Normal", expected, jaccardIndex,
				0.0000000001);
	}

	@Test
	public void testjaccardIndexHard() {

		Arrays.sort(pDocument);
		Arrays.sort(pQuery);
		double jaccardIndex = Assignment1.jaccardIndex(pDocument, pQueryEmpty);
		double expected = 0.0;
		assertEquals("Fail - jaccardIndex::Hard", expected, jaccardIndex,
				0.0000000001);
	}

}
