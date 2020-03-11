package com.caesarlib.cserialhelper

import java.util.*

/**
 * 串口数据转换工具类
 * Created by Administrator on 2016/6/2.
 */
object CSerialDataUtils {
    //-------------------------------------------------------
// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    fun isOdd(num: Int): Int {
        return num and 1
    }

    //-------------------------------------------------------
//Hex字符串转int
    fun HexToInt(inHex: String): Int {
        return inHex.toInt(16)
    }

    fun IntToHex(intHex: Int): String {
        return Integer.toHexString(intHex)
    }

    //-------------------------------------------------------
//Hex字符串转byte
    fun HexToByte(inHex: String): Byte {
        return inHex.toInt(16).toByte()
    }

    //-------------------------------------------------------
//1字节转2个Hex字符
    fun Byte2Hex(inByte: Byte): String {
        return String.format("%02x", *arrayOf<Any>(inByte)).toUpperCase()
    }

    //-------------------------------------------------------
//字节数组转转hex字符串
    fun ByteArrToHex(inBytArr: ByteArray): String {
        val strBuilder = StringBuilder()
        for (valueOf in inBytArr) {
            strBuilder.append(Byte2Hex(java.lang.Byte.valueOf(valueOf)))
            strBuilder.append(" ")
        }
        return strBuilder.toString()
    }

    //-------------------------------------------------------
//字节数组转转hex字符串，可选长度
    fun ByteArrToHex(inBytArr: ByteArray, offset: Int, byteCount: Int): String {
        val strBuilder = StringBuilder()
        for (i in offset until byteCount) {
            strBuilder.append(Byte2Hex(java.lang.Byte.valueOf(inBytArr[i])))
        }
        return strBuilder.toString()
    }

    //-------------------------------------------------------
//转hex字符串转字节数组
    fun HexToByteArr(inHex: String): ByteArray {
        var inHex = inHex
        val result: ByteArray
        var hexlen = inHex.length
        if (isOdd(hexlen) == 1) {
            hexlen++
            result = ByteArray(hexlen / 2)
            inHex = "0$inHex"
        } else {
            result = ByteArray(hexlen / 2)
        }
        var j = 0
        var i = 0
        while (i < hexlen) {
            result[j] = HexToByte(inHex.substring(i, i + 2))
            j++
            i += 2
        }
        return result
    }

    /**
     * 按照指定长度切割字符串
     *
     * @param inputString 需要切割的源字符串
     * @param length      指定的长度
     * @return
     */
    fun getDivLines(
        inputString: String,
        length: Int
    ): List<String> {
        val divList: MutableList<String> =
            ArrayList()
        val remainder = inputString.length % length
        // 一共要分割成几段
        val number = Math.floor(inputString.length / length.toDouble()).toInt()
        for (index in 0 until number) {
            val childStr =
                inputString.substring(index * length, (index + 1) * length)
            divList.add(childStr)
        }
        if (remainder > 0) {
            val cStr = inputString.substring(number * length, inputString.length)
            divList.add(cStr)
        }
        return divList
    }

    /**
     * 计算长度，两个字节长度
     *
     * @param val value
     * @return 结果
     */
    fun twoByte(`val`: String): String {
        var `val` = `val`
        if (`val`.length > 4) {
            `val` = `val`.substring(0, 4)
        } else {
            val l = 4 - `val`.length
            for (i in 0 until l) {
                `val` = "0$`val`"
            }
        }
        return `val`
    }

    /**
     * 校验和
     *
     * @param cmd 指令
     * @return 结果
     */
    fun sum(cmd: String): String {
        var cmd = cmd
        val cmdList = getDivLines(cmd, 2)
        var sumInt = 0
        for (c in cmdList) {
            sumInt += HexToInt(c)
        }
        var sum = IntToHex(sumInt)
        sum = twoByte(sum)
        cmd += sum
        return cmd.toUpperCase()
    }
}