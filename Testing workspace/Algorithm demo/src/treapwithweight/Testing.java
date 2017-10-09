package treapwithweight;
//testing Collection/Song
public class Testing {
	public static void main(String args[]) throws Exception {
		Song songlist[] = new Song[10];
		

		Collection test = new Collection();
		
		//testing add
		for(int i = 1; i<=10;i++) {
			songlist[i-1] = new Song(i,Integer.toString(i));
			test.addsong(songlist[i-1]);
		}
		
		
		

	
		
	}
}
