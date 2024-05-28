package com.dostonbek.firststepofscanner

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dostonbek.firststepofscanner.databinding.ActivityMainBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MainActivity : AppCompatActivity() {
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC).build()
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CAPTURE_IMAGE = 1
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
captureImage.setOnClickListener {


    takeImage()
    textView.text = ""
}
            detect.setOnClickListener {
    detectImage()
}

detectImage()



        }
    }

    private fun detectImage() {
        if (imageBitmap!=null){
            val image=InputImage.fromBitmap(imageBitmap!!,0)
            val scanner=BarcodeScanning.getClient(options)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.toString()=="[]"){
                        Toast.makeText(this,"Nothing to show",Toast.LENGTH_SHORT).show()
                    }
                    for (barcode in barcodes){
                        val valueType=barcode.valueType
                        when (valueType){
                            Barcode.TYPE_WIFI ->{
                                val ssid=barcode.wifi!!.ssid
                                val password=barcode.wifi!!.password
                                val type=barcode.wifi!!.encryptionType
                                binding.textView.text=ssid+"\n"+password+"\n"+type
                            }
                            Barcode.TYPE_URL ->{
                                val title=barcode.url!!.title
                                val url=barcode.url!!.url
                                binding.textView.text=title+"\n"+url

                            }



                        }


                    }

                }
        }
        else{
            Toast.makeText(this,"Please select photo",Toast.LENGTH_SHORT).show()

        }
    }


    private fun takeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE)
        } catch (e: Exception) {

        }


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            if (imageBitmap != null) {
                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
    }
}