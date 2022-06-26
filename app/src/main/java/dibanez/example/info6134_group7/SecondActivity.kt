package dibanez.example.info6134_group7

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*


interface CellClickListener{
    fun onCellClickListener(lat: Double,lon: Double)
}

class SecondActivity : AppCompatActivity(),CellClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var viewManager: RecyclerView.LayoutManager



    companion object {
        var dogDataAge: String = ""
        var dogDataBreed: String = ""
        var dogDataGender: String = ""
        var dogDataDimensions: String = ""
        var dogDataName: String = ""
        var dogDataLat: Float = 0f
        var dogDataLon: Float = 0f
        var dogDataWeight: String = ""
        val dogData: MutableList<DataType> = mutableListOf<DataType>()

        // share selected Data from RecyclerView to ThirdActivity
        var shareDogName: String = ""
        var shareDogAge: String = ""
        var shareDogGender: String = ""
        var shareDogBreed: String = ""

        var shareDogDataDimensions: String = ""
        var shareLat: Double = 0.0
        var shareLon: Double = 0.0


        // Receive Data from ThirdActivity
        var receiveDogName: String = ""
        var receiveDogAge: String = ""
        var receiveDogGender: String = ""
        var receiveDogWeight: String = ""
        var receiveDogBreed: String = ""
        var receiveDogHeight: String = ""
        var receiveDogLength: String = ""
        var receiveDogDimensions: String = ""
        var receiveLat: Double = 0.0
        var receiveLon: Double = 0.0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

//        dogData.add(DataType("Dog1","3","female","10 cm",41.5245,-70.6709,false))
//        dogData.add(DataType("Dog2","3","female","10 cm",30.5245,-60.6709,false))
//        dogData.add(DataType("Dog3","3","female","10 cm",20.5245,-50.5245,false))
        //println("test1" + dogData)

    }
    fun readData(){
         dogData.clear()
         Firebase.database.reference.child("User/${MainActivity.userID}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        dogData.add(DataType(
                            "${snapshot.child("name").getValue()}",
                            "${snapshot.child("age").getValue()}",
                            "${snapshot.child("gender").getValue()}",
                            "${snapshot.child("breed").getValue()}",
                            "${snapshot.child("dimension").getValue()}",
                            snapshot.child("lat").getValue().toString().toDouble(),
                            snapshot.child("lon").getValue().toString().toDouble(),
                            snapshot.child("check").getValue().toString().toBoolean()
                        ))
                    }
                    recyclerView.adapter = RecyclerAdapter(dogData, this@SecondActivity)
                    recyclerAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    println(databaseError)
                }
            })
    }

    // call from ThirdActivity
    fun addData(){
        var dogObject = DataType(receiveDogName,
            receiveDogAge,
            receiveDogGender,
            receiveDogBreed,

            receiveDogDimensions,
            receiveLat,
            receiveLon)
        Firebase.database.reference.child("User/${MainActivity.userID}/${receiveDogName}").setValue(dogObject)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Data added successfully.",
                    Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(baseContext, "Data added failed.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    // call from ThirdActivity
    fun updateData(){
        var dogObject: DataType = DataType(receiveDogName,
            receiveDogAge,
            receiveDogGender,
            receiveDogBreed,
            receiveDogDimensions,
            receiveLat,
            receiveLon,check = false)
        Firebase.database.reference.child("User/${MainActivity.userID}/${receiveDogName}").setValue(dogObject)
            .addOnSuccessListener {
              //  Toast.makeText(baseContext, "Data updated successfully.", Toast.LENGTH_SHORT).show()
                //readData()
            }
            .addOnFailureListener {
                Toast.makeText(baseContext, "Data updated failed.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    fun removeData(){
        if(shareDogName != "" && shareDogGender != "" && shareDogAge != "" && shareDogDataDimensions != "" ) {
            dogData.removeAll { it.check == true }
            recyclerView.adapter = RecyclerAdapter(dogData, this)
            recyclerAdapter.notifyDataSetChanged()
            println(dogData)

            val Snapshot = Firebase.database.reference.child("User/${MainActivity.userID}")
                .orderByChild("check").equalTo(true)

            Snapshot.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChild: String?) {
                    snapshot.ref.setValue(null)
                        .addOnSuccessListener {
                            Toast.makeText(
                                baseContext, "Data removed successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                baseContext, "Data removed failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    println(snapshot)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    println(snapshot)
                    Snapshot.removeEventListener(this);

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    println(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error)
                }
            })
        } else {
            Toast.makeText(baseContext, "Please Select Dog",
                Toast.LENGTH_SHORT).show()
        }


    }


    override fun onResume() {
        super.onResume()

        viewManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter(dogData,this)  // pass in data to be displayed
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView).apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recyclerAdapter  }

       // readData()
//        addData()
    }
    fun RecyclerViewOnView(){
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(dogData, this)
        recyclerAdapter.notifyDataSetChanged()
        readData()
    }

    fun onClickSignout(view: View) {
        Firebase.auth.signOut()
        Toast.makeText(baseContext, "You signed out successfully.",
            Toast.LENGTH_SHORT).show()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


    fun deleteBtn(view: View) {
        removeData()
    }

    fun editBtn(view: View) {
        //updateData()
        if(shareDogName != "" && shareDogGender != "" && shareDogAge != "" && shareDogDataDimensions != "" )
        {
            UpdateActivity.DogName= shareDogName
            UpdateActivity.DogAge=  shareDogAge
            UpdateActivity.DogGender= shareDogGender
            UpdateActivity.DogBreed = shareDogBreed
            UpdateActivity.DogDataDimensions= shareDogDataDimensions
            UpdateActivity.DogLat = shareLat
            UpdateActivity.DogLon= shareLon

            val intent = Intent(this,UpdateActivity::class.java)
            startActivity(intent)

        }else{
            Toast.makeText(baseContext, "Please Select Dog",
                Toast.LENGTH_SHORT).show()
        }

    }

    fun addBtn(view: View) {
        //addData()
        val intent = Intent(this,CreateActivity::class.java)
        startActivity(intent)
    }


    fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit() }

    override fun onCellClickListener( lat: Double, lon: Double) {
        val fragment = MapsFragment()
        supportFragmentManager.inTransaction { replace(R.id.fragmentContainerView, fragment) }
        MapsFragment.latitudeFrag = lat
        MapsFragment.longitudeFrag = lon
    }

    fun backBtn(view: View) {
        val fragment = RecyclerViewFragment()
        supportFragmentManager.inTransaction { replace(R.id.fragmentContainerView, fragment) }
        Log.i("TEST", "this is the message")

    }


}