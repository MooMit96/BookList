package com.moomit.booklist.db


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recentlysearchbook")
class RecentlySearchBook(@ColumnInfo(name = "bookname") var bookName: String?,
                         @ColumnInfo(name = "searchdate") var searchDate: String?

){
    @PrimaryKey(autoGenerate = true ) var number = 0
}