package com.example.worktimemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkTimeAdapter(private val workTimeList: List<WorkTime>) :
        RecyclerView.Adapter<WorkTimeAdapter.WorkTimeViewHolder>() {

            class WorkTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                val dateText: TextView = view.findViewById(R.id.dateText)
                val startTimeText: TextView = view.findViewById(R.id.startTimeText)
                val endTimeText: TextView = view.findViewById(R.id.endTimeText)
                val totalTimeText: TextView = view.findViewById(R.id.totalTimeText)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkTimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.work_time_item, parent, false)
        return WorkTimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkTimeViewHolder, position: Int) {
        val workTime = workTimeList[position]
        holder.dateText.text = "Date: ${workTime.date}"
        holder.startTimeText.text = "Start: ${workTime.startTime}"
        holder.endTimeText.text = "End: ${workTime.endTime}"
        holder.totalTimeText.text = "Total: ${workTime.getTotalHours()} hrs"
    }

    override fun getItemCount() = workTimeList.size
        }