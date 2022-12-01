package common

import com.google.common.io.Resources
import java.io.File

private fun getResourcePath(dayNumber: Int, taskNumber: Int, fileName: String): String{
    return Resources.getResource("day_$dayNumber/task_$taskNumber/$fileName")
        .path
        .toString()
}
fun getInputFile(dayNumber: Int, taskNumber: Int): File {
    return File(getResourcePath(dayNumber, taskNumber, "input.txt"))
}

fun getSampleFile(dayNumber: Int, taskNumber: Int): File {
    return File(getResourcePath(dayNumber, taskNumber, "sample.txt"))
}
