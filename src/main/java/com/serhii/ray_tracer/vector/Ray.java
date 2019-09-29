package com.serhii.ray_tracer.vector;

public class Ray {
	private Vec3 origin;
	private Vec3 direction;
	private HitRecord hitRecord;
	
	public Ray (Vec3 origin, Vec3 direction){
		this.origin = origin;
		this.direction = direction;
	}
	
	public Vec3 getOrigin(){
		return origin;
	}
	
	public Vec3 getDirection(){
		return direction;
	}
	
	public Vec3 getPointAt(double t){
		return origin.add(direction.mult(t));
	}
	
	public void setHitRecord(HitRecord hitRecord){
		if (hitRecord == null){
			return;
		}
		if (this.hitRecord == null){
			this.hitRecord = hitRecord;
		} else if (this.hitRecord.getDistance() > hitRecord.getDistance()){
			this.hitRecord = hitRecord;
		}
		
	}
	
	public HitRecord getHitRecord(){
		return hitRecord;
	}
	
	public Ray getScatteredRay(){
		return hitRecord.getScatteredRay(this);
	}
	

}
