package treapwithweight;

import java.util.ArrayList;
import java.util.Random;

public class Collection {

	private ArrayList<Song> playlist = new ArrayList<Song>();
	private ArrayList<Song> shuffle = new ArrayList<Song>();

	//TODO to implement smart play list

	//TODO add more variable; 

	//this constructor is mainly a placeholder, we can store more info in the future
	Collection(){}

	//replace position indexShuffleList  
	private void insertShuffle(Song g,int indexShuffleList){
		double oldVal;
		double curVal = g.getSeed_value();


		if(indexShuffleList == shuffle.size()) {
			g.setLeft_sum(0);
			g.setRight_sum(0);
			shuffle.add(g);
			oldVal = 0;
		}

		else {
			Song curShuffleEnt = shuffle.get(indexShuffleList);
			oldVal = curShuffleEnt.getSeed_value();
			g.setLeft_sum(curShuffleEnt.getLeft_sum());
			g.setRight_sum(curShuffleEnt.getRight_sum());
			shuffle.remove(indexShuffleList);
			shuffle.add(indexShuffleList, g);

			if(indexShuffleList == 0) {
				return;
			}
		}

		//update values on top, a bit inefficient, but rt is about the same 
		while(indexShuffleList != 0){

			Song curShuffleEnt = shuffle.get((indexShuffleList -1)/2); 



			if(indexShuffleList%2 == 0) {
				curShuffleEnt.setRight_sum(curShuffleEnt.getRight_sum() + curVal - oldVal);
			}
			else {
				curShuffleEnt.setLeft_sum(curShuffleEnt.getLeft_sum() + curVal - oldVal);
			}
			indexShuffleList = (indexShuffleList-1)/2;
		}
	}

	//remove the last element of shuffle
	private Song removeLastShuffle() throws Exception {

		if(shuffle.size() == 0) {
			throw new Exception("Shuffle Arraylist is empty");
		}
		if(shuffle.size() == 1) {
			return shuffle.remove(0);
		}

		Song return_temp = shuffle.remove(shuffle.size()-1);

		int indexShuffleList = shuffle.size();

		while(indexShuffleList != 0){
			Song curShuffleEnt = shuffle.get((indexShuffleList -1)/2); 


			if(indexShuffleList%2 == 1) {
				curShuffleEnt.setLeft_sum(curShuffleEnt.getLeft_sum() - return_temp.getSeed_value());
			}
			else {
				curShuffleEnt.setRight_sum(curShuffleEnt.getRight_sum() -  return_temp.getSeed_value());
			}
			indexShuffleList = (indexShuffleList -1)/2;
		}
		return return_temp;
	}


	private void bubbledown(int index) {
		//check if there is a layer below
		while(2*index+1 < shuffle.size()) {
			//bubble down right 

			Song curIndex = shuffle.get(index);
			Song leftLeaf = shuffle.get(2*index+1);
			
			//a counter to see if the current node have bubbledown
			boolean contIf= true;

			if(2*index+2 < shuffle.size()) {				
				Song rightLeaf = shuffle.get(2*index+2);
				//checking if the right size is bigger than both left and right
				if(curIndex.getSeed_value() <  rightLeaf.getSeed_value() && 
						leftLeaf.getSeed_value() <  rightLeaf.getSeed_value() ) {
					double temp_left = rightLeaf.getLeft_sum();
					double temp_right = rightLeaf.getRight_sum();

					//swapping index positions
					shuffle.set(index, rightLeaf);
					rightLeaf.setRight_sum(curIndex.getRight_sum() + curIndex.getSeed_value()-rightLeaf.getSeed_value());
					rightLeaf.setLeft_sum(curIndex.getLeft_sum());

					shuffle.set(2*index+2, curIndex);
					curIndex.setLeft_sum(temp_left);
					curIndex.setRight_sum(temp_right);

					//update for the next iteration of the loop
					index = 2*index +2;
					
					contIf = false;
				}
			}
			//bubble down left
			if(curIndex.getSeed_value() <  leftLeaf.getSeed_value() && contIf) {
				double temp_left = leftLeaf.getLeft_sum();
				double temp_right = leftLeaf.getRight_sum();

				//swapping index positions
				shuffle.set(index,leftLeaf);
				leftLeaf.setLeft_sum(curIndex.getLeft_sum() + curIndex.getSeed_value()-shuffle.get(index).getSeed_value());
				leftLeaf.setRight_sum(curIndex.getRight_sum());

				shuffle.set(2*index+1, curIndex);
				curIndex.setLeft_sum(temp_left);
				curIndex.setRight_sum(temp_right);

				//update for the next iteration of the loop
				index = 2*index +1;
			}

			else if(contIf){
				return;
			}	
		}
	}

	//bubble up from index
	private void bubbleUp(int index) {
		//exit the loop (cause overflow is a thing)

		while(index != 0){
			Song curIndex = shuffle.get(index);
			Song parentNode = shuffle.get((index-1)/2);

			if(parentNode.getSeed_value() <  curIndex.getSeed_value() ) {
				double temp_left =curIndex.getLeft_sum();
				double temp_right = curIndex.getRight_sum();

				//swapping index positions
				if(index%2 == 1) {
					curIndex.setLeft_sum(parentNode.getLeft_sum() + parentNode.getSeed_value()-curIndex.getSeed_value());
					curIndex.setRight_sum(parentNode.getRight_sum());
				}

				else {
					curIndex.setRight_sum(parentNode.getRight_sum() + parentNode.getSeed_value()-curIndex.getSeed_value());
					curIndex.setLeft_sum(parentNode.getLeft_sum());
				}


				parentNode.setLeft_sum(temp_left);
				parentNode.setRight_sum(temp_right);

				shuffle.set(index, parentNode);
				shuffle.set((index-1)/2, curIndex);

				//update for the next iteration of the loop
				index = (index-1)/2;
			}
			else {
				return;
			}
		}
	}

	//adding a song to the playlist
	void addsong(Song g) {
		playlist.add(g);
		insertShuffle(g, shuffle.size());
		bubbleUp(shuffle.size()-1);
	}

	//remove a song base index within the playlist
	Song remove(int insertIndex) throws Exception {
		Song return_temp = playlist.get(insertIndex) ;
		int pos_in_shuffle = shuffle.indexOf(return_temp);

		if(pos_in_shuffle != shuffle.size()-1) {
			Song lastEnt = removeLastShuffle();

			insertShuffle(lastEnt, pos_in_shuffle);

			
			//there is internal mag for checking lol.

			bubbleUp(pos_in_shuffle);
			bubbledown(pos_in_shuffle);
			System.out.println(logString());

		}

		else {
			removeLastShuffle();
		}

		playlist.remove(insertIndex);
		return return_temp;
	}

	//this is ment to extract a song from the collection randomly base on the seed value
	//Huni, Hi, fb message me "some think random" so that I know you read the code, (yes there is a typo on purpose)
	//another question, should we make the whole song extract thing internal or we will handle it in the app intent
	Song removesuffle() throws Exception {
		double rand = Math.random()*getWeight();

		//I want to keep the method call clean, so that is why i make this stuff
		return removeShuffleRecur(rand,0);
	}

	//remove a song base on the shuffling list (base on weight), who knows when a helper function is actually used
	//to handle the recursion
	private Song removeShuffleRecur(double curseedvaule,int index) throws Exception{
		//I don't want to access it the index element a billion time, so here we are
		Song curNode = shuffle.get(index);

		//Use the "binary search tree" ish algorithm  
		if (curseedvaule > curNode.getLeft_sum() && curseedvaule < curNode.getLeft_sum() + curNode.getSeed_value()) {
			//set the last element to the to be remove element

			//remove the current index, put the bottom node to the new position
			Song lastEnt = removeLastShuffle();
			insertShuffle(lastEnt, index);

			bubbleUp(index);
			bubbledown(index);

			playlist.remove(curNode);

			return curNode;
		}

		//recursion 
		else if(curseedvaule < curNode.getLeft_sum()) {
			return removeShuffleRecur(curseedvaule, 2*index+1);
		}
		else {
			return removeShuffleRecur(curseedvaule - curNode.getLeft_sum() - curNode.getSeed_value() , 2*index+2);
		}
	}


	//get the total seed weight of the program 
	public double getWeight() {
		return shuffle.get(0).getsum();
	}


	//getter for the playlist (I want this to be exported as an non object thing)
	//my suggestion will be 
	public ArrayList<Song> getPlaylist() {
		return playlist;
	}

	//display the song in text format()
	public String logString() {
		String rtnStr = "";
		for(int i = 0; i< playlist.size();i++) {
			Song tempStor = playlist.get(i);
			rtnStr += "index " + i + ": " + tempStor.getName() + " " + tempStor.getSeed_value() + " " + tempStor.getLeft_sum() + " " + tempStor.getRight_sum() + "\n";
		}

		for(int i = 0; i< shuffle.size();i++) {
			Song tempStor = shuffle.get(i);
			rtnStr += tempStor.getName() + " " + tempStor.getSeed_value() + " " + tempStor.getLeft_sum() + " " + tempStor.getRight_sum() + "\n";
		}
		return rtnStr;
	}
}

