package com.serhii.ray_tracer.vector;

import java.util.Arrays;

public class Vec3 {
	double[] v = new double[3];
	
	public Vec3(double e0, double e1, double e2){
		v[0] = e0;
		v[1] = e1;
		v[2] = e2;
	}
	
	public Vec3(double e){
		v[0] = e;
		v[1] = e;
		v[2] = e;
	}
	
	public double get(int index){
		return v[index];
	}
	
	public int getDimensions(){
		return v.length;
	}
	
	public double getLength(){
		return Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2) + Math.pow(v[2], 2));
	}
	
	public Vec3 add(Vec3 v){
		return new Vec3(
				this.get(0) + v.get(0),
				this.get(1) + v.get(1),
				this.get(2) + v.get(2)
				);
	}
	
	public Vec3 sub(Vec3 v){
		return new Vec3(
				this.get(0) - v.get(0),
				this.get(1) - v.get(1),
				this.get(2) - v.get(2)
				);
	}
	
	public Vec3 mult(Vec3 v){
		return new Vec3(
				this.get(0) * v.get(0),
				this.get(1) * v.get(1),
				this.get(2) * v.get(2)
				);
	}
	
	public Vec3 div(Vec3 v){
		return new Vec3(
				this.get(0) / v.get(0),
				this.get(1) / v.get(1),
				this.get(2) / v.get(2)
				);
	}
	
	public Vec3 mult(double k){
		return new Vec3(
				this.get(0) * k,
				this.get(1) * k,
				this.get(2) * k
				);
	}
	
	public Vec3 div(double k){
		return new Vec3(
				this.get(0) / k,
				this.get(1) / k,
				this.get(2) / k
				);
	}
	
	public Vec3 getUnitVector(){
		return div(getLength());
	}
	
	
	public double dot(Vec3 v){
		return this.get(0) * v.get(0) + this.get(1) * v.get(1) + this.get(2) * v.get(2);
	}
	
	public Vec3 cross(Vec3 v){
		return new Vec3(
				this.get(1)*v.get(2) - this.get(2)*v.get(1),
				this.get(2)*v.get(0) - this.get(0)*v.get(2),
				this.get(0)*v.get(1) - this.get(1)*v.get(0)
				);
	}
	
	public String toString(){
		return Arrays.toString(v);
	}
}
