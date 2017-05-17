package dist;
import java.util.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

import org.xml.sax.SAXException;

public class AnagramSolver {
	public HashMap<String, String> instanceMapper;
	public AnagramSolver(){
		this.instanceMapper = new HashMap<String, String>();
	}
	
	public String[] getFileResource(String fileName){
		String[] resultsFromFileRaw = new String[10000];
		String currentLine;
		BufferedReader br;
		URL url;
		int indexCounter = 0;
		try(URL e= Paths.get(fileName).toUri().toURL()){
			br = new BufferedReader(new FileReader(url.toString());
			while((currentLine = br.readLine()) != null){
				//Process and store line
				resultsFromFileRaw[indexCounter++] = currentLine;
			}
		}
		catch(IOException E){
			E.printStackTrace();
		}
		br.close();
		
		return resultsFromFileRaw;
	}
	
	public void fillInHashMap(String[] args){
		
	}
	
	public String[] getResults(){
		String[] results = new String[100];
		
		return results;
	}
	
	public static void main(String[] args){
		//Create an instance of this class
		AnagramSolver solver = new AnagramSolver();
		/*
		 * First we read from file operation. We will store the result of each line into an array for easy access later.
		 */
		String[] catchResultsFromFileRaw = new String[10000];
		catchResultsFromFileRaw = solver.getFileResource("dictionary.txt");
		for(int i = 0; i <catchResultsFromFileRaw.length; i++){
			System.out.println("WORD: " + catchResultsFromFileRaw[i]);
		}
		
	}
}
