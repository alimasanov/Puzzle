package com.iwgroup.puzzle

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import com.iwgroup.puzzle.utils.getBitmap
import com.iwgroup.puzzle.utils.slice
import com.iwgroup.puzzle.utils.toPx

class FirstFragment : Fragment() {

    companion object {
        const val SPAN_COUNT = 3
    }

    private lateinit var adapter: GridAdapter
    private lateinit var hDividerItemDecoration: DividerItemDecoration
    private lateinit var vDividerItemDecoration: DividerItemDecoration

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

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
        val bitmapList = context?.getBitmap(R.drawable.ic_launcher_background, 100.toPx() * SPAN_COUNT, 80.toPx() * SPAN_COUNT)
            ?.slice(SPAN_COUNT)
            ?.map { it to false }
            ?.toMutableList()

        bitmapList?.let {  } initRecyclerView()

        binding.btnAddFragment.setOnClickListener {
            adapter.listItem?.let { adapterListItem ->
                with(adapterListItem.indexOfFirst { !it.second }) {
                    if (this != -1) adapter.activateItem(this)
                    else adapter.reset()
                }
            }
        }
    }

    private fun initRecyclerView(bitmapList: MutableList<Pair<Bitmap, Boolean>>) {
        initDecorator()
        initAdapter(bitmapList)
        binding.rvItems.layoutManager = GridLayoutManager(context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
    }

    private fun initAdapter(bitmapList: MutableList<Pair<Bitmap, Boolean>>) {
        adapter = GridAdapter(requireContext(), bitmapList, SPAN_COUNT)
        binding.rvItems.adapter = adapter
    }

    private fun initDecorator() {
        hDividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        vDividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)

        hDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_item_decoration))
        vDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_item_decoration))

        context?.let { binding.rvItems.addItemDecoration(hDividerItemDecoration) }
        context?.let { binding.rvItems.addItemDecoration(vDividerItemDecoration) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}