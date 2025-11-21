import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun isDateInPast(dateString: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val date = LocalDate.parse(dateString, formatter)
    return date.isBefore(LocalDate.now())
}

fun tsvParser(filePath: String): List<Pair<String, String>> {
    val lines = File(filePath).readLines()

    // Find indices of the columns we care about
    val idIndex = 3
    val endDateIndex = 8
    val varPoleIndex = 9

    // Process lines after header
    val dataLines = lines.dropWhile { !it.startsWith("\tVP\tTO\tID obj.") }.drop(1)

    return dataLines.drop(1).mapNotNull { line ->
        val cols = line.split("\t")
        val idObj = cols[idIndex].trim()
        val varPole = cols[varPoleIndex].removePrefix("S ")

        // Skip line if date is in the past
        if (isDateInPast(cols[endDateIndex])) {
            null
        } else {
            idObj to varPole
        }
    }
}
