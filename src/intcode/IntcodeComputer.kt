package intcode

class IntcodeComputer(val instructions: MutableList<Int>, var firstInput: Int = 1) {
    var inputCount = 0
    var ip = 0
    var relativeBase = 0

    fun compute(): Int {
        var i = 0

        while (i < instructions.size) {
            val opCode = Opcode(instructions[i])

            when(opCode.opcode) {
                99 -> return instructions[0]
                1 -> { plus(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getValue(opCode.p3, i + 3)); i+=4 }
                2 -> { multi(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getValue(opCode.p3, i + 3)); i+=4 }
                3 -> { input( getValue(opCode.p1, i + 1)); i+=2 }
                4 -> { println(output(getValue(opCode.p1, i + 1))); i+=2 }
                5 -> i = jumpIfTrue(i, opCode)
                6 -> i = jumpIfFalse(i, opCode)
                7 -> { lessThan(i, opCode); i+=4 }
                8 -> { equals(i, opCode); i+=4 }
                9 -> {relativeBase += getValue(opCode.p1, ip + 1); ip+=2 }
            }
        }
        return instructions[0]
    }

    fun computeOut(input: Int = 1): Output {

        while (ip < instructions.size) {
            val opCode = Opcode(instructions[ip])

            when(opCode.opcode) {
                99 -> return Output(0, instructions[ip])
                1 -> { plus(getValue(opCode.p1, instructions[ip + 1]), getValue(opCode.p2, instructions[ip + 2]), getValue(opCode.p3, ip + 3)); ip+=4 }
                2 -> { multi(getValue(opCode.p1, instructions[ip + 1]), getValue(opCode.p2, instructions[ip + 2]), getValue(opCode.p3, ip + 3)); ip+=4 }
                3 -> { input( getValue(opCode.p1, ip + 1), input); ip+=2 }
                4 -> { val out =  Output(output(getValue(opCode.p1, ip + 1)), null); ip+=2; return out }
                5 -> ip = jumpIfTrue(ip, opCode)
                6 -> ip = jumpIfFalse(ip, opCode)
                7 -> { lessThan(ip, opCode); ip+=4 }
                8 -> { equals(ip, opCode); ip+=4 }
                9 -> {relativeBase += getValue(opCode.p1, ip + 1); ip+=2 }
            }
        }

        return Output(0, instructions[ip])
    }

    private fun lessThan(i: Int, mode: Opcode) {
        if (getValue(mode.p1, instructions[i + 1]) < getValue(mode.p2, instructions[i + 2])) {
            instructions[getValue(mode.p3, i + 3)] = 1
        } else {
            instructions[getValue(mode.p3, i + 3)] = 0
        }
    }

    private fun equals(i: Int, mode: Opcode) {
        if (getValue(mode.p1, instructions[i + 1]) == getValue(mode.p2, instructions[i + 2])) {
            instructions[getValue(mode.p3, i + 3)] = 1
        } else {
            instructions[getValue(mode.p3, i + 3)] = 0
        }
    }

    private fun jumpIfTrue(i: Int, mode: Opcode): Int {
        if (getValue(mode.p1, instructions[i + 1]) == 0) {
            return i + 3
        }
        return getValue(mode.p2, instructions[i + 2])
    }

    private fun jumpIfFalse(i: Int, mode: Opcode): Int {
        if (getValue(mode.p1, instructions[i + 1]) != 0) {
            return i + 3
        }
        return getValue(mode.p2, instructions[i + 2])
    }

    fun plus(a: Int, b: Int, pos: Int) {
        instructions[pos] = a + b
    }

    fun multi(a: Int, b: Int, pos: Int) {
        instructions[pos] = a * b
    }

    fun input(pos: Int, input: Int = 1) {
        instructions[pos] = if (inputCount == 0)  firstInput else input
        inputCount++
    }

    fun output(pos: Int): Int {
        return instructions[pos]
    }

    fun getValue(mode: Int, value: Int): Int {
        return when (mode) {
            0 -> instructions[value]
            1 -> value
            2 -> instructions[value + relativeBase]
            else -> value
        }
    }

}

data class Opcode(val input: Int) {
    val opcode: Int = input.toString().padStart(5, '0').substring(3).toInt()
    val p1: Int = input.toString().padStart(5, '0').toCharArray()[2].toString().toInt()
    val p2: Int = input.toString().padStart(5, '0').toCharArray()[1].toString().toInt()
    val p3: Int = input.toString().padStart(5, '0').toCharArray()[0].toString().toInt()
}

data class Output(val lastOutput: Int, val halt: Int?)