package com.example.spoofblocker

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import android.content.Context

class SpoofCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) {
            return
        }

        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: ""
        val sharedPrefs = getSharedPreferences("SpoofBlockerPrefs", Context.MODE_PRIVATE)
        val blockedPrefix = sharedPrefs.getString("blocked_prefix", "") ?: ""

        Log.d("SpoofBlocker", "Incoming call from: $phoneNumber")
        Log.d("SpoofBlocker", "Checking against prefix: $blockedPrefix")

        if (blockedPrefix.isNotEmpty() && isSpoofing(phoneNumber, blockedPrefix)) {
            Log.d("SpoofBlocker", "Blocking spoofed call: $phoneNumber")
            respondToCall(callDetails, buildResponse(true))
        } else {
            respondToCall(callDetails, buildResponse(false))
        }
    }

    private fun isSpoofing(phoneNumber: String, prefix: String): Boolean {
        // Remove caracteres não numéricos para comparação
        val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        val cleanPrefix = prefix.replace(Regex("[^0-9]"), "")
        
        return cleanNumber.startsWith(cleanPrefix)
    }

    private fun buildResponse(shouldBlock: Boolean): CallResponse {
        val responseBuilder = CallResponse.Builder()
        if (shouldBlock) {
            responseBuilder.apply {
                setDisallowCall(true)
                setRejectCall(true)
                setSkipCallLog(false)
                setSkipNotification(true)
            }
        }
        return responseBuilder.build()
    }
}
