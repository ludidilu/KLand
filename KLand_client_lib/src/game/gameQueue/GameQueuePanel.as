package game.gameQueue
{
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	
	import connect.Connect;
	
	import starling.core.Starling;

	public class GameQueuePanel extends Sprite
	{
		private var bt:Sprite;
		private var tf:TextField;
		
		private var isWaitting:Boolean;
		
		private var bt2:Sprite;
		private var tf2:TextField;
		
		private var isWaitting2:Boolean;
		
		public function GameQueuePanel()
		{
			bt = new Sprite;
			bt.graphics.beginFill(0xFF0000);
			bt.graphics.drawRect(0,0,100,30);
			
			bt.x = (Starling.current.stage.stageWidth - bt.width) * 0.5;
			bt.y = 300;
			
			var textFormat:TextFormat = new TextFormat;
			
			textFormat.size = 20;
			textFormat.align = TextFormatAlign.CENTER;
			
			tf = new TextField;
			
			tf.defaultTextFormat = textFormat;
			
			tf.width = 100;
			tf.height = 30;
			
			tf.selectable = false;
			
			bt.addChild(tf);
			
			tf.text = "Enter Game";
			
			addChild(bt);
			
			bt.addEventListener(MouseEvent.CLICK,btClick);
			
			
			bt2 = new Sprite;
			bt2.graphics.beginFill(0xFF0000);
			bt2.graphics.drawRect(0,0,100,30);
			
			bt2.x = (Starling.current.stage.stageWidth - bt.width) * 0.5;
			bt2.y = 400;
			
			tf2 = new TextField;
			
			tf2.defaultTextFormat = textFormat;
			
			tf2.width = 100;
			tf2.height = 30;
			
			tf2.selectable = false;
			
			bt2.addChild(tf2);
			
			tf2.text = "Enter Ai";
			
			addChild(bt2);
			
			bt2.addEventListener(MouseEvent.CLICK,btClick2);
		}
		
		private function btClick(e:MouseEvent):void{
			
			if(!isWaitting){
				
				Connect.sendData(4,enterQueueOK);
				
			}else{
				
				Connect.sendData(6,quitQueueOK);
			}
		}
		
		public function enterQueueOK(_b:Boolean):void{
			
			if(_b){
			
				isWaitting = true;
				
				tf.text = "Quit Game";
				
				bt2.visible = false;
				tf2.visible = false;
			}
		}
		
		public function quitQueueOK(_b:Boolean):void{
			
			if(_b){
			
				isWaitting = false;
				
				tf.text = "Enter Game";
				
				bt2.visible = true;
				tf2.visible = true;
			}
		}
		
		
		private function btClick2(e:MouseEvent):void{
			
			if(!isWaitting2){
				
				Connect.sendData(14,fightAiOK);
				
			}else{
				
				Connect.sendData(16,quitAiOK);
			}
		}
		
		public function fightAiOK(_b:Boolean):void{
			
			if(_b){
				
				isWaitting2 = true;
				
				tf2.text = "Quit Ai";
				
				bt.visible = false;
				tf.visible = false;
			}
		}
		
		public function quitAiOK(_b:Boolean):void{
			
			if(_b){
				
				isWaitting2 = false;
				
				tf2.text = "Enter Ai";
				
				bt.visible = true;
				tf.visible = true;
			}
		}
		
		public function reset():void{
			
			quitQueueOK(true);
			quitAiOK(true);
		}
	}
}