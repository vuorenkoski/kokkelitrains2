package fi.vuorenkoski.kokkelitrains

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class Train(
    val number: Int,
    departureStation: String?,
    lineId: Char,
    track: String?,
    departureTime: Date,
    cancelled: Boolean,
    estimateExists: Boolean,
    estimatedTime: Date,
    arrivalTime: Date?,
    arrivalStation: String?,
    finalStation: String?,
    causes: String?
) : Comparable<Train?> {
    private val departureStation: Station?
    val lineId: Char
    val track: String?
    val departureTime: Date
    private val cancelled: Boolean
    private val estimateExists: Boolean
    private val estimatedTime: Date?
    private val arrivalTime: Date?
    private val arrivalStation: Station?
    private val finalStation: Station?
    val causes: String?

    val speed: String?

    private var late: Int

    init {
        this.departureStation = Station(departureStation ?: "")
        this.lineId = lineId
        this.track = track
        this.departureTime = departureTime
        this.cancelled = cancelled
        this.estimateExists = estimateExists
        this.estimatedTime = estimatedTime
        this.arrivalTime = arrivalTime

        this.arrivalStation = Station(arrivalStation ?: "")
        this.finalStation = Station(finalStation ?: "")
        this.late = ((estimatedTime.getTime() - departureTime.getTime()) / 1000).toInt()
        if (this.late < 60) this.late = 0
        this.causes = causes

        this.speed = null
    }

    val departureTimeStr: String
        get() = timeString(departureTime)

    val estimatedTimeStr: String
        get() = timeString(estimatedTime)

    val arrivalTimeStr: String
        get() = timeString(arrivalTime)

    val correctedDepartureTimeStr: String
        get() {
            if (this.late != 0) {
                return timeString(departureTime) + "-->" + timeString(estimatedTime)
            } else return timeString(departureTime)
        }

    val notification: String
        get() {
            if (this.cancelled) return "Peruttu"
            if (this.estimateExists && this.departureTimeStr != this.estimatedTimeStr) return "->" + this.estimatedTimeStr
            return ""
        }

    private fun timeString(date: Date?): String {
        val dateFormat: DateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(date)
    }

    override fun toString(): String {
        if (this.cancelled) return "" + lineId + " " + this.departureTimeStr + " PERUTTU"
        if (this.late != 0) return "" + lineId + " " + track + " " + this.departureTimeStr + "-->" + this.estimatedTimeStr + "  (" + this.arrivalStation + " " + this.arrivalTimeStr + ")"
        return "" + lineId + " " + track + " " + this.estimatedTimeStr + "  (" + this.arrivalStation + " " + this.arrivalTimeStr + ")"
    }

    override fun compareTo(other: Train?): Int {
        if (other == null) {
            return 1
        }
        return (this.departureTime.time - other.departureTime.time).toInt()
    }
}