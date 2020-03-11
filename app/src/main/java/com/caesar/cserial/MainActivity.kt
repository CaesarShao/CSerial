package com.caesar.cserial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.caesarlib.cserialhelper.CSerialCallback
import com.caesarlib.cserialhelper.CSerialDataUtils
import com.caesarlib.cserialhelper.CSerialPortUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UartdeCtrlUtil.switchUartde(UartdeCtrlUtil.SOURCE_RECEIVE)
        val port = CSerialPortUtil()

        Thread {
            port.openSerialPortSync()
        }.start()
        port.onCSerialCallBackListener(object : CSerialCallback {
            override fun onReceiveData(data: ByteArray) {
                val readString = CSerialDataUtils.ByteArrToHex(data, 0, data.size)
                Log.i("caesar", "收到了数据:" + readString)
            }

            override fun onSendData(data: ByteArray, tag: Int) {
                val readString = CSerialDataUtils.ByteArrToHex(data, 0, data.size)
                Log.i("caesar", "发送数据:" + readString)
            }

            override fun onSendComplete(data: ByteArray, tag: Int) {
                val readString = CSerialDataUtils.ByteArrToHex(data, 0, data.size)
                Log.i("caesar", "发送完成数据:" + readString)
                Log.i("caesar", "线程:" + Thread.currentThread().name)

                Thread {
                    Thread.sleep(30)
                    UartdeCtrlUtil.switchUartde(UartdeCtrlUtil.SOURCE_RECEIVE)
                }.start()
            }

        })

        findViewById<Button>(R.id.button).setOnClickListener {
            UartdeCtrlUtil.switchUartde(UartdeCtrlUtil.SOURCE_SEND)
            port.sendSerialPort("FA00020210071BFE")

        }
    }
}
