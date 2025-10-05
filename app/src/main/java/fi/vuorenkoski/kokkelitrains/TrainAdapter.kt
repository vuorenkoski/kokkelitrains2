package fi.vuorenkoski.kokkelitrains

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainAdapter internal constructor(context: Context?, data: ArrayList<Train?>) :
    RecyclerView.Adapter<TrainAdapter.ViewHolder?>() {
    private val mData: ArrayList<Train?>
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null

    // data is passed into the constructor
    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = data
    }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_row, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val train = mData[position]
        holder.lineIdView.text = train?.lineId.toString()

        holder.trackView.text = train?.track
        holder.departureTimeView.text = train?.departureTimeStr
        holder.notificationView.text = train?.notification
        holder.arrivalTimeView.text = train?.arrivalTimeStr
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var lineIdView: TextView
        var trackView: TextView
        var departureTimeView: TextView
        var notificationView: TextView
        var arrivalTimeView: TextView

        init {
            lineIdView = itemView.findViewById<TextView?>(R.id.train_line_id)
            trackView = itemView.findViewById<TextView?>(R.id.train_track)
            departureTimeView = itemView.findViewById<TextView?>(R.id.train_departure_time)
            notificationView = itemView.findViewById<TextView?>(R.id.train_notification)
            arrivalTimeView = itemView.findViewById<TextView?>(R.id.train_arrival_time)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): String {
        return mData.get(id).toString()
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        this.mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    // ... inside your TrainAdapter class
    fun updateData(newTrains: List<Train?>?) {
        this.mData?.clear()
        if (newTrains != null) {
            this.mData?.addAll(newTrains)
        }
        notifyDataSetChanged() // Notifies the RecyclerView to refresh
    }
}