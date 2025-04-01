package com.ssamsara98.dicodingevents.util

sealed class Resource<out R, out E> {
    data object Loading : Resource<Nothing, Nothing>()
    data class Success<out S>(val data: S) : Resource<S, Nothing>()
    data class Error<out E>(val error: E) : Resource<Nothing, E>()
}