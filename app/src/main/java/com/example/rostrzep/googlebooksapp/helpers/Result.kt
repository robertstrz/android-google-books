package com.example.rostrzep.googlebooksapp.helpers

import io.reactivex.Observable

open class Result<T>(val data: T? = null, val error: Throwable? = null) {
    companion object {
        fun <T> fromData(data: T): Result<T> {
            return Result(data, null)
        }

        fun <T> fromError(error: Throwable): Result<T> {
            return Result(null, error)
        }
    }

    fun isError(): Boolean {
        return error != null
    }

    fun isSuccess(): Boolean {
        return data != null
    }
}

fun <T> Observable<T>.toResult(): Observable<Result<T>> {
    return map { Result.fromData(it) }
            .onErrorResumeNext { throwable: Throwable ->
                Observable.just(Result.fromError(throwable))
            }
}

fun <T> Observable<Result<T>>.onlySuccess() : Observable<T> {
    return filter { it.isSuccess() }
            .map { it.data!! }
}

fun <T> Observable<Result<T>>.onlyError() : Observable<Throwable> {
    return filter { it.isError() }
            .map { it.error!! }
}