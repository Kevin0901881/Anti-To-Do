package antitodo.kevinli.com.antitodo

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.tags.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddItem : AppCompatActivity() {

    var tags = ArrayList<String>()
    var layouts = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val catamaran = Typeface.createFromAsset(assets, "fonts/Catamaran-Regular.ttf")
        val catamaranSemi = Typeface.createFromAsset(assets, "fonts/Catamaran-SemiBold.ttf")
        val catamaranBold = Typeface.createFromAsset(assets, "fonts/Catamaran-Bold.ttf")
        val sdf = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val day = Date()
        val currentTime = sdf.format(day).toUpperCase()

        val iTime = intent.getStringExtra("time")
        val iTitle = intent.getStringExtra("title")
        val iDesc = intent.getStringExtra("desc")
        if (!intent.getBooleanExtra("add", false) && intent.getStringArrayListExtra("tags").isNotEmpty()) {
            tags = intent.getStringArrayListExtra("tags")
            desc_text.animate()
                    .translationYBy(120f)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            desc_edit.animate()
                    .translationYBy(120f)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            for (i in 0 until tags.size) {
                val tagLayout = layoutInflater.inflate(R.layout.tags, null)
                val tagsString = "#" + tags[i]
                tagLayout.tag_text.text = tagsString
                layouts.add(tagLayout)
                tags_view.addView(tagLayout)
            }
            for (i in 0 until layouts.size) {
                layouts[i].setOnClickListener {
                    tags_view.removeView(layouts[i])
                    tags.removeAt(i)
                    layouts.removeAt(i)
                    if (tags.isEmpty()) {
                        desc_text.animate()
                                .translationYBy(-120f)
                                .setInterpolator(DecelerateInterpolator())
                                .start()
                        desc_edit.animate()
                                .translationYBy(-120f)
                                .setInterpolator(DecelerateInterpolator())
                                .start()
                    }
                }
            }
        }

        if (!intent.getBooleanExtra("add", true)) {
            val display = windowManager.defaultDisplay
            val dm = DisplayMetrics()
            display.getMetrics(dm)
            val density = resources.displayMetrics.density

            val button = Button(this)
            button.text = "REMOVE"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.backgroundTintList = ContextCompat.getColorStateList(baseContext, R.color.button_color)
            }
            button.width = (150 * density + 0.5f).toInt()
            button.height = (55 * density + 0.5f).toInt()
            buttons_layout.addView(button, 0)

            button.setOnClickListener({
                val intent = Intent()
                setResult(2, intent)
                finish()
            })

            add_item.text = "UPDATE"
        }
        if (iTime == null) {
            time.text = currentTime
        } else {
            time.text = iTime
        }
        time.typeface = catamaranSemi
        recorded_at.typeface = catamaranSemi
        title_text.typeface = catamaranBold
        if (iTitle == null) {
            title_edit.typeface = catamaran
        } else {
            title_edit.setText(iTitle)
            title_edit.isEnabled = false
        }
        tags_text.typeface = catamaranBold
        tags_edit.typeface = catamaran
        desc_text.typeface = catamaranBold
        if (iDesc != null) {
            desc_edit.setText(iDesc)
        }
        desc_edit.typeface = catamaran

        close_touch.setOnClickListener({ onBackPressed() })

        add_tags_touch.setOnClickListener {
            if (!tags_edit.text.isEmpty()) {
                if (tags.isEmpty()) {
                    desc_text.animate()
                            .translationYBy(120f)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    desc_edit.animate()
                            .translationYBy(120f)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                }
                val tagLayout = layoutInflater.inflate(R.layout.tags, null)
                val tagsString = "#" + tags_edit.text.toString()
                tagLayout.tag_text.text = tagsString
                layouts.add(tagLayout)
                tags_view.addView(tagLayout)
                tags.add(tags_edit.text.toString())
                tags_edit.setText("")

                removeTag()
            }
        }

        add_item.setOnClickListener {
            if (title_edit.text.isEmpty()) {
                Toast.makeText(baseContext, "You must provide a title", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent()
                intent.putExtra("time", time.text)
                intent.putExtra("title", title_edit.text.toString())
                intent.putExtra("tags", tags)
                intent.putExtra("desc", desc_edit.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    fun removeTag() {
        for (i in 0 until layouts.size) {
            layouts[i].setOnClickListener {
                tags_view.removeView(layouts[i])
                tags.removeAt(i)
                layouts.removeAt(i)
                if (tags.isEmpty()) {
                    desc_text.animate()
                            .translationYBy(-120f)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    desc_edit.animate()
                            .translationYBy(-120f)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                }
                removeTag()
            }
        }
    }
}
