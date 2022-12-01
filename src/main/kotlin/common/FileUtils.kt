package common

import com.google.common.io.Resources
import java.io.File

private fun getResourcePath(dayNumber: Int, fileName: String): String{
    return Resources.getResource("day_$dayNumber/$fileName")
        .path
        .toString()
}
fun getInputFile(dayNumber: Int): File {
    return File(getResourcePath(dayNumber, "input.txt"))
}

fun getSampleFile(dayNumber: Int): File {
    return File(getResourcePath(dayNumber, "sample.txt"))
}
