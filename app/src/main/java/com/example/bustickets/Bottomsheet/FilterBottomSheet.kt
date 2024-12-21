package com.example.bustickets.Bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.bustickets.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class FilterBottomSheet(private val onFilterApplied: (String) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filters = listOf(
            view.findViewById<TextView>(R.id.textViewFilterMorning),
            view.findViewById<TextView>(R.id.textViewFilterAfternoon),
            view.findViewById<TextView>(R.id.textViewFilterEvening)
        )

        val types = listOf(
            view.findViewById<TextView>(R.id.textView20Seat),
            view.findViewById<TextView>(R.id.textView34Seat),
            view.findViewById<TextView>(R.id.textView42Seat)
        )

        val btnConfirm = view.findViewById<MaterialButton>(R.id.btnConfirm)
        val imgExit = view.findViewById<ImageView>(R.id.imgExit)

        setSelectable(filters)
        setSelectable(types)

        btnConfirm.setOnClickListener {
            val selectedTimeFilter = filters.find { it.isSelected }?.text.toString()
            val selectedTypeFilter = types.find { it.isSelected }?.text.toString()

            if (selectedTimeFilter.isNotEmpty() || selectedTypeFilter.isNotEmpty()) {
                val combinedFilter = "$selectedTimeFilter|$selectedTypeFilter"
                onFilterApplied(combinedFilter)
            } else {
                onFilterApplied("")
            }

            dismiss()
        }

        imgExit.setOnClickListener {
            dismiss()
        }
    }

    private fun setSelectable(items: List<TextView>) {
        items.forEach { textView ->
            textView.setOnClickListener {
                items.forEach { it.isSelected = false }
                textView.isSelected = true
            }
        }
    }
}

