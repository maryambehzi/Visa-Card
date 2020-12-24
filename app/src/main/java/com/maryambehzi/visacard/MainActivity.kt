package com.maryambehzi.visacard

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.getbouncer.cardscan.ui.CardScanActivity
import com.getbouncer.cardscan.ui.CardScanActivityResult
import com.getbouncer.cardscan.ui.CardScanActivityResultHandler

class MainActivity : AppCompatActivity(), CardScanActivityResultHandler {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences("Visa", MODE_PRIVATE)
        editor =  sharedPreferences.edit()


        val detailsFragment =
                CardInputFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, detailsFragment, "dogDetails")
                .addToBackStack(null)
                .commit()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (CardScanActivity.isScanResult(requestCode)) {
            CardScanActivity.parseScanResult(resultCode, data, this)
        }
    }

    override fun analyzerFailure(scanId: String?) {}

    override fun cameraError(scanId: String?) {}

    override fun canceledUnknown(scanId: String?) {}

    override fun cardScanned(scanId: String?, scanResult: CardScanActivityResult) {

        val builder = AlertDialog.Builder(this)
        val message = StringBuilder()
        message.append(scanResult.pan)
        builder.setMessage(message)

        builder.setPositiveButton("OK"){
            _, _ ->
            editor.putString("number", message.toString())
            editor.commit()
            val detailsFragment =
                    CardInputFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.root_layout, detailsFragment, "dogDetails")
                    .addToBackStack(null)
                    .commit() }

        builder.show()

    }

    override fun enterManually(scanId: String?) {}

    override fun userCanceled(scanId: String?) {}

}



