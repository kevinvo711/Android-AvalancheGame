package afzal.mugrai.vo.avalanche;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class SmallBlocks {

    private int yVel = 15;

    BitmapDrawable block;
    int x;
    int y;
    int Game_Height;


    public SmallBlocks( BitmapDrawable block1, int x0, int y0, int height)
    {
        block = block1;
        x = x0;
        y = y0;
        Game_Height = height;
    }

    public void update()
    {
        if ((y+block.getBitmap().getHeight()) >= Game_Height)
            y = Game_Height - block.getBitmap().getHeight();
        else
            y += yVel;
    }

    public void draw(Canvas c)
    {
        c.drawBitmap(block.getBitmap(), x, y, null);
    }

    public int getX()
    { return x;}

    public int getY()
    { return y;}

    public BitmapDrawable getBitMap()
    {return block;}

}
