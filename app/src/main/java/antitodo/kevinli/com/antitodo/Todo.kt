package antitodo.kevinli.com.antitodo

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_todo.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Todo : AppCompatActivity() {

    lateinit var cAdapter : CustomAdapter

    var allTags = ArrayList<ArrayList<String>>()
    var tags = ArrayList<String>()
    var titles = ArrayList<String>()
    var times = ArrayList<String>()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(left_drawer)
        } else {
            super.onBackPressed()
        }
    }

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

        actionbar.text = dayOfWeek
        actionbar.typeface = catamaranSemi
        date.text = fullDate
        date.typeface = catamaranSemi
        app_name.typeface = catamaranSemi
        desc.typeface = catamaran
        settings.typeface = catamaran

//        val sdf3 = SimpleDateFormat("hh:mm aa", java.util.Locale.getDefault())

//        titles.add("Went to the gym")
//        allTags.add("#gym #workout")
//        times.add(sdf3.format(day).toString())
//        titles.add("Studied for Stats midterm")
//        allTags.add("#midterm")
//        times.add(sdf3.format(day).toString())
//        titles.add("Cooked a delectable dinner")
//        allTags.add("#food")
//        times.add(sdf3.format(day).toString())

        cAdapter = CustomAdapter(applicationContext, titles, allTags, times)
        list.adapter = cAdapter

        menu_touch.setOnClickListener({v -> drawer_layout.openDrawer(left_drawer)})

//        val rotate45 = AnimationUtils.loadAnimation(this, R.anim.rotate_add)
//        val rotateBack45 = AnimationUtils.loadAnimation(this, R.anim.rotate_back_add)

        add_touch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@Todo, AddItem::class.java)
                startActivityForResult(intent, 1)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val catamaran = Typeface.createFromAsset(assets, "fonts/Catamaran-Regular.ttf")

        val adjectives = arrayOf("disruptive", "challenging", "fun", "exciting", "new", "revolutionary",
                "different", "important", "brave", "quirky", "pleasant", "funny", "productive", "ambitious",
                "clever", "crazy", "generous", "romantic", "wild", "simple", "relaxing", "adventurous",
                "smart", "light-hearted", "practical", "charming", "creative", "imaginative",
                "kind", "social", "straightforward", "thoughtful", "active", "athletic", "brilliant",
                "courageous", "flamboyant", "impressive", "insightful", "intelligent", "profound",
                "purposeful", "realistic", "serious", "spontaneous", "sporty", "wise", "daring")
        val rand = Random()
        val number = rand.nextInt(adjectives.size - 1)

        val doSomething = "Do something " + adjectives.get(number) + " today!"

        if (cAdapter.count == 0) {
            nothing.text = doSomething
            nothing.typeface = catamaran
            nothing.alpha = 0.5f
        } else {
            nothing.text = ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                titles.add(data!!.getStringExtra("title"))
                allTags.add(data.getStringArrayListExtra("tags"))
                times.add(data.getStringExtra("time"))
            }
        }
    }
}