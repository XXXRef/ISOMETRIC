package Isometric;

//was possible to name 'Cylinder'
public class Actor {
	float x,y,z;
	float angle;
	float width,height;
	
	Actor(){
		x=0;
		y=0;
		z=0;
		
		angle=0;
		
		width=0;
		height=0;
	}
	
	Actor(float xPar,float yPar, float zPar, float anglePar, float widthPar, float heightPar){
		x=xPar;
		y=yPar;
		z=zPar;
		
		angle=anglePar;
		
		width=widthPar;
		height=heightPar;
	}
}
