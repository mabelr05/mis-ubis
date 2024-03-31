package ec.edu.tecnologicoloja.misubis;

import android.graphics.Bitmap;
import android.view.View;

public class Utils {
    public static Bitmap getMarkerBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        return returnedBitmap;
    }
}
