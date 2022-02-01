package com.adyen.android.assignment.presentation.displayVenues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.databinding.VenueItemBinding
import com.adyen.android.assignment.domain.model.VenueDetail

class DisplayVenuesAdapter :
    ListAdapter<VenueDetail, DisplayVenuesAdapter.DisplayVenueViewHolder>(VenueDiffCallback) {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class DisplayVenueViewHolder(val binding: VenueItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DisplayVenueViewHolder {
        // Create a new view, which defines the UI of the list item

        return DisplayVenueViewHolder(
            VenueItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(displayVenueViewHolder: DisplayVenueViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        var venueDetail: VenueDetail = getItem(position)
        displayVenueViewHolder.binding.venueText.text = venueDetail.name
    }


    object VenueDiffCallback : DiffUtil.ItemCallback<VenueDetail>() {
        override fun areItemsTheSame(oldItem: VenueDetail, newItem: VenueDetail): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: VenueDetail, newItem: VenueDetail): Boolean {
            return oldItem.locationId == newItem.locationId
        }
    }
}