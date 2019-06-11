package app.aproximador.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import app.aproximador.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private var tvContent: TextView = findViewById(R.id.tvContent)

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        tvContent.text = "${e.x} - ${e.y}"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}
