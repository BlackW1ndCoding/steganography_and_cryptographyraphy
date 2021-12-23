package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

fun hide(inputPath: String, outputPath: String, message: String) {
    val inputFile = File(inputPath)
    val outputFile = File(outputPath)
    val convertedString = (message + "003").encodeToByteArray()
        .map { byte -> List(8) { getBit(byte.toInt(), it) }.reversed() }
        .flatten()
        .toIntArray()

    try {
        val image: BufferedImage = ImageIO.read(inputFile)

        if (image.width * image.height < (convertedString.size) * 8) {
            println("The input image is not large enough to hold this message.")
            return
        }

        var y = 0
        var index = 0

        outer@ while (y < image.height) {
            var x = 0
            while (x < image.width) {
                val color = Color(image.getRGB(x, y))
                val bit = convertedString[index]

                image.setRGB(
                    x++, y,
                    Color(
                        color.red,
                        color.green,
                        if (bit == 0) color.blue and 1.inv() else color.blue or 1
                    ).rgb
                )
                if (++index > convertedString.lastIndex) {
                    break@outer
                }
            }
            y++
        }

        ImageIO.write(image, "png", outputFile)

        println("Message saved in $outputPath image.")

    } catch (e: IOException) {
        println("Can't read input file!")
    }
}

fun show(inputPath: String): String {
    val inputFile = File(inputPath)
    val bitArray = mutableListOf<Int>();
    var outputMessage = ""

    try {
        val image: BufferedImage = ImageIO.read(inputFile)

        var x = 0
        var y = 0

        var index = 0
        loop@ while (!outputMessage.contains("003")) {

            val color = Color(image.getRGB(x++, y))
            val bit = color.blue.toByte().takeLowestOneBit().toInt()
            bitArray.add(bit)

            if (bitArray.size % 8 == 0) {
                outputMessage = bitArray.chunked(8).map {
                    it.joinToString("")
                        .toByte(2)
                }
                    .toByteArray().toString(Charsets.UTF_8)
            }
            index++


            if (x == image.width) {
                x = 0
                y++
                if (y == image.height) break@loop
            }
        }

    } catch (e: IOException) {
        println("File can't be read!!")
    }
    //println("loop end")
    return outputMessage.dropLast(3)
}

fun getBit(value: Int, position: Int): Int {
    return (value shr position) and 1;
}

fun rotateBinary(input: Int): Int {
    var res = 0;
    while (input > 0) {
        res = res shl 1;
        res = res or (input and 1)
        input shr 1
    }
    return res
}