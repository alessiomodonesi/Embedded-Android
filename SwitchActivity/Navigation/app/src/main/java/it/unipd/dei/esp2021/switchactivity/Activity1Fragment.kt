package it.unipd.dei.esp2021.switchactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Activity1Fragment : Fragment()
{
    // View initialization logic
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        return inflater.inflate(R.layout.fragment_1,
            container,
            false)
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val next : Button = view.findViewById(R.id.Button1)
        next.setOnClickListener {
            // Note: no result is coming back
            findNavController().navigate(R.id.activity2)
        }
    }
}
