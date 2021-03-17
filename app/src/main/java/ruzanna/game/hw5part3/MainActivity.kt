package ruzanna.game.hw5part3

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity :  AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login = findViewById<Button>(R.id.login)
        val eMail = findViewById<EditText>(R.id.e_mail)
        val name = findViewById<EditText>(R.id.first_name)
        val lastName = findViewById<EditText>(R.id.last_name)
        val phone = findViewById<EditText>(R.id.tel)

        login.setOnClickListener {
            val eMailText = eMail.text.toString()
            val phoneText = phone.text.toString()
            val nameText = name.text.toString().capitalize()
            val lastNameText = lastName.text.toString().capitalize()

            if (isValidEmail(eMailText) && isValidPhone(phoneText) && nameText.isNotEmpty() && lastNameText.isNotEmpty()) {
                val intent = Intent(this, NextActivity::class.java)
                intent.putExtra("eMail", eMailText)
                intent.putExtra("phone", phoneText)
                intent.putExtra("name", nameText)
                intent.putExtra("lastName", lastNameText)
                startActivityForResult(intent, 1000)
            } else {
                Toast.makeText(
                    applicationContext, "You have provided incorrect information.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}

class NextActivity: AppCompatActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.next_activity)
        val camera = findViewById<Button>(R.id.camera)
        val email = findViewById<Button>(R.id.e_mail)
        val phone = findViewById<Button>(R.id.tel)
        val extras = intent.extras
        if(extras != null){
            email.text = extras.get("eMail") as String
            phone.text = extras.get("phone") as String
        }
        camera.setOnClickListener{
            val getCamera = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
            try {
                startActivityForResult(getCamera, 1)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    applicationContext, "There is no camera on the device.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        email.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(extras?.get("eMail").toString()))
                putExtra(Intent.EXTRA_SUBJECT, "${extras?.get("name")} ${extras?.get("lastName")}")
                putExtra(Intent.EXTRA_TEXT, phone.text)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, 1)
            }
        }
        phone.setOnClickListener{
            //ACTION_CALL -ov chstacvec
            val intent = Intent(Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone.text)))
            startActivityForResult(intent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            Toast.makeText(
                applicationContext, "Code OK",
                Toast.LENGTH_LONG
            ).show()
        }else{
            Toast.makeText(
                applicationContext, "Bad request",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
