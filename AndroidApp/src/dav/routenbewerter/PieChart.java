package dav.routenbewerter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.Shader;

public class PieChart extends Drawable {

	private List<PieItem> items;
	private Paint paint;
	private RectF arc_bounds;
	private float left_edge;
	private float top_edge;
	private float right_edge;
	private float bottom_edge;
	private int textSize;

	public PieChart(int view_w, int radius, int textSize) {
		left_edge = (view_w / 2) - radius;
		right_edge = (view_w / 2) + radius;
		top_edge = 50;
		bottom_edge = top_edge + radius * 2;
		this.textSize = textSize;

		paint = new Paint();
		items = new ArrayList<PieItem>();
	}

	@Override
	public void draw(Canvas canvas) {
		// PieChart Rectangle erstellen
		arc_bounds = new RectF(left_edge, top_edge, right_edge, bottom_edge);

		// Summe der itemValues bestimmen
		int value_sum = 0;
		for (PieItem item : items)
			value_sum += item.itemValue;

		float startAngle = 0;

		// Jedes Item durchlaufen und zeichnen
		for (PieItem item : items) {
			if (item.itemValue == 0)
				continue;

			// Start und End Angel bestimmen
			float endAngle = value_sum == 0 ? 0 : 360 * item.itemValue
					/ value_sum;
			float newStartAngle = startAngle + endAngle;

			//Farbe für den Arc des Items festlegen
			paint.setColor(item.colorValue);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(0.5f);

			// Gradient füll Farbe erstellen
			LinearGradient linearGradient = new LinearGradient(arc_bounds.left,
					arc_bounds.top, arc_bounds.right, arc_bounds.bottom,
					item.colorValue, Color.WHITE, Shader.TileMode.CLAMP);
			paint.setShader(linearGradient);

			// Arc zeichnen
			canvas.drawArc(arc_bounds, startAngle, endAngle, true, paint);

			Paint linePaint = new Paint();
			linePaint.setAntiAlias(true);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Join.ROUND);
			linePaint.setStrokeCap(Cap.ROUND);
			linePaint.setStrokeWidth(0.5f);
			linePaint.setColor(Color.BLACK);

			// Arc umrandung zeichnen
			canvas.drawArc(arc_bounds, startAngle, endAngle, true, linePaint);

			// Mitte des gerade gezeichneten Arcs bestimmen
			double tdeg = startAngle + (endAngle / 2);														
			float r = arc_bounds.width() / 4;
			double trad = tdeg * (Math.PI / 180d);

			float x = (r * (float) Math.cos(trad)) + arc_bounds.centerX();
			float y = (r * (float) Math.sin(trad)) + arc_bounds.centerY();

			// Farbe und Textgröße für Text und Linie festlegen
			Paint textPaint = new Paint();
			textPaint.setAntiAlias(true);
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(textSize);

			// Lonie und Text zeichnen
			float xEnd = 0;
			if (tdeg < 90 || tdeg > 270) {	//Linie und Text Rechts zeichnen
				xEnd = x + 80;
				drawString(canvas, textPaint, item.itemName, xEnd, y, false);
				canvas.drawLine(x, y, xEnd, y, textPaint);
			} else {	//Linie und Text Links zeichnen
				xEnd = x - 80;
				drawString(canvas, textPaint, item.itemName, xEnd, y, true);

				canvas.drawLine(x, y, xEnd, y, textPaint);
			}

			startAngle = newStartAngle;
		}
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter arg0) {
		// TODO Auto-generated method stub

	}

	public void addItem(String itemName, int colorValue, float itemValue) {
		items.add(new PieItem(itemName, colorValue, itemValue));
	}

	private Rect bounds = new Rect();
	void drawString(Canvas canvas, Paint paint, String str, float x, float y, Boolean left) {
	    String[] lines = str.split("\n");

	    int yoff = 0;
	    for (int i = 0; i < lines.length; ++i) {
	    	if(left)
	    		canvas.drawText(lines[i], x-paint.measureText(lines[i]), y + yoff, paint);
	    	else
	    		canvas.drawText(lines[i], x, y + yoff, paint);
	        paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
	        yoff += bounds.height();
	    }
	}
	
	public class PieItem {	//SubClass für PieItems
		public String itemName;
		public int colorValue;
		public float itemValue;

		public PieItem(String itemName, int colorValue, float itemValue) {
			this.itemName = itemName;
			this.colorValue = colorValue;
			this.itemValue = itemValue;
		}
	}
}
