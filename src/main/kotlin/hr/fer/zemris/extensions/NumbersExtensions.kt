package hr.fer.zemris.extensions

fun Number.d() = this.toDouble()

fun Number.f() = this.toFloat()

fun Number.i() = this.toInt()

fun Byte.toUnsignedInt() = this.i() and 0xFF