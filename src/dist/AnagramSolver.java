package dist;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Files.*;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

public class AnagramSolver {
	public HashMap<Integer, LinkedList<String>> instanceMapper;
	public HashMap<Character, Integer> AlphabetIndexer;
	public LinkedList<String> permuationHandler;
	public AnagramSolver(){
		this.instanceMapper = new HashMap<Integer, LinkedList<String>>();
		this.AlphabetIndexer = new HashMap<Character, Integer>();
		this.permuationHandler = new LinkedList<String>();
	}
	
	public String[] getFileResource(String fileName){
		String[] resultsFromFileRaw = new String[350000];
		String currentLine;
		String ter = "";
		try{
			ter = this.getClass().getClassLoader().getResource("Files/"+fileName).getPath().toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		int indexCounter = 0;
		int insertPos = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(ter))){
			while((currentLine = br.readLine()) != null){
				//Process and store line
				resultsFromFileRaw[indexCounter++] = currentLine;
				insertPos = decidePath(currentLine.charAt(0));
				this.instanceMapper.put(insertPos, addToInnerLinkedList(currentLine, insertPos));
				
			}
		}
		catch(IOException E){
			E.printStackTrace();
		}
		
		return resultsFromFileRaw;
	}
	
	public LinkedList<String> addToInnerLinkedList(String itemToAdd, int currentListLocation){
		LinkedList<String> old = this.instanceMapper.get(currentListLocation);
		if(old == null){
			old = new LinkedList<String>();
			old.add(itemToAdd);
		}
		else{
			old.add(itemToAdd);
		}
		return old;
	}
	
	public int decidePath(char letterToCheck){
		letterToCheck = Character.toUpperCase(letterToCheck);
		int myNum = this.AlphabetIndexer.get(letterToCheck);
		return myNum;
	}
	
	
	public boolean calculateAnagramResult(String wordToBeTested, String wordGiven){
		char[] first = wordGiven.replaceAll("[\\s]",  "").toCharArray();
		char[] second = wordToBeTested.replaceAll("[\\s]", "").toCharArray();
		
		Arrays.sort(first);
		Arrays.sort(second);
		
		return Arrays.equals(first, second);
	}
	
	public void setupAlphabetHash(){
		char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		for(int i=0; i<26; i++){
			this.AlphabetIndexer.put(alphabet[i], i);
		}
	}
	
	public void getAllPerm(String perm, String word){
		if(word.isEmpty()){
			if(this.permuationHandler == null){
				this.permuationHandler = new LinkedList<String>();
				this.permuationHandler.add(perm + word);
			}
			else{
				this.permuationHandler.add(perm + word);
			}
		}
		else{
			word = word.toLowerCase();
			for(int i = 0; i < word.length(); i++){
			   getAllPerm(perm + word.charAt(i), word.substring(0,  i) + word.substring(i+1, word.length()));
			}
		}
	}
	
	public String[] findAllResults(String givenWord, String[] dictionary){
		System.err.println("Being finding all answers...");
		long startTime = System.currentTimeMillis();
		String[] results = new String[1000];
		//results = getAllPerm("", givenWord);
		getAllPerm("", givenWord);
	    results = copyIntoArray(this.permuationHandler);
	    results = removeDuplicates(results);
	    System.err.println("End finding all answers! It took: " + (System.currentTimeMillis()-startTime));
		return results;
	}
	
	public String[] copyIntoArray(LinkedList<String> source){
		System.err.println("Begin copy operation...");
		long startTime = System.currentTimeMillis();
		int looper = 0;
		String[] copy = new String[1000000];
		for(looper = 0; looper < source.size(); looper++){
			copy[looper] = source.get(looper);
		}
		System.err.println("End copy operation! It took: " + (System.currentTimeMillis()-startTime));
		return copy;
	}
	
	public String[] removeDuplicates(String[] possibleDoops){
		System.err.println("Starting to remove duplicates in answer...");
		long startTime = System.currentTimeMillis();
		String[] result = new String[1000000];
		int insertedNum = 0;
		int incrementor = 0;
		while(possibleDoops[incrementor] != null){
			if(isWord(possibleDoops[incrementor])){
				if(!AnagramSolver.contains(result, possibleDoops[incrementor])){
					result[insertedNum++] = possibleDoops[incrementor];
				}
			}
			
			incrementor++;
		}
		System.err.println("Done removing all duplicates in answer It took: " + (System.currentTimeMillis()-startTime));
		return result;
	}
	
	public boolean isWord(String wordToCheck){
		System.err.println("Starting Word Check Case...");
		long startTime = System.currentTimeMillis();
		wordToCheck = wordToCheck.toUpperCase();
		int lookupNum = this.AlphabetIndexer.get(wordToCheck.charAt(0));
		LinkedList<String> currentAnswerSet = this.instanceMapper.get(lookupNum);
		System.err.println("Copying result into usable subset...");
		String[] usableCopy = copyIntoArray(currentAnswerSet);
		if(AnagramSolver.contains(usableCopy, wordToCheck.toLowerCase())){
			System.err.println("End word check case!");
			return true;
		}
		System.err.println("End word check case! It took: " + (System.currentTimeMillis()-startTime));
		return false;
	}
	
	public void writeResultsToFile(String[] dataInput, String wordGiven){
		System.err.println("Being writing to file...");
		long startTime = System.currentTimeMillis();
		String homeDir = System.getProperty("user.home");
		homeDir = homeDir + "\\Desktop";
		
		if(!Files.exists(Paths.get(homeDir + "\\Anagram-Answers.txt"), LinkOption.NOFOLLOW_LINKS)){
			try{
				File file = new File(homeDir + "\\Anagram-Answers.txt");
				int writeHead = 0;
				BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
				outputWriter.write("\n" + wordGiven + " : ");
			    while(dataInput[writeHead] != null){
				    outputWriter.write(dataInput[writeHead++] + ",");
			    }
			    outputWriter.close();
			    System.err.println("Wrote to File Anagram-Answers.txt and saved it to: " + homeDir);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
		}
		else{
			int writeHead = 0;
			try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(homeDir + "\\Anagram-Answers.txt", true))){
				outputWriter.write("\n" + wordGiven + " : ");
				while(dataInput[writeHead] != null){
					outputWriter.write(dataInput[writeHead++] + "\n");
					
				}
				outputWriter.close();
				System.err.println("Wrote to File Anagram-Answers.txt and saved it to: " + homeDir + "It took: "  + (System.currentTimeMillis()-startTime));
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static <T> boolean contains(final T[] array, final T v){
		for(final T e: array){
			if(e == v || v != null && v.equals(e)){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args){
		//Create an instance of this class
		AnagramSolver solver = new AnagramSolver();
		solver.setupAlphabetHash();
		/*
		 * First we read from file operation. We will store the result of each line into an array for easy access later.
		 */
		String[] catchResultsFromFileRaw = new String[350000];
		String[] resultsForOneWord = new String[100000];
		catchResultsFromFileRaw = solver.getFileResource("dictionary.txt");
		
		resultsForOneWord = solver.findAllResults("anna", solver.getFileResource("dictionary.txt"));
		solver.writeResultsToFile(resultsForOneWord, "anna");
		
		//Solve for each word in dictionary
		int maxToDo = 10;
		int currentMax = 0;
		while(currentMax != maxToDo){
			resultsForOneWord = solver.findAllResults(catchResultsFromFileRaw[currentMax], solver.getFileResource("dictionary.txt"));
			System.out.println(resultsForOneWord[0]);
			solver.writeResultsToFile(resultsForOneWord, catchResultsFromFileRaw[currentMax++]);
		}
	}
}