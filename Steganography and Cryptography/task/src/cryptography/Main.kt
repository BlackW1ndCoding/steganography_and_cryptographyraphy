package cryptography

import kotlin.system.exitProcess

const val EXIT = "exit"
const val HIDE = "hide"
const val SHOW = "show"
const val EXIT_MESSAGE = "Bye!"
const val HIDE_MESSAGE = "Hiding message in image."
const val SHOW_MESSAGE = "Obtaining message from image."
const val WRONG_TASK = "Wrong task: "


fun main() {
    inputHandler()
}

/**
 * This is recursive method handling user command input.
 *
 * @since 1.0
 *
 * exit - command is used to close application.
 *
 * hide - command used to initiate message concealing.
 *
 * show - command used to extract and show hidden image.
 */
fun inputHandler(){
    println("Task (hide, show, exit):")


    val input = readLine()?.trim()
""
    if (input != null) {
        when (input){
            EXIT -> {
                println(EXIT_MESSAGE)
                exitProcess(0)
            }
            HIDE -> {
                //println(HIDE_MESSAGE)
                CommandsHandler.hideCommand()
            }
            SHOW -> CommandsHandler.showCommand()
            else -> println(WRONG_TASK + input)
        }
    }
    inputHandler()
}

