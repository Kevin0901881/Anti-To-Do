package antitodo.kevinli.com.antitodo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_todo.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class Todo : AppCompatActivity() {

    lateinit var cAdapter : CustomAdapter

    var allTags = ArrayList<ArrayList<String>>()
    var titles = ArrayList<String>()
    var times = ArrayList<String>()
    var descriptions = ArrayList<String>()

    var doSomething = ""

    var editItem = 0

    lateinit var sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var gson = Gson()

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

        sp = getSharedPreferences("myprefs", Context.MODE_PRIVATE)
        editor = sp.edit()

        val catamaran = Typeface.createFromAsset(assets, "fonts/Catamaran-Regular.ttf")
        val catamaranSemi = Typeface.createFromAsset(assets, "fonts/Catamaran-SemiBold.ttf")
        val catamaranBold = Typeface.createFromAsset(assets, "fonts/Catamaran-Bold.ttf")

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
        thoughts.typeface = catamaranBold
        notes.typeface = catamaran

        if (sp.getString("titles", null).isNotEmpty()) {
            val txt = sp.getString("titles", null)
            titles = gson.fromJson(txt, ArrayList<String>().javaClass)
        }
        if (sp.getString("tags", null).isNotEmpty()) {
            val txt = sp.getString("tags", null)
            allTags = gson.fromJson(txt, ArrayList<ArrayList<String>>().javaClass)
        }
        if (sp.getString("times", null).isNotEmpty()) {
            val txt = sp.getString("times", null)
            times = gson.fromJson(txt, ArrayList<String>().javaClass)
        }
        if (sp.getString("desc", null).isNotEmpty()) {
            val txt = sp.getString("desc", null)
            descriptions = gson.fromJson(txt, ArrayList<String>().javaClass)
        }
        if (sp.getString("notes", null) != null) {
            notes.setText(sp.getString("notes", null))
        }

        cAdapter = CustomAdapter(applicationContext, titles, allTags, times)
        list.adapter = cAdapter

        val adjectives = arrayOf("disruptive", "challenging", "fun", "exciting", "new", "revolutionary",
                "different", "important", "brave", "quirky", "pleasant", "funny", "productive", "ambitious",
                "clever", "crazy", "generous", "romantic", "wild", "simple", "relaxing", "adventurous",
                "smart", "light-hearted", "practical", "charming", "creative", "imaginative",
                "kind", "sociable", "straightforward", "thoughtful", "active", "athletic", "brilliant",
                "courageous", "flamboyant", "impressive", "insightful", "intelligent", "profound",
                "purposeful", "realistic", "serious", "spontaneous", "sporty", "wise", "daring", "rewarding",
                "friendly", "useful", "amusing", "exhilarating", "self-assuring", "corny", "reflective",
                "competitive", "spectacular", "witty", "goofy", "considerate", "out of this world")
        val rand = Random()
        val number = rand.nextInt(adjectives.size - 1)

        doSomething = "Do something " + adjectives[number] + " today!"

        menu_touch.setOnClickListener({drawer_layout.openDrawer(left_drawer)})

        add_touch.setOnClickListener {
            val intent = Intent(this@Todo, AddItem::class.java)
            intent.putExtra("add", true)
            startActivityForResult(intent, 1)
        }

        list.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
            val intent = Intent(this@Todo, AddItem::class.java)
            editItem = p2
            intent.putExtra("add", false)
            intent.putExtra("time", times[p2])
            intent.putExtra("title", titles[p2])
            intent.putExtra("tags", allTags[p2])
            intent.putExtra("desc", descriptions[p2])
            startActivityForResult(intent, 2)
        }

        notes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                editor.putString("notes", notes.text.toString())
                editor.commit()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val catamaran = Typeface.createFromAsset(assets, "fonts/Catamaran-Regular.ttf")

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
                descriptions.add(data.getStringExtra("desc"))

                cAdapter = CustomAdapter(applicationContext, titles, allTags, times)
                list.adapter = cAdapter
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                allTags[editItem] = data!!.getStringArrayListExtra("tags")
                descriptions[editItem] = data.getStringExtra("desc")

                cAdapter = CustomAdapter(applicationContext, titles, allTags, times)
                list.adapter = cAdapter
            } else if (resultCode == 2) {
                allTags.removeAt(editItem)
                titles.removeAt(editItem)
                times.removeAt(editItem)
                descriptions.removeAt(editItem)

                cAdapter = CustomAdapter(applicationContext, titles, allTags, times)
                list.adapter = cAdapter
            }
        }

        val titleJson = gson.toJson(titles)
        val tagJson = gson.toJson(allTags)
        val timeJson = gson.toJson(times)
        val descJson = gson.toJson(descriptions)

        editor.putString("titles", titleJson)
        editor.putString("tags", tagJson)
        editor.putString("times", timeJson)
        editor.putString("desc", descJson)
        editor.commit()
    }
}