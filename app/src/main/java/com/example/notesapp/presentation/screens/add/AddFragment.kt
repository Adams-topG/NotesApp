package com.example.notesapp.presentation.screens.add

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.notesapp.R
import com.example.notesapp.data.models.NoteModel
import com.example.notesapp.databinding.FragmentAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddFragment : Fragment(R.layout.fragment_add) {
    private val args: AddFragmentArgs by navArgs()
    private val binding: FragmentAddBinding by viewBinding(FragmentAddBinding::bind)
    private var title: String = ""
    private var description: String = ""
    private val viewModel: AddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArgs()
        binding.etTitle.addTextChangedListener {
            title = it.toString()
            changeLength()
        }
        binding.etDesc.addTextChangedListener {
            description = it.toString()
            changeLength()
        }
        buttonListeners()
        if (!args.isAdding) {
            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_add, menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_copy -> {
                            textCopyThenPost(textCopied = "$title \n $description")
                            return true
                        }
                        R.id.action_delete -> {

                            args.myArg?.let {
                                viewModel.delete(it) {
                                    findNavController().navigateUp()
                                }
                            }
                            return true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
            changeLength()
        }
    }

    private fun setArgs() {
        with(binding) {
            etTitle.setText(args.myArg?.title ?: "")
            title = args.myArg?.title ?: ""
            etDesc.setText(args.myArg?.description ?: "")
            description = args.myArg?.description ?: ""

        }
    }

    private fun buttonListeners() {
        binding.ibBackAndSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDesc.text.toString()
            if (args.isAdding) {
                if (title.isEmpty() && description.isNotEmpty()) {
                    saveModel(
                        NoteModel(
                            title = "",
                            description = description
                        )
                    )
                } else if (title.isEmpty() && description.isEmpty()) {
                    findNavController().navigate(AddFragmentDirections.actionAddFragmentToStartFragment())
                } else if (title.isNotEmpty() && description.isEmpty()) {
                    saveModel(
                        NoteModel(
                            title = title,
                            description = ""
                        )
                    )
                } else saveModel(NoteModel(title = title, description = description))
            } else {
                if (title.isEmpty() && description.isEmpty()) {
                    args.myArg?.let {
                        viewModel.delete(it) {
                            findNavController().navigateUp()
                        }
                    }

                } else if (title != args.myArg?.title || description != args.myArg?.description) {
                    args.myArg?.copy(title = title, description = description)?.let { it1 ->
                        viewModel.insert(it1) {
                            withContext(Dispatchers.Main) {
                                findNavController().navigate(AddFragmentDirections.actionAddFragmentToStartFragment())
                            }
                        }
                    }
                } else findNavController().navigate(AddFragmentDirections.actionAddFragmentToStartFragment())
            }

        }
    }

    private fun changeLength() {
        val titleLen = title.length
        val descLen = description.length
        val symbol = getString(R.string.symbol)
        val symbols = getString(R.string.symbols)
        val count = titleLen + descLen
        if (titleLen + descLen > 1) {
            binding.tvCountOfSymbols.text = ("$count $symbols").toString()
        } else
            binding.tvCountOfSymbols.text = ("$count $symbol").toString()
    }

    private fun saveModel(noteModel: NoteModel) {
        noteModel.copy(title = noteModel.title, description = noteModel.description).apply {
            viewModel.insert(this) {
                findNavController().navigate(AddFragmentDirections.actionAddFragmentToStartFragment())
            }

        }
    }

    fun textCopyThenPost(textCopied: String) {
        if (textCopied.isNotEmpty()) {
            val myClipboard =
                getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("copy text", textCopied)
            myClipboard.setPrimaryClip(clip)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                Toast.makeText(
                    context,
                    requireContext().getString(R.string.action_copied),
                    Toast.LENGTH_SHORT
                ).show()
        } else Toast.makeText(
            context,
            requireContext().getString(R.string.action_cant_copy),
            Toast.LENGTH_SHORT
        ).show()
    }
}

