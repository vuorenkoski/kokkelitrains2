package fi.vuorenkoski.kokkelitrains

class Station(station: String) {
    private var station = 0
    var shortCode: String? = null
        private set
    var name: String? = null
        private set

    init {
        if (station == "HKI" || station == "Helsinki") {
            this.station = 1
            this.shortCode = "HKI"
            this.name = "Helsinki"
        }
        if (station == "PSL" || station == "Pasila") {
            this.station = 2
            this.shortCode = "PSL"
            this.name = "Pasila"
        }
        if (station == "ILA" || station == "Ilmala") {
            this.station = 3
            this.shortCode = "ILA"
            this.name = "Ilmala"
        }
        if (station == "HPL" || station == "Huopalahti") {
            this.station = 4
            this.shortCode = "HPL"
            this.name = "Huopalahti"
        }
        if (station == "VMO" || station == "Valimo") {
            this.station = 5
            this.shortCode = "VMO"
            this.name = "Valimo"
        }
        if (station == "PJM" || station == "Pitäjänmäki") {
            this.station = 6
            this.shortCode = "PJM"
            this.name = "Pitäjänmäki"
        }
        if (station == "MÄK" || station == "Mäkkylä") {
            this.station = 7
            this.shortCode = "MÄK"
            this.name = "Mäkkylä"
        }
        if (station == "LPV" || station == "Leppävaara") {
            this.station = 8
            this.shortCode = "LPV"
            this.name = "Leppävaara"
        }
        if (station == "KIL" || station == "Kilo") {
            this.station = 9
            this.shortCode = "KIL"
            this.name = "Kilo"
        }
        if (station == "KEA" || station == "Kera") {
            this.station = 10
            this.shortCode = "KEA"
            this.name = "Kera"
        }
        if (station == "KNI" || station == "Kauniainen") {
            this.station = 11
            this.shortCode = "KNI"
            this.name = "Kauniainen"
        }
        if (station == "KVH" || station == "Koivuhovi") {
            this.station = 12
            this.shortCode = "KVH"
            this.name = "Koivuhovi"
        }
        if (station == "TRL" || station == "Tuomarila") {
            this.station = 13
            this.shortCode = "TRL"
            this.name = "Tuomarila"
        }
        if (station == "EPO" || station == "Espoo") {
            this.station = 14
            this.shortCode = "EPO"
            this.name = "Espoo"
        }
        if (station == "KLH" || station == "Kauklahti") {
            this.station = 15
            this.shortCode = "KLH"
            this.name = "Kauklahti"
        }

        if (station == "MAS" || station == "Masala") {
            this.station = 16
            this.shortCode = "MAS"
            this.name = "Masala"
        }

        if (station == "JRS" || station == "Jorvas") {
            this.station = 17
            this.shortCode = "JRS"
            this.name = "Jorvas"
        }

        if (station == "TOL" || station == "Tolsa") {
            this.station = 18
            this.shortCode = "TOL"
            this.name = "Tolsa"
        }

        if (station == "KKN" || station == "Kirkkonummi") {
            this.station = 19
            this.shortCode = "KKN"
            this.name = "Kirkkonummi"
        }

        if (station == "STI" || station == "Siuntio") {
            this.station = 20
            this.shortCode = "STI"
            this.name = "Siuntio"
        }
    }

    override fun toString(): String {
        return this.name!!
    }
}