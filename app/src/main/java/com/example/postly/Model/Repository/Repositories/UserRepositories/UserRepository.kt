package com.example.postly.Model.Repository.Repositories.UserRepositories

import com.example.postly.Model.DataSource.Local.DAO.UserDao
import com.example.postly.Model.DataSource.Local.Entity.UserEntity
import com.example.postly.Model.Repository.Contracts.User.IFUserRegisterRepository
import com.example.postly.Model.Repository.Contracts.User.IFUserSignInRepository
import com.example.postly.Model.Types.AppError
import com.example.postly.Model.Types.AuthState
import com.example.postly.Model.Types.LoginRequest
import com.example.postly.Model.Types.RegisterRequest
import com.example.postly.Model.Types.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) : IFUserSignInRepository, IFUserRegisterRepository {

    override suspend fun login(request: LoginRequest): AuthState {
        return try {
            val userEntity = userDao.getUserByEmail(request.email)

            when {
                userEntity == null -> AuthState.Error(AppError.UserNotFound)
                request.password != userEntity.password -> AuthState.Error(AppError.InvalidPassword)
                else -> {
                    val updatedUser = userEntity.copy(lastLogin = System.currentTimeMillis())
                    userDao.updateUser(updatedUser)
                    AuthState.Success(updatedUser.toUser())
                }
            }
        } catch (e: Exception) {
            AuthState.Error(
                AppError.CustomError (
                    code = "DB_LOGIN_001",
                    userMessage = "Login failed. Please try again.",
                    debugMessage = "Login exception: ${e.message}"
                )
            )
        }
    }

    override suspend fun register(request: RegisterRequest): AuthState {
        return try {
            if (userDao.getUserByEmail(request.email) != null) {
                return AuthState.Error(AppError.EmailAlreadyRegistered)
            }

            val userEntity = UserEntity(
                email = request.email,
                password = request.password
            )

            val userId = userDao.insertUser(userEntity)
            val createdUser = userDao.getUserById(userId) ?:
            return AuthState.Error(AppError.UserCreationFailed)

            AuthState.Success(createdUser.toUser())
        } catch (e: Exception) {
            AuthState.Error(
                AppError.CustomError(
                    code = "DB_REG_001",
                    userMessage = "Registration failed. Please try again.",
                    debugMessage = "Registration exception: ${e.message}"
                )
            )
        }
    }

    override suspend fun logout() {
        // Clear session data
    }

    override suspend fun isLoggedIn(): Boolean {
        return userDao.getUserCount() > 0
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getFirstUser()?.toUser()
    }
}

private fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        email = this.email,
        createdAt = this.createdAt,
        lastLogin = this.lastLogin
    )
}