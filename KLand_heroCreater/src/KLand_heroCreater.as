package
{
	import flash.display.Sprite;
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	import flash.utils.Dictionary;
	
	import csv.Csv;
	
	public class KLand_heroCreater extends Sprite
	{
		public static const csvPath:String = "c:/inetpub/wwwroot/KLand/data/csv/";
		
		public function KLand_heroCreater()
		{
			Csv.init(csvPath,Vector.<String>([Csv_heroData.NAME,Csv_heroTypeData.NAME]),Vector.<Class>([Csv_heroData,Csv_heroTypeData]),getCsv);
		}
		
		private function getCsv():void{
			
//			var num:int = 10;
//			
//			var vec:Vector.<int> = new Vector.<int>(num + 1,true);
//			
//			for(var sss:int = 0 ; sss < 10000 ; sss++){
//				
//				var result:int = getBalanceRandomData(num);
//				
//				vec[result] = vec[result] + 1;
//			}
//			
//			return;
			
//			var ttt:String = "";
//			
//			for(var hh:int = 1 ; hh <= 100 ; hh++){
//				
//				if(hh != 100){
//				
//					ttt = ttt + hh + "$";
//					
//				}else{
//					
//					ttt = ttt + hh;
//				}
//			}
//			
//			trace(ttt);
//			
//			return;
			
			var str:String = "";
			
			var id:int = 1;
			
			var dic:Dictionary = Csv.getDic(Csv_heroData.NAME);
			
			var traceStr:String = "";
			
			for each(var unit:Csv_heroData in dic){
				
//				var tmpList:Vector.<int> = Vector.<int>([0,0,1,1,2,2,3,3,4,5,6,7]);
				
				var tmpList:Vector.<int> = Vector.<int>([0,0,1,1,2,2,3,3,5,6,7]);
				
				for(var i:int = 0 ; i < 10 ; i++){
				
					str = str + id + "," + "player" + id + "," + id + ",";
					
					traceStr = traceStr + id + "$";
					
					var rand:int = int(Math.random() * tmpList.length);
					
					var type:int = tmpList[rand];
					
					tmpList.splice(rand,1);
					
					var attNum:int = unit.attNum + unit["type" + type] - (Csv.getData(Csv_heroTypeData.NAME,type) as Csv_heroTypeData).hpNum;
					
					var hp:int = 1;
					
					var addHp:int = getBalanceRandomData(int(attNum / (Csv.getData(Csv_heroTypeData.NAME,type) as Csv_heroTypeData).hpNum));
					
					hp = hp + addHp;
					
					attNum = attNum - addHp * (Csv.getData(Csv_heroTypeData.NAME,type) as Csv_heroTypeData).hpNum;
					
					var atk:int = int(attNum / (Csv.getData(Csv_heroTypeData.NAME,type) as Csv_heroTypeData).atkNum);
					
					str = str + type + "," + unit.star + "," + hp + "," + atk + "," + unit.power + ",100,100\r\n";
					
					id++;
				}
			}
			
			var file:File = new File("c:/a.csv");
			
			var fs:FileStream = new FileStream;
			
			fs.open(file,FileMode.WRITE);
			
			fs.writeUTFBytes(str);
			
			fs.close();
			
			trace("over!!!");
			
			traceStr = traceStr.substr(0,traceStr.length - 1);
			
			trace(traceStr);
		}
		
		private function getBalanceRandomData(_max:int):int{
			
			var allScore:int = 0;
			
			var scoreVec:Vector.<int> = new Vector.<int>(_max + 1,true);
			
			if(_max % 2 == 0){
				
				for(i = 0 ; i < (_max - 1) / 2 ; i++){
					
					var score:int = getScore(i);
					
					scoreVec[i] = scoreVec[_max - i] = score;
					
					allScore = allScore + score * 2;
				}
				
				score = getScore(i);
				
				scoreVec[int(_max / 2)] = score;
				
				allScore = allScore +score;
				
			}else{
				
				for(var i:int = 0 ; i < _max / 2 ; i++){
					
					score = getScore(i);
					
					scoreVec[i] = scoreVec[_max - i] = score;
					
					allScore = allScore + score * 2;
				}
			}
			
			var random:int = int(Math.random() * allScore);
			
			for(i = 0 ; i <= _max ; i++){
				
				if(random < scoreVec[i]){
					
					return i;
					
				}else{
					
					random = random - scoreVec[i];
				}
			}
			
			return 0;
		}
		
		private function getScore(_i:int):int{
			
			return _i * 4 + 1;
		}
	}
}