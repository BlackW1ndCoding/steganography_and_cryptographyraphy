package cryptography

object CommandsHandler {
    private const val FILE_PATTERN = ".+\\.png"

    fun hideCommand() {
        val fileRegex = FILE_PATTERN.toRegex()
        var inputPath = ""
        var outputPath = ""
        var message = ""

        //read input file
        fun enterInput(){
            println("Input image file:")
            val input = readLine()!!
            if (input.matches(fileRegex)) {
                inputPath = input
            }else{
                println("Wrong input file format!!")
                enterInput()
            }
        }
        //read output file
        fun enterOutput(){
            println("Output image file:")
            val output = readLine()!!
            if (output.matches(fileRegex)) {
                outputPath = output
            }else{
                println("Wrong output file format!!")
                enterOutput()
            }
        }
        //read message
        fun readMessage(){
            println("Message to hide:")
            message = readLine().toString()
        }

        enterInput()
        enterOutput()
        readMessage()
//        println("Input image: $inputPath")
//        println("Output image: $outputPath")

        hide(inputPath, outputPath, message)


    }

    fun showCommand(){
        val fileRegex = FILE_PATTERN.toRegex()
        var inputPath = ""

        fun enterInput(){
            println("Input image file:")
            inputPath = readLine()!!
            if (inputPath.matches(fileRegex)){
                println("Message:")
                println(show(inputPath))
            }else{
                println("Incorrect file name!!")
                showCommand()
            }
        }
        enterInput()
    }
}