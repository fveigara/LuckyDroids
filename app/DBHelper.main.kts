#!/usr/bin/env kotlin

class CasinoDB(context: Context) :
    SQLiteOpenHelper(context, "casino.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE player (
                id INTEGER PRIMARY KEY,
                coins INTEGER
            )
        """)

        db.execSQL("""
            CREATE TABLE history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                result TEXT,
                bet INTEGER,
                coins INTEGER,
                timestamp INTEGER
            )
        """)

        db.execSQL("INSERT INTO player VALUES (1, 2000)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}