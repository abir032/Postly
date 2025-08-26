package com.example.postly.Model.Repository.Repositories.UserRepositories

import com.example.postly.Model.DataSource.Local.DAO.UserDao
import com.example.postly.Model.DataSource.Local.Entity.UserEntity
import com.example.postly.Model.Repository.Contracts.User.IFUserRegisterRepository
import com.example.postly.Model.Repository.Contracts.User.IFUserSignInRepository
import com.example.postly.Utils.AppError
import com.example.postly.Model.DataModels.Result
import com.example.postly.Model.DataModels.LoginRequest
import com.example.postly.Model.DataModels.RegisterRequest
import com.example.postly.Model.DataModels.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) : IFUserSignInRepository, IFUserRegisterRepository {

    override suspend fun login(request: LoginRequest): Result<User> {
        return try {
            val userEntity = userDao.getUserByEmail(request.email)

            when {
                userEntity == null -> Result.Error(AppError.UserNotFound)
                request.password != userEntity.password -> Result.Error(AppError.InvalidPassword)
                else -> {
                    val updatedUser = userEntity.copy(lastLogin = System.currentTimeMillis())
                    userDao.updateUser(updatedUser)
                    Result.Success(updatedUser.toUser())
                }
            }
        } catch (e: Exception) {
            Result.Error(
                AppError.CustomError (
                    code = "DB_LOGIN_001",
                    userMessage = "Login failed. Please try again.",
                    debugMessage = "Login exception: ${e.message}"
                )
            )
        }
    }

    override suspend fun register(request: RegisterRequest): Result<User> {
        return try {
            if (userDao.getUserByEmail(request.email) != null) {
                return Result.Error(AppError.EmailAlreadyRegistered)
            }

            val userEntity = UserEntity(
                email = request.email,
                password = request.password
            )

            val userId = userDao.insertUser(userEntity)
            val createdUser = userDao.getUserById(userId) ?:
            return Result.Error(AppError.UserCreationFailed)

            Result.Success(createdUser.toUser())
        } catch (e: Exception) {
            Result.Error(
                AppError.CustomError(
                    code = "DB_REG_001",
                    userMessage = "Registration failed. Please try again.",
                    debugMessage = "Registration exception: ${e.message}"
                )
            )
        }
    }

    override suspend fun logout() {

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