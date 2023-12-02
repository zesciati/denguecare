package com.example.testing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isNotEmpty


class Diagnosis : AppCompatActivity() {
    val KUNCI ="diagnosis"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis)
    }

    fun fDiagnosis(view: View) {
        val radioButtonYes1: RadioButton = findViewById(R.id.ya1)

        val radioButtonYes2: RadioButton = findViewById(R.id.ya2)

        val radioButtonYes3: RadioButton = findViewById(R.id.ya3)

        val radioButtonYes4: RadioButton = findViewById(R.id.ya4)

        val radioButtonYes5: RadioButton = findViewById(R.id.ya5)

        val radioButtonYes6: RadioButton = findViewById(R.id.ya6)

        val radioButtonYes7: RadioButton = findViewById(R.id.ya7)

        val radioButtonYes8: RadioButton = findViewById(R.id.ya8)

        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val answer1: Boolean = radioButtonYes1.isChecked
            val answer2: Boolean = radioButtonYes2.isChecked
            val answer3: Boolean = radioButtonYes3.isChecked
            val answer4: Boolean = radioButtonYes4.isChecked
            val answer5: Boolean = radioButtonYes5.isChecked
            val answer6: Boolean = radioButtonYes6.isChecked
            val answer7: Boolean = radioButtonYes7.isChecked
            val answer8: Boolean = radioButtonYes8.isChecked

            val rGroup1: RadioGroup = findViewById(R.id.qa1)
            val rGroup2: RadioGroup = findViewById(R.id.qa2)
            val rGroup3: RadioGroup = findViewById(R.id.qa3)
            val rGroup4: RadioGroup = findViewById(R.id.qa4)
            val rGroup5: RadioGroup = findViewById(R.id.qa5)
            val rGroup6: RadioGroup = findViewById(R.id.qa6)
            val rGroup7: RadioGroup = findViewById(R.id.qa7)
            val rGroup8: RadioGroup = findViewById(R.id.qa8)

            if (rGroup1.checkedRadioButtonId != -1 && rGroup2.checkedRadioButtonId != -1 && rGroup3.checkedRadioButtonId != -1 && rGroup4.checkedRadioButtonId != -1 && rGroup5.checkedRadioButtonId != -1 && rGroup6.checkedRadioButtonId != -1 && rGroup7.checkedRadioButtonId != -1 && rGroup8.checkedRadioButtonId != -1){
                val diagnosis = checkDemam(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8)

                val keHasilDiagnosis = Intent(this, HasilDiagnosis::class.java)
                keHasilDiagnosis.apply {
                    putExtra(KUNCI, diagnosis)
                }
                startActivity(keHasilDiagnosis)
            } else{
                Toast.makeText(this@Diagnosis,"Harap isi semua form",Toast.LENGTH_SHORT).show()

            }
        }
    }


    fun checkDemam(q1: Boolean, q2: Boolean, q3: Boolean, q4: Boolean, q5: Boolean, q6: Boolean, q7: Boolean, q8: Boolean): String {
        val stepResult = !(q1 && q2)
        val step1Result = (q1 && q2 && q3 && q4)
        val step2Result = step1Result && (q5 && q6)
        val step3Result = step2Result && q7
        val step4Result = step3Result && q8

        return when {
            step4Result -> "Anda diperkirakan terkena Demam Dengue dengan tingkat DENV-4."
            step3Result -> "Anda diperkirakan terkena Demam Dengue dengan tingkat DENV-3."
            step2Result -> "Anda diperkirakan terkena Demam Dengue dengan tingkat DENV-2."
            step1Result -> "Anda diperkirakan terkena Demam Dengue dengan tingkat DENV-1."
            stepResult -> "Anda diperkirakan hanya terkena demam biasa."
            else -> "Kami belum bisa mendeteksi penyakit berdasarkan data yang anda berikan."
        }
    }

    fun kembaliD(view: View){
        val intent = Intent(this@Diagnosis, BerandaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
