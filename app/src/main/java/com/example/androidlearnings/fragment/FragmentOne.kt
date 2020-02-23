package com.example.androidlearnings.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidlearnings.R

class FragmentOne : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(FragmentOne::class.java.simpleName, "onAttach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(FragmentOne::class.java.simpleName, "onCreate called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(FragmentOne::class.java.simpleName, "onCreateView called")
        return inflater.inflate(R.layout.fragment_one, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(FragmentOne::class.java.simpleName, "onViewCreated called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(FragmentOne::class.java.simpleName, "onActivityCreated called")
    }


    override fun onStart() {
        super.onStart()
        Log.d(FragmentOne::class.java.simpleName, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(FragmentOne::class.java.simpleName, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(FragmentOne::class.java.simpleName, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(FragmentOne::class.java.simpleName, "onStop called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(FragmentOne::class.java.simpleName, "onDestroyView called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(FragmentOne::class.java.simpleName, "onDestroy called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(FragmentOne::class.java.simpleName, "onDetach called")
    }
}