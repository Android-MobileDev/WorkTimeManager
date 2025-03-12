package com.example.worktimemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class WorkTimeAdapter(private val workTimeList: List<WorkTime>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    // Data structure to include both headers and work times
    private val groupedItems: List<Any> = createGroupedList(workTimeList)

    class WorkTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dateText)
        val startTimeText: TextView = view.findViewById(R.id.startTimeText)
        val endTimeText: TextView = view.findViewById(R.id.endTimeText)
        val totalTimeText: TextView = view.findViewById(R.id.totalTimeText)
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val monthHeader: TextView = view.findViewById(R.id.monthHeader)
    }

    override fun getItemViewType(position: Int): Int {
        return if (groupedItems[position] is String) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_month_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.work_time_item, parent, false)
            WorkTimeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.monthHeader.text = groupedItems[position] as String
        } else if (holder is WorkTimeViewHolder) {
            val workTime = groupedItems[position] as WorkTime
            holder.dateText.text = "Date: ${workTime.date}"
            holder.startTimeText.text = "Start: ${workTime.startTime}"
            holder.endTimeText.text = "End: ${workTime.endTime}"
            holder.totalTimeText.text = "Total: ${workTime.getTotalHours()} hrs"
        }
    }

    override fun getItemCount() = groupedItems.size

    private fun createGroupedList(workTimes: List<WorkTime>): List<Any> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

        val groupedList = mutableListOf<Any>()
        var currentMonth: String? = null

        workTimes.sortedByDescending {
            dateFormat.parse(it.date)
        }.forEach { workTime ->
            val workDate = dateFormat.parse(workTime.date) ?: return@forEach
            val month = monthFormat.format(workDate)

            if (month != currentMonth) {
                groupedList.add(month) // Add month header
                currentMonth = month
            }
            groupedList.add(workTime)
        }

        return groupedList
    }
}