package longcode

class LongcodeComputer(val instructions: MutableList<Long>, var firstInput: Int = 1) {
    var inputCount = 0
    var ip = 0
    var relativeBase = 0L
    val codes = instructions.mapIndexed {index, l ->  Pair(index, l)}.toMap()

    fun compute(): Long {
        padInstructions()
        var i = 0

        while (i < instructions.size) {
            val opCode = Opcode(instructions[i])

            when(opCode.opcode) {
                99 -> return instructions[0]
                1 -> { plus(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getPoint(opCode.p3, i + 3).toInt()); i+=4 }
                2 -> { multi(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getPoint(opCode.p3, i + 3).toInt()); i+=4 }
                3 -> { input( getPoint(opCode.p1, i + 1).toInt()); i+=2 }
                4 -> { println(output(getPoint(opCode.p1, i + 1).toInt())); i+=2 }
                5 -> i = jumpIfTrue(i, opCode)
                6 -> i = jumpIfFalse(i, opCode)
                7 -> { lessThan(i, opCode); i+=4 }
                8 -> { equals(i, opCode); i+=4 }
                9 -> {relativeBase += getValue(opCode.p1, instructions[i + 1]); i+=2 }
            }
        }
        return instructions[0]
    }

    private fun padInstructions() {
        while (instructions.size < 10000000) {
            instructions.add(0)
        }
    }


    fun getValue(mode: Int, value: Long): Long {
        if (value > Int.MAX_VALUE) {
            println("Too big! : " + value)
        }

        return when (mode) {
            0 -> instructions[value.toInt()]
            1 -> value
            2 -> instructions[value.toInt() + relativeBase.toInt()]  //may need to move relative base for things other than output
            else -> value
        }
    }

    fun getPoint(mode: Int, value: Int): Long {
        val v =  when (mode) {
            0 -> instructions[value]
            1 -> value.toLong()
            2 -> instructions[value] + relativeBase //may need to move relative base for things other than output
            else -> value.toLong()
        }


        if (v > Int.MAX_VALUE) {
            println("Too big! : " + v)
        }
        return v
    }

    private fun lessThan(i: Int, mode: Opcode) {
        if (getValue(mode.p1, instructions[i + 1]) < getValue(mode.p2, instructions[i + 2])) {
            instructions[getValue(mode.p3, i + 3L).toInt()] = 1
        } else {
            instructions[getValue(mode.p3, i + 3L).toInt()] = 0
        }
    }

    private fun equals(i: Int, mode: Opcode) {
        if (getValue(mode.p1, instructions[i + 1]) == getValue(mode.p2, instructions[i + 2])) {
            instructions[getValue(mode.p3, i + 3L).toInt()] = 1
        } else {
            instructions[getValue(mode.p3, i + 3L).toInt()] = 0
        }
    }

    private fun jumpIfTrue(i: Int, mode: Opcode): Int {
        if (getValue(mode.p1, instructions[i + 1]) == 0L) {
            return i + 3
        }
        return getValue(mode.p2, instructions[i + 2]).toInt()
    }

    private fun jumpIfFalse(i: Int, mode: Opcode): Int {
        if (getValue(mode.p1, instructions[i + 1]) != 0L) {
            return i + 3
        }
        return getValue(mode.p2, instructions[i + 2]).toInt()
    }

    fun plus(a: Long, b: Long, pos: Int) {
        instructions[pos] = a + b
    }

    fun multi(a: Long, b: Long, pos: Int) {
        instructions[pos] = a * b
    }

    fun input(pos: Int, input: Int = 1) {
        instructions[pos] = if (inputCount == 0)  firstInput.toLong() else input.toLong()
        inputCount++
    }

    fun output(pos: Int): Long {
        return instructions[pos]
    }

}

data class Opcode(val input: Long) {
    val opcode: Int = input.toString().padStart(5, '0').substring(3).toInt()
    val p1: Int = input.toString().padStart(5, '0').toCharArray()[2].toString().toInt()
    val p2: Int = input.toString().padStart(5, '0').toCharArray()[1].toString().toInt()
    val p3: Int = input.toString().padStart(5, '0').toCharArray()[0].toString().toInt()
}

data class Output(val lastOutput: Long, val halt: Long?)