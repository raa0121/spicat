package com.github.raa0121.spicat.system

interface KeyVals {
    operator fun get(key : String) : String?
}

class Variables(private val keyVals: KeyVals) {
    operator fun <T> get(v : Var<T>) : T = v.parse(keyVals[v.key])
}

sealed class Var<out T>(val key : String) {
    abstract fun parse(v : String?) : T
}

class StrVar(key : String) : Var<String>(key) {
    override fun parse(v: String?) = v ?: throw KeyNotFound(key)
}

class NullableStrVar(key : String) : Var<String?>(key) {
    override fun parse(v: String?): String? = v
}

class KeyNotFound(key : String) : Exception("System variable with key [$key] is not set")