package cryptography

object CommandsHandler {
    private const val FILE_PATTERN = ".+\\.png"
    private const val PASSWORD_PATTERN = "\\w+"

    fun hideCommand() {
        val fileRegex = FILE_PATTERN.toRegex()
        val passwordRegex = PASSWORD_PATTERN.toRegex()
        var inputPath = ""
        var outputPath = ""
        var message = ""
        var password = ""

        //read input file
        fun enterInput() {
            println("Input image file:")
            val input = readLine()!!
            if (input.matches(fileRegex)) {
                inputPath = input
            } else {
                println("Wrong input file format!!")
                enterInput()
            }
        }

        //read output file
        fun enterOutput() {
            println("Output image file:")
            val output = readLine()!!
            if (output.matches(fileRegex)) {
                outputPath = output
            } else {
                println("Wrong output file format!!")
                enterOutput()
            }
        }

        //read message
        fun readMessage() {
            println("Message to hide:")
            message = readLine().toString()
        }

        //read password
        fun readPassword() {
            println("Password:")
                password = readln()
        }
        enterInput()
        enterOutput()
        readMessage()
        readPassword()
//        println("Input image: $inputPath")
//        println("Output image: $outputPath")

        hide(inputPath, outputPath, message, password)
    }

    fun showCommand() {
        val fileRegex = FILE_PATTERN.toRegex()
        val passwordRegex = PASSWORD_PATTERN.toRegex()
        var inputPath = ""
        var password = ""

        fun enterInput() {
            println("Input image file:")
            inputPath = readLine()!!

            if (!inputPath.matches(fileRegex)) {
                println("Incorrect file name!!")
                showCommand()
            }

            println("Password:")
            password = readln()
            println("Message:")
            println(show(inputPath, password))
        }
        enterInput()
    }
}