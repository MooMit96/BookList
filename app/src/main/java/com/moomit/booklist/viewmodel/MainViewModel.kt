package com.moomit.booklist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moomit.booklist.API.NaverBookSearchAPI
import com.moomit.booklist.model.Book

class MainViewModel : ViewModel() {

    private var _isComplete = MutableLiveData<Boolean>()
    private var _book = MutableLiveData<Book>()

    val isComplete: LiveData<Boolean>
     get() = _isComplete

    val book : LiveData<Book>
        get() = _book


    fun requestBookData( searchWord : String?,start : Int){
        _isComplete.postValue(false)

        NaverBookSearchAPI.main(searchWord, start, object : NaverBookSearchAPI.NaverBookSearchAPIListener{
            override fun onSuccess(book: Book?) {
                book?.let {

                    _book.postValue(it)
                }

            }

            override fun onFailed() {
                _isComplete.postValue(true)
            }
        })




    }

}