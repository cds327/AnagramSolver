package dist;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.LinkOption;



public class AnagramSolver implements Runnable{
	public HashMap<Integer, LinkedList<String>> instanceMapper;
	public HashMap<Character, Integer> AlphabetIndexer;
	public LinkedList<String> permuationHandler;
	public volatile int problemInt;
	
	public AnagramSolver(){
		this.instanceMapper = new HashMap<Integer, LinkedList<String>>();
		this.AlphabetIndexer = new HashMap<Character, Integer>();
		this.permuationHandler = new LinkedList<String>();
		this.problemInt = 0;
	}
	@Override
	public void run(){
		
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
			br.close();
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
	
	
	public String[] findAllResults(String givenWord, String[] dictionary){
		//System.err.println("Being finding all answers...");
		//long startTime = System.currentTimeMillis();
		String[] results = new String[350000];
		//results = getAllPerm("", givenWord);
		//getAllPerm("", givenWord);
		PermutationFinder worker = new PermutationFinder(this.permuationHandler, "", givenWord);
		worker.run();
		this.permuationHandler = worker.getReturn();
	    results = copyIntoArray(this.permuationHandler);
	    results = removeDuplicates(results);
	   // System.err.println("End finding all answers! It took: " + (System.currentTimeMillis()-startTime));
		return results;
	}
	
	public String[] copyIntoArray(LinkedList<String> source){
		//System.err.println("Begin copy operation...");
		//long startTime = System.currentTimeMillis();
		String[] copy = source.toArray(new String[source.size()-1]);
		//System.err.println("End copy operation! It took: " + (System.currentTimeMillis()-startTime));
		return copy;
	}
	
	public String[] removeDuplicates(String[] possibleDoops){
		//System.err.println("Starting to remove duplicates in answer...");
		//long startTime = System.currentTimeMillis();
		String[] result = new String[possibleDoops.length];
		int insertedNum = 0;
		int incrementor = 0;
		//System.err.println(possibleDoops.length);
		while(possibleDoops[incrementor] != null && incrementor < possibleDoops.length-1){
			if(isWord(possibleDoops[incrementor])){
				if(!AnagramSolver.contains(result, possibleDoops[incrementor])){
					result[insertedNum++] = possibleDoops[incrementor];
				}
			}
			
			incrementor++;
		}
		//System.err.println("Done removing all duplicates in answer It took: " + (System.currentTimeMillis()-startTime));
		return result;
	}
	
	public boolean isWord(String wordToCheck){
		//System.err.println("Starting Word Check Case...");
		//long startTime = System.currentTimeMillis();
		wordToCheck = wordToCheck.toUpperCase();
		int lookupNum = this.AlphabetIndexer.get(wordToCheck.charAt(0));
		LinkedList<String> currentAnswerSet = this.instanceMapper.get(lookupNum);
		//System.err.println("Copying result into usable subset...");
		String[] usableCopy = copyIntoArray(currentAnswerSet);
		if(AnagramSolver.contains(usableCopy, wordToCheck.toLowerCase())){
			//System.err.println("End word check case!");
			return true;
		}
		//System.err.println("End word check case! It took: " + (System.currentTimeMillis()-startTime));
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
			    outputWriter.write("\n");
			    outputWriter.close();
			    this.permuationHandler = null;
			    System.err.println("Wrote to File Anagram-Answers.txt and saved it to: " + homeDir);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
		}
		else{
			int writeHead = 0;
			try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(homeDir + "\\Anagram-Answers.txt", true))){
				outputWriter.newLine();
				outputWriter.write(wordGiven + " : ");
				while(dataInput[writeHead] != null){
					outputWriter.write(dataInput[writeHead++] + ",");
					
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
		catchResultsFromFileRaw = solver.getFileResource("dictionary.txt");
		long globalStartTime = System.currentTimeMillis();
		AnagramSolver kkE= new AnagramSolver();
		LinkedList<String> wordsToSolve = new LinkedList<String>(Arrays.asList(catchResultsFromFileRaw));
		int numProblemCounter = 0;
		
		String currentWord;
		
		ExecutorService executor = Executors.newFixedThreadPool(20);
	    while((currentWord = wordsToSolve.get(numProblemCounter)) != null && numProblemCounter < 105){
	    	 AnagramAutomator trial = new AnagramAutomator(numProblemCounter, kkE, catchResultsFromFileRaw);
	    	 executor.execute(trial);
	    	 System.gc();
	    	 numProblemCounter++;
	    }
	    executor.shutdown();
	    System.out.println("Full Process has been completed and all answers are in a text file called Anagram-Answer.txt on your desktop! It took a total time of: " +  (System.currentTimeMillis() - globalStartTime) + 
	    		" to process " + numProblemCounter + " anagrams!");
	    
	}
}

