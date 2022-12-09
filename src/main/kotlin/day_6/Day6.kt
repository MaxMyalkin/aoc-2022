package day_6

import common.getInputFile

private fun String.containsDifferentChars(): Boolean {
    return toSet().size == length
}

private fun findStartMessageMarker(input: String, packetSize: Int): Int {
    var currentIndex = 0
    while (currentIndex + packetSize <= input.length) {
        val substring = input.substring(currentIndex, currentIndex + packetSize)
        if (substring.containsDifferentChars()) {
            return currentIndex + packetSize
        } else {
            currentIndex++
        }
    }
    return -1
}

fun main() {

    val input = getInputFile(6)
    val inputString = input.readLines()[0]

    val part1PacketSize = 4
    val part2PacketSize = 14

    check(findStartMessageMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb", part1PacketSize) == 7)
    check(findStartMessageMarker("bvwbjplbgvbhsrlpgdmjqwftvncz", part1PacketSize) == 5)
    check(findStartMessageMarker("nppdvjthqldpwncqszvftbrmjlhg", part1PacketSize) == 6)
    check(findStartMessageMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", part1PacketSize) == 10)
    check(findStartMessageMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", part1PacketSize) == 11)

    println("Part1 = ${findStartMessageMarker(inputString, part1PacketSize)}")

    check(findStartMessageMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb", part2PacketSize) == 19)
    check(findStartMessageMarker("bvwbjplbgvbhsrlpgdmjqwftvncz", part2PacketSize) == 23)
    check(findStartMessageMarker("nppdvjthqldpwncqszvftbrmjlhg", part2PacketSize) == 23)
    check(findStartMessageMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", part2PacketSize) == 29)
    check(findStartMessageMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", part2PacketSize) == 26)

    println("Part2 = ${findStartMessageMarker(inputString, part2PacketSize)}")

}