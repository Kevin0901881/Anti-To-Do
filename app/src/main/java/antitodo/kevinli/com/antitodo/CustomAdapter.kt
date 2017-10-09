package antitodo.kevinli.com.antitodo

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by Kevin on 10/8/2017.
 */
class CustomAdapter : BaseAdapter {

    var c: Context
    var cList: ArrayList<String>
    var cList2: ArrayList<ArrayList<String>>
    var cListTime: ArrayList<String>
    var inflater: LayoutInflater

    constructor(c: Context, cList: ArrayList<String>, cList2: ArrayList<ArrayList<String>>, cListTime: ArrayList<String>) : super() {
        this.c = c
        this.cList = cList
        this.cList2 = cList2
        this.cListTime = cListTime
        inflater = LayoutInflater.from(c)
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val catamaran = Typeface.createFromAsset(c.assets, "fonts/Catamaran-Regular.ttf")
        val catamaranMed = Typeface.createFromAsset(c.assets, "fonts/Catamaran-Medium.ttf")
        var z = inflater.inflate(R.layout.list_item, null)
        val title = z.title
        val tags = z.tags
        val time = z.time
        var cList2String = ""
        title.text = cList[p0]
        for (i in cList2[p0]) {
            cList2String += "#" + i + "  "
        }
        tags.text = cList2String
        time.text = cListTime[p0]
        title.typeface = catamaranMed
        tags.typeface = catamaran
        time.typeface = catamaranMed
        return z
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return cList.size
    }
}