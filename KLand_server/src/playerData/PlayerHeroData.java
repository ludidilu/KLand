package playerData;

import java.util.ArrayList;

import userData.UserDataUnit;

public class PlayerHeroData extends UserDataUnit{

	public ArrayList<Integer> heros;
	
	public void init(){
		
		heros = new ArrayList<>();
		
		for(int i = 0 ; i < 40 ; i++){
			
			heros.add(i + 1);
		}
	}
}
