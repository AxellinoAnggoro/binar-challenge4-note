package com.axellinoanggoro.binar_challenge4

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.axellinoanggoro.binar_challenge4.databinding.FragmentHomeBinding
import com.axellinoanggoro.binar_challenge4.room.DataNote
import com.axellinoanggoro.binar_challenge4.room.NoteDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var noteDb: NoteDatabase? = null
    private lateinit var adapterNote: AdapterNote
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var pref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = requireContext().getSharedPreferences("data_reg", Context.MODE_PRIVATE)
        _binding?.tvUsername?.text = "Hi, " + pref.getString("nama", "name")

        noteDb = NoteDatabase.getInstance(requireContext())

        adapterNote = AdapterNote(ArrayList())
        binding.noteRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.noteRv.adapter = adapterNote

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        noteViewModel.getAllNoteObservers().observe(viewLifecycleOwner) {
            adapterNote.setData(it as ArrayList<DataNote>)
        }

        binding.btnAdd.setOnClickListener {
            Toast.makeText(requireContext(), "Add Note Success", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment2_to_addNoteFragment)
        }

        binding.btnAsc.setOnClickListener {
            Toast.makeText(requireContext(), "Showing in Ascending", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment2_to_ascFragment)
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            val data = noteDb?.noteDao()?.getDataNote()

            activity?.runOnUiThread {
                adapterNote = AdapterNote(data!!)
                binding.noteRv.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.noteRv.adapter = adapterNote
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}