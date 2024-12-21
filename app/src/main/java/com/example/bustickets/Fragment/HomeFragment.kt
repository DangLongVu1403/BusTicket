package com.example.bustickets.Fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bustickets.Activity.SelectAddressActivity
import com.example.bustickets.Activity.SelectTicketActivity
import com.example.bustickets.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: String? = null

    private var pickUpPoint: String? = null
    private var dropOffPoint: String? = null
    private var codeStartStation: String? = null
    private var codeEndStation: String? = null

    private val pickUpLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                pickUpPoint = result.data?.getStringExtra("station_name")
                codeStartStation = result.data?.getStringExtra("station_code")

                binding.textViewPickUpPoint.text = "\t $pickUpPoint"
            }
        }

    private val dropOffLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                dropOffPoint = result.data?.getStringExtra("station_name")
                codeEndStation = result.data?.getStringExtra("station_code")
                binding.textViewDropOffPoint.text = "\t $dropOffPoint"
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        binding.textViewDate.setText("\t $formattedDate")

        binding.textViewPickUpPoint.setOnClickListener {
            val intent = Intent(requireActivity(), SelectAddressActivity::class.java)
            intent.putExtra("type", "1")
            pickUpLauncher.launch(intent)
        }

        binding.textViewDropOffPoint.setOnClickListener {
            val intent = Intent(requireActivity(), SelectAddressActivity::class.java)
            intent.putExtra("type", "2")
            dropOffLauncher.launch(intent)
        }

        binding.textViewDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Xử lý khi nhấn nút
        binding.btnFindBus.setOnClickListener {
            if (pickUpPoint.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng chọn điểm đón!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dropOffPoint.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng chọn điểm trả!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            selectedDate = binding.textViewDate.text.toString()

            val intent = Intent(requireActivity(), SelectTicketActivity::class.java)
            intent.putExtra("codePickUpPoint",codeStartStation)
            intent.putExtra("codeDropOffPoint",codeEndStation)
            intent.putExtra("pickUpPoint", pickUpPoint)
            intent.putExtra("dropOffPoint", dropOffPoint)
            intent.putExtra("date",selectedDate)
            startActivity(intent)
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val today = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 24)
        val maxDate = calendar.timeInMillis
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "\t$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.textViewDate.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = today
        datePickerDialog.datePicker.maxDate = maxDate
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}