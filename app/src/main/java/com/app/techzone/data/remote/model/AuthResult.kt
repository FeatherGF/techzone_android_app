package com.app.techzone.data.remote.model


sealed class AuthResult<T>(val data: T? = null) {
    class Authorized<T>(authData: T? = null): AuthResult<T>(authData)
    class Unauthorized<T>(authData: T? = null): AuthResult<T>(authData)
    class CodeSent<T>: AuthResult<T>()
    class CodeIncorrect<T>: AuthResult<T>()
    class UnknownError<T>: AuthResult<T>()
}