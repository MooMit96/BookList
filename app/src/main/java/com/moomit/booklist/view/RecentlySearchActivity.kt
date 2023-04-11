package com.moomit.booklist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.moomit.booklist.adapter.RecentlySearchAdapter
import com.moomit.booklist.databinding.ActivityRecentlySearchBinding
import com.moomit.booklist.db.BookDatabase
import com.moomit.booklist.db.RecentlySearchBook

class RecentlySearchActivity : AppCompatActivity() {
    var mBinding : ActivityRecentlySearchBinding? = null
    private var bookdatabase: BookDatabase? = null
    private var recentlySearchBook = listOf<RecentlySearchBook>()

    val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRecentlySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadWord()

    }

    private fun loadWord(){
        val adapter = RecentlySearchAdapter(this)
        bookdatabase = BookDatabase.getInstance(this)


        val insertRunnable = Runnable {
            recentlySearchBook = bookdatabase?.bookDao()?.getTopTenData()!!
            for(i in 0..recentlySearchBook.size-1){
                adapter.addData(recentlySearchBook.get(i).bookName.toString())
            }

        }

        Thread(insertRunnable).start()

        binding.rvRecentlySearchList.adapter = adapter
        binding.rvRecentlySearchList.layoutManager = LinearLayoutManager(this)


    }

}