package com.memoizr

import com.memoizr.ShankCache.factories
import com.memoizr.ShankCache.globalScope

class SingletonProvider<T : Any>(f: () -> T) : Provider<T>(f) {
    operator fun invoke(): T = get(globalScope) { invokes() }
    fun overrideFactory(f: () -> T) {
        remove()
        factories[this] = f as Function<Any>
    }
}

class SingletonProvider1<A : Any, T : Any>(f: (A) -> T) : Provider<T>(f) {
    operator fun invoke(a: A): T = get(globalScope) { invokes(a) }
    fun overrideFactory(f: (A) -> T) {
        remove()
        factories[this] = f as Function<Any>
    }
}

class SingletonProvider2<A : Any, B : Any, T : Any>(f: (A, B) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B): T = get(globalScope) { invokes(a, b) }
    fun overrideFactory(f: (A, B) -> T) {
        remove()
        factories[this] = f as Function<Any>
    }
}

class SingletonProvider3<A : Any, B : Any, C : Any, T : Any>(f: (A, B, C) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B, c: C): T = get(globalScope) { invokes(a, b, c) }
    fun overrideFactory(f: (A, B, C) -> T) {
        remove()
        factories[this] = f as Function<Any>
    }
}

class SingletonProvider4<A : Any, B : Any, C : Any, D : Any, T : Any>(f: (A, B, C, D) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B, c: C, d: D): T = get(globalScope) { invokes(a, b, c, d) }
    fun overrideFactory(f: (A, B, C, D) -> T) {
        remove()
        factories[this] = f as Function<Any>
    }
}

class SingletonProvider5<A : Any, B : Any, C : Any, D : Any, E : Any, T : Any>(f: (A, B, C, D, E) -> T) :
    Provider<T>(f) {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = get(globalScope) { invokes(a, b, c, d, e) }
    fun overrideFactory(f: (A, B, C, D, E) -> T) {
        remove()
        factories[this] = f as Function<Any>
    }
}
