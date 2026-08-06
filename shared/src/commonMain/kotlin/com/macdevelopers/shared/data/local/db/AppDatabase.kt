package com.macdevelopers.shared.data.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.macdevelopers.shared.data.local.dao.VendorDao
import com.macdevelopers.shared.data.local.entity.VendorEntity

@Database(entities = [VendorEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vendorDao(): VendorDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(connection: SQLiteConnection) {
                // Create the new table with String ID and new fields
                connection.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS vendors_new (
                        id TEXT NOT NULL, 
                        businessName TEXT NOT NULL, 
                        description TEXT, 
                        category TEXT, 
                        location TEXT, 
                        gstNumber TEXT, 
                        verified INTEGER NOT NULL, 
                        ownerEmail TEXT, 
                        createdAt TEXT, 
                        averageRating REAL, 
                        totalReviews INTEGER, 
                        PRIMARY KEY(id)
                    )
                    """.trimIndent()
                )

                // Copy data from the old table, casting id to TEXT
                connection.execSQL(
                    """
                    INSERT INTO vendors_new (
                        id, businessName, description, category, location, 
                        gstNumber, verified, ownerEmail, createdAt
                    ) 
                    SELECT 
                        CAST(id AS TEXT), businessName, description, category, location, 
                        gstNumber, verified, ownerEmail, createdAt 
                    FROM vendors
                    """.trimIndent()
                )

                // Remove the old table and rename the new one
                connection.execSQL("DROP TABLE vendors")
                connection.execSQL("ALTER TABLE vendors_new RENAME TO vendors")
            }
        }
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
