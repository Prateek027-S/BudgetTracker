package com.example.budgettracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.entities.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: Users)

    @Query("UPDATE Users SET password = :password WHERE id = :id")
    suspend fun update(password: String, id: Int)

    @Query("SELECT id FROM Users WHERE name = :username AND recovery_code = :code")
    suspend fun getUserIdByNameCode(username: String, code: String): Int

    @Query("SELECT id FROM Users ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): Int

    @Query("SELECT EXISTS (SELECT * FROM Users WHERE name = :name)")
    fun usernameExists(name: String): Flow<Boolean>

    @Query("SELECT EXISTS (SELECT * FROM Users WHERE name = :name AND password = :password)")
    fun userExists(name: String, password: String): Flow<Boolean>

    @Query("SELECT EXISTS (SELECT * FROM Users WHERE name = :name AND recovery_code = :code)")
    fun userExistsByUserNameRecoveryCode(name: String, code: String): Flow<Boolean>

    @Query("SELECT id FROM Users WHERE name = :username")
    fun getUserIdByName(username: String): Flow<Int>
}