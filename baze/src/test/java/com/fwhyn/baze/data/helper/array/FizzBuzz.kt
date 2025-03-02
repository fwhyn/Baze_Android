
fun main(args: Array<String>) {

    for (i in 1 .. 100) {
        var output = ""
        
        if (i % 3 == 0) {
            output += "Fizz"
        }
        
        if (i % 5 == 0) {
            output += "Buzz"
        }
        
        if (i % 7 == 0) {
            output += "Bazz"
        }
        
        println(if(output == "") i else output)
    }

}