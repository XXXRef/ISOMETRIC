package Isometric;

public class Game {
	
	Actor player;
	World world;
	
	Game(){
		System.out.println("Game constructor.");
		world=new World();
		player= new Actor(Config.CHUNKSIZE*Config.BLOCKSIZE/2,Config.CHUNKSIZE*Config.BLOCKSIZE/2,Config.CHUNKSIZE*Config.BLOCKSIZE/2,0,10,5);
		System.out.println("\tplayer.x="+player.x+" player.y="+player.y+" player.z="+player.z);
	}
	
	
	void movePlayer(float x,float y, float z){
		//(x,y,z) offset
		player.x+=x;
		player.y+=y;
		player.z+=z;
		
		//check if visible range out of world matrix
		if((player.x+Config.RANGECOVERAGEBLOCKSX*Config.BLOCKSIZE)>((world.worldX+Config.WORLDMATRIXSIZECHUNKSX)*Config.BLOCKSIZE*Config.CHUNKSIZE) ||//x
		   (player.x-Config.RANGECOVERAGEBLOCKSX*Config.BLOCKSIZE)<((world.worldX)*Config.BLOCKSIZE*Config.CHUNKSIZE) ||
		   
		   (player.y+Config.RANGECOVERAGEBLOCKSY*Config.BLOCKSIZE)>((world.worldY+Config.WORLDMATRIXSIZECHUNKSY)*Config.BLOCKSIZE*Config.CHUNKSIZE) ||//y
		   (player.y-Config.RANGECOVERAGEBLOCKSY*Config.BLOCKSIZE)<((world.worldY)*Config.BLOCKSIZE*Config.CHUNKSIZE) ||
		   
		   (player.z+Config.RANGECOVERAGEBLOCKSZ*Config.BLOCKSIZE)>((world.worldZ+Config.WORLDMATRIXSIZECHUNKSZ)*Config.BLOCKSIZE*Config.CHUNKSIZE) ||//z
		   (player.z-Config.RANGECOVERAGEBLOCKSZ*Config.BLOCKSIZE)<((world.worldZ)*Config.BLOCKSIZE*Config.CHUNKSIZE) 
		){
		//TODO refreshing world matrix
			
			//player's chunk's coords - middle chunk's coords
			int playerChunkX=(int)player.x/(Config.BLOCKSIZE*Config.CHUNKSIZE);	
			playerChunkX-=(player.x>-1)?0:1;
			int playerChunkY=(int)player.y/(Config.BLOCKSIZE*Config.CHUNKSIZE);	
			playerChunkY-=(player.y>-1)?0:1;
			int playerChunkZ=(int)player.z/(Config.BLOCKSIZE*Config.CHUNKSIZE);	
			playerChunkZ-=(player.z>-1)?0:1;
			
			int middleChunkX=world.worldX+Config.WORLDMATRIXSIZECHUNKSX/2;
			int middleChunkY=world.worldY+Config.WORLDMATRIXSIZECHUNKSY/2;
			int middleChunkZ=world.worldZ+Config.WORLDMATRIXSIZECHUNKSZ/2;
			
			/*
			int x1=((int)player.x/(Config.BLOCKSIZE*Config.CHUNKSIZE))-(world.worldX+Config.WORLDMATRIXSIZECHUNKSX/2);
			int y1=((int)player.y/(Config.BLOCKSIZE*Config.CHUNKSIZE))-(world.worldY+Config.WORLDMATRIXSIZECHUNKSY/2);
			int z1=((int)player.z/(Config.BLOCKSIZE*Config.CHUNKSIZE))-(world.worldZ+Config.WORLDMATRIXSIZECHUNKSZ/2);
			*/
			
			world.refresh(playerChunkX-middleChunkX, playerChunkY-middleChunkY, playerChunkZ-middleChunkZ);
			
			/*
			 * теперь надо загрузить из файлов незагруженные чанки
			 * возможно удобнее это встроить в верхний цикл
			*/
			
		}
	}
}
