package com.ekenya.rnd.baseapp.ui.bixolon

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ekenya.rnd.baseapp.databinding.ActivityBixolonBinding

class BixolonPrinterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBixolonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBixolonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setUpUi()
    }

    /*
    private val bondedDevices = ArrayList<CharSequence>()
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>
    private lateinit var mBluetoothAdapter: BluetoothAdapter

    //
    private lateinit var bxlConfigLoader: BXLConfigLoader
    private lateinit var posPrinter: POSPrinter
    private var bluetoothDeviceActualName: String? = null
    private var brightness = 50

    private fun setUpUi() {
         mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
         // mBluetoothAdapter.startDiscovery()

         onClickButtons()
         //
         showPairedDevices()
         setBondedDevices()
         loadBXLConfig()
         //
         turnOnBlueToothDevice()
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

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         when (requestCode) {
             REQUEST_CODE_BLUETOOTH -> setBondedDevices()
             REQUEST_ENABLE_BT -> turnOnBlueToothDevice()

        }
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

    private fun showPairedDevices() {
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            bondedDevices
        )
        binding.listViewPairedDevices.adapter = arrayAdapter
        binding.listViewPairedDevices.choiceMode = ListView.CHOICE_MODE_SINGLE

        binding.listViewPairedDevices.setOnItemClickListener { parent, view, position, id ->
            val device = (view as TextView).text.toString()

            val name = device.substring(0, device.indexOf(DEVICE_ADDRESS_START))

            val address = device.substring(
                device.indexOf(DEVICE_ADDRESS_START) +
                    DEVICE_ADDRESS_START.length,
                device.indexOf(DEVICE_ADDRESS_END)
            )
            Timber.e("BLUETOOTH ADDRESS ====> $address")
            Timber.e("BLUETOOTH NAME  ====> $name")

            bluetoothDeviceActualName = setProductName(name)
            setUpBxlConfigLoaderConfiguration(bluetoothDeviceActualName!!, address)
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
            Timber.e("Error removing entry.", e)
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
            Timber.e("Error saving file.", e)
        }
    }

    private fun loadBXLConfig() {
        bxlConfigLoader = BXLConfigLoader(this)
        try {
            bxlConfigLoader.openFile()
        } catch (e: Exception) {
            Timber.e(e.message)
            bxlConfigLoader.newFile()
        }
        posPrinter = POSPrinter(this)
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

    private fun setBondedDevices() {
        bluetoothDeviceActualName = null
        bondedDevices.clear()
        if (mBluetoothAdapter != null) {
            val bondedDeviceSet = mBluetoothAdapter.bondedDevices
            for (device in bondedDeviceSet) {
                bondedDevices.add(
                    device.name + DEVICE_ADDRESS_START +
                        device.address + DEVICE_ADDRESS_END
                )
            }
            if (arrayAdapter != null) {
                arrayAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun openPrinter() {
        try {
            posPrinter.open("SPP-R310")
            Timber.e("\n\n BLUETOOTHDEVICENAME ====================> $bluetoothDeviceActualName")
            // posPrinter!!.open(bluetoothDeviceActualName)
            posPrinter.claim(2000)
            posPrinter.deviceEnabled = true
        } catch (e: JposException) {
            Timber.e("Error opening printer.", e.message.toString())
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            closePrinter()
        }
    }

    private fun closePrinter() {
        try {
            posPrinter.close()
        } catch (e: JposException) {
            Timber.e("Error closing printer.", e)
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
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
            "${java.time.LocalDate.now()}   10:48          1234QWAER" + "\n",
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

    private fun getPermission() {
        ActivityCompat.requestPermissions(
            this@BixolonPrinterActivity,
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN
            ),
            1
        )
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

    companion object {
        private const val TAG = "TAG"
        private const val REQUEST_CODE_BLUETOOTH = 1
        private const val REQUEST_ENABLE_BT = 101
        private const val REQUEST_LOCATION = 102
        private const val DEVICE_ADDRESS_START = " ("
        private const val DEVICE_ADDRESS_END = ")"
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!

                if (device.bondState != BluetoothDevice.BOND_BONDED) {
                    bondedDevices.add(device.name)
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                toast("DEVICE DISCOVERY ENDED")
            }
        }
    }

    private fun turnOnBlueToothDevice() {
        if (mBluetoothAdapter == null) {
            toast("Bluetooth not supported")
        }
        if (!mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            askForPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_LOCATION)
        }
    }

    private fun askForPermission(permission: String, requestCode: Int) {
        val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startDeviceDiscovery()
        }
    }

    private fun startDeviceDiscovery() {
        if (mBluetoothAdapter.startDiscovery()) {
            toast("Discovering other bluetooth devices")
            val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            this.registerReceiver(mReceiver, intentFilter)
        } else {
            toast("Something went wrong")
        }
    }*/
}

