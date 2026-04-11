package com.example.workshop5

import java.io.File

// Part c & d: Data class with two variables
data class LineData(val field1: String, val field2: String)

fun main() {
    val fileName = "workshop_data.txt"
    val file = File(fileName)

    // Writing the file
    val content = "Apple,Red\nBanana,Yellow\nCherry,Red"
    file.writeText(content)

    // a) Use forEachLine to read and print
    println("--- Part a: forEachLine ---")
    file.forEachLine { line ->
        println(line)
    }

    // b) Use readLines to read into a list
    println("\n--- Part b: readLines ---")
    val linesList: List<String> = file.readLines()
    println(linesList)

    // c) & d) Create objects using split and add to a mutable list
    println("\n--- Part c & d: Data Objects ---")
    val dataObjects = mutableListOf<LineData>()
    file.forEachLine { line ->
        val parts = line.split(",")
        if (parts.size == 2) {
            dataObjects.add(LineData(parts[0], parts[1]))
        }
    }

    dataObjects.forEach { obj ->
        println("Item: ${obj.field1}, Color: ${obj.field2}")
    }
}
