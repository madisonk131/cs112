package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		
		// System.out.println("tester");
//		File holder = new File(docFile);
		// System.out.println("hello");
		
		// (1) check IF exists - throw exception
		if (docFile == null) {
			throw new FileNotFoundException("file not found");
		}
		
		
		// creating hash map which will be return val
		HashMap<String, Occurrence> hash = new HashMap<String, Occurrence>();
		// int freq = 0;
		
		// (2) iterate through docFile (the book) - separating each word
		Scanner sc = new Scanner(new File(docFile));
		
		while (sc.hasNext()) {
			String word = getKeyword(sc.next().trim());
			// String word = sc.next();
			
			if (word != null) {
				// check IF duplicate (then add to freq)
				if (hash.containsKey(word)) {
					Occurrence replace = hash.get(word);
					replace.frequency++;
					// int freq = hash.get(sc.toString()).frequency;
//					Occurrence replace = new Occurrence(docFile, freq);
					// hash.replace(sc.toString(), replace);
				} else {	// IF NOT duplicate then add completely new keyword
					// new keywords have frequency 1
					Occurrence temp = new Occurrence(docFile, 1);
					hash.put(word, temp);
				}
			} 
//			else {
//				continue;
//			}
		}
		sc.close();			// must close scanner
				
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return hash;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		// Insert EVERYTHING INTO HashMap<String,ArrayList<Occurrence>> keywordsIndex;
		
		// iterate though all keys in kws
		// no duplicates allowed
		// using For-Each loop
		for (String key : kws.keySet()) {
			Occurrence replace = kws.get(key);		// the occurrence at specified key
			
			// (1) key is NOT a duplicate
			if (!(keywordsIndex.containsKey(key))) {
				ArrayList<Occurrence> tempOcc = new ArrayList<Occurrence>();
				tempOcc.add(replace);
				keywordsIndex.put(key, tempOcc);
			} else {		// (2) key is a DUPLICATE
				// replace <key, value> pair
				ArrayList<Occurrence> tempOcc = keywordsIndex.get(key);
				tempOcc.add(replace);
				insertLastOccurrence(keywordsIndex.get(key));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		// (1) make word lowercase
		word = word.toLowerCase();
		
		// (2) strip all trailing punctuation - using substrings
		String punc = "!\\\"#$%&'()*+,-./:;?@[\\\\]^_`{|}~ '";		// array OF punctuation to delete trailing punctuation
		
		int index;
		for (index = word.length()-1; index>=0; index--) {
			if (!(punc.contains(String.valueOf(word.charAt(index))))) {
				// then break
				break;
			}
		}
		
		word = word.substring(0, index+1);
		
		// (3) check IF still contains punctuation (in the middle somewhere)
		for (int i=0; i<word.length(); i++) {
			if (punc.contains(String.valueOf(word.charAt(index)))) {
				return null;		// punctuation not allowed so return null
			}
		}
		
		// (4) check IF word in noisewords.txt
		if (noiseWords.contains(word)) {
			return null;
		}
		
		// (5) checking IF all characters (no numbers)
		for (int count=word.length()-1; count>=0; count--){
			if (!Character.isLetter(word.charAt(count))){
				return null;
			}
		}
		
		// (6) OTHERWISE - return word
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		// checking null condition (input list size is 1)
		if (occs.size() == 1) {
			return null;
		}
		
		// holding arraylist OF midpoint indexes - will return
		ArrayList<Integer> midIndexes = new ArrayList<Integer>();
		
		// grabbing the last element in ArrayList
		Occurrence lastElem = occs.get(occs.size() - 1);
		
		int min = 0;
		int max = occs.size() - 2;		// not including last element
		int mid = 0;
		
		// executing binary search
		while(min<=max) {
			mid = (min + max)/2;
			midIndexes.add(mid);
			int freq = occs.get(mid).frequency;
			
			// (1) base case - check IF freqs match
			if (freq == lastElem.frequency) {
				break;
			} else if (freq > lastElem.frequency) {		// (2) going LEFT
				min = mid+1;
			} else {		// going RIGHT
				max = mid-1;
			}
		}
		
		int len = occs.size() - 1;
		
		// params String docFile and int freq
		Occurrence temp = occs.remove(len);		// removing last element
		occs.add(mid+1, temp);		
		
		if (max<min) {		// replacing min
			occs.add(min, occs.remove(len));
		}
		
		// Testing 
//		System.out.println(midIndexes + " midpoints");
//		System.out.println(occs);
		
		return midIndexes;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		kw1.toLowerCase();
		kw2.toLowerCase();
		
		// array list to hold occurrences in kw1 and kw2
		ArrayList<Occurrence> listOne = keywordsIndex.get(kw1);
		ArrayList<Occurrence> listTwo = keywordsIndex.get(kw2);
		
		// create arraylist to hold top5 - will return
		ArrayList<String> topFive = new ArrayList<String>();
		
		// (1) both strings not found
		if ((listOne == null && listTwo == null) || (keywordsIndex.isEmpty()) || (!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2))) {
			return null;
		}
		
		// temporary array to sort
		ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
		
		// adding all elements to temp arraylist
		if (listOne != null) temp.addAll(listOne);
		if (listTwo != null) temp.addAll(listTwo);
		
		int max = 0;
		int x = 0;
		String holder = "";
		
		// adding to Top Five List - until it has 5 elements
		while (topFive.size()<5) {
			max = 0;
			
			for (int i=0; i<temp.size(); i++) {
				int freq = temp.get(i).frequency;
				if (freq > max) {
					max = freq;
					holder = temp.get(i).document;
					x=i;
				}
			}
			temp.remove(x);
			if (!topFive.contains(holder)) topFive.add(holder);		// no duplicates
			
			if (temp.size() == 0) break;
		}
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return topFive;
	
	}
}
