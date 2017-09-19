package treapwithweight;

import java.util.ArrayList;

public class Collection {
	
	//this is used to store the music collection in some order for the user to see
	private ArrayList<Song> playlist = new ArrayList<Song>();
	
	//this is used to store the music collection for the shuffle function 
	private ArrayList<Song> shuffle = new ArrayList<Song>();
	
	//TODO to implement smart play list
	
	//TODO add more variable; 
	
	//this constructor is mainly a placeholder, we can store more info in the future
	Collection(){}
	
	void Addsong(double seed, String name){
		
	}
	
	void bubbleup() {
		
	}
	
	
	
	
	
	//getter for the playlist
	public ArrayList<Song> getPlaylist() {
		return playlist;
	}

	public ArrayList<Song> getShuffle() {
		return shuffle;
	}
}

