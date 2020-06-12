package lse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class driver {
	public static void main(String[] args) throws IOException {
		LittleSearchEngine engine= new LittleSearchEngine();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter document file name => ");//docs.txt
		String docsFile=br.readLine();
		System.out.print("Enter noise words file name => ");//noisewords.txt
		
		// prints everything in midpoints
		String noiseWordsFile=br.readLine();
		engine.makeIndex(docsFile, noiseWordsFile);
		
		
		// below is TOP 5 driver
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("keyword 1: ");
		String keyword1 =  scanner.next();
		System.out.print("keyword 2: ");
		String keyword2 = scanner.next();
		scanner.close();
		ArrayList<String> results = engine.top5search(keyword1, keyword2);
		System.out.println(results);
		System.exit(0);
	}
}
