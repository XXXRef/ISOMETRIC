package Isometric;

import java.util.ArrayList;
import java.util.UUID;

class Matrix<T>{
	private ArrayList<ArrayList<ArrayList<T>>> p;
	private int x,y,z;
	private UUID id=UUID.randomUUID();
	
	public Matrix(){
		p=null;
		x=y=z=0;
	}
	
	public Matrix(int xx,int yy,int zz){
		if(!reset(xx,yy,zz)){
			p=null;
			x=y=z=0;
		}
	}
	
	public Matrix(Matrix<? extends T> m){
		this.reset(m.getX(), m.getY(), m.getZ());
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				for(int k=0;k<z;k++){
					this.set(m.get(i,j,k), i,j,k);
				}
			}
		}
	}
	
	public boolean reset(int xx,int yy,int zz){
		if(xx<0 || yy<0 || zz<0){
			return false;
		}
		x=xx;
		y=yy;
		z=zz;
		
		p=new ArrayList<ArrayList<ArrayList<T>>>(z);
		
		for(int counter=0;counter<z;counter++){
			p.add(new ArrayList<ArrayList<T>>(y));
		}
		
		for(ArrayList<ArrayList<T>> iter:p){
			for(int counter1=0;counter1<y;counter1++){
				iter.add(new ArrayList<T>(x));
			}	
			for(ArrayList<T> iter1: iter){
				for(int counter2=0;counter2<x;counter2++){
					iter1.add(null);
				}
			}
		}	
		return true;
	}
	
	public T get(int xx,int yy,int zz){
		if(xx<0 || xx>=x || yy<0 || yy>=y || zz<0 || zz>=z){
			return null;
		}
		return p.get(zz).get(yy).get(xx);
	}
	
	public boolean set(T val, int xx, int yy, int zz){
		if(xx<0 || xx>=x || yy<0 || yy>=y || zz<0 || zz>=z){
			return false;
		}
		ArrayList<T> temp=p.get(zz).get(yy);
		temp.set(xx,val);
		return true;
	}
	
	public void fill(T val){
		for(ArrayList<ArrayList<T>> iter1:p){
			for(ArrayList<T> iter2:iter1){
				for(int counter=0;counter<iter2.size();counter++){
					iter2.set(counter, val);
				}
			}
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	public void print(){
		for(ArrayList<ArrayList<T>> iter1:p){
			for(ArrayList<T> iter2:iter1){
				for(T iter3:iter2){
					System.out.print(iter3+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public String toString(){
		return new String("Matrix@id="+id);
	}
}
