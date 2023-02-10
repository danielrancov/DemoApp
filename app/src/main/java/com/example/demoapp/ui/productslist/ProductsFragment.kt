package com.example.demoapp.ui.productslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.databinding.FragmentProductsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: FragmentProductsBinding

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNextProducts()

        val recyclerView = binding.productsRV
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = ProductsAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.productsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ProductsViewModel.ProductsResult.Loading -> {}
                is ProductsViewModel.ProductsResult.Success -> {
                    isLoading = false
                    adapter.submitList(result.products.toMutableList())
                }
                is ProductsViewModel.ProductsResult.Error -> {
                    isLoading = false
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.addOnScrollListener(setupListener(layoutManager))

        adapter.setListener {
            Toast.makeText(requireContext(), "You clicked on: ${it.title}", Toast.LENGTH_SHORT).show()
            //TODO Here we can add navigation to the details page
        }
    }

    private fun setupListener(layoutManager: LinearLayoutManager) =
        object : CustomPaginationListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                viewModel.getNextProducts()
            }

            override fun getTotalPageCount(): Int {
                return viewModel.getTotalPageCount()
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        }
}