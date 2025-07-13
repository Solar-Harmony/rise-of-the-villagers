package org.solarharmony.utils

import net.minecraft.util.math.Vec3d

// vector addition
operator fun Vec3d.plus(other: Vec3d): Vec3d = this.add(other)

// vector inversion shortcut (wtf is this maths library...)
operator fun Vec3d.unaryMinus(): Vec3d = Vec3d(-x, -y, -z)

// scalar product shortcut
operator fun Vec3d.times(other: Double): Vec3d = this.multiply(other)

fun Vec3d.withX(x: Double) = Vec3d(x, this.y, this.z)
fun Vec3d.withY(y: Double) = Vec3d(this.x, y, this.z)
fun Vec3d.withZ(z: Double) = Vec3d(this.x, this.y, z)

fun Vec3d.with(x: Double = this.x, y: Double = this.y, z: Double = this.z) = Vec3d(x, y, z)