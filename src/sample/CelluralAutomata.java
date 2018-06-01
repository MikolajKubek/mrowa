package sample;

import com.sun.istack.internal.Nullable;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public interface CelluralAutomata {/*interfejs automatu komorkowego*/

    public void process();
    public void addElement(int x, int y, @Nullable Color color, @Nullable Direction direction);
    public void editCanvas(Canvas canvas);
    public int getWidth();
    public int getHeight();
    public void restart();
}
