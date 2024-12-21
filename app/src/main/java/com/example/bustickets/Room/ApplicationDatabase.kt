package com.example.bustickets.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TokenEntity::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao

    companion object {
        // Singleton để đảm bảo chỉ có một instance của database tồn tại
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            // Nếu instance đã tồn tại, trả về instance đó
            return INSTANCE ?: synchronized(this) {
                // Nếu chưa có instance nào, tạo mới database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Phục hồi cơ sở dữ liệu nếu cấu trúc thay đổi
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}