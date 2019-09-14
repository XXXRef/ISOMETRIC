package Isometric;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class DisplayClass {
	Game game=new Game();
	
	public void start(){
		System.out.println("game.world.world.print(Start):");
		game.world.world.print();
		try{
			Display.setDisplayMode(new DisplayMode(600,600));
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.out.println("LWJGLException: "+e);
			return;
		}
		initGL();
		while(!Display.isCloseRequested()){
			
			pollInput();
			
			paintGL();
			
			Display.update();
		}
		Display.destroy();
	}
	
	public void initGL(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-(Config.RANGECOVERAGEBLOCKSX+0.5)*Config.BLOCKSIZE*Math.sqrt(2)-50,(Config.RANGECOVERAGEBLOCKSX+0.5)*Config.BLOCKSIZE*Math.sqrt(2)+50,
				     -(Config.RANGECOVERAGEBLOCKSY+0.5)*Config.BLOCKSIZE*Math.sqrt(2)-50,(Config.RANGECOVERAGEBLOCKSY+0.5)*Config.BLOCKSIZE*Math.sqrt(2)+50,
				     -(Config.RANGECOVERAGEBLOCKSZ+0.5)*Config.BLOCKSIZE*Math.sqrt(2)-50,(Config.RANGECOVERAGEBLOCKSZ+0.5)*Config.BLOCKSIZE*Math.sqrt(2)+50);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0,0,0,0);
		GL11.glLineWidth(2);
	}
	
	public void paintGL(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		
		
		
		GL11.glPushMatrix();
		GL11.glRotatef(45, 0, 0, 1);
		GL11.glRotatef(-60, 1, -1, 0);
		
		/*
		GL11.glColor3f(1, 0, 1);
		paintCube(0,0,0,20);
		*/
		
		paintWorld();
		
		//axis
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(0, 0,0);
			GL11.glVertex3f((float)(Config.RANGECOVERAGEBLOCKSX+0.5)*Config.BLOCKSIZE, 0,0);
			GL11.glVertex3f(0,0,0);
			GL11.glVertex3f(0,(float)(Config.RANGECOVERAGEBLOCKSY+0.5)*Config.BLOCKSIZE,0);
			GL11.glVertex3f(0, 0,0);
			GL11.glVertex3f(0, 0,(float)(Config.RANGECOVERAGEBLOCKSZ+0.5)*Config.BLOCKSIZE);
		GL11.glEnd();
		
		GL11.glColor3f(1, 0, 0);//x - red
		paintCube((float)((Config.RANGECOVERAGEBLOCKSX+0.5)*Config.BLOCKSIZE-2.5),0,0,5);
		
		GL11.glColor3f(0, 1, 0);//y - green
		paintCube(0,(float)((Config.RANGECOVERAGEBLOCKSY+0.5)*Config.BLOCKSIZE-2.5),0,5);
		
		GL11.glColor3f(0, 0, 1);//z - blue
		paintCube(0,0,(float)((Config.RANGECOVERAGEBLOCKSZ+0.5)*Config.BLOCKSIZE-2.5),5);
		
		
		
		
		GL11.glPopMatrix();
	}
	
	public void pollInput(){
		if(Mouse.isButtonDown(1)){
			game.movePlayer(-Config.CHUNKSIZE*Config.BLOCKSIZE, -Config.CHUNKSIZE*Config.BLOCKSIZE, -Config.CHUNKSIZE*Config.BLOCKSIZE);
		}
		if(Mouse.isButtonDown(0)){
			game.movePlayer(Config.CHUNKSIZE*Config.BLOCKSIZE, Config.CHUNKSIZE*Config.BLOCKSIZE, Config.CHUNKSIZE*Config.BLOCKSIZE);
		}
		
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){//pressed
				switch(Keyboard.getEventKey()){
					case Keyboard.KEY_A:
						game.movePlayer(0, Config.BLOCKSIZE, 0);
						break;
					case Keyboard.KEY_D:
						game.movePlayer(0, -Config.BLOCKSIZE, 0);
						break;
					case Keyboard.KEY_W:
						game.movePlayer(Config.BLOCKSIZE, 0, 0);
						break;
					case Keyboard.KEY_S:
						game.movePlayer(-Config.BLOCKSIZE, 0, 0);
						break;
					case Keyboard.KEY_Q:
						game.movePlayer(0, 0, -Config.BLOCKSIZE);
						break;
					case Keyboard.KEY_E:
						game.movePlayer(0, 0, Config.BLOCKSIZE);
						break;
				}
			}else{//released
				switch(Keyboard.getEventKey()){
				case Keyboard.KEY_A:
					System.out.println("A released");
					break;
				case Keyboard.KEY_D:
					System.out.println("D released");
					break;
				case Keyboard.KEY_W:
					System.out.println("W released");
					break;
				case Keyboard.KEY_S:
					System.out.println("S released");
					break;
			}
			}
			
		}
	}
	
	public void paintWorld(){

		//player block
		int blockX=(int)((game.player.x<0)?(game.player.x/Config.BLOCKSIZE-1):(game.player.x/Config.BLOCKSIZE));
		int blockY=(int)((game.player.y<0)?(game.player.y/Config.BLOCKSIZE-1):(game.player.y/Config.BLOCKSIZE));
		int blockZ=(int)((game.player.z<0)?(game.player.z/Config.BLOCKSIZE-1):(game.player.z/Config.BLOCKSIZE));
		
		for(int i=0;i<(Config.RANGECOVERAGEBLOCKSZ*2+1);i++){
			for(int j=0;j<(Config.RANGECOVERAGEBLOCKSY*2+1);j++){
				for(int k=0;k<(Config.RANGECOVERAGEBLOCKSX*2+1);k++){
					if(game.world.getBlock(blockX-Config.RANGECOVERAGEBLOCKSX+k,blockY-Config.RANGECOVERAGEBLOCKSY+j,blockZ-Config.RANGECOVERAGEBLOCKSZ+i)!=0){
						GL11.glColor3f(1,0,0);
						paintCube((k-Config.RANGECOVERAGEBLOCKSX)*Config.BLOCKSIZE,(j-Config.RANGECOVERAGEBLOCKSY)*Config.BLOCKSIZE,(i-Config.RANGECOVERAGEBLOCKSZ)*Config.BLOCKSIZE, Config.BLOCKSIZE);
						
					}else{
						GL11.glColor3f(0,1,1);
					}
					
				}
			}
		}
	}
	
	public void paintCube(float x,float y,float z, float side){
		
		x-=side/2;
		y-=side/2;
		z-=side/2;
		
		//cube
		GL11.glBegin(GL11.GL_QUAD_STRIP);
			GL11.glVertex3f(x, y,z);
			GL11.glVertex3f(x+side, y,z);
		
			GL11.glVertex3f(x, y, z+side);//1st quad
			GL11.glVertex3f(x+side,y,z+side);
			
			GL11.glVertex3f(x, y+side, z+side);//2nd quad
			GL11.glVertex3f(x+side,y+side,z+side);
			
			GL11.glVertex3f(x, y+side,z);//3rd quad
			GL11.glVertex3f(x+side, y+side,z);
			
			GL11.glVertex3f(x, y,z);//4th quad
			GL11.glVertex3f(x+side, y,z);
			
			GL11.glVertex3f(x, y,z);//1st side quad
			GL11.glVertex3f(x, y,z+side);
			GL11.glVertex3f(x, y+side,z+side);
			GL11.glVertex3f(x, y+side,z);
			
			GL11.glVertex3f(x+side, y,z);//2nd side quad
			GL11.glVertex3f(x+side, y,z+side);
			GL11.glVertex3f(x+side, y+side,z+side);
			GL11.glVertex3f(x+side, y+side,z);
		GL11.glEnd();
		
		//cube bounds
		GL11.glColor3f(0,0,0);
		GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex3f(x,y,z);
			GL11.glVertex3f(x+side,y,z);
			GL11.glVertex3f(x+side,y+side,z);
			GL11.glVertex3f(x,y+side,z);
			GL11.glVertex3f(x,y,z);
			GL11.glVertex3f(x,y,z+side);
			GL11.glVertex3f(x+side,y,z+side);
			GL11.glVertex3f(x+side,y+side,z+side);
			GL11.glVertex3f(x,y+side,z+side);
			GL11.glVertex3f(x,y,z+side);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(x+side,y,z+side);
			GL11.glVertex3f(x+side,y,z);
			GL11.glVertex3f(x+side,y+side,z+side);
			GL11.glVertex3f(x+side,y+side,z);
			GL11.glVertex3f(x,y+side,z+side);
			GL11.glVertex3f(x,y+side,z);
		GL11.glEnd();
	}
}
