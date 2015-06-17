package data.map
{
	import flash.utils.Dictionary;

	public class MapUnit
	{
		public var mapWidth:int;
		public var mapHeight:int;
		
		public var size:int;
		
		public var dic:Dictionary = new Dictionary;
		
		public var neightbourDic:Dictionary = new Dictionary;
		
		public function init():void{
			
			size = mapWidth * mapHeight - int(mapHeight * 0.5);
			
			for(var str:String in dic){
				
				var pos:int = int(str);
				
				neightbourDic[pos] = getNeighbourPosVec(pos);
			}
		}
		
		private function  getNeighbourPosVec(_pos:int):Vector.<int>{
			
			var vec:Vector.<int> = new Vector.<int>(6,true);
			
			if(_pos % (mapWidth * 2 - 1) != 0){
				
				if(_pos > mapWidth - 1){
					
					var p:int = _pos - mapWidth;
					
					if(p in dic){
						
						vec[5] = p;
						
					}else{
						
						vec[5] = -1;
					}
					
				}else{
					
					vec[5] = -1;
				}
				
				if(_pos < size - mapWidth){
					
					p = _pos + mapWidth - 1;
					
					if(p in dic){
						
						vec[3] = p;
						
					}else{
						
						vec[3] = -1;
					}
					
				}else{
					
					vec[3] = -1;
				}
				
				if(_pos % (mapWidth * 2 - 1) != mapWidth){
					
					p = _pos - 1;
					
					if(p in dic){
						
						vec[4] = p;
						
					}else{
						
						vec[4] = -1;
					}
					
				}else{
					
					vec[4] = -1;
				}
				
			}else{
				
				vec[3] = -1;
				vec[4] = -1;
				vec[5] = -1;
			}
			
			if(_pos % (mapWidth * 2 - 1) != mapWidth - 1){
				
				if(_pos > mapWidth - 1){
					
					p = _pos - mapWidth + 1;
					
					if(p in dic){
						
						vec[0] = p;
						
					}else{
						
						vec[0] = -1;
					}
					
				}else{
					
					vec[0] = -1;
				}
				
				if(_pos < size - mapWidth){
					
					p = _pos + mapWidth;
					
					if(p in dic){
						
						vec[2] = p;
						
					}else{
						
						vec[2] = -1;
					}
					
				}else{
					
					vec[2] = -1;
				}
				
				if(_pos % (mapWidth * 2 - 1) != mapWidth * 2 - 2){
					
					p = _pos + 1;
					
					if(p in dic){
						
						vec[1] = p;
						
					}else{
						
						vec[1] = -1;
					}
					
				}else{
					
					vec[1] = -1;
				}
				
			}else{
				
				vec[0] = -1;
				vec[1] = -1;
				vec[2] = -1;
			}
			
			return vec;
		}
	}
}