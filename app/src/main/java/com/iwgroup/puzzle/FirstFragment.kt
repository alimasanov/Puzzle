package com.iwgroup.puzzle

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iwgroup.puzzle.databinding.FragmentFirstBinding
import com.iwgroup.puzzle.grid.GridAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        const val SPAN_COUNT = 3
    }

    private lateinit var adapter: GridAdapter
    private lateinit var hDividerItemDecoration: DividerItemDecoration
    private lateinit var vDividerItemDecoration: DividerItemDecoration

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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GridAdapter(requireContext(), imageList, SPAN_COUNT)
        hDividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        vDividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        hDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_item_decoration))
        vDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_item_decoration))

        binding.rvItems.layoutManager = GridLayoutManager(context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
        binding.rvItems.adapter = adapter
        context?.let { binding.rvItems.addItemDecoration(hDividerItemDecoration) }
        context?.let { binding.rvItems.addItemDecoration(vDividerItemDecoration) }

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