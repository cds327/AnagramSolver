package dist;


public class AnagramAutomator implements Runnable{
	public int loopCounter;
	AnagramSolver sharedInstance;
	public String[] results;
	public String[] fileResults;
	
	public AnagramAutomator(int loopCounter, AnagramSolver sharedInstance){
		this.loopCounter = loopCounter;
		this.sharedInstance = sharedInstance;
	}
	
	@Override
	public void run(){
		makeHistory(loopCounter, sharedInstance);
	}
	
	private void makeHistory(int loopCounter, AnagramSolver solver){
		String[] results = new String[100000];
	    String[] catchResultsFromFile = new String[350000];
	    int k = loopCounter;
		System.out.println("Starting word number " + k + " to be solved...");
 		System.out.println("EIJE: " + k);
 		long startTimer = System.currentTimeMillis();
 		solver.setupAlphabetHash();
 		catchResultsFromFile = solver.getFileResource("dictionary.txt");
 		results = solver.findAllResults(catchResultsFromFile[k], solver.getFileResource("dictionary.txt"));
 		solver.writeResultsToFile(results, catchResultsFromFile[k]);
 		this.loopCounter = this.loopCounter + 1;
 		System.out.println("AFTER: " + solver.problemInt);
 		System.out.println("End word puzzle. It took: " + ((System.currentTimeMillis() - startTimer)/1000 + " seconds to be solved!"));
 		return;
	}
	
	public int getNewLoopCount(){
		return this.loopCounter;
	}
}
