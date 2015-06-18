package
{
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	import flash.geom.Rectangle;
	import flash.net.FileFilter;
	import flash.text.TextField;
	import flash.text.TextFieldType;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	
	import starling.core.Starling;
	import starling.display.Button;
	import starling.display.Quad;
	import starling.display.Sprite;
	import starling.events.Event;
	import starling.textures.Texture;
	
	[SWF(width="960",height="640",frameRate="60")]
	public class KLand_MapEditor extends flash.display.Sprite
	{
		private var container:starling.display.Sprite;
		
		private var maskContainer:starling.display.Sprite;
		
		private var newBt:Button;
		private var openBt:Button;
		private var saveBt:Button;
		
		private var inputSp:flash.display.Sprite;
		
		private var tfWidth:TextField;
		private var tfHeight:TextField;
		
		private var browserFile:File;
		
		public function KLand_MapEditor()
		{
			super();
			
			addEventListener(flash.events.Event.ADDED_TO_STAGE,addedToStage);
		}
		
		private function addedToStage(e:flash.events.Event):void{
			
			// 支持 autoOrient
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			
			var myStarling:Starling = new Starling(starling.display.Sprite,stage,new Rectangle(0,0,stage.stageWidth,stage.stageHeight));
			
			myStarling.addEventListener(starling.events.Event.CONTEXT3D_CREATE,initOK);
		}
		
		private function initOK(e:starling.events.Event):void{
			
			Starling.current.start();
			
			start();
		}
		
		private function start():void{
			
			browserFile = File.applicationDirectory.resolvePath("");
			
			browserFile.addEventListener(flash.events.Event.SELECT,fileOpen);
			
			container = new starling.display.Sprite;
			
			Starling.current.stage.addChild(container);
			
			newBt = new Button(Texture.fromColor(100,30,0xFFFF0000),"New");
			
			newBt.x = Starling.current.backBufferWidth - 30 - newBt.width;
			
			newBt.y = 30;
			
			newBt.addEventListener(starling.events.Event.TRIGGERED,newBtClick);
			
			Starling.current.stage.addChild(newBt);
			
			openBt = new Button(Texture.fromColor(100,30,0xFFFF0000),"Open");
			
			openBt.x = Starling.current.backBufferWidth - 30 - openBt.width;
			
			openBt.y = 90;
			
			openBt.addEventListener(starling.events.Event.TRIGGERED,openBtClick);
			
			Starling.current.stage.addChild(openBt);
			
			saveBt = new Button(Texture.fromColor(100,30,0xFFFF0000),"Save");
			
			saveBt.x = Starling.current.backBufferWidth - 30 - saveBt.width;
			
			saveBt.y = 150;
			
			saveBt.addEventListener(starling.events.Event.TRIGGERED,saveBtClick);
			
			saveBt.visible = false;
			
			Starling.current.stage.addChild(saveBt);
			
			var quad:Quad = new Quad(Starling.current.backBufferWidth,Starling.current.backBufferHeight,0x000000);
			
			quad.alpha = 0.3;
			
			maskContainer = new starling.display.Sprite;
			
			maskContainer.addChild(quad);
			
			maskContainer.flatten();
			
			maskContainer.visible = false;
			
			Starling.current.stage.addChild(maskContainer);
			
			inputSp = new flash.display.Sprite;
			
			inputSp.graphics.beginFill(0xFF0000);
			
			var spWidth:Number = 400;
			var spHeight:Number = 300;
			
			inputSp.graphics.drawRect(0,0,spWidth,spHeight);
			
			var textFormat:TextFormat = new TextFormat;
			
			textFormat.size = 30;
			
			tfWidth = new TextField;
			
			tfWidth.defaultTextFormat = textFormat;
			
			tfWidth.restrict = "0123456789";
			
			tfWidth.type = TextFieldType.INPUT;
			
			tfWidth.width = 200;
			
			tfWidth.height = 40;
			
			tfWidth.background = true;
			
			tfWidth.border = true;
			
			inputSp.addChild(tfWidth);
			
			tfWidth.x = (inputSp.width - tfWidth.width) * 0.5;
			tfWidth.y = 30;
			
			tfHeight = new TextField;
			
			tfHeight.defaultTextFormat = textFormat;
			
			tfHeight.restrict = "012345679";
			
			tfHeight.type = TextFieldType.INPUT;
			
			tfHeight.width = 200;
			
			tfHeight.height = 40;
			
			tfHeight.background = true;
			
			tfHeight.border = true;
			
			inputSp.addChild(tfHeight);
			
			tfHeight.x = (inputSp.width - tfHeight.width) * 0.5;
			tfHeight.y = 90;
			
			inputSp.x = (Starling.current.backBufferWidth - spWidth) * 0.5;
			inputSp.y = (Starling.current.backBufferHeight - spHeight) * 0.5;
			
			var sp:flash.display.Sprite = new flash.display.Sprite;
			
			sp.graphics.beginFill(0x00FF00);
			
			sp.graphics.drawRect(0,0,80,40);
			
			var ttf:TextFormat = new TextFormat;
			
			ttf.align = TextFormatAlign.CENTER;
			
			var tf:TextField = new TextField;
			
			tf.width = 80;
			tf.height = 40;
			
			tf.defaultTextFormat = ttf;
			
			tf.selectable = false;
			
			tf.text = "OK";
			
			sp.addChild(tf);
			
			sp.x = 80;
			sp.y = 200;
			
			sp.addEventListener(MouseEvent.CLICK,inputOKClick);
			
			inputSp.addChild(sp);
			
			sp = new flash.display.Sprite;
			
			sp.graphics.beginFill(0xFFFF00);
			
			sp.graphics.drawRect(0,0,80,40);
			
			tf = new TextField;
			
			tf.width = 80;
			tf.height = 40;
			
			tf.defaultTextFormat = ttf;
			
			tf.selectable = false;
			
			tf.text = "Cancel";
			
			sp.addChild(tf);
			
			sp.x = 240;
			sp.y = 200;
			
			sp.addEventListener(MouseEvent.CLICK,inputCancelClick);
			
			inputSp.addChild(sp);
			
			inputSp.visible = false;
			
			stage.addChild(inputSp);
		}
		
		private function newBtClick(e:starling.events.Event):void{
			
			maskContainer.visible = true;
			inputSp.visible = true;
			
			tfWidth.text = "";
			tfHeight.text = "";
		}
		
		private function openBtClick(e:starling.events.Event):void{
			
			browserFile.browse([new FileFilter("map","*.map")]);
		}
		
		private function fileOpen(e:flash.events.Event):void{
			
			var file:File = e.target as File;
			
			var fs:FileStream = new FileStream;
			
			fs.open(file,FileMode.READ);
			
			var byteArray:ByteArray = new ByteArray;
			
			fs.readBytes(byteArray);
			
			fs.close();
			
			var mapWidth:int = byteArray.readInt();
			
			var mapHeight:int = byteArray.readInt();
			
			var length:int = byteArray.readInt();
			
			var dic:Dictionary = new Dictionary;
			
			for(var i:int = 0 ; i < length ; i++){
				
				var index:int = byteArray.readInt();
				
				var value:int = byteArray.readInt();
				
				dic[index] = value;
			}
			
			Map.start(container,mapWidth,mapHeight,dic);
			
			saveBt.visible = true;
		}
		
		private function inputOKClick(e:MouseEvent):void{
			
			startEditor(int(tfWidth.text),int(tfHeight.text),new Dictionary);
		}
		
		private function inputCancelClick(e:MouseEvent):void{
			
			maskContainer.visible = false;
			inputSp.visible = false;
		}
		
		private function startEditor(_width:int,_height:int,_dic:Dictionary):void{
			
			maskContainer.visible = false;
			inputSp.visible = false;
			
			Map.start(container,_width,_height,_dic);
			
			saveBt.visible = true;
		}
		
		private function saveBtClick(e:starling.events.Event):void{
			
			Map.save();
		}
	}
}