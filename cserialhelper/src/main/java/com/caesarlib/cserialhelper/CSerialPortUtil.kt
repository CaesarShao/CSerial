package com.caesarlib.cserialhelper

import android.util.Log
import android_serialport_api.SerialPort
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * @author by AllenJ on 2018/4/20.
 *
 * 通过串口用于接收或发送数据
 */
class CSerialPortUtil {
    private var serialPort: SerialPort? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var mReceiveThread: ReceiveThread? = null
    private var isStart = false
    private var cserialCallback: CSerialCallback? = null
    /**
     * 打开串口，接收数据
     * 通过串口，接收单片机发送来的数据
     */
    fun openSerialPort(path: String? = "/dev/ttyS3",portRate:Int=9600) {
        startSerialPort(path,portRate)
        getSerialPort()
    }


    fun openSerialPortSync(path: String? = "/dev/ttyS3",portRate:Int=9600) {
        startSerialPort(path,portRate)
        runingStream()
    }

    private fun startSerialPort(path: String? = "/dev/ttyS3",portRate:Int=9600) {
        try {
            serialPort = SerialPort(File(path), portRate, 0)
            //调用对象SerialPort方法，获取串口中"读和写"的数据流
            inputStream = serialPort!!.inputStream
            outputStream = serialPort!!.outputStream
            isStart = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onCSerialCallBackListener(cserialCallback: CSerialCallback?) {
        this.cserialCallback = cserialCallback
    }

    /**
     * 发送数据
     * 通过串口，发送数据到单片机
     *
     * @param data 要发送的数据
     */
    fun sendSerialPort(data: String, tag: Int = 0) {
        val sendData = CSerialDataUtils.HexToByteArr(data)
        try {
            cserialCallback?.onSendData(sendData, tag)
            outputStream!!.write(sendData)
            outputStream!!.flush()
            cserialCallback?.onSendComplete(sendData, tag)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getSerialPort() {
        if (mReceiveThread == null) {
            mReceiveThread = ReceiveThread()
        }
        mReceiveThread!!.start()
    }

    /**
     * 接收串口数据的线程
     */
    private inner class ReceiveThread : Thread() {

        override fun run() {
            super.run()
            //条件判断，只要条件为true，则一直执行这个线程
            runingStream()
        }
    }

    fun runingStream() {
        while (isStart) {
            if (inputStream == null) {
                return
            }
            val readData = ByteArray(256)
            try {
                val size = inputStream!!.read(readData)
                if (size > 0) {
                    val sendData = ByteArray(size)
                    for (i in 0 until size) {
                        sendData[i] = readData[i]
                    }
                    cserialCallback?.onReceiveData(sendData)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 关闭串口
     * 关闭串口中的输入输出流
     */
    fun closeSerialPort() {
        Log.i("test", "关闭串口")
        try {
            isStart = false
            if (inputStream != null) {
                inputStream!!.close()
            }
            if (outputStream != null) {
                outputStream!!.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
