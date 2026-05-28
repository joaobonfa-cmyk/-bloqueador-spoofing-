package com.example.spoofblocker

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spoofblocker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_ID_CALL_SCREENING = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("SpoofBlockerPrefs", Context.MODE_PRIVATE)
        binding.etPrefix.setText(sharedPrefs.getString("blocked_prefix", "2199257"))

        binding.btnSave.setOnClickListener {
            val prefix = binding.etPrefix.text.toString()
            sharedPrefs.edit().putString("blocked_prefix", prefix).apply()
            Toast.makeText(this, R.string.msg_saved, Toast.LENGTH_SHORT).show()
        }

        requestCallScreeningRole()
    }

    private fun requestCallScreeningRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                    startActivityForResult(intent, REQUEST_ID_CALL_SCREENING)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ID_CALL_SCREENING) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "App definido como filtrador de chamadas!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissão negada. O bloqueio não funcionará.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
