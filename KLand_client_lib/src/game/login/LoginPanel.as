package game.login
{
	import flash.display.Shape;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFieldType;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	
	import connect.Connect;
	
	import game.Game;
	
	import starling.core.Starling;
	
	public class LoginPanel extends Sprite
	{
		private var nameText:TextField;
		private var pwdText:TextField;
		private var bt:Sprite;
		
		public function LoginPanel()
		{
			super();
			
			var textFormat:TextFormat = new TextFormat;
			
			textFormat.size = 30;
			textFormat.align = TextFormatAlign.CENTER;
			
			nameText = new TextField;
			
			nameText.defaultTextFormat = textFormat;
			
			nameText.type = TextFieldType.INPUT;
			
			nameText.border = true;
			
			addChild(nameText);
			
			nameText.width = 200;
			nameText.height = nameText.textHeight;
			
			nameText.x = (Starling.current.stage.stageWidth - nameText.width) * 0.5;
			nameText.y = 150;
			
			
			pwdText = new TextField;
			
			pwdText.defaultTextFormat = textFormat;
			
			pwdText.type = TextFieldType.INPUT;
			
			pwdText.border = true;
			
			pwdText.displayAsPassword = true;
			
			addChild(pwdText);
			
			pwdText.width = 200;
			pwdText.height = nameText.textHeight;
			
			pwdText.x = (Starling.current.stage.stageWidth - pwdText.width) * 0.5;
			pwdText.y = 200;
			
			var shape:Shape = new Shape;
			shape.graphics.beginFill(0xFF0000);
			shape.graphics.drawRect(0,0,100,30);
			bt = new Sprite;
			bt.addChild(shape);
			
			bt.x = (Starling.current.stage.stageWidth - bt.width) * 0.5;
			bt.y = 300;
			
			addChild(bt);
			
			bt.addEventListener(MouseEvent.CLICK,click);
			
		}
		
		private function click(e:MouseEvent):void{
			
			Connect.sendData(0,Game.loginOK,nameText.text,pwdText.text);
		}
		
		public function loginFail():void{
			
			nameText.text = "";
			pwdText.text = "";
		}
		
	}
}