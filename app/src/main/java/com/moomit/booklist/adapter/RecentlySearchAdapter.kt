package com.moomit.booklist.adapter

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.moomit.booklist.databinding.RecentlySearchItemBinding
import com.moomit.booklist.model.SearchList
import com.moomit.booklist.view.MainActivity

class RecentlySearchAdapter(var context : Activity): RecyclerView.Adapter<RecentlySearchAdapter.MyViewHolder>() {
    var datalist = mutableListOf<SearchList>()
    private val mListener: OnItemClickListener? = null

    inner class MyViewHolder(private val binding: RecentlySearchItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int,searchlist: SearchList){

            binding.tvWord.text= searchlist.word

            // 최근 KeyWord 클릭시 해당 KeyWord로 검색 페이지로 이동하여 검색 Action
            itemView.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                    var keyWord = datalist.get(position).word
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("keyword", keyWord)

                    context.setResult(RESULT_OK, intent)
                    context.finish()

                    if (mListener != null) {
                        mListener.onItemClick(v, pos)
                    }
                }
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= RecentlySearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int =datalist.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position , datalist[position])
    }


     fun addData(word : String){
        var SearchList : SearchList? = null
        SearchList = SearchList(word)
        datalist.add(SearchList)

         notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}