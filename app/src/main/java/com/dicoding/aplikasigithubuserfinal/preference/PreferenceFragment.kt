package com.dicoding.aplikasigithubuserfinal.preference

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.aplikasigithubuserfinal.R
import com.dicoding.aplikasigithubuserfinal.alarm.AlarmReceiver

class PreferenceFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMIND: String
    private lateinit var CHANGE_LAN: String
    private lateinit var reminderState: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver
    private var state: Boolean = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        init()
        setSummaries()
    }

    private fun init(){
        REMIND = getString(R.string.reminder_key)
        CHANGE_LAN = getString(R.string.ch_lan_key)
        reminderState = findPreference<SwitchPreference>(REMIND) as SwitchPreference
    }

    private fun setSummaries(){
        val pref = preferenceManager.sharedPreferences
        reminderState.isChecked = pref.getBoolean(REMIND, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key){
            CHANGE_LAN -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == REMIND){
            state = sharedPreferences.getBoolean(REMIND, false)
            reminderState.isChecked = state
            setAlarm(state)
        }
    }

    private fun setAlarm(remind: Boolean){
        if (remind){
            alarmReceiver.setRepeatingAlarm(requireContext(), getString(R.string.remind_message))
        } else {
            alarmReceiver.cancelAlarm(requireContext())
        }
    }

}