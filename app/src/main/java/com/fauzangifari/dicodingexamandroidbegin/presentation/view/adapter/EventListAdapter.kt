import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent
import com.fauzangifari.dicodingexamandroidbegin.data.remote.response.ListEventsItem
import com.fauzangifari.dicodingexamandroidbegin.databinding.ItemListEventBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.view.DetailActivity
import com.fauzangifari.dicodingexamandroidbegin.util.EventDiffCallback

class EventListAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(EventDiffCallback()) {

    private var isFromApi: Boolean = true

    class ApiViewHolder(private val binding: ItemListEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(event.mediaCover)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(listEventImage)

                listEventTitle.text = event.name
            }
        }
    }

    class DbViewHolder(private val binding: ItemListEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEvent) {
            binding.apply {
                Glide.with(itemView)
                    .load(event.mediaCover)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(listEventImage)

                listEventTitle.text = event.name
            }
        }
    }

    fun setDataFromApi(events: List<ListEventsItem>) {
        isFromApi = true
        submitList(events)
    }

    fun setDataFromDb(events: List<FavoriteEvent>) {
        isFromApi = false
        submitList(events)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemListEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (isFromApi) {
            ApiViewHolder(binding)
        } else {
            DbViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isFromApi) {
            val event = getItem(position) as ListEventsItem
            (holder as ApiViewHolder).bind(event)
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("event", event)
                }
                context.startActivity(intent)
            }
        } else {
            val event = getItem(position) as FavoriteEvent
            (holder as DbViewHolder).bind(event)
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("event", event)
                }
                context.startActivity(intent)
            }
        }
    }
}
