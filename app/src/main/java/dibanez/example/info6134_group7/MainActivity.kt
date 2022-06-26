package dibanez.example.info6134_group7

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var editTextEmail: EditText
    lateinit var editTextPass: EditText
    companion object{
      var userID: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        editTextEmail = findViewById(R.id.editTextTextEmailAddress)
        editTextPass = findViewById(R.id.editTextTextPassword)
    }

    fun onClickLogin(view: View) {
        var email = editTextEmail.text.toString()
        var password = editTextPass.text.toString()


        if(email.isEmpty() == true && password.isEmpty() == true){
            Toast.makeText(baseContext, "Please enter Email and Password for Logging in ",
                Toast.LENGTH_LONG).show()
        }else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(
                            baseContext, "Authentication Successful.",
                            Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                         userID = user!!.uid
                        val intent = Intent(this,SecondActivity::class.java)
                        startActivity(intent)

                        // updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // updateUI(null)
                    }
                }
            }
        }

    fun onClickSignup(view: View) {
        var email = editTextEmail.text.toString()
        var password = editTextPass.text.toString()

        if(email.isEmpty() == true && password.isEmpty() == true){
            Toast.makeText(baseContext, "Please enter new Email and Password for registering ",
                Toast.LENGTH_LONG).show()
        }else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, "Email and Password were created successful.",
                            Toast.LENGTH_SHORT).show()
                         //user = auth.currentUser
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Email and Password were created Failed.",
                            Toast.LENGTH_SHORT).show()
                        // updateUI(null)
                    }
                }
            }
        }

    fun onSkipButton(view: View) {
        val intent = Intent(this, CreateActivity:: class.java)
        startActivity(intent)
    }


}