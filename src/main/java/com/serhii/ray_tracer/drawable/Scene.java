package com.serhii.ray_tracer.drawable;

import java.util.List;

import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Scene {
	
	private List<Hitable> objects;
	private Sky skyColor;
	
	public Scene(List<Hitable> objects) {
		super();
		this.objects = objects;
		this.skyColor = getDefaultSkyColor();
	}

	public Scene(List<Hitable> objects, Sky skyColor) {
		super();
		this.objects = objects;
		this.skyColor = skyColor;
	}

	public List<Hitable> getObjects() {
		return objects;
	}

	public Sky getSky() {
		return skyColor;
	}
	private Sky getDefaultSkyColor(){
		return (ray) -> {
			Vec3 direction = ray.getDirection().getUnitVector();
			double k = direction.get(1);
			Vec3 yellow = new Vec3(1, 1, 1);
			Vec3 blue = new Vec3(0.15, 0.15, 0.3);
			Vec3 color = blue.mult(1-Math.abs(k)).add(yellow.mult(Math.max(-k, 0)));
			return color;
		};
	}

	public static interface Sky {
		Vec3 getColor(Ray ray);
	}
}
