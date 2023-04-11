package com.moomit.booklist.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moomit.booklist.R
import com.moomit.booklist.databinding.BookListItemBinding
import com.moomit.booklist.model.Book

class BookListAdapter(var context : Context): RecyclerView.Adapter<BookListAdapter.MyViewHolder>() {

    val datalist = arrayListOf<Book.Item>()

    inner class MyViewHolder(private val binding: BookListItemBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind( book:Book.Item){
                with(binding){
                    book.let {
                        tvBookTitle.text= context.getString(R.string.book_title)+ it.title
                        tvBookPrice.text= context.getString(R.string.book_price)+it.discount
                        tvBookCompany.text= context.getString(R.string.book_publisher)+it.publisher
                        tvBookWriten.text= context.getString(R.string.book_author)+it.author
                        Glide.with(context)
                            .load(it.image)
                            .into(ivBook)
                    }
                }

            //해당 검색 Item 링크 이동
            itemView.setOnClickListener {
                var moveToWebView = Intent(Intent.ACTION_VIEW, Uri.parse(book.link))
                context.startActivity(moveToWebView)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder (BookListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int = datalist.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datalist[position])
    }
    /*
     *    Data 추가 Method
     */
    fun addData(items : ArrayList<Book.Item>?){
        items?.let {
            datalist.addAll(items)
            notifyDataSetChanged()
        }
    }

    /*
     *    새로운 word 검색 시 Adater Clear Method
     */
    fun clear(){
        datalist.clear()
        notifyDataSetChanged()
    }
}