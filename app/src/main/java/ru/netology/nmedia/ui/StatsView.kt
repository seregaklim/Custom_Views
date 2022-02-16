package ru.netology.nmedia.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
import ru.netology.nmedia.R
import ru.netology.nmedia.R.styleable.StatsView_color1
import ru.netology.nmedia.util.AndroidUtils
import kotlin.math.min
import kotlin.random.Random


// позволяют «инструктировать» компилятор по поводу того, какой код генерировать
// (преимущественно имеют префикс @Jvm)
class StatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    //Исходя из этих размеров, мы можем рассчитать текущий радиус нашей диаграммы, а также координаты центра:
    private var radius = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)

    private var lineWidth = AndroidUtils.dp(context, 5F).toFloat()
    private var fontSize = AndroidUtils.dp(context, 40F).toFloat()
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attrs, R.styleable.StatsView) {
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            fontSize = getDimension(R.styleable.StatsView_fontSize, fontSize)

            //И т.д. для всех цветов.(через вынесенные атрибуты)
            colors = listOf(
                getColor(
                    StatsView_color1,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color2,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color3,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color4,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color5,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color6,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color7,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color8,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color9,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color10,
                    randomColor()
                )
            )
        }
    }
    //Paint – условно «кисть», определяющая параметры рисования:сглаживание (anti-aliasing),толщину,
// цвет,форму краев и т.д.
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    //Для отображения текста выберем другую«кисть»
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }
    //Научимся передавать в наше View данные: Ключевое здесь – вызов метода invalidate:
    //Вызов этого метода приводит к тому, что наше View перерисуется (при условии, что оно видимо).
    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }


    //Первый метод, который нужно переопределить  onSizeChanged,
    // в него нам пришлют размеры при первой отрисовке:

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //рассчитываем текущий радиус нашей диаграммы, а также координаты центра:
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)

        //рисуем круг
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }
    //Метод onDraw может вызываться очень часто, поэтому все ресурсоемкие операции стоит
    // выносить из него (например, создание и инициализацию объектов):
    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }

        //25% = 3600 * 0.25 = 90 градусов (дуга)(поменяй дугу сместится угол дуги)
        var startFrom = -90F
        for ((index, datum) in data.withIndex()) {
            val angle = 360F * datum/2000
            paint.color = colors.getOrNull(index) ?: randomColor()
            //немного математики, чтобы рисовать «дуги»:
            canvas.drawArc(oval, startFrom, angle, false, paint)
            startFrom += angle
        }

        //ставим точку
        var startFromPoint = -90F
        for ((index, datum) in data.withIndex()) {
            val angle = 1F * datum/2000
          paint.color =  colors.getOrNull(R.styleable.StatsView_color1) ?: R.styleable.StatsView_color1
            canvas.drawArc(oval, startFromPoint, angle, false, paint)
            startFromPoint += angle
        }



//Для отображения текста выберем другую«кисть»:
        //И в onDraw просто выведем текст, используя format:
        canvas.drawText(
            "%.2f%%".format(data.sum() * 100/2000),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint,
        )

        //нарисовать квадрат с привязкой к кругу
        // canvas.drawRect(oval,paint)

    }



    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}