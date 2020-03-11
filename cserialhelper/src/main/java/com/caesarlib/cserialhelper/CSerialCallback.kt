package com.caesarlib.cserialhelper

/**
 * Created by Caesar
 * email : caesarshao@163.com
 */
interface CSerialCallback {
    fun onReceiveData(data: ByteArray)

    fun onSendData(data: ByteArray, tag: Int)
    fun onSendComplete(data: ByteArray, tag: Int)
}