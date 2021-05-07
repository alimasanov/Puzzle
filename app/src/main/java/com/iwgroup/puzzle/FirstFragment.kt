package com.iwgroup.puzzle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iwgroup.puzzle.databinding.FragmentFirstBinding
import com.iwgroup.puzzle.grid.GridAdapter
import com.iwgroup.puzzle.grid.GridDecorator

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        const val SPAN_COUNT = 3
    }

    private lateinit var adapter: GridAdapter

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val imageList = mutableListOf(
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false,
        R.drawable.ic_launcher_background to false
    )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GridAdapter(requireContext(), imageList, SPAN_COUNT)

        binding.rvItems.layoutManager = GridLayoutManager(context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
        binding.rvItems.adapter = adapter
        context?.let { binding.rvItems.addItemDecoration(GridDecorator(it, 3)) }

        binding.btnAddFragment.setOnClickListener {
            with(adapter.listItem.indexOfFirst { !it.second }) {
                if (this != -1) adapter.activateItem(this)
                else adapter.reset()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}