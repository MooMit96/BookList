package com.moomit.booklist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import retrofit2.http.DELETE


@Dao
interface bookDao {
    //최근 10개의 검색 기록 가져오는 Query
    @Query("SELECT * FROM recentlysearchbook ORDER BY  searchDate  desc limit 10")
    fun getTopTenData(): List<RecentlySearchBook>

    //검색어 중복 체크 Query
    @Query("SELECT bookname FROM recentlysearchbook WHERE bookname  = :word")
    fun getWord(word : String): List<String>

    //업데이트 객체 가져오는 Query
    @Query("SELECT * FROM recentlysearchbook WHERE bookname  = :word")
    fun getLine(word : String): List<RecentlySearchBook>

    //Insert Query
    @Insert(onConflict = REPLACE)
    fun insert(book: RecentlySearchBook)

    @Update
    fun update(book: RecentlySearchBook)

    @Query("DELETE from recentlysearchbook")
    fun deleteAll()

}