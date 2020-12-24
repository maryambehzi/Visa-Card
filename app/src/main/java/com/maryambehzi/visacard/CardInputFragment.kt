package com.maryambehzi.visacard

import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.getbouncer.cardscan.ui.CardScanActivity
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher
import java.util.*

class CardInputFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    companion object {
        @JvmStatic
        fun newInstance(): CardInputFragment {
            val args = Bundle()
            val fragment = CardInputFragment()
            fragment.arguments = args
            return fragment
          }

        private const val API_KEY = "vzByiVvxrEZuoUkDMZ1hxLFUZLPsA5cY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        sharedPreferences = activity!!.getSharedPreferences("Visa", MODE_PRIVATE)
        editor =  sharedPreferences.edit()

        val view: View = inflater.inflate(R.layout.fragment_card_input, container, false)
        val cardNumber = view.findViewById<EditText>(R.id.CardNumber)
        val expDate = view.findViewById<EditText>(R.id.ExpDate)
        val CardTypeImg = view.findViewById<ImageButton>(R.id.CardTypeImg)
        val DateArrowDown = view.findViewById<ImageButton>(R.id.DateArrowDown)
        val ScanCreditCard = view.findViewById<ImageButton>(R.id.ScanCreditCard)

        CardTypeImg.visibility = View.GONE;

        cardNumber.addTextChangedListener(PatternedTextWatcher("####-####-####-####"))


        if(sharedPreferences.getString("number", "") != ""){
                cardNumber.setText(sharedPreferences.getString("number", ""))
                editor.putString("number", "")
                editor.commit()}

        val dpd = context?.let {
            DatePickerDialog(
                it, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, _ ->
                val mounthplus = monthOfYear+1
                    expDate.setText("$mounthplus/$year")
                },
                year, month, day
            )
        }

        expDate.setOnClickListener {
            dpd!!.show()
        }

        DateArrowDown.setOnClickListener {
            dpd!!.show()
        }

        ScanCreditCard.setOnClickListener { _ ->
            CardScanActivity.start(
                activity = ((activity as MainActivity?)!!),
                    apiKey = API_KEY,
                    enableEnterCardManually = true,
                    enableExpiryExtraction = true,
                    enableNameExtraction = true)
        }

        CardScanActivity.warmUp(context = context!!, apiKey = API_KEY, initializeNameAndExpiryExtraction = true)

        if (cardNumber.text.toString().isNotEmpty()){
            if (cardNumber.text.toString().startsWith("5")){
                CardTypeImg.visibility = View.VISIBLE
                CardTypeImg.setImageResource(R.drawable.mastercard)}
            else if (cardNumber.text.toString().startsWith("4")){
                CardTypeImg.visibility = View.VISIBLE
                CardTypeImg.setImageResource(R.drawable.visa)}
        }

        cardNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (cardNumber.text.toString().isNotEmpty()) {
                    if (cardNumber.text.toString().startsWith("5")){
                        CardTypeImg.visibility = View.VISIBLE
                        CardTypeImg.setImageResource(R.drawable.mastercard)}
                    else if (cardNumber.text.toString().startsWith("4")){
                        CardTypeImg.visibility = View.VISIBLE
                        CardTypeImg.setImageResource(R.drawable.visa)}
                } else
                    CardTypeImg.visibility = View.GONE

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (cardNumber.text.toString().isNotEmpty()) {
                    if (cardNumber.text.toString().startsWith("5"))
                        CardTypeImg.setImageResource(R.drawable.mastercard)
                    else if (cardNumber.text.toString().startsWith("4"))
                        CardTypeImg.setImageResource(R.drawable.visa)
                    else if (cardNumber.text.toString().startsWith("4"))
                        CardTypeImg.setImageResource(R.drawable.visa)
                } else
                    CardTypeImg.setImageResource(R.drawable.ic_credit_card)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        getActivity()!!.finish()
    }
}