package com.moomit.booklist.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moomit.booklist.adapter.BookListAdapter
import com.moomit.booklist.databinding.ActivityMainBinding
import com.moomit.booklist.db.BookDatabase
import com.moomit.booklist.db.RecentlySearchBook
import com.moomit.booklist.viewmodel.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object{
        const val DISPLAY = 10
    }

    var mBinding : ActivityMainBinding? = null
    private  lateinit var  viewModel : MainViewModel
    private lateinit var getResultText : ActivityResultLauncher<Intent>
    private val adapter: BookListAdapter by lazy {
        BookListAdapter(this)
    }
    private var bookdatabase: BookDatabase? = null
    private var start = 1
    private var  multiply = 1
    private var total : Int = 0
    private var searchWord : String? = null

    val binding get() = mBinding!!
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)

        setResultgetKeyWord()
        binding.rvSearchBookList.adapter = adapter

        with(viewModel){
            book.observe(this@MainActivity){book ->

                book.items.let {
                    adapter.addData(book.items)
                    multiply +=  1

                    book.total?.let {
                            total ->
                        this@MainActivity.total = total
                    }
                }
                binding.pb.visibility = View.GONE
            }
            isComplete.observe(this@MainActivity){
                binding.pb.visibility = if(it) View.GONE else View.VISIBLE
            }

        }

        with(binding){
            tvRecentlySearchWord.setOnClickListener {
                searchWord = etSearchBookWord.text.toString()
            }

            btSearch.setOnClickListener {
                adapter.clear()
                multiply = 1
                searchWord = etSearchBookWord.text.toString()
                //insert DB
                var currentTime = getTime()
                insertWord(searchWord, currentTime)
                //Request Data From NaverAPI
                //최초 검색일 경우, 1Page 표출 start == 페이지
                viewModel.requestBookData(searchWord,start)
            }

            tvRecentlySearchWord.setOnClickListener {
                val moveToRecentlySearchPage = Intent(this@MainActivity, RecentlySearchActivity::class.java)
                getResultText.launch(moveToRecentlySearchPage)
            }

            rvSearchBookList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                    if (lastVisibleItemPosition == itemTotalCount) {

                        if( pb.visibility == View.GONE &&(  (multiply * DISPLAY ) + start ) < total) {
                            searchWord =  etSearchBookWord.text.toString()
                            viewModel.requestBookData(searchWord, (multiply * DISPLAY ) + start)
                        }
                    }
                }
            })
        }
    }


    private fun insertWord(searchWord : String?, searchDate : String?){
        bookdatabase = BookDatabase.getInstance(this)
        var recentlySearchBook = RecentlySearchBook(searchWord, searchDate)

        val insertRunnable = Runnable {
            searchWord?.let {
                //최근 검색어에 동일한 검색어가 없다면
                if(bookdatabase?.bookDao()?.getWord(it)?.size == 0){
                    bookdatabase?.bookDao()?.insert(recentlySearchBook)
                }else{
                     bookdatabase?.bookDao()?.getLine(it)?.get(0).let {
                         if (it != null) {
                             recentlySearchBook = it
                         }
                         recentlySearchBook.searchDate = searchDate
                         bookdatabase?.bookDao()?.update(recentlySearchBook)
                    }
                }

            }

        }
        Thread(insertRunnable).start()
    }

    /*
     *  저장시점으로 정렬하여 가장 최근 검색한 KeyWord 최상단 Index로 배치시킬 수 있는 구분자
     */
     fun getTime(): String? {
         val now = System.currentTimeMillis()
         return now.toString()
    }

    /*
     *    최근 검색어 페이지에서 선택된 KeyWord로 검색 Action 만드는 Method
     */
    private fun setResultgetKeyWord(){
        getResultText = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val recentlySearchKeyWord :String = result.data?.getStringExtra("keyword") ?: ""

                recentlySearchKeyWord.let {
                    binding.etSearchBookWord.setText(recentlySearchKeyWord.toString())
                    binding.btSearch.performClick()
                }
            }
        }
    }

}