package com.example.notesapp.presentation.screens.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.notesapp.R
import com.example.notesapp.data.db.NoteDatabase
import com.example.notesapp.data.db.repository.NoteRealization
import com.example.notesapp.databinding.FragmentStartBinding
import com.example.notesapp.presentation.adapter.NoteAdapter

class StartFragment : Fragment(R.layout.fragment_start) {
    private val binding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)
    private val viewModel: StartViewModel by viewModels()
    private var adapter : NoteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val noteDao =
            NoteDatabase.getInstance(requireContext().applicationContext).getNoteDao()
        noteDao.let { viewModel.repository = NoteRealization(it) }
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapter = NoteAdapter {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToAddFragment(it, false))
        }
        binding.rvNotes.adapter = adapter
        viewModel.getAllNotes()?.observe(viewLifecycleOwner) { listNotes ->
            adapter?.setList(listNotes.asReversed())
        }
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToAddFragment(null, true))
        }

    }
}