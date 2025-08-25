package com.example.postly.Model.Repository.Contracts.User

import com.example.postly.Model.DataModels.Result
import com.example.postly.Model.DataModels.LoginRequest
import com.example.postly.Model.DataModels.RegisterRequest
import com.example.postly.Model.DataModels.User

//Registry
interface IFUserRegisterRepository {
    suspend fun register(request: RegisterRequest): Result<User>
}

//sign in
interface IFUserSignInRepository {
    suspend fun login(request: LoginRequest): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
}






