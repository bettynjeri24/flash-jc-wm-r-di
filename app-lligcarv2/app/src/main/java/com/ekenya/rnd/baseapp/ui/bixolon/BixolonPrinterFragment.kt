package com.ekenya.rnd.baseapp.ui.bixolon

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.bxl.config.editor.BXLConfigLoader
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.databinding.FragmentBixolonPrinterBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillFragment
import jpos.JposException
import jpos.POSPrinter
import jpos.POSPrinterConst
import jpos.config.JposEntry
import org.joda.time.DateTime
import timber.log.Timber
import java.io.InputStream
import java.nio.ByteBuffer
import java.time.ZonedDateTime

class BixolonPrinterFragment : BaseCommonCargillFragment<FragmentBixolonPrinterBinding>(
    FragmentBixolonPrinterBinding::inflate
) {

    private lateinit var bxlConfigLoader: BXLConfigLoader
    private lateinit var posPrinter: POSPrinter
    private val args: BixolonPrinterFragmentArgs by navArgs()

    private var brightness = 50

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showReceiptToUser()
        onClickButtons()
        loadBXLConfig()
    }

    private fun showReceiptToUser() {
        val message = StringBuilder()
        message.append("\t\t\t\t***** Customer Copy *****\t\t\t\t")
        message.append("\n")
        message.append("\t\t\tCargill Digital Payment \t\t\t")
        message.append("\n")
        message.append("\t\t\t Code:BASAM \t\t\t")
        message.append("\t DATE\tTIME\tTERMINAL ID \t")
        val now = DateTime.now()

        message.append("\t ${now}\t${now}\tTERMINAL ID \t")

        binding.tvReceiptTitle.text = message
    }

    private fun onClickButtons() {
        binding.buttonOpenPrinter.setOnClickListener {
            openPrinter()
        }
        binding.buttonPrint.setOnClickListener {
            printTransactionData()
        }
        binding.buttonClosePrinter.setOnClickListener {
            closePrinter()
        }
        binding.seekBarBrightness.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.textViewProgress.text = Integer.toString(progress)
                    brightness = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // you can probably leave this empty
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // you can probably leave this empty
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        closePrinter()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.ACCESS_COARSE_LOCATION) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        // showPairedDevices()
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                            REQUEST_LOCATION
                        )
                    }
                }
            }
        }
    }

    private fun setUpBxlConfigLoaderConfiguration(
        actualDeviceName: String,
        address: String
    ) {
        try {
            for (entry in bxlConfigLoader.entries) {
                val jposEntry = entry as JposEntry
                bxlConfigLoader.removeEntry(jposEntry.logicalName)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error removing entry.", e)
        }
        try {
            bxlConfigLoader.addEntry(
                actualDeviceName,
                BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER,
                actualDeviceName,
                BXLConfigLoader.DEVICE_BUS_BLUETOOTH,
                address
            )
            bxlConfigLoader!!.saveFile()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving file.", e)
        }
    }

    private fun loadBXLConfig() {
        bxlConfigLoader = BXLConfigLoader(requireActivity().applicationContext)
        try {
            bxlConfigLoader.openFile()
        } catch (e: Exception) {
            Timber.e(e.message)
            bxlConfigLoader.newFile()
        }
        posPrinter = POSPrinter(requireActivity().applicationContext)

        setUpBxlConfigLoaderConfiguration(
            actualDeviceName = args.blueToothData.name,
            address = args.blueToothData.address
        )
    }

    private fun setProductName(name: String): String {
        if (name.contains("SPP-R200II")) {
            if (name.length > 10) {
                if (name.substring(10, 11) == "I") {
                    return BXLConfigLoader.PRODUCT_NAME_SPP_R200III
                }
            }
        } else if (name.contains("SPP-R210")) {
            return BXLConfigLoader.PRODUCT_NAME_SPP_R210
        } else if (name.contains("SPP-R310")) {
            return BXLConfigLoader.PRODUCT_NAME_SPP_R310
        } else if (name.contains("SPP-R300")) {
            return BXLConfigLoader.PRODUCT_NAME_SPP_R300
        } else if (name.contains("SPP-R400")) {
            return BXLConfigLoader.PRODUCT_NAME_SPP_R400
        }
        return BXLConfigLoader.PRODUCT_NAME_SPP_R200II
    }

    private fun openPrinter(bluetoothDeviceActualName: String? = null) {
        try {
            posPrinter.open("SPP-R310")
            Timber.e("\n\n BLUETOOTHDEVICENAME ====================> $bluetoothDeviceActualName")
            // posPrinter!!.open(bluetoothDeviceActualName)
            posPrinter.claim(2000)
            posPrinter.deviceEnabled = true
        } catch (e: JposException) {
            Timber.e("Error opening printer.", e.message.toString())
            Toast.makeText(requireActivity(), e.message.toString(), Toast.LENGTH_SHORT).show()
            closePrinter()
        }
    }

    private fun closePrinter() {
        try {
            posPrinter.close()
        } catch (e: JposException) {
            Timber.e("Error closing printer.", e)
            Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildBuffer(): ByteBuffer {
        val buffer = ByteBuffer.allocate(4)
        buffer.put(POSPrinterConst.PTR_S_RECEIPT.toByte())
        buffer.put(brightness.toByte())
        val compress = 1
        buffer.put(compress.toByte())
        buffer.put(0x00.toByte())
        return buffer
    }

    private fun closeInputStream(inputStream: InputStream?) {
        if (inputStream != null) {
            try {
                inputStream.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing input stream.", e)
            }
        }
    }

    @Throws(JposException::class)
    private fun printTransactionData() {
        try {
            val stringBodyToPrint = ArrayList<String>()
            val printerDataList: List<PrinterData> = printerDataDummyData()

            stringBodyToPrint.add(
                """
                --------------------------
                
                """.trimIndent()
            )
            for (printerData in printerDataList) {
                stringBodyToPrint.add(
                    """
                    ${printerData.printRow}
                    
                    """.trimIndent()
                )
            }
            val headerArray: Array<String> = getHeader("Customer Copy")
            val footerArray: Array<String> =
                getFooter()

            var bodyTextArray = arrayOfNulls<String>(stringBodyToPrint.size)
            bodyTextArray = stringBodyToPrint.toArray(bodyTextArray)

            val buffer = buildBuffer()

            // ADDING BITMAP
            var inputStream: InputStream? = null
            inputStream = resources.openRawResource(R.raw.logotwo)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            posPrinter!!.printBitmap(
                buffer.getInt(0),
                bitmap,
                posPrinter!!.recLineWidth,
                POSPrinterConst.PTR_BM_LEFT
            )
            // ADDING HEADER
            for (headerData in headerArray) {
                Timber.e("HEADER DATA ======= > $headerData HEADER ARRAY ======= > $headerArray")
                posPrinter!!.printNormal(buffer.getInt(0), headerData)
            }
            // ADDING BODY
            for (bodyString in bodyTextArray) {
                posPrinter!!.printNormal(buffer.getInt(0), bodyString)
            }
            // ADDING FOOTER
            for (footerData in footerArray) {
                posPrinter!!.printNormal(buffer.getInt(0), footerData)
            }
            // hide the printer button and customer copy issue
        } catch (e: java.lang.Exception) {
        }
    }

    private fun getHeader(copyOwner: String): Array<String> {
        return arrayOf(
            centeredText(
                "***** $copyOwner *****"
            ) + "\n",
            "\n",
            centeredText("Cargill Digital Payment") + "\n",
            centeredText("Code: " + "BASAM") + "\n",
            "\n",
            """
            DATE         TIME        TERMINAL ID
            """.trimIndent() + "\n",
            "${ZonedDateTime.now()}   10:48          1234QWAER" + "\n",
            """
            TRAN NUM:  5225152525
            
            """.trimIndent(),
            """
     SECTION: BASAM
    
            """.trimIndent(),
            "${getSeparator()}"
        )
    }

    private fun getSeparator(): String? {
        return "--------------------------------"
    }

    private fun getFooter(): Array<String> {
        return arrayOf(
            "${getSeparator()}",
            "\n",
            """
     Served by: ${"DANIEL"}
     
            """.trimIndent(),
            """
            Powered by: Cargill Co.
            
            """.trimIndent(),
            "${getSeparator()}",
            "\n",
            "\n"
        )
    }

    private fun centeredText(text: String): String {
        var text = text
        if (TextUtils.isEmpty(text)) {
            return ""
        }
        val textLength = text.length
        val paperSize = 48
        return if (textLength > paperSize) {
            val compactText = text.substring(0, paperSize)
            val lastSpaceIndex = compactText.lastIndexOf(" ")
            if (lastSpaceIndex <= 0) {
                """
     $compactText
     ${centeredText(text.substring(paperSize, textLength))}
                """.trimIndent()
            } else {
                """
     ${centeredText(compactText.substring(0, lastSpaceIndex))}
     ${centeredText(text.substring(lastSpaceIndex + 1, textLength))}
                """.trimIndent()
            }
        } else if (textLength == paperSize) {
            text
        } else {
            val whiteSpaceLength = paperSize - textLength
            val padding = whiteSpaceLength / 2
            for (i in 0 until padding) {
                text = " $text"
            }
            text
        }
    }

    @Throws(JposException::class)
    private fun buyerPrinterCopy() {
        try {
            val printRows = java.util.ArrayList<String>()
            val plist: List<PrinterData> = printerDataDummyData()
            printRows.add(
                """
                -------------------------------------------
                
                """.trimIndent()
            )
            for (pdata in plist) {
                printRows.add(
                    """
                    ${pdata.printRow}
                    
                    """.trimIndent()
                )
            }
            val headerArray: Array<String> =
                getHeader("Buyer Copy")
            val footerArray: Array<String> =
                getFooter()
            var stringArray = arrayOfNulls<String>(printRows.size)
            stringArray = printRows.toArray(stringArray)
            val buffer = buildBuffer()
            var inputStream: InputStream? = null
            inputStream = resources.openRawResource(R.raw.logotwo)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            posPrinter!!.printBitmap(
                buffer.getInt(0),
                bitmap,
                posPrinter!!.recLineWidth,
                POSPrinterConst.PTR_BM_LEFT
            )
            for (hh in headerArray) {
                posPrinter!!.printNormal(buffer.getInt(0), hh)
            }
            for (hh in stringArray) {
                posPrinter!!.printNormal(buffer.getInt(0), hh)
            }
            for (hh in footerArray) {
                posPrinter!!.printNormal(buffer.getInt(0), hh)
            }
        } catch (e: java.lang.Exception) {
        }
    }

    private fun printerDataDummyData(): List<PrinterData> {
        val print1 = PrinterData("Name \t\t:\tDaniel Kimani", 1, 1, true)
        val print2 = PrinterData("Phone Number\t:\t2250798997967", 1, 1, true)
        val print3 = PrinterData("Amount \t\t:\t23000", 1, 1, true)
        val print4 = PrinterData("Payment type \t:\tPAYMENTTYPE", 1, 1, true)
        val print5 = PrinterData("Reason \t\t:\tPay farmer", 1, 1, true)

        val listdata: List<PrinterData> = listOf(print1, print2, print3, print4, print5)
        return listdata
    }
}
