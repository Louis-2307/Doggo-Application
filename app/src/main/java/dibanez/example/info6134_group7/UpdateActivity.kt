package dibanez.example.info6134_group7

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class UpdateActivity : AppCompatActivity(),OnItemSelectedListener  {

    //variables for data elements
    lateinit var nameETUpdate: TextView
    lateinit var genderRGUpdate: RadioGroup

    lateinit var ageSpinUpdate: Spinner
    lateinit var breedSpinUpdate: Spinner
    lateinit var heightSpinUpdate: Spinner
    lateinit var lengthSpinUpdate: Spinner
    lateinit var weightSpinUpdate: Spinner

    lateinit var radioButtonMale: RadioButton
    lateinit var radioButtonFemale: RadioButton

    lateinit var streetETUpdate: EditText
    lateinit var zipETUpdate: EditText
    lateinit var cityETUpdate: EditText
    lateinit var stateETUpdate: EditText

    lateinit var testButton: Button
    lateinit var testTVUpdate: TextView

    companion object{
        var DogName: String = ""
        var DogAge: String = ""
        var DogGender: String = ""
        var DogBreed: String = ""
        var DogDataDimensions: String = ""
        var DogLat: Double = 0.0
        var DogLon: Double = 0.0
    }


    var currentAge: String = ""
    var currentGender: String = ""
    var currentHeight: String = ""
    var currentWeight: String = ""
    var currentLength: String = ""
    var currentBreed: String = ""
    var currentLat: Double = 0.0
    var currentLon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)


        //assign variables to corresponding views
        nameETUpdate = findViewById(R.id.editTextNameUpdate)

        genderRGUpdate = findViewById(R.id.radioGroupGenderUpdate)
        radioButtonMale = findViewById(R.id.radioButtonMaleUpdate)
        radioButtonFemale = findViewById(R.id.radioButtonFemaleUpdate)

        ageSpinUpdate = findViewById(R.id.spinnerAgeUpdate)
        breedSpinUpdate = findViewById(R.id.spinnerBreedUpdate)
        heightSpinUpdate = findViewById(R.id.spinnerHeightUpdate)
        lengthSpinUpdate = findViewById(R.id.spinnerLengthUpdate)
        weightSpinUpdate = findViewById(R.id.spinnerWeightUpdate)

        streetETUpdate = findViewById(R.id.editTextStreetUpdate)
        zipETUpdate = findViewById(R.id.editTextZipUpdate)
        cityETUpdate = findViewById(R.id.editTextCityUpdate)
        stateETUpdate = findViewById(R.id.editTextStateUpdate)

//        testButton = findViewById(R.id.buttonTestUpdate)
//        testTVUpdate = findViewById(R.id.textViewTestUpdate)

        //setting up the spinner adapters
        val ageSpinnerAdapterUpdate = ArrayAdapter.createFromResource(
            this,
            R.array.ageArray,
            android.R.layout.simple_spinner_item
        )
        val breedSpinnerAdapterUpdate = ArrayAdapter.createFromResource(
            this,
            R.array.breedArray,
            android.R.layout.simple_spinner_item
        )
        val heightSpinnerAdapterUpdate = ArrayAdapter.createFromResource(
            this,
            R.array.heightArray,
            android.R.layout.simple_spinner_item
        )
        val lengthSpinnerAdapterUpdate = ArrayAdapter.createFromResource(
            this,
            R.array.lengthArray,
            android.R.layout.simple_spinner_item
        )
        val weightSpinnerAdapterUpdate = ArrayAdapter.createFromResource(
            this,
            R.array.weightArray,
            android.R.layout.simple_spinner_item
        )

        ageSpinnerAdapterUpdate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        breedSpinnerAdapterUpdate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        heightSpinnerAdapterUpdate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lengthSpinnerAdapterUpdate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightSpinnerAdapterUpdate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        ageSpinUpdate.adapter = ageSpinnerAdapterUpdate
        breedSpinUpdate.adapter = breedSpinnerAdapterUpdate
        heightSpinUpdate.adapter = heightSpinnerAdapterUpdate
        lengthSpinUpdate.adapter = lengthSpinnerAdapterUpdate
        weightSpinUpdate.adapter = weightSpinnerAdapterUpdate

        ageSpinUpdate.onItemSelectedListener = this
        breedSpinUpdate.onItemSelectedListener = this
        heightSpinUpdate.onItemSelectedListener = this
        lengthSpinUpdate.onItemSelectedListener = this
        weightSpinUpdate.onItemSelectedListener = this
    }

    override fun onResume() {
        super.onResume()

        nameETUpdate.text = DogName
        ageSpinUpdate.setSelection(DogAge.toInt())

        when(DogBreed) {
            "Australian Shepard" -> breedSpinUpdate.setSelection(1)
            "Beagle" -> breedSpinUpdate.setSelection(2)
            "Corgi" -> breedSpinUpdate.setSelection(3)
            "French Bulldog" -> breedSpinUpdate.setSelection(4)
            "Golden Retriever" -> breedSpinUpdate.setSelection(5)
            "Pomerian" -> breedSpinUpdate.setSelection(6)
            "Shiba Inu" -> breedSpinUpdate.setSelection(7)
        }

        if(DogGender == "Male"){
            radioButtonMale.isChecked = true
        }else{
            radioButtonFemale.isChecked = true
        }
        var Height: String = ((DogDataDimensions!!.substringAfter("Height:")).substringBefore("cm"))
        Height = Height.replace("\\s".toRegex(), "")
        var Length: String = ((DogDataDimensions!!.substringAfter("Length:")).substringBefore("cm"))
        Length = Length.replace("\\s".toRegex(), "")
        var Weight: String = ((DogDataDimensions!!.substringAfter("Weight:")).substringBefore("kg"))
        Weight = Weight.replace("\\s".toRegex(), "")
//        var Breed: String = ((DogGender!!.substringBefore(",")))
//        Breed = Breed.replace("\\s".toRegex(), "")
//        var Gender: String = ((DogGender!!.substringAfter(",")))
//        Gender = Gender.replace("\\s".toRegex(), "")

        heightSpinUpdate.setSelection(Height.toInt())
        lengthSpinUpdate.setSelection(Length.toInt())
        weightSpinUpdate.setSelection(Weight.toInt())

        ConvertLatLonToAddress()

        currentAge = DogAge
        currentBreed = DogBreed
        currentGender = DogGender
        currentLat = DogLat
        currentLon = DogLon
        currentHeight = DogDataDimensions!!.substringAfter("Height:").substringBefore(",")
        currentLength = DogDataDimensions!!.substringAfter("Length:").substringBefore(",")
        currentWeight = DogDataDimensions!!.substringAfter("Weight:")
    }

    fun ConvertAddressToLatLon(){
        val addressCompiler = streetETUpdate.text.toString() + ", " + cityETUpdate.text.toString() + ", " + stateETUpdate.text.toString() + ", " + zipETUpdate.text.toString() + ", "
        val geocode = Geocoder(this, Locale.getDefault())
        val addList = geocode.getFromLocationName(addressCompiler, 1)
        currentLat = addList.get(0).latitude
        currentLon = addList.get(0).longitude
    }
    fun ConvertLatLonToAddress(){
        val geocode = Geocoder(this, Locale.getDefault())
        val addList = geocode.getFromLocation(DogLat, DogLon, 1)
        println(addList.get(0).getAddressLine(0))
        streetETUpdate.setText((addList.get(0).getAddressLine(0)).substringBefore("St,"))
        zipETUpdate.setText(addList.get(0).getPostalCode())
        cityETUpdate.setText(addList.get(0).getLocality())
        stateETUpdate.setText(addList.get(0).getAdminArea())
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //actions for selected options in the spinner
        when(p0?.id) {
            R.id.spinnerAgeUpdate-> {
                if (p2 != 0) {
                    currentAge = ageSpinUpdate.selectedItem.toString()
                }else{
                    currentAge = DogAge
                }
            }
            R.id.spinnerBreedUpdate-> {
                if (p2 != 0) {
                    currentBreed = breedSpinUpdate.selectedItem.toString()
                }
                else{
                    currentBreed = DogBreed
                }
            }
            R.id.spinnerHeightUpdate-> {
                if (p2 != 0) {
                    currentHeight = heightSpinUpdate.selectedItem.toString()
                }
                else{
                    currentHeight = ((DogDataDimensions!!.substringAfter("Height:")).substringBefore(","))
                }
            }
            R.id.spinnerLengthUpdate-> {
                if (p2 != 0) {
                    currentLength = lengthSpinUpdate.selectedItem.toString()
                }
                else{
                    currentLength = ((DogDataDimensions!!.substringAfter("Length:")).substringBefore(","))
                }
            }
            R.id.spinnerWeightUpdate-> {
                if (p2 != 0) {
                    currentWeight = weightSpinUpdate.selectedItem.toString()
                }else{
                    currentWeight = ((DogDataDimensions!!.substringAfter("Weight:")))
                }
            }
        }
        // you can also use this command anywhere outside of the onItemSelected
        //       textView.setText(spinner.selectedItem.toString())
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this, "Nothing Selected", Toast.LENGTH_LONG).show()
    }

    fun onRadioClicked(view: View) {

        when(view.id){
            R.id.radioButtonMaleUpdate-> {
                currentGender = getString(R.string.male)
            }
            R.id.radioButtonFemaleUpdate-> {
                currentGender = getString(R.string.female)
            }
        }
    }

    fun btnSave(view: View) {
            ConvertAddressToLatLon()

            SecondActivity.receiveDogName = DogName
            SecondActivity.receiveDogAge = currentAge

        SecondActivity.receiveDogBreed = currentBreed

            SecondActivity.receiveDogGender = currentGender
            SecondActivity.receiveDogDimensions = "Height:${currentHeight},\nLength:${currentLength},\nWeight:${currentWeight}"

            SecondActivity.receiveLat = currentLat
            SecondActivity.receiveLon = currentLon

            SecondActivity().updateData()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)

    }

    fun onTestClicked(view: View) {

//        testTVUpdate.text = DogName + " " + DogLat.toString() + " " + DogLon.toString() +  " " + DogDataDimensions + " "  + DogAge.toString() + " " + DogGender + " " + DogBreed
        testTVUpdate.text = DogName + " " + currentLat.toString() + " " + currentLon.toString() +  " " + currentHeight + " "  + currentWeight + " "  + currentLength + " "  + currentAge + " " + currentGender + " " + currentBreed


    }
}