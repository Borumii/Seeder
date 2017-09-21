package treapwithweight;

public class Song {
	//song initial variables
	private String name; 
	
	//Keep track of the sum of the left and the sum of the right
	private double seed_value;
	
	//just make it easier to keep track of where it's being store
	//index_shuffle is the position of the 
	
	//this set of parameter is use for 
	private double left_sum;
	private double right_sum;
	
	//this is the set of variable for the playlist
	private int index_playlist;
	
	//constructor, there is no memory allocation so I won't bother with big 5
	Song(double seed_value,String name){
		
		//placeholder, but realisticly we are using the Collection to handle this stuff
		setLeft_sum(0);
		setRight_sum(0);
		setName(name);
		setSeed_value(seed_value);
	}
	
	//returns the sum of the nodes, I think it's just a good habit
	public double getsum() {
		return seed_value + left_sum + right_sum;
	}
	
	//getter and setter lol, thanks eclipse 
	public double getSeed_value() {
		return seed_value;
	}

	public void setSeed_value(double seed_vaule) {
		this.seed_value = seed_vaule;
	}

	public double getLeft_sum() {
		return left_sum;
	}

	public void setLeft_sum(double left_sum) {
		this.left_sum = left_sum;
	}

	public double getRight_sum() {
		return right_sum;
	}

	public void setRight_sum(double right_sum) {
		this.right_sum = right_sum;
	}

	public int getIndex_playlist() {
		return index_playlist;
	}

	public void setIndex_playlist(int index_playlist) {
		this.index_playlist = index_playlist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
