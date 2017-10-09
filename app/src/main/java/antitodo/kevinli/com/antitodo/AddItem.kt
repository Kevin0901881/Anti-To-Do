package antitodo.kevinli.com.antitodo

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_item.*
import java.text.SimpleDateFormat
import java.util.*

class AddItem : AppCompatActivity() {

    var tags = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val catamaran = Typeface.createFromAsset(assets, "fonts/Catamaran-Regular.ttf")
        val catamaranSemi = Typeface.createFromAsset(assets, "fonts/Catamaran-SemiBold.ttf")
        val catamaranBold = Typeface.createFromAsset(assets, "fonts/Catamaran-Bold.ttf")
        val sdf = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val day = Date()
        val currentTime = sdf.format(day).toUpperCase()

        time.text = currentTime
        time.typeface = catamaranSemi
        recorded_at.typeface = catamaranSemi
        title_text.typeface = catamaranBold
        title_edit.typeface = catamaran
        tags_text.typeface = catamaranBold
        tags_edit.typeface = catamaran
        desc_text.typeface = catamaranBold
        desc_edit.typeface = catamaran

        close_touch.setOnClickListener({ v -> onBackPressed() })

        add_touch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (!tags_edit.text.isEmpty()) {
                    if (tags.isEmpty()) {
                        desc_text.animate()
                                .translationYBy(35f)
                                .setInterpolator(DecelerateInterpolator())
                                .start()
                        desc_edit.animate()
                                .translationYBy(35f)
                                .setInterpolator(DecelerateInterpolator())
                                .start()
                    }
                    tags.add(tags_edit.text.toString())
                    tags_edit.setText("")
                }
            }
        })

        add.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (title_edit.text.isEmpty()) {
                    Toast.makeText(baseContext, "You must provide a title", Toast.LENGTH_LONG).show()
                } else {
                    var intent = Intent()
                    intent.putExtra("time", time.text)
                    intent.putExtra("title", title_edit.text.toString())
                    intent.putExtra("tags", tags)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        })
    }
}
