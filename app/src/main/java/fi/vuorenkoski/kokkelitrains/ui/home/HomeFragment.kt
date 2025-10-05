package fi.vuorenkoski.kokkelitrains.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import fi.vuorenkoski.kokkelitrains.DataSearch
import fi.vuorenkoski.kokkelitrains.R // Make sure this is your project's R, not android.R
import fi.vuorenkoski.kokkelitrains.Station
import fi.vuorenkoski.kokkelitrains.Train
import fi.vuorenkoski.kokkelitrains.TrainAdapter
import fi.vuorenkoski.kokkelitrains.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    // 1. Use a single, non-nullable binding property
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 2. Initialize the adapter once
    private lateinit var trainAdapter: TrainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 3. Setup views here, using the binding object
        setupRecyclerView()
        setupSpinners()
    }

    private fun setupRecyclerView() {
        // Initialize with an empty list. The adapter will be updated later.
        trainAdapter = TrainAdapter(requireContext(), ArrayList())
        binding.trainList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trainAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSpinners() {
        // Use a single listener instance for both spinners
        val eventListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 4. Fetch data when a selection changes
                fetchTrainData()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.selectableStations,
            android.R.layout.simple_spinner_item // Use Android's built-in layout
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.departureStationSpinner.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = eventListener
            // setPopupBackgroundResource can be set here if needed
        }

        binding.destinationStationSpinner.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = eventListener
            // setPopupBackgroundResource can be set here if needed
        }
    }

    private fun fetchTrainData() {
        // Get selected stations from spinners via the binding object
        val departure = Station(binding.departureStationSpinner.selectedItem.toString())
        val destination = Station(binding.destinationStationSpinner.selectedItem.toString())

        if (departure.shortCode == destination.shortCode) {
            // Clear the list if departure and destination are the same
            trainAdapter.updateData(emptyList())
            return
        }

        // 5. Use coroutines to move network calls off the main thread
        lifecycleScope.launch {
            try {
                // Show a loading indicator (optional but recommended)
                // binding.progressBar.visibility = View.VISIBLE

                // Switch to a background thread for the network call
                val trains = withContext(Dispatchers.IO) {
                    DataSearch.getTrains(departure.shortCode, destination.shortCode)
                }

                // Switch back to the main thread to update the UI
                trainAdapter.updateData(trains)

            } catch (e: Exception) {
                // Handle exceptions (e.g., no internet)
                Toast.makeText(
                    requireContext(),
                    "Datan hakeminen ei onnistunut: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                // Clear the list on error
                trainAdapter.updateData(emptyList())
            } finally {
                // Hide loading indicator
                // binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important to avoid memory leaks
    }
}
