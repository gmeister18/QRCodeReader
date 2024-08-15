package com.example.qrcodereader

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.qrcodereader.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {
    private val requestPermissionLaucher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted: Boolean ->
            if (isGranted){
                showCamera()

            }
            else{
                //pq ter permission
            }
        }
    private val scanLauncher =
        registerForActivityResult(ScanContract()){
            result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }
        }

    private lateinit var binding: ActivityMainBinding

    private fun setResult( string: String) {
        binding.textResult.text = string
    }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()

    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            checkPermissionCamera(this)
        }
    }

    private fun checkPermissionCamera(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                showCamera()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissionLaucher.launch(Manifest.permission.CAMERA)
            }
        } else {
            // Se o dispositivo estiver em uma versão abaixo do API 23, considere que a permissão já foi concedida
            showCamera()
        }
    }



    private fun initBinding() {
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}