package com.grupo12.guiaviajespersonalizada.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grupo12.guiaviajespersonalizada.R

class PopularDestinationsAdapter(
    private val onDestinationClick: (PopularDestination) -> Unit
) : RecyclerView.Adapter<PopularDestinationsAdapter.DestinationViewHolder>() {

    private var destinations: List<PopularDestination> = emptyList()

    fun updateDestinations(newDestinations: List<PopularDestination>) {
        destinations = newDestinations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destination_card, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(destinations[position])
    }

    override fun getItemCount(): Int = destinations.size

    inner class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageDestination: ImageView = itemView.findViewById(R.id.iv_destination_image)
        private val textName: TextView = itemView.findViewById(R.id.tv_destination_name)
        private val textCountry: TextView = itemView.findViewById(R.id.tv_destination_country)
        private val textPrice: TextView = itemView.findViewById(R.id.tv_destination_price)
        private val textRating: TextView = itemView.findViewById(R.id.tv_destination_rating)

        fun bind(destination: PopularDestination) {
            textName.text = destination.name
            textCountry.text = destination.country
            textPrice.text = "desde ${destination.price}"
            textRating.text = "★ ${destination.rating}"

            // Cargar imagen usando el nombre del recurso
            val imageResId = itemView.context.resources.getIdentifier(
                destination.imageRes,
                "drawable",
                itemView.context.packageName
            )

            if (imageResId != 0) {
                imageDestination.setImageResource(imageResId)
            } else {
                // Imagen por defecto si no se encuentra
                imageDestination.setImageResource(R.drawable.ic_launcher_background)
            }

            itemView.setOnClickListener {
                onDestinationClick(destination)
            }
        }
    }
}