package dist;

import java.util.Arrays;
import java.util.concurrent.atomic.*;

public class AnagramAutomator implements Runnable{
	private int loopCounter;
	AnagramSolver sharedInstance;
	String[] resultFromPerm;
	private String[] fileResults;
	private String[] passDictionary;
	
	public AnagramAutomator(int loopCounter, AnagramSolver sharedInstance, String[] passDictionary){
		this.loopCounter = loopCounter;
		this.sharedInstance = sharedInstance;
		this.resultFromPerm = new String[350000];
		this.passDictionary = passDictionary;
	}
	
	@Override
	public void run(){
		int passLoop = loopCounter;
		AnagramSolver newt = sharedInstance;
		makeHistory(passLoop, newt);
	}
	
	private void makeHistory(int loopCounter, AnagramSolver solver){
	    int k = loopCounter;
		System.out.println("Starting word number " + k + " to be solved...");
 		long startTimer = System.currentTimeMillis();
 		solver.setupAlphabetHash();
 		this.resultFromPerm = solver.findAllResults(this.passDictionary[k], solver.getFileResource("dictionary.txt"));
 		solver.writeResultsToFile(this.resultFromPerm, this.passDictionary[k]);
 		System.out.println("End word puzzle " + k + " It took: " + ((System.currentTimeMillis() - startTimer)/1000 + " seconds to be solved!"));
 		return;
	}
	
	public int getNewLoopCount(){
		return this.loopCounter;
	}
	
	public String[] getResults(){
		return this.resultFromPerm;
	}
	
	public String[] resetResultArr(){
		Arrays.fill(this.resultFromPerm, null);
		return this.resultFromPerm;
	}
}
