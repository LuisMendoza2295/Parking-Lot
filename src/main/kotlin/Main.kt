package parking

const val PARK = "park"
const val LEAVE = "leave"
const val EXIT = "exit"
const val CREATE = "create"
const val STATUS = "status"
const val REG_BY_COLOR = "reg_by_color"
const val SPOT_BY_COLOR = "spot_by_color"
const val SPOT_BY_REG = "spot_by_reg"

const val PARKING_NOT_CREATED = "Sorry, a parking lot has not been created."
const val PARKING_EMPTY = "Parking lot is empty."
const val PARKING_FULL = "Sorry, the parking lot is full."

class Car(val regNo: String, color: String) {
    val color: String = color.lowercase()

    fun formattedColor() = color.replaceFirstChar { it.uppercase() }

    fun printStatus() = println("$regNo ${formattedColor()}")

    fun isColor(color: String) = this.color == color.lowercase()
}

class Slot(val car: Car = Car("-", "-"), val position: Int, val isFree: Boolean = true) {
    fun printStatus() = println("$position ${car.printStatus()}")
}

class Parking() {
    val slots = mutableListOf<Slot>()

    fun init(size: Int = 0) {
        slots.clear()
        repeat(size) { slots.add(Slot(position = it)) }
        println("Created a parking lot with $size spots.")
    }

    fun printStatus() {
        if (isSlotsCreated().not()) return
        if (slots.all { it.isFree }) {
            println(PARKING_EMPTY)
            return
        }
        slots.filter { it.isFree.not() }
            .forEach { println(it.printStatus()) }
    }

    fun park(car: Car) {
        if (isSlotsCreated().not()) return
        val index = slots.indexOfFirst { slot -> slot.isFree }
        if ((index in 0 .. slots.size).not()) {
            println(PARKING_FULL)
        }
        val slot = Slot(car = car, position = index + 1, isFree = false)
        slots[index] = slot
        println("${car.formattedColor()} car parked in spot ${slot.position}.")
    }

    fun leave(i: Int = 0) {
        if (isSlotsCreated().not()) return
        if (i > slots.size) {
            println("There is no car in spot ${i + 1}.")
            return
        }
        val slot = slots[i]
        if (slot.isFree) {
            println("There is no car in spot ${slot.position}.")
            return
        }
        slots[i] = Slot(position = i + 1)
        println("Spot ${slot.position} is free.")
    }

    fun regByColor(color: String) {
        if (isSlotsCreated().not()) return
        val colorSlots = slots.filter { it.car.isColor(color) }.map { it.car.regNo }
        printSlotQuery(color, colorSlots)
    }

    fun spotByColor(color: String) {
        if (isSlotsCreated().not()) return
        val colorSlots = slots.filter { it.car.isColor(color) }.map { it.position.toString() }
        printSlotQuery(color, colorSlots)
    }

    fun spotByReg(regNo: String) {
        if (isSlotsCreated().not()) return
        val slot = slots.find { it.car.regNo == regNo }
        println(slot?.position ?: "No cars with registration number $regNo were found.")
    }

    fun printSlotQuery(color: String, colorSlots: List<String>) {
        if (colorSlots.isEmpty()) {
            println("No cars with color ${color.uppercase()} were found.")
            return
        }
        println(colorSlots.joinToString())
    }

    fun isSlotsCreated(): Boolean {
        if (slots.isEmpty()) {
            println(PARKING_NOT_CREATED)
            return false
        }
        return true
    }
}

fun main() {
    val parking = Parking()
    while (true) {
        val input = readln().split(" ")
        when(input[0]) {
            CREATE -> parking.init(input[1].toInt())
            STATUS -> parking.printStatus()
            PARK -> parking.park(Car(input[1], input[2]))
            LEAVE -> parking.leave(input[1].toInt() - 1)
            REG_BY_COLOR -> parking.regByColor(input[1])
            SPOT_BY_COLOR -> parking.spotByColor(input[1])
            SPOT_BY_REG -> parking.spotByReg(input[1])
            EXIT -> break
        }
    }
}
