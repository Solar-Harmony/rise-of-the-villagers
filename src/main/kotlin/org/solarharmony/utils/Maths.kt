package org.solarharmony.utils

import net.minecraft.util.math.Vec3d

// vector addition
operator fun Vec3d.plus(other: Vec3d): Vec3d = this.add(other)

// vector subtraction
operator fun Vec3d.minus(other: Vec3d): Vec3d = this.subtract(other)

// vector inversion shortcut
operator fun Vec3d.unaryMinus(): Vec3d = Vec3d(-x, -y, -z)

// scalar product shortcut
operator fun Vec3d.times(other: Double): Vec3d = this.multiply(other)

// cross product shortcut
operator fun Vec3d.times(other: Vec3d): Vec3d = this.crossProduct(other)

// copy with modified components
fun Vec3d.with(x: Double = this.x, y: Double = this.y, z: Double = this.z) = Vec3d(x, y, z)