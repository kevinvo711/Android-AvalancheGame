package afzal.mugrai.vo.avalanche;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends ImageView implements View.OnTouchListener {

    //variables
    private Context mContext;
    private Handler h;

    ArrayList<SmallBlocks> smallBlockList;
    Random chooser;
    SmallBlocks smallBlock;

    boolean smallOverlap;

    // constant
    private final int FRAME_RATE = 30;

    private int timer = 0;

    public GameView (Context context, AttributeSet a)
    {
        super(context, a);

        mContext = context;
        h = new Handler();

        smallBlockList = new ArrayList<SmallBlocks>();
        chooser = new Random();

    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) {

        timer = timer + 1;
        Log.e("fdsaereqw", ""+timer);
        //PUT THIS INSIDE A TIMER SO IT DROPS A NEW BLOCK AFTER A CERTAIN TIME
        {
            if (timer%50 == 0) {
                int blockChooser = chooser.nextInt(2);

                if (blockChooser == 0) {
                    BitmapDrawable block1 = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.small);
                    int x = chooser.nextInt(this.getWidth() - block1.getBitmap().getWidth());
                    int y = 0;
                    int Game_Height = this.getHeight();
                    smallBlock = new SmallBlocks(block1, x, y, Game_Height);
                    smallBlockList.add(smallBlock);
                } else {
                    //THIS IS FOR THE BIG BLOCK ONE
                }
            }

        }

        //THIS IS THE FORWARD LOOP
        //run through the ArrayList for the small boxes
        for(int i = 0; i < smallBlockList.size(); i++)
        {
            if (i == 0)
            {
                smallBlockList.get(i).update();
                smallBlockList.get(i).draw(c);
            }
            else {
                //check to make sure they do not overlap
                SmallOverlapChecker(smallBlockList, i);

                if (!smallOverlap) {
                    smallBlockList.get(i).update();
                }
                else
                //do something like remove the first one and shift everything over
                {}
                smallBlockList.get(i).draw(c);
                smallOverlap = false;
            }
        }

        //have a small delay
        //send and process Message and Runnable objects
        //have them be executed after some time
        h.postDelayed(r, FRAME_RATE);

    }

    //USE FOR THE FORWARD LOOP
    public void SmallOverlapChecker(ArrayList<SmallBlocks> blockList, int i)
    {
        int xC = blockList.get(i).getX();
        int yC = blockList.get(i).getY();
        BitmapDrawable smallBlock = blockList.get(i).getBitMap();
        for (int k = i; k > 0; k--)
        {
            int xO = blockList.get(k-1).getX();
            int yO = blockList.get(k-1).getY();
            BitmapDrawable smallBlockO = blockList.get(k-1).getBitMap();

            if (((xC >= xO && xC <= xO + smallBlockO.getBitmap().getWidth()) &&
                    (yC + smallBlock.getBitmap().getHeight() >= yO &&  yC + smallBlock.getBitmap().getHeight() <= yO + smallBlockO.getBitmap().getHeight())) ||
                    ((xC + smallBlock.getBitmap().getWidth() >= xO && xC + smallBlock.getBitmap().getWidth() <= xO + smallBlockO.getBitmap().getWidth()) &&
                            yC + smallBlock.getBitmap().getHeight() >= yO && yC + smallBlock.getBitmap().getHeight() <= yO + smallBlockO.getBitmap().getHeight()) )
            {
                smallOverlap = true;
            }

            //if done moving, remove from orginal and add to a second arrayList
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent e)
    {



        return false;
    }

}
