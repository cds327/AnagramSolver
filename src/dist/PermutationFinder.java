package dist;

import java.util.LinkedList;

public class PermutationFinder implements Runnable {
	public LinkedList<String> permutationHandler;
	public String perm;
	public String word;
	
	public PermutationFinder(LinkedList<String> permutationHandler, String perm, String word){
		this.permutationHandler = permutationHandler;
		this.perm = perm;
		this.word = word;
	}
	
	@Override
	public void run(){
		permMe(perm, word);
	}
	
	private void permMe(String perm, String word){
		if(word.isEmpty()){
			if(permutationHandler == null){
				this.permutationHandler = new LinkedList<String>();
				this.permutationHandler.add(perm + word);
			}
			else{
				this.permutationHandler.add(perm + word);
			}
		}
		else{
			word = word.toLowerCase();
			for(int i = 0; i < word.length(); i++){
			   permMe(perm + word.charAt(i), word.substring(0,  i) + word.substring(i+1, word.length()));
			}
		}
	}
	
	public LinkedList<String> getReturn(){
		return this.permutationHandler;
	}
	
}
