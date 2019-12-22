package intcode

class IntcodeComputer(val instructions: MutableList<Int>, val input: Int = 1, var firstInput: Int = 1) {

    public fun compute(): Int {
        var i = 0

        while (i < instructions.size) {
            val opCode = opCode(instructions[i])

            when(opCode.opcode) {
                99 -> return instructions[0]
                1 -> { plus(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getValue(opCode.p3, i + 3)); i+=4 }
                2 -> { multi(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getValue(opCode.p3, i + 3)); i+=4 }
                3 -> { input( getValue(opCode.p1, i + 1)); i+=2 }
                4 -> { output(getValue(opCode.p1, i + 1)); i+=2 }
                5 -> i = jumpIfTrue(i, opCode)
                6 -> i = jumpIfFalse(i, opCode)
                7 -> { lessThan(i, opCode); i+=4 }
                8 -> { equals(i, opCode); i+=4 }
            }
        }
        return instructions[0]
    }

    public fun computeOut(): Int {
        var i = 0

        while (i < instructions.size) {
            val opCode = opCode(instructions[i])

            when(opCode.opcode) {
                99 -> return instructions[0]
                1 -> { plus(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getValue(opCode.p3, i + 3)); i+=4 }
                2 -> { multi(getValue(opCode.p1, instructions[i + 1]), getValue(opCode.p2, instructions[i + 2]), getValue(opCode.p3, i + 3)); i+=4 }
                3 -> { input( getValue(opCode.p1, i + 1)); i+=2 }
                4 -> return output(getValue(opCode.p1, i + 1))
                5 -> i = jumpIfTrue(i, opCode)
                6 -> i = jumpIfFalse(i, opCode)
                7 -> { lessThan(i, opCode); i+=4 }
                8 -> { equals(i, opCode); i+=4 }
            }
        }
        return instructions[0]
    }

    private fun lessThan(i: Int, mode: opCode) {
        if (getValue(mode.p1, instructions[i + 1]) < getValue(mode.p2, instructions[i + 2])) {
            instructions[getValue(mode.p3, i + 3)] = 1
        } else {
            instructions[getValue(mode.p3, i + 3)] = 0
        }
    }

    private fun equals(i: Int, mode: opCode) {
        if (getValue(mode.p1, instructions[i + 1]) == getValue(mode.p2, instructions[i + 2])) {
            instructions[getValue(mode.p3, i + 3)] = 1
        } else {
            instructions[getValue(mode.p3, i + 3)] = 0
        }
    }

    private fun jumpIfTrue(i: Int, mode: opCode): Int {
        if (getValue(mode.p1, instructions[i + 1]) == 0) {
            return i + 3
        }
        return getValue(mode.p2, instructions[i + 2])
    }

    private fun jumpIfFalse(i: Int, mode: opCode): Int {
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

    fun input(pos: Int) {
        instructions[pos] = firstInput
        firstInput = input
    }

    fun output(pos: Int): Int {
        println(instructions[pos])
        return instructions[pos]
    }

    fun getValue(mode: Int, value: Int): Int {
        return when (mode) {
            0 -> instructions[value]
            1 -> value
            else -> value
        }
    }

    fun MutableList<Int>.runOp(start : Int, op: (Int, Int) -> Int) {
        val result = op(this.getVal(start + 1), this.getVal(start + 2))
        this[this[start + 3]] = result
    }


    fun List<Int>.getVal(index: Int) = this[this[index]]

}

data class opCode(val input: Int) {
    val opcode: Int = input.toString().padStart(5, '0').substring(3).toInt()
    val p1: Int = input.toString().padStart(5, '0').toCharArray()[2].toString().toInt()
    val p2: Int = input.toString().padStart(5, '0').toCharArray()[1].toString().toInt()
    val p3: Int = input.toString().padStart(5, '0').toCharArray()[0].toString().toInt()
}