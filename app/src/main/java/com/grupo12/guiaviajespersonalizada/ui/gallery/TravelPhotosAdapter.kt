package com.grupo12.guiaviajespersonalizada.ui.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grupo12.guiaviajespersonalizada.R

class TravelPhotosAdapter(
    private var photos: List<TravelPhoto>,
    private val onActionClick: (TravelPhoto, PhotoAction) -> Unit
) : RecyclerView.Adapter<TravelPhotosAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPhoto: ImageView = view.findViewById(R.id.iv_travel_photo)
        val tvTitle: TextView = view.findViewById(R.id.tv_photo_title)
        val tvLocation: TextView = view.findViewById(R.id.tv_photo_location)
        val tvDate: TextView = view.findViewById(R.id.tv_photo_date)
        val btnFavorite: ImageButton = view.findViewById(R.id.btn_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_travel_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]

        // ✅ Mostrar imagen: primero intenta con URI, luego con recurso local
        if (photo.imageUri != null) {
            Glide.with(holder.itemView.context)
                .load(photo.imageUri)
                .placeholder(R.drawable.ic_image_placeholder)
                .centerCrop()
                .into(holder.ivPhoto)
        } else if (photo.imageRes != null) {
            holder.ivPhoto.setImageResource(photo.imageRes)
        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_image_placeholder)
        }

        // ✅ Asignar texto
        holder.tvTitle.text = photo.title
        holder.tvLocation.text = photo.location
        holder.tvDate.text = photo.date

        // ✅ Estado del favorito
        holder.btnFavorite.setImageResource(
            if (photo.isFavorite) R.drawable.ic_favorite_filled
            else R.drawable.ic_favorite_border
        )

        // ✅ Acciones
        holder.itemView.setOnClickListener { onActionClick(photo, PhotoAction.VIEW) }
        holder.btnFavorite.setOnClickListener { onActionClick(photo, PhotoAction.FAVORITE) }
    }

    override fun getItemCount(): Int = photos.size

    fun updatePhotos(newPhotos: List<TravelPhoto>) {
        photos = newPhotos
        notifyDataSetChanged()
    }
}