package treapwithweight;

import java.util.ArrayList;

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
	void addsong(Song g) {
		playlist.add(g);

		shuffle.add(g);
		
		//need bubble up to do something else with the indexing, so this will do for now
		//update the weights from above
		double seed_val = g.getSeed_value();
		int index = shuffle.size() - 1;
		while(index !=0) {
			if(index/2 ==0) {
				shuffle.get(index/2).setLeft_sum(shuffle.get(index/2).getLeft_sum() + seed_val);
			}
			else {
				shuffle.get(index/2).setRight_sum(shuffle.get(index/2).getRight_sum() + seed_val);
			}
			
			index = index/2;
		}
		
		bubbleUp(shuffle.size() - 1);
	}
	
	Song remove(int index) {
		Song return_temp = playlist.get(index);
		int pos_in_shuffle = shuffle.indexOf(return_temp);
		
		//set the last element to the to be remove element
		shuffle.set(pos_in_shuffle, shuffle.get(shuffle.size()-1));
		shuffle.remove(shuffle.size()-1);
		
		bubbledown(pos_in_shuffle);
		
		playlist.remove(index);
		
		return return_temp;
	}


	//getter for the playlist
	public ArrayList<Song> getPlaylist() {
		return playlist;
	}

	public ArrayList<Song> getShuffle() {
		return shuffle;
	}
}

