package day_7

import common.getInputFile
import common.getSampleFile
import java.io.File

private sealed interface Structure {

    val size: Int

    data class Folder(
        val name: String,
        override var size: Int,
        val content: MutableList<Structure>,
    ) : Structure {
        fun addContent(contentItem: Structure) {
            size += contentItem.size
            content.add(contentItem)
        }
    }

    data class File(
        val name: String,
        override val size: Int,
    ) : Structure
}

private sealed interface Console {
    data class GoToDir(
        val dirName: String
    ) : Console

    object GoUp : Console
    object ListFiles : Console

    data class ListDirItem(
        val dirName: String
    ) : Console

    data class ListFileItem(
        val fileName: String,
        val fileSize: Int
    ) : Console
}

private fun calculateSize(file: File): Int {
    val consoleOutput = getConsoleOutput(file)
    val result = buildFileTree(consoleOutput, 0, null)
    return calculateTreeResult(result.root!!)
}


private fun getConsoleOutput(file: File): List<Console> {
    return file.bufferedReader()
        .lines()
        .map { parseConsoleOutput(it) }
        .toList()
}

private fun parseConsoleOutput(output: String): Console {
    return when {
        output == "\$ ls" -> Console.ListFiles
        output == "\$ cd .." -> Console.GoUp
        output.startsWith("\$ cd") -> Console.GoToDir(dirName = output.split(" ").last())
        output.startsWith("dir ") -> Console.ListDirItem(dirName = output.split(" ").last())
        else -> output.split(" ").let { Console.ListFileItem(fileName = it[1], fileSize = it[0].toInt()) }
    }
}

private data class RecursiveResult(
    val resumeIndex: Int,
    val root: Structure.Folder?
)

private fun buildFileTree(
    consoleOutput: List<Console>,
    startIndex: Int,
    parent: Structure.Folder?
): RecursiveResult {
    var index = startIndex
    var root: Structure.Folder? = parent
    while (index < consoleOutput.size) {
        when (val console = consoleOutput[index]) {
            is Console.GoToDir -> {
                val newFolder = Structure.Folder(
                    name = console.dirName,
                    size = 0,
                    content = mutableListOf(),
                )
                val result = buildFileTree(consoleOutput, index + 1, newFolder)
                val prevRoot = root
                if (root == null) root = result.root
                index = result.resumeIndex
                prevRoot?.addContent(newFolder)
                continue
            }

            is Console.ListFileItem -> {
                val file = Structure.File(
                    name = console.fileName,
                    size = console.fileSize
                )
                root?.addContent(file)
            }

            is Console.ListDirItem -> { /* ignore */
            }

            Console.ListFiles -> { /* ignore */
            }

            Console.GoUp -> return RecursiveResult(resumeIndex = index + 1, root = root)
        }
        index++
    }

    return RecursiveResult(resumeIndex = index, root = root)
}

private fun calculateTreeResult(folder: Structure.Folder): Int {
    val limit = 100_000
    var result = 0
    if (folder.size <= limit) {
        result += folder.size
    }
    result += folder.content.filterIsInstance<Structure.Folder>()
        .map { calculateTreeResult(it) }
        .sum()
    return result
}

private fun getAllDirsLargerThanLimit(root: Structure.Folder, limit: Int): List<Int> {
    val sizes = mutableListOf<Int>()
    if(root.size >= limit) {
        sizes.add(root.size)
    }
    root.content.forEach {
        if(it is Structure.Folder) {
            sizes.addAll(getAllDirsLargerThanLimit(it, limit))
        }
    }
    return sizes
}
private fun deleteDirectory(file: File): Int {
    val consoleOutput = getConsoleOutput(file)
    val result = buildFileTree(consoleOutput, 0, null)
    val root = result.root!!
    val usedSpace = root.size
    val fullSpace = 70_000_000
    val freeSpace = fullSpace - usedSpace
    val requiredSpace = 30_000_000
    val needToDelete = requiredSpace - freeSpace
    return getAllDirsLargerThanLimit(root, needToDelete)
        .sorted()
        .first()
}

fun main() {

    val input = getInputFile(7)
    val sample = getSampleFile(7)

    check(calculateSize(sample) == 95437)
    println("Part1 = ${calculateSize(input)}")

    check(deleteDirectory(sample) == 24933642)
    println("Part2 = ${deleteDirectory(input)}")
}