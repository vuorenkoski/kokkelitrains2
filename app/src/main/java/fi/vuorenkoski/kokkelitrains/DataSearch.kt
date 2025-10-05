package fi.vuorenkoski.kokkelitrains

import org.json.JSONArray
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Scanner
import java.util.TimeZone
import java.util.function.Supplier
import java.util.stream.Collectors

object DataSearch {
    // hakee yhdeltä asemalta lähtevät junat, parametreina lahtoasema ja maaraasema, palauttaa listan Junista
    @Throws(Exception::class)
    fun getTrains(station: String?, destination: String?): java.util.ArrayList<Train?> {
        val trains: java.util.ArrayList<Train?> = java.util.ArrayList<Train?>()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
        val currentTime = Date(System.currentTimeMillis())

        //  URL urli=new URL("https://rata.digitraffic.fi/api/v1/live-trains?station="+asema+"&departing_trains=70&train");
        // haku aikamäärällä, mutta palauttaa paljon junia
        // URL urli=new URL("https://rata.digitraffic.fi/api/v1//live-trains/station/"+asema+"?minutes_before_departure=15&minutes_after_departure=15&minutes_before_arrival=240&minutes_after_arrival=15&train_categories=Commuter");
        val url =
            URL("https://rata.digitraffic.fi/api/v1/live-trains/station/" + station + "?departing_trains=50&train_categories=Commuter")
        val fileReader = Scanner(url.openStream())
        val data = JSONArray(fileReader.nextLine())

        for (i in 0..<data.length()) {
            val lineIdStr = data.getJSONObject(i).getString("commuterLineID")
            if (lineIdStr.length == 1) { // kauko ym. junilla tämä koodi on tyhjä
                val lineId = lineIdStr.get(0)
                val timeTableRows =
                    data.getJSONObject(i).getJSONArray("timeTableRows") // Aikautalutiedot

                val number = data.getJSONObject(i).getInt("trainNumber")

                // Junan koko reitin aikataulutiedot on asemittain, erikseen DEPARTURE ja ARRIVAL
                // ensin etsitään asema jolta halutaan lähteä
                var departureStationFound = false
                var arrivalStationFound = false
                var estimateExists = false
                var estimateTime = Date()
                var departureTime = Date()
                var arrivalTime = Date()
                var finalStation = ""
                var cancelled = false
                var track = ""
                var causes: String? = null

                for (j in 0..<timeTableRows.length()) {
                    if (timeTableRows.getJSONObject(j).getString("stationShortCode") == station &&
                        timeTableRows.getJSONObject(j).getString("type") == "DEPARTURE" &&
                        timeTableRows.getJSONObject(j).getBoolean("trainStopping")
                    ) {
                        departureStationFound = true

                        departureTime = dateFormat.parse(
                            timeTableRows.getJSONObject(j).getString("scheduledTime")
                        )
                        if (timeTableRows.getJSONObject(j).has("liveEstimateTime")) {
                            estimateTime = dateFormat.parse(
                                timeTableRows.getJSONObject(j).getString("liveEstimateTime")
                            )
                            estimateExists = true
                        }
                        cancelled = timeTableRows.getJSONObject(j).getBoolean("cancelled")
                        track = timeTableRows.getJSONObject(j).getString("commercialTrack")
                    }
                    if (departureStationFound && timeTableRows.getJSONObject(j)
                            .getString("stationShortCode") == destination &&
                        timeTableRows.getJSONObject(j).getString("type") == "ARRIVAL" &&
                        timeTableRows.getJSONObject(j).getBoolean("trainStopping")
                    ) {
                        arrivalStationFound = true
                        arrivalTime = dateFormat.parse(
                            timeTableRows.getJSONObject(j).getString("scheduledTime")
                        )
                    }
                    finalStation = timeTableRows.getJSONObject(j).getString("stationShortCode")
                    causes = timeTableRows.getJSONObject(j).getJSONArray("causes").toString()
                }

                if (!estimateExists) estimateTime = departureTime

                if (arrivalStationFound && estimateTime.compareTo(currentTime) > 0) {
                    trains.add(
                        Train(
                            number,
                            station,
                            lineId,
                            track,
                            departureTime,
                            cancelled,
                            estimateExists,
                            estimateTime,
                            arrivalTime,
                            destination,
                            finalStation,
                            causes
                        )
                    )
                }
            }
        }
        return trains.stream().sorted().collect(Collectors.toCollection(Supplier { ArrayList() }))
    }
}