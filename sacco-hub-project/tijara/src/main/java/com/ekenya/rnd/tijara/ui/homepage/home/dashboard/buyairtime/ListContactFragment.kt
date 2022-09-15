package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.adapters.layoutAdapter.ListContactAdapter
import com.ekenya.rnd.tijara.databinding.FragmentListContactBinding
import com.ekenya.rnd.tijara.network.model.BillerData
import com.ekenya.rnd.tijara.network.model.ContactModel
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.ContactCallBack
import com.ekenya.rnd.tijara.utils.FieldValidators
import com.ekenya.rnd.tijara.utils.showErrorSnackBar
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class ListContactFragment : Fragment(),ContactCallBack {
    lateinit var contactListAdapter: ListContactAdapter
  //  var listContact: ArrayList<ContactModel> = ArrayList()
    val PERMISSION_REQUES_CODE: Int = 999
    lateinit var searchbtn:SearchView
    private lateinit var conatctBinding:FragmentListContactBinding
    val displayList = ArrayList<ContactModel>()
    var arrayList = ArrayList<ContactModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        conatctBinding= FragmentListContactBinding.inflate(layoutInflater)
        conatctBinding.myPhone.text=Constants.PHONENUMBER
      //  searchContact()


        conatctBinding.apply {
            backButton.setOnClickListener {
                Constants
                findNavController().navigateUp()
            }



        }
        conatctBinding.clMyContact.setOnClickListener {
            findNavController().navigateUp()
        }

        requestPermission()




        return conatctBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchContact()
    }
    private fun searchContact(){
        val searchView = conatctBinding.search
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    Timber.d("SEARCHING")
                    displayList.clear()
                    conatctBinding.tvNoContacts.visibility=View.GONE
                    val search = newText.toLowerCase(Locale.US)
                    Timber.d("SEARCHING...$search")
                    /*val filteredList = arrayList.filter { it.name.toLowerCase(Locale.US).contains(search) }
                    displayList.addAll(filteredList)*/
                    arrayList.forEach {
                        if (it.name.toLowerCase(Locale.US).contains(search)) {
                            Timber.d("DISPLAYING...$it")
                            displayList.add(it)
                        }
                    }
                    if (displayList.isEmpty()){
                        conatctBinding.tvNoContacts.visibility=View.VISIBLE
                    }else{
                        conatctBinding.tvNoContacts.visibility=View.GONE
                    }
                    conatctBinding.rcvContacts.adapter?.notifyDataSetChanged()
                } else {
                    conatctBinding.tvNoContacts.visibility=View.GONE
                    displayList.clear()
                    displayList.addAll(arrayList)
                    conatctBinding.rcvContacts.adapter?.notifyDataSetChanged()
                }
                return true
            }
        })

    }
    private fun requestPermission(){
        val permission = ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.READ_CONTACTS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Timber.v("Permission to record denied")
            makePermisionRequest()
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                getContactList()

            }
        }


    }
    private fun makePermisionRequest() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUES_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUES_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    GlobalScope.launch(Dispatchers.Main) {
                        getContactList()
                    }


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    findNavController().navigateUp()
                }
                return
            }

        }
    }
   /* private suspend fun getContactList() {
        conatctBinding.loadingContacts.visibility=View.VISIBLE
        val q = Contacts.getQuery()
//            q.include(Contact.Field.DisplayName,Contact.Field.PhoneNormalizedNumber)
        q.hasPhoneNumber()
        val contacts = q.find()

        val gson = Gson()
        val listString: String = gson.toJson(
            contacts,
            object : TypeToken<ArrayList<Contact>?>() {}.type
        )
        val newArray = JSONArray(listString)
        Log.d("Contacts List", newArray.toString())
        for (contact in contacts) {
            if (contact.displayName != null) {
                for (phone in contact.phoneNumbers) {
                    val phones = phone.normalizedNumber
                    if (!phones.isNullOrEmpty()) {
                        listContact.add(
                            ContactModel(contact.displayName, phone.normalizedNumber)
                        )
                        Timber.d("NAME ::: ${contact.displayName} (${contact.displayName[0]})PHONE::: ${phone.normalizedNumber} LABEL ::: ${phone.label}")
                    } else {
                        *//**
                         * Add country code
                         *//*
                        val formatedNumber = phone.number
                            .replace("(", "")
                            .replace(")", "")
                            .replace("-", "")
                            .replace(" ", "")
                        listContact.add(
                            ContactModel(contact.displayName, formatedNumber)
                        )
                        Timber.d("NAME ::: ${contact.displayName} (${contact.displayName[0]})PHONE::: ${phone.number} LABEL ::: ${phone.label}")

                    }
                }
            }
        }
        conatctBinding.loadingContacts.visibility=View.GONE
      //  displayList.addAll(listmodel)
       // contactsArrayList.addAll(listmodel)
        contactListAdapter = ListContactAdapter(listContact)
        contactListAdapter.notifyDataSetChanged()
        conatctBinding.rcvContacts.adapter = contactListAdapter
        conatctBinding.rcvContacts.layoutManager=LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )


    }*/

    private val PROJECTION = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    private fun getContactList() {
        conatctBinding.loadingContacts.visibility=View.VISIBLE
        val cr: ContentResolver = requireActivity().contentResolver
        val cursor: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null) {
            conatctBinding.loadingContacts.visibility=View.VISIBLE
            val mobileNoSet = HashSet<String>()
            try {
                val nameIndex: Int = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex: Int =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var name: String
                var number: String
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex)
                    number = cursor.getString(numberIndex)
                    number = number.replace(" ", "")
                    if (!mobileNoSet.contains(number)) {
                        val contact=FieldValidators().formatContact(number)
                        displayList.add(ContactModel(name, contact))
                        arrayList.add(ContactModel(name, contact))
                        Timber.d("CONTTTTTACT $contact")
                        mobileNoSet.add(number)
                        Log.d(
                            "hvy", "onCreaterrView  Phone Number: name = " + name
                                    + " No = " + number
                        )
                    }
                }
            } finally {
                cursor.close()

                contactListAdapter = ListContactAdapter(displayList,this)
                conatctBinding.rcvContacts.adapter = contactListAdapter
                conatctBinding.rcvContacts.layoutManager=LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                conatctBinding.loadingContacts.visibility=View.GONE
            }
        }
    }

    override fun onItemSelected(cont: ContactModel) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("contact", cont.phonenumber)


        findNavController().navigateUp()
    }


}
