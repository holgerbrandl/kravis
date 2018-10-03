//@file:DependsOnMaven("com.github.holgerbrandl:kravis:0.4")

import kravis.geomCol
import kravis.plot

// required to run via Intellij
System.setProperty("java.awt.headless", "false")

enum class Gender { male, female }
data class Person(val name: String, val gender: Gender, val heightCm: Int, val weightKg: Double)

//' define some persons
val persons = listOf(
    Person("Max", Gender.male, 192, 80.3),
    Person("Anna", Gender.female, 162, 56.3),
    Person("Maria", Gender.female, 172, 66.3)
)

//' peek into persons
persons

//' visualize some persons
persons.plot(x = {name}, y = { weightKg }, fill = { gender.toString() })
    .geomCol()
    .xLabel("height [m]")
    .yLabel("weight [kg]")
    .title("Body Size Distribution")
    .show()