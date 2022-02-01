package com.adyen.android.assignment.presentation.displayVenues

import android.Manifest
import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyen.android.assignment.R
import com.adyen.android.assignment.core.Constants.LOCATION_PERMISSION_REQUEST
import com.adyen.android.assignment.core.LocationUtil
import com.adyen.android.assignment.core.NetworkResult
import com.adyen.android.assignment.core.displayNotificationToUser
import com.adyen.android.assignment.databinding.FragmentDisplayVenuesBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DisplayVenuesFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentDisplayVenuesBinding? = null
    private val binding get() = _binding!!
    private val venueDetailsListViewModel: VenueDetailsListViewModel by viewModels()
    private var isGPSEnabled = false
    private val displayVenuesAdapter = DisplayVenuesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayVenuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Check weather Location/GPS is ON or OFF
        isUserGPSEnabled()

        startLocationUpdates()
    }

    private fun isUserGPSEnabled() {
        LocationUtil(requireContext()).turnGPSOn(object :
            LocationUtil.OnLocationOnListener {

            override fun locationStatus(isLocationOn: Boolean) {
                this@DisplayVenuesFragment.isGPSEnabled = isLocationOn
            }
        })
    }

    /**
     * Initiate Location updated by checking Location/GPS settings is ON or OFF
     * Requesting permissions to read location.
     */
    private fun startLocationUpdates() {
        when {
            !isGPSEnabled -> {

                displayNotificationToUser(requireContext(),getString(R.string.enable_gps_toast))
            }

            isLocationPermissionsGranted() -> {

                displayNotificationToUser(requireContext(),getString(R.string.permission_granted))
                observeLocationUpdates()
            }
            else -> {
                askLocationPermission()
            }
        }
    }

    private fun observeLocationUpdates() {
        venueDetailsListViewModel.locationData.observe(viewLifecycleOwner) {
            val query = mapOf(
                "ll" to "${it.latitude},${it.longitude}"
            )
            venueDetailsListViewModel.getVenueDetailsFromServer(query)

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@observe
            with(sharedPref.edit()) {
                putString(
                    getString(R.string.saved_lat_long_values),
                    "${it.latitude},${it.longitude}"
                )
                apply()
            }

        }

        showVenueDetailsOnAList()
        searchVenue()
    }

    private fun showVenueDetailsOnAList() {


        venueDetailsListViewModel.venueDetails.observe(viewLifecycleOwner) { netWorkResponse ->

            when (netWorkResponse) {
                is NetworkResult.Success -> {
                    displayVenuesAdapter.submitList(netWorkResponse.data)
                    _binding?.venuesRecycler?.apply {
                        adapter = displayVenuesAdapter
                        layoutManager = LinearLayoutManager(context)
                        addItemDecoration(
                            DividerItemDecoration(
                                this.context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }
                    _binding?.progressBar?.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                    displayNotificationToUser(requireContext(), netWorkResponse.message)

                }
                is NetworkResult.Loading -> {
                    _binding?.progressBar?.visibility = View.VISIBLE

                }
            }

        }


    }

    private fun searchVenue() {

        _binding?.apply {
            searchBtn.setOnClickListener {
                val placeToSearch = searchEditText.text
                //  searchEditText.text.clear()

                val sharedPref =
                    activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
                val latLong =
                    sharedPref.getString(getString(R.string.saved_lat_long_values), null)


                if (!latLong.isNullOrBlank() && !placeToSearch.isNullOrBlank()) {
                    val query = mapOf(
                        "ll" to "$latLong",
                        "query" to "$placeToSearch"
                    )
                    venueDetailsListViewModel.getVenueDetailsFromServer(query)
                } else {
                  displayNotificationToUser(requireContext(), getString(R.string.write_gps_request))
                }

                searchEditText.text.clear()
            }
        }

    }

    private fun askLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permission_denied),
            LOCATION_PERMISSION_REQUEST,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }


    private fun isLocationPermissionsGranted() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            askLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        displayNotificationToUser(requireContext(), getString(R.string.permission_granted_by_user))
        isGPSEnabled = true
        startLocationUpdates()
    }


}