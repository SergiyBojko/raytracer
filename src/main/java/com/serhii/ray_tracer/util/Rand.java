package com.serhii.ray_tracer.util;

import java.util.Random;

import com.serhii.ray_tracer.vector.Vec3;

public class Rand {
	public static Random rand = new Random();
	public static Vec3 randInSphere(){
		Vec3 randPoint = null;
		do{
			randPoint = new Vec3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()).mult(2).sub(new Vec3(1, 1, 1));
		} while (randPoint.getLength() >= 1);
		return randPoint;
	}
	
	public static Vec3 randInDisc(){
		double x = 2*rand.nextDouble() - 1;
		//bad distribution???
		double y = (2*rand.nextDouble()-1)*Math.sqrt(1-x*x);
		return new Vec3(x, y, 0);
	}
}
