package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.util.*
import javax.imageio.ImageIO
import kotlin.experimental.xor

fun hide(inputPath: String, outputPath: String, message: String, password: String) {
    val inputFile = File(inputPath)
    val outputFile = File(outputPath)
    val convertedString = (encryptMessage(message, password) + "\u0000\u0000\u0003".encodeToByteArray())
        .joinToString("") {
            if (it.toString(2).length == 8) {
                it.toString(2)
            } else {
                "0".repeat(8 - it.toString(2).length) + it.toString(2)
            }
        }
        .map { it.digitToInt() }

    try {
        val sourceImage = ImageIO.read(inputFile)
        val image = BufferedImage(
            sourceImage.width, sourceImage.height, BufferedImage.TYPE_INT_RGB
        )

        println("ConvertedString: $convertedString")

        if (image.width * image.height < (convertedString.size)) {
            println("The input image is not large enough to hold this message.")
            return
        }

        var y = 0
        var index = 0

        outer@ while (y < image.height) {
            var x = 0
            while (x < image.width) {
                val color = Color(sourceImage.getRGB(x, y))

                if (index <= convertedString.lastIndex) {
                    val bit = convertedString[index]
                    val changedColor =
                        Color(
                            color.red,
                            color.green,
                            color.blue.and(254).or(bit)
                        ).rgb
                    println("${color.rgb} ${changedColor} $bit")
                    image.setRGB(
                        x, y,
                        Color(
                            color.red,
                            color.green,
                            color.blue.and(254).or(bit)
                        ).rgb
                    )
                } else {
                    image.setRGB(x, y, color.rgb)
                }
                index++
                x++
            }
            y++
        }

        ImageIO.write(image, "png", outputFile)

        println("Message saved in $outputPath image.")

    } catch (e: IOException) {
        println("Can't read input file!")
    }
}

fun show(inputPath: String, password: String): String {
    val inputFile = File(inputPath)
    val bitArray = mutableListOf<Int>();
    var byteArray = ByteArray(0)
    var outputMessage = ""

    try {
        val image: BufferedImage = ImageIO.read(inputFile)

        var x = 0
        var y = 0

        var index = 0
        loop@ while (!outputMessage.contains("\u0000\u0000\u0003")) {

            val color = Color(image.getRGB(x++, y))
            val bit = color.blue.toUInt() shl 31 shr 31
            //println("Bit read is $bit")
            bitArray.add(bit.toInt())

            try {

                if (bitArray.size % 8 == 0) {
                    byteArray = bitArray.chunked(8).map {
                        it.joinToString("")
                            .toByte(2)
                    }.toByteArray()

                    outputMessage = byteArray.toString(Charsets.UTF_8)
                }
            } catch (e: NumberFormatException){
                //do nothing
            }
            index++

            //println("Message $outputMessage")
            if (x == image.width) {
                x = 0
                y++
                if (y == image.height) break@loop
            }
        }

    } catch (e: IOException) {
        println("File can't be read!!")
    }

    byteArray = bitArray.chunked(8).map {
        it.joinToString("")
            .toByte(2)
    }.toByteArray()
    //println("loop end")
    return decryptMessage(byteArray, password)
}

fun encryptMessage(message: String, password: String): ByteArray {
    val messageToBytes = message.toByteArray()
    val passwordToBytes = password.toByteArray()
    var passwordIndex = 0

    for (i in messageToBytes.indices){
        messageToBytes[i] = messageToBytes[i].xor(passwordToBytes[passwordIndex++])
        if(passwordIndex > passwordToBytes.lastIndex) passwordIndex = 0
    }

    return messageToBytes
}

fun decryptMessage(message: ByteArray, password: String): String{
    val outputArray = ByteArray(message.size - 3)
    val passwordArray = password.toByteArray()
    var passwordIndex = 0

    for(i in 0..message.lastIndex - 3){
        outputArray[i] = message[i].xor(passwordArray[passwordIndex++])
        if(passwordIndex > passwordArray.lastIndex) passwordIndex = 0
    }

    return outputArray.toString(Charsets.UTF_8)
}