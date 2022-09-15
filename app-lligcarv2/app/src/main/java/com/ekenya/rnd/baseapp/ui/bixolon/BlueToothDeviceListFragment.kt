package com.ekenya.rnd.baseapp.ui.bixolon

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.databinding.FragmentBixolonBluetoothBinding
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.utils.base.BaseCommonCargillFragment
import com.ekenya.rnd.common.utils.custom.toasty
import timber.log.Timber

class BlueToothDeviceListFragment : BaseCommonCargillFragment<FragmentBixolonBluetoothBinding>(
    FragmentBixolonBluetoothBinding::inflate
) {

    // private lateinit var binding: FragmentBixolonBluetoothBinding
    private lateinit var blueToothListAdapter: BlueToothListAdapter
    private var bluetoothDeviceActualName: String? = null
    private val bondedDeviceBlueToothData = ArrayList<BlueToothData>()
    private val mBluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        setBondedDevices()
        //
        turnOnBlueToothDevice()
    }

    private fun setBondedDevices() {
        bluetoothDeviceActualName = null
        // bondedDeviceBlueToothData.clear()
        if (mBluetoothAdapter != null) {
            val bondedDeviceSet = mBluetoothAdapter.bondedDevices
            for (device in bondedDeviceSet) {
                Timber.e("DEVICE.NAME====1= ${device.name}")
                Timber.e("DEVICE.ADDRESS====1= ${device.address}")
                bondedDeviceBlueToothData.add(
                    BlueToothData(
                        name = device.name.toString(),
                        address = device.address.toString()
                    )
                )
            }

            inflateRecyclerView(bondedDeviceBlueToothData)
        }
    }

    private fun inflateRecyclerView(itemsRV: List<BlueToothData>) {
        Timber.e("INFLATERECYCLERVIEW===== $itemsRV")
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvBlueTooth.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.rvBlueTooth.visibility = View.VISIBLE
            blueToothListAdapter =
                BlueToothListAdapter(itemsRV, onBlueToothListListener)
            binding.rvBlueTooth.apply {
                layoutManager = LinearLayoutManager(requireActivity().applicationContext!!)
                adapter = blueToothListAdapter
                setHasFixedSize(true)
            }
            blueToothListAdapter?.notifyDataSetChanged()
        }
    }

    private val onBlueToothListListener = object : OnBlueToothListListener {
        override fun onItemClicked(view: View, model: BlueToothData) {
            when (view.id) {
                R.id.rootTransaction -> {
                    findNavController().navigate(
                        BlueToothDeviceListFragmentDirections
                            .actionBlueToothDeviceListFragmentToBixolonPrinterFragment(model)
                    )
                    Timber.e(model.name.toString())
                }

                else -> {
                    toasty("FarmerHomeFragment")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_BLUETOOTH -> setBondedDevices()
            REQUEST_ENABLE_BT -> turnOnBlueToothDevice()
            /* askForPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    REQUEST_LOCATION
                )*/
        }
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!

                if (device.bondState != BluetoothDevice.BOND_BONDED) {
                    Timber.e("DEVICE.NAME===== ${device.name}")
                    Timber.e("DEVICE.ADDRESS===== ${device.address}")
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
            askForPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                REQUEST_LOCATION
            )
        }
    }

    private fun askForPermission(permission: String, requestCode: Int) {
        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), permission)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startDeviceDiscovery()
        }
    }

    private fun startDeviceDiscovery() {
        if (mBluetoothAdapter.startDiscovery()) {
            toast("Discovering other bluetooth devices")
            val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            requireActivity().registerReceiver(mReceiver, intentFilter)
        } else {
            toast("Something went wrong")
        }
    }
}
