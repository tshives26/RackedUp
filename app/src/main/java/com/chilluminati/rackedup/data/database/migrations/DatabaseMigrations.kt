package com.chilluminati.rackedup.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database migrations to handle schema changes without data loss
 * 
 * IMPORTANT: Never use fallbackToDestructiveMigration() in production apps
 * as it will delete all user data when schema changes occur.
 */

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Migration from version 1 to 2
        // Add new columns to exercises table
        database.execSQL("ALTER TABLE exercises ADD COLUMN source_id TEXT")
        database.execSQL("ALTER TABLE exercises ADD COLUMN instruction_steps TEXT NOT NULL DEFAULT '[]'")
        database.execSQL("ALTER TABLE exercises ADD COLUMN force TEXT")
        database.execSQL("ALTER TABLE exercises ADD COLUMN mechanic TEXT")
        database.execSQL("ALTER TABLE exercises ADD COLUMN image_paths TEXT NOT NULL DEFAULT '[]'")
        
        // Update existing data to have proper defaults
        database.execSQL("UPDATE exercises SET instruction_steps = '[]' WHERE instruction_steps IS NULL")
        database.execSQL("UPDATE exercises SET image_paths = '[]' WHERE image_paths IS NULL")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Migration from version 2 to 3
        // This appears to be a minor schema change, likely just identity hash update
        // No structural changes needed
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Migration from version 3 to 4
        // This appears to be a minor schema change, likely just identity hash update
        // No structural changes needed
    }
}

// Add future migrations here as needed
// Example:
// val MIGRATION_4_5 = object : Migration(4, 5) {
//     override fun migrate(database: SupportSQLiteDatabase) {
//         // Add new columns, tables, or modify existing schema
//         database.execSQL("ALTER TABLE exercises ADD COLUMN new_field TEXT")
//     }
// }
