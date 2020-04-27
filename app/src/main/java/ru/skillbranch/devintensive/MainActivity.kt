package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("M_MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        benderImage = findViewById(R.id.iv_bender)
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        val wrongAnswers = savedInstanceState?.getInt("WRONG_ANSWERS") ?: 0
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question), wrongAnswers)

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        outState.putInt("WRONG_ANSWERS", benderObj.wrongAnswers)
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name}")
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.question.name}")
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.wrongAnswers}")
    }

    override fun onRestart() {
        Log.d("M_MainActivity", "onRestart")
        super.onRestart()
    }

    override fun onStart() {
        Log.d("M_MainActivity", "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("M_MainActivity", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("M_MainActivity", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("M_MainActivity", "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("M_MainActivity", "onDestroy")
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString()
                .toLowerCase(Locale.ROOT))
            messageEt.setText("")
            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
        }
        hideKeyboard()
    }
}
