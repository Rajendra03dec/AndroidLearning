package com.example.androidlearnings.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlearnings.R
import com.example.androidlearnings.activity.AddWordActivity
import com.example.androidlearnings.activity.DictionaryActivity
import com.example.androidlearnings.databinding.RecyclerViewWordItemBinding
import com.example.androidlearnings.room.Word
import com.example.androidlearnings.utils.WordConstant

class WordAdapter(diffCallback: DiffUtil.ItemCallback<Word>) : ListAdapter<Word, WordAdapter.CustomViewHolder>(diffCallback) {

    class CustomViewHolder(itemView: RecyclerViewWordItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RecyclerViewWordItemBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var binding = DataBindingUtil.inflate<RecyclerViewWordItemBinding>(LayoutInflater.from(parent.context), R.layout.recycler_view_word_item, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var wordModel = getItem(position)
        holder.binding.wordModel = wordModel


        holder.binding.root.setOnClickListener {
            var bundle = Bundle().apply {
                putParcelable(WordConstant.EDIT_WORD_OBJECT, wordModel)
            }
            var intent = Intent(it.context, AddWordActivity::class.java)
            intent.putExtras(bundle)
            (it.context as DictionaryActivity).startActivityForResult(intent, WordConstant.EDIT_WORD_REQUEST_CODE)
        }
    }

    fun getWordAt(position: Int) = getItem(position)
}