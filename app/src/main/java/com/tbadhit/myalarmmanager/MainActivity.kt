package com.tbadhit.myalarmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tbadhit.myalarmmanager.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

// Siapkan asset" :
// icon "access time" (New → Vector Asset)
// icon "date range" (New → Vector Asset)

// Codelab (One Time Alarm Manager):
// Update "activity_main.xml"
// Create new receiver class "AlarmReceiver" (New → Other → Broadcast Receiver)
// add code "AlarmReceiver" (1) (2) (3) (4)
// Create new class "DatePickerFragment"
// add code "DatePickerFragment" (1) (kelas ini untuk membantu kita mengambil waktu)
// Create new class "TimePickerFragment"
// add code "TimePickerFragment" (1) (kelas fragment ini akan membantu untuk setup jam)
// add code "MainActivity" (1) (2) (saatnya kita memberikan aksi pada View)
// add permission "AndoridManifest.xml" (1)

// Codelab (Repeating AlarmManager):
// add code "AlarmReceiver" (5) "add setRepatingAlarm() method"
// add code "activity_main.xml" (2)
// add code "MainActivity" (3)

// Codelab (Cancel AlarmManager) :
// add code "AlarmReceiver" (6) "add cancelAlarm() method"
// add code "activity_main.xml" (3)
// add code "MainActivity" (4)

// (2) add "DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener"
class MainActivity : AppCompatActivity(), View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    private var binding : ActivityMainBinding? = null
    // (1)
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // (1)
        // Listener one time alarm
        binding?.btnOnceDate?.setOnClickListener(this)
        binding?.btnOnceTime?.setOnClickListener(this)
        binding?.btnSetOnceAlarm?.setOnClickListener(this)
        //-----

        // (3)
        // Listener repeating alarm
        binding?.btnRepeatingTime?.setOnClickListener(this)
        binding?.btnSetRepeatingAlarm?.setOnClickListener(this)
        //-----

        // (4)
        binding?.btnCancelRepeatingAlarm?.setOnClickListener(this)
        //------

        // (1)
        alarmReceiver = AlarmReceiver()
    }

    // (1)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_once_date -> {
                // (2)
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_once_time -> {
                // (2)
                val timePickerFragmentOne = TimePickerFragment()
                timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_set_once_alarm -> {
                // (2)
                val onceDate = binding?.tvOnceDate?.text.toString()
                val onceTime = binding?.tvOnceTime?.text.toString()
                val onceMessage = binding?.edtOnceMessage?.text.toString()
                alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                    onceDate,
                    onceTime,
                    onceMessage)
            }

            // (3)
            R.id.btn_repeating_time -> {
                val timPickerFragmentRepeat = TimePickerFragment()
                timPickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }

            // (3)
            R.id.btn_set_repeating_alarm -> {
                val repeatTime = binding?.tvRepeatingTime?.text.toString()
                val repeatMessage = binding?.edtRepeatingMessage?.text.toString()
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage)
            }

            // (4)
            R.id.btn_cancel_repeating_alarm -> {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
            }
        }
    }



    // (1)
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // (2)
    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    // (2)
    override fun onDialogDataSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        // Siapkan date formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Set text dari textview once
        binding?.tvOnceDate?.text = dateFormat.format(calendar.time)
    }

    // (2)
    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        // Siapkan time formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // Set text dari textview berdasarkan tag
        when (tag) {
            TIME_PICKER_ONCE_TAG -> binding?.tvOnceTime?.text = dateFormat.format(calendar.time)
            // (3)
            TIME_PICKER_REPEAT_TAG -> binding?.tvRepeatingTime?.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }
}