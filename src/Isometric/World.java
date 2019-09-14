package Isometric;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class World {
	Matrix<Matrix<Integer>> world;
	int worldX, worldY,worldZ;//coords of 1st chunk of world matrix
	
	World(){
		this(0,0,0);
	}
	
	World(int x, int y, int z){
		//(x,y,z)-init chunk that will be in middle of the world matrix
		
		System.out.println("constructor World");
		
		worldX=x-Config.WORLDMATRIXSIZECHUNKSX/2;
		worldY=y-Config.WORLDMATRIXSIZECHUNKSY/2;
		worldZ=z-Config.WORLDMATRIXSIZECHUNKSZ/2;
		
		world=new Matrix<>(Config.WORLDMATRIXSIZECHUNKSX,Config.WORLDMATRIXSIZECHUNKSY,Config.WORLDMATRIXSIZECHUNKSZ);
		
		System.out.println("\tworldX="+worldX+" worldY="+worldY+" worldZ="+worldZ);
		
		for(int i=0;i<Config.WORLDMATRIXSIZECHUNKSZ;i++){
			for(int j=0;j<Config.WORLDMATRIXSIZECHUNKSY;j++){
				for(int k=0;k<Config.WORLDMATRIXSIZECHUNKSX;k++){
					//reserving memory for chunk
					world.set(new Matrix<Integer>(Config.CHUNKSIZE,Config.CHUNKSIZE,Config.CHUNKSIZE),k, j, i);
					//loading it
					if(!load(worldX+k, worldY+j,worldZ+i)){
						System.out.println("Error loading!");
					}
				}
			}
		}
	}
	
	public void refresh(int x1,int y1,int z1){
		//(x,y,z) - offset vector
		
		worldX+=x1;
		worldY+=y1;
		worldZ+=z1;
		
		//counters
		int iCounter;
		int jCounter;
		int kCounter;
		
		//intial value
		int iInit;
		int jInit;
		int kInit;
		
		//stop condition
		int iCondition;
		int jCondition;
		int kCondition;
		
		if(x1>0){
			iCounter=1;
			iInit=0;
			iCondition=Config.WORLDMATRIXSIZECHUNKSX;
		}else{
			iCounter=-1;
			iInit=Config.WORLDMATRIXSIZECHUNKSX-1;
			iCondition=-1;
		}
		
		if(y1>0){
			jCounter=1;
			jInit=0;
			jCondition=Config.WORLDMATRIXSIZECHUNKSY;
		}else{
			jCounter=-1;
			jInit=Config.WORLDMATRIXSIZECHUNKSY-1;
			jCondition=-1;
		}
		
		if(z1>0){
			kCounter=1;
			kInit=0;
			kCondition=Config.WORLDMATRIXSIZECHUNKSZ;
		}else{
			kCounter=-1;
			kInit=Config.WORLDMATRIXSIZECHUNKSZ-1;
			kCondition=-1;
		}
		/*
		for(int i=iInit;(i+z1)!=iCondition;i+=iCounter){
			for(int j=jInit;(j+y1)!=jCondition;j+=jCounter){
				for(int k=kInit;(k+x1)!=kCondition;k+=kCounter){
					world.set(world.get(k+x1, j+y1, i+z1), k, j, i);
				}
			}
		}
		*/
		
		for(int k=kInit;k!=kCondition;k+=kCounter){
			for(int j=jInit;j!=jCondition;j+=jCounter){
				for(int i=iInit;i!=iCondition;i+=iCounter){
					//if chunk doesn't need to load
					if(((i+x1)>-1)&&((i+x1)<Config.WORLDMATRIXSIZECHUNKSX) &&
					   ((j+y1)>-1)&&((j+y1)<Config.WORLDMATRIXSIZECHUNKSY) &&
					   ((k+z1)>-1)&&((k+z1)<Config.WORLDMATRIXSIZECHUNKSZ)){
						Matrix<Integer> tempMatrix=world.get(i+x1, j+y1, k+z1);
						world.set(tempMatrix, i, j, k);
					}else{
						//load chunk from file
						load(worldX+i,worldY+j,worldZ+k);
						
					}
				}
			}
		}
	}
	
	public boolean load(int x, int y, int z){
		//(x,y,z) - absolute chunk coord
		
		String path="world\\"+x+"."+y+"."+z+".map";
		
		File file=new File(path);
		
		if(file.exists()){
			if(!file.canRead()){
				System.out.println("Can't read file!");
				return false;
			}
			
			
			FileInputStream fis=null;
			DataInputStream dis=null;
			
			try{
				fis=new FileInputStream(file);
				dis=new DataInputStream(fis);
				
				dis.readInt();//read matrix sizes 
				dis.readInt();
				dis.readInt();
				
				//TODO look at z
				Matrix<Integer> tempMatrix=new Matrix<>(Config.CHUNKSIZE,Config.CHUNKSIZE,Config.CHUNKSIZE);//get chunk pointer for loading in it
				if(tempMatrix==null){
					System.out.println("\ttempMatrix==NULL");
				}
				for(int i=0;i<Config.CHUNKSIZE;i++){
					for(int j=0;j<Config.CHUNKSIZE;j++){
						for(int k=0;k<Config.CHUNKSIZE;k++){
							tempMatrix.set(dis.readInt(), k, j, i);
						}
					}
				}
				
				world.set(tempMatrix,x-worldX, y-worldY, z-worldZ);
				
				
			}catch(IOException e){
				System.out.println("\tIOException: "+e.toString());
				return false;
			}
			finally{
				try{
					if(dis!=null){
						dis.close();
					}
					if(fis!=null){
						fis.close();
					}
				}
				catch(IOException e){
					System.out.println("\tLoad IOException!");
				}
				
			}
			//System.out.println("End load");
			return true;
		}else{
			//gen new chunk
			genChunk(x,y,z);
			//System.out.println("End load1");
			return load(x,y,z);
		}
	}
	
	public boolean genChunk(int x,int y,int z){//generates chunk with parameters given by Config class
		Matrix<Integer> tempMatrix=new Matrix<>(Config.CHUNKSIZE,Config.CHUNKSIZE,Config.CHUNKSIZE);
		Random RandomGenerator=new Random();
		for(int i=0;i<Config.CHUNKSIZE;i++){
			for(int j=0;j<Config.CHUNKSIZE;j++){
				for(int k=0;k<Config.CHUNKSIZE;k++){
					tempMatrix.set((x%2+y%2+z%2)%2, k, j, i);
				}
			}
		}
		File outputFile=new File("world\\"+x+"."+y+"."+z+".map");
		
		FileOutputStream fos=null;
		DataOutputStream dos=null;
		
		try{
			outputFile.createNewFile();
			
			fos=new FileOutputStream(outputFile);
			dos=new DataOutputStream(fos);
			
			dos.writeInt(Config.CHUNKSIZE);
			dos.writeInt(Config.CHUNKSIZE);
			dos.writeInt(Config.CHUNKSIZE);
			for(int i=0;i<Config.CHUNKSIZE;i++){
				for(int j=0;j<Config.CHUNKSIZE;j++){
					for(int k=0;k<Config.CHUNKSIZE;k++){
						dos.writeInt(tempMatrix.get(k, j, i));
					}
				}
			}
		}catch(IOException e){
			System.out.println("\tIOException: "+e.toString());
			return false;
		}
		finally{
			try{
				if(dos!=null){
					dos.close();
				}
				if(fos!=null){
					fos.close();
				}
			}
			catch(IOException e){
			}
		}
		return true;
	}
	
	public int getBlock(int x, int y,int z){
		Integer result;	
		int deltaX=x-worldX*Config.CHUNKSIZE;
		int deltaY=y-worldY*Config.CHUNKSIZE;
		int deltaZ=z-worldZ*Config.CHUNKSIZE;
		
		Matrix<Integer> tempMatrix=world.get(deltaX/Config.CHUNKSIZE,deltaY/Config.CHUNKSIZE,deltaZ/Config.CHUNKSIZE);
		if(tempMatrix==null){
			return Config.BLOCK_UNDEF;
		}
		
		result=tempMatrix.get(deltaX%Config.CHUNKSIZE,deltaY%Config.CHUNKSIZE,deltaZ%Config.CHUNKSIZE);
		if(result==null){
			result=0;
		}
		return result;
	}
	
	public boolean save(String path){
		return true;
	}
	
	public void print(){
		for(int i=0;i<Config.WORLDMATRIXSIZECHUNKSZ;i++){
			for(int j=0;j<Config.WORLDMATRIXSIZECHUNKSY;j++){
				for(int k=0;k<Config.WORLDMATRIXSIZECHUNKSX;k++){
					System.out.println("Worldprint ("+k+","+j+","+i+"):");
					world.get(k, j, i).print();
				}
			}
		}
	}
}
