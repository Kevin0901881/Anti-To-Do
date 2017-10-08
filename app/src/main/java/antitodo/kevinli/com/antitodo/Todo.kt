package antitodo.kevinli.com.antitodo

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_todo.*
import java.text.SimpleDateFormat
import java.util.*

class Todo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val catamaran = Typeface.createFromAsset(assets, "fonts/Catamaran-Regular.ttf")
        val catamaranSemi = Typeface.createFromAsset(assets, "fonts/Catamaran-SemiBold.ttf")

        val sdf = SimpleDateFormat("EEEE", java.util.Locale.getDefault())
        val sdf2 = SimpleDateFormat("MMM dd, YYYY", java.util.Locale.getDefault())
        val day = Date()
        val dayOfWeek = sdf.format(day).toUpperCase()
        val fullDate = sdf2.format(day)

        val doSomething = "Do something " + "" + " today"

        actionbar.text = dayOfWeek
        actionbar.typeface = catamaranSemi
        date.text = fullDate
        date.typeface = catamaranSemi
        nothing.text = doSomething
        nothing.typeface = catamaran

        supportActionBar!!.hide()
    }
}
