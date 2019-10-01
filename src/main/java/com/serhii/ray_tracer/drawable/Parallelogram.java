package com.serhii.ray_tracer.drawable;

import com.serhii.ray_tracer.material.Material;
import com.serhii.ray_tracer.vector.HitRecord;
import com.serhii.ray_tracer.vector.Ray;
import com.serhii.ray_tracer.vector.Vec3;

public class Parallelogram extends Plane {

    private double side1length;
    private double side2length;

    private Vec3 uSide1;
    private Vec3 uSide2;

    public Parallelogram(Vec3 origin, Vec3 side1, Vec3 side2, Material material) {
        super(origin, side1.cross(side2), material);
        this.uSide1 = side1.getUnitVector();
        this.uSide2 = side2.getUnitVector();
        this.side1length = side1.getLength();
        this.side2length = side2.getLength();
    }

    @Override
    public HitRecord tryHit(Ray ray) {
        HitRecord hr = super.tryHit(ray);
        if (hr == null){
            return null;
        }

        Vec3 intersection = hr.getPoint().sub(origin);

        double projection1length = uSide1.dot(intersection);
        if (projection1length >= side1length || projection1length < 0){
            return null;
        }

        double projection2length = uSide2.dot(intersection);
        if (projection2length >= side2length || projection2length < 0){
            return null;
        }
        return hr;
    }
}
