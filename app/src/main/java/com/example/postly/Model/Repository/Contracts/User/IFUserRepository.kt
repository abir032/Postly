package com.example.postly.Model.Repository.Contracts.User

import com.example.postly.Model.Types.AuthState
import com.example.postly.Model.Types.LoginRequest
import com.example.postly.Model.Types.RegisterRequest
import com.example.postly.Model.Types.User

//Registry
interface IFUserRegisterRepository {
    suspend fun register(request: RegisterRequest): AuthState
}

//sign in
interface IFUserSignInRepository {
    suspend fun login(request: LoginRequest): AuthState
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
}






