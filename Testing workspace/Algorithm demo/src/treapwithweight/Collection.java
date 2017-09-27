package treapwithweight;

import java.util.ArrayList;
import java.util.Random;

public class Collection {

	//this is used to store the music collection in some order for the user to see
	private ArrayList<Song> playlist = new ArrayList<Song>();

	//this is used to store the music collection for the shuffle function, since we are storing an interval here, I will use array list to take advantage of the constant access time
	private ArrayList<Song> shuffle = new ArrayList<Song>();

	//TODO to implement smart play list

	//TODO add more variable; 

	//this constructor is mainly a placeholder, we can store more info in the future
	Collection(){}

	//perform bubble down at a certain index, no recursion from array
	//TODO might use a temporary pointer for the constant arraylist access
	private void bubbledown(int index) {

		//exit the loop (cause overflow is a thing)
		boolean exit = false;
		//check if there is a layer below
		while(2*index < shuffle.size()&& exit) {
			//bubble down right 
			if(2*index+1 < shuffle.size()) {

				//checking if the right size is bigger than both left and right
				if(shuffle.get(index).getSeed_value() <  shuffle.get(2*index+1).getSeed_value() && 
						shuffle.get(2*index).getSeed_value() <  shuffle.get(2*index+1).getSeed_value() ) {
					Song temp = shuffle.get(index);
					double temp_left = shuffle.get(2*index+1).getLeft_sum();
					double temp_right = shuffle.get(2*index+1).getLeft_sum();

					//swapping index positions
					shuffle.set(index, shuffle.get(2*index+1));
					shuffle.get(index).setRight_sum(temp.getRight_sum() + temp.getSeed_value()-shuffle.get(index).getSeed_value());
					shuffle.get(index).setLeft_sum(temp.getLeft_sum());

					shuffle.set(2*index+1, temp);
					temp.setLeft_sum(temp_left);
					temp.setRight_sum(temp_right);

					//update for the next iteration of the loop
					index = 2*index +1;
				}
			}
			//bubble down left
			else if(shuffle.get(index).getSeed_value() <  shuffle.get(2*index).getSeed_value() ) {
				Song temp = shuffle.get(index);
				double temp_left = shuffle.get(2*index).getLeft_sum();
				double temp_right = shuffle.get(2*index).getLeft_sum();

				//swapping index positions
				shuffle.set(index, shuffle.get(2*index));
				shuffle.get(index).setLeft_sum(temp.getLeft_sum() + temp.getSeed_value()-shuffle.get(index).getSeed_value());
				shuffle.get(index).setRight_sum(temp.getRight_sum());

				shuffle.set(2*index, temp);
				temp.setLeft_sum(temp_left);
				temp.setRight_sum(temp_right);

				//update for the next iteration of the loop
				index = 2*index +1;
			}

			else {
				exit = true;
			}	
		}
	}

	//bubble up from index
	//TODO might use a temporary pointer for the constant arraylist access
	private void bubbleUp(int index) {
		//exit the loop (cause overflow is a thing)
		boolean exit = false;

		while(index != 0 && exit){
			if(shuffle.get(index/2).getSeed_value() <  shuffle.get(index).getSeed_value() ) {
				Song temp = shuffle.get(index/2);
				double temp_left = shuffle.get(index).getLeft_sum();
				double temp_right = shuffle.get(index).getLeft_sum();

				//swapping index positions
				if(index%2 == 0) {
					shuffle.set(index/2, shuffle.get(index));
					shuffle.get(index/2).setLeft_sum(temp.getLeft_sum() + temp.getSeed_value()-shuffle.get(index/2).getSeed_value());
					shuffle.get(index/2).setRight_sum(temp.getRight_sum());
				}
				
				else {
					shuffle.set(index/2, shuffle.get(index));
					shuffle.get(index/2).setRight_sum(temp.getRight_sum() + temp.getSeed_value()-shuffle.get(index/2).getSeed_value());
					shuffle.get(index/2).setLeft_sum(temp.getLeft_sum());
				}
				shuffle.set(2*index, temp);
				temp.setLeft_sum(temp_left);
				temp.setRight_sum(temp_right);

				//update for the next iteration of the loop
				index = index/2;
			}
		}
	}

	//adding a song to the playlist
	//TODO fix indexing problems
	void addsong(Song g) {
		playlist.add(g);

		shuffle.add(g);
		
		//need bubble up to do something else with the indexing, so this will do for now
		//update the weights from above, to make bubble up smoother 
		double seed_val = g.getSeed_value();
		int index = shuffle.size() - 1;
		while(index !=0) {
			if(index%2 ==0) {
				shuffle.get((index-1)/2).setLeft_sum(shuffle.get(index/2).getLeft_sum() + seed_val);
			}
			else {
				shuffle.get((index-1)/2).setRight_sum(shuffle.get(index/2).getRight_sum() + seed_val);
			}
			
			index = (index-1)/2;
		}
		
		bubbleUp(shuffle.size() - 1);
	}
	
	//remove a song base index within the playlist
	Song remove(int index) {
		Song return_temp = playlist.get(index);
		int pos_in_shuffle = shuffle.indexOf(return_temp);
		
		//set the last element to the to be remove element
		Song bot = shuffle.get(shuffle.size()-1);
		bot.setLeft_sum(return_temp.getLeft_sum());
		bot.setRight_sum(return_temp.getRight_sum());
		
		shuffle.set(pos_in_shuffle, shuffle.get(shuffle.size()-1));
		shuffle.remove(shuffle.size()-1);
		
		bubbledown(pos_in_shuffle);
		
		playlist.remove(index);
		
		return return_temp;
	}
	
	//this is ment to extract a song from the collection randomly base on the seed value
	//Huni, Hi, fb message me "some think random" so that I know you read the code, (yes there is a typo on purpose)
	//another question, should we make the whole song extract thing internal or we will handle it in the app intent
	Song removesuffle() {
		double rand = Math.random()*getWeight();
		
		//I want to keep the method call clean, so that is why i make this stuff
		return removeShuffleRecur(rand,0);
	}
	
	//remove a song base on the shuffling list (base on weight), who knows when a helper function is actually used
	//to handle the recursion
	private Song removeShuffleRecur(double curseedvaule,int index){
		//I don't want to access it the index element a billion time, so here we are
		Song curNode = shuffle.get(index);
		
		//Use the "binary search tree" ish algorithm  
		if (curseedvaule > curNode.getLeft_sum() && curseedvaule < curNode.getLeft_sum() + curNode.getSeed_value()) {
			//set the last element to the to be remove element
			
			//remove the current index, put the bottom node to the new position
			shuffle.set(index, shuffle.get(shuffle.size()-1));
			shuffle.remove(shuffle.size()-1);
			
			bubbledown(index);
			
			return curNode;
		}
		
		//recursion 
		else if(curseedvaule < curNode.getLeft_sum()) {
			return removeShuffleRecur(curseedvaule, 2*index+1);
		}
		else {
			return removeShuffleRecur(curseedvaule, 2*index+2);
		}
	}
	
	
	//get the total seed weight of the program 
	double getWeight() {
		
		return shuffle.get(0).getsum();
	}


	//getter for the playlist
	public ArrayList<Song> getPlaylist() {
		return playlist;
	}

	public ArrayList<Song> getShuffle() {
		return shuffle;
	}
}

