package io.eclectics.cargilldigital.utils.dk

import android.content.Context
import android.graphics.PorterDuff
import android.util.SparseArray
import android.view.inputmethod.InputConnection
import android.view.LayoutInflater
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData

import io.eclectics.cargilldigital.R

class MyCustomKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var button4: Button? = null
    private var button5: Button? = null
    private var button6: Button? = null
    private var button7: Button? = null
    private var button8: Button? = null
    private var button9: Button? = null
    private var button0: Button? = null
    private var buttonDelete: ImageView? = null
    private var buttonEnter: ImageView? = null
    private val keyValues = SparseArray<String>()
    private var inputConnection: InputConnection? = null
    private var pinView: Boolean = false
    val checkBtnClicked = MutableLiveData<Boolean>()
    val loginPinLengthOkay = MutableLiveData<Boolean>()
    val currentPinLengthOkay = MutableLiveData<Boolean>()
    val newPinLengthOkay = MutableLiveData<Boolean>()
    val confirmNewPinLengthOkay = MutableLiveData<Boolean>()
    val pinValue = MutableLiveData<String>()
    val deletePinValue: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_keyboard_auth, this, true)
        button1 = findViewById<View>(R.id.btnOne) as Button
        button1!!.setOnClickListener(this)
        button2 = findViewById<View>(R.id.btnTwo) as Button
        button2!!.setOnClickListener(this)
        button3 = findViewById<View>(R.id.btnThree) as Button
        button3!!.setOnClickListener(this)
        button4 = findViewById<View>(R.id.btnFour) as Button
        button4!!.setOnClickListener(this)
        button5 = findViewById<View>(R.id.btnFive) as Button
        button5!!.setOnClickListener(this)
        button6 = findViewById<View>(R.id.btnSix) as Button
        button6!!.setOnClickListener(this)
        button7 = findViewById<View>(R.id.btnSeven) as Button
        button7!!.setOnClickListener(this)
        button8 = findViewById<View>(R.id.btnEight) as Button
        button8!!.setOnClickListener(this)
        button9 = findViewById<View>(R.id.btnNine) as Button
        button9!!.setOnClickListener(this)
        button0 = findViewById<View>(R.id.btnZero) as Button
        button0!!.setOnClickListener(this)
        buttonDelete = findViewById<View>(R.id.btnDelete) as ImageView
        buttonDelete!!.setOnClickListener(this)
        buttonEnter = findViewById<View>(R.id.btnCheck) as ImageView
        buttonEnter!!.setOnClickListener(this)
        keyValues.put(R.id.btnOne, "1")
        keyValues.put(R.id.btnTwo, "2")
        keyValues.put(R.id.btnThree, "3")
        keyValues.put(R.id.btnFour, "4")
        keyValues.put(R.id.btnFive, "5")
        keyValues.put(R.id.btnSix, "6")
        keyValues.put(R.id.btnSeven, "7")
        keyValues.put(R.id.btnEight, "8")
        keyValues.put(R.id.btnNine, "9")
        keyValues.put(R.id.btnZero, "0")
        //keyValues.put(R.id.check, "\n")

        checkBtnClicked.value = false
    }

    override fun onClick(view: View) {
        if (inputConnection == null) return
        if (view.id == R.id.btnDelete) {
            if (!pinView) {
                val selectedText = inputConnection!!.getTextBeforeCursor(1, 0)
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection!!.deleteSurroundingText(1, 0)
                } else {
                    inputConnection!!.commitText("", 1)
                }
            } else {
                deletePinValue.value = true
            }
        } else if (view.id == R.id.btnCheck) {
            checkButtonClicked()
        } else {
            view.background.setColorFilter(
                resources.getColor(R.color.primary_dark),
                PorterDuff.Mode.SRC_ATOP
            )
            val value = keyValues[view.id]
            if (!pinView) {
                inputConnection!!.commitText(value, 1)
            } else {
                Log.e("PinView 1", pinValue.value.toString())
                pinValue.value = value
                Log.e("PinView 2", pinValue.value.toString())
            }
        }
    }

    fun setInputConnection(ic: InputConnection?) {
        inputConnection = ic
    }

    fun setPinView(ic: Boolean) {
        pinView = ic
    }

    private fun checkButtonClicked() {
        checkBtnClicked.value = true
    }

    fun loginUser(pin: String) {
        loginPinLengthOkay.value = pin.length == 4
    }

    fun currentPinSet(pin: String) {
        currentPinLengthOkay.value = pin.length == 4
    }

    fun newPinSet(pin: String) {
        newPinLengthOkay.value = pin.length == 4
    }

    fun confirmNewPinSet(pin: String) {
        confirmNewPinLengthOkay.value = pin.length == 4
    }

    init {
        init(context, attrs)
    }
}