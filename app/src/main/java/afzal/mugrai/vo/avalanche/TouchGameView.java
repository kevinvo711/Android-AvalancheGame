package afzal.mugrai.vo.avalanche;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TouchGameView extends View{

    ArrayList<RectF> objects = new ArrayList<RectF>();
    Paint paint = new Paint();
    Paint lavaPaint = new Paint();
    Paint playerPaint = new Paint();

    int width, height, rectWidth, rectX, rectY;
    Random num = new Random();
    private float velocity = 10f;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    private Handler handler = new Handler();
    Camera mCam = new Camera();

    Player player;
    RectF playerObj;

    public TouchGameView(Context context, AttributeSet set) {
        super(context, set);
        startTimer();

        handler.postDelayed(runnable, 100);
        pHandler.postDelayed(pMovement, 0);

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        createPaint();

        absoluteLeft = 0;
        absoluteTop = (height/2) + (height/4);
        absoluteRight = width;
        absoluteBot = height + 50;
        newTop = 0;
        //kok

        player = new Player(width/25, width/30);
        player.setLeft(width/2 - width/30);
        player.setTop(height - width/25);
        player.setRight(width/2);
        player.setBot(height);

        playerObj = new RectF(player.getLeft(), player.getTop(), player.getRight(), player.getBot());

        lava = new RectF(0, absoluteBot, width, absoluteBot + 1);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
    }

    public void createPaint(){
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        lavaPaint.setColor(Color.RED);
        lavaPaint.setStrokeWidth(3);
        playerPaint.setColor(Color.DKGRAY);
        playerPaint.setStrokeWidth(3);
    }

    public void setButtonHeight(int h)
    {
        //   absoluteTop -= h;

        player = new Player(width/25, width/30);
        player.setLeft(width/2 - width/30);
        player.setTop(absoluteTop - width/25);
        player.setRight(width/2);
        player.setBot(absoluteTop);

        jumpImpulse = ((absoluteTop/9.5f));
        playerTop = player.getTop();
        playerBot = player.getBot();

        playerObj = new RectF(player.getLeft(), player.getTop(), player.getRight(), player.getBot());
        lava = new RectF(0, absoluteBot, width, absoluteBot +10000);

    }

    RectF lava;

    public void risingLava(float delta)
    {
        lava.offset(0f, -(delta * .01f));
        //  objects.get(i).offset(0f, delta * .2f);
    }
    int score;

    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();
        mCam.applyToCanvas(canvas);

        for(RectF rect : objects)
        {
            canvas.drawRect(rect, paint);
        }
        int x = absoluteTop - (int)playerObj.top;
        score = Math.abs(x);
        paint.setTextSize(25);

        canvas.drawText("Score: " + score, width - 150, 25 + newTop, paint);
        canvas.drawRect(playerObj, playerPaint);
        canvas.drawLine(0, absoluteTop, width, absoluteTop, paint);
        canvas.drawRect(lava, lavaPaint);

        newTop = (int)playerObj.bottom - absoluteTop;
        canvas.restore();
    }

    private void startTimer(){
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {

                        rectX = num.nextInt(width + 1);

                        if (num.nextInt(51) > 25) {
                            rectWidth = width / 10; // big squares
                        } else
                            rectWidth = width / 20; // small ones

                        // create new rectangle. top left is (x location, height of screen) bot right is (x location + width, x loc (top), height of rect)
                        RectF rect = new RectF(rectX, newTop, rectX + rectWidth, rectWidth + newTop);

                        objects.add(rect);
                        intersect.add(false);
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 5000);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)

        {
            mTimer1.cancel();
            mTimer1.purge();
            mTimerHandler.removeCallbacks(mTt1);
            handler.removeCallbacks(runnable);

            pHandler.removeCallbacks(pMovement);
            // return true;
        }

        return false;
    }
    int b = 0;
    public void gameOver()
    {
        if (b ==0){
            mTimer1.cancel();
            mTimer1.purge();
            mTimerHandler.removeCallbacks(mTt1);
            handler.removeCallbacks(runnable);

            pHandler.removeCallbacks(pMovement);
            GameActivity.showAlert((GameActivity)getContext(), "Your Score: " + Integer.toString(score));
            b++;}

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            long time1 = System.currentTimeMillis();
            long time2;

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
            }

            time2 = System.currentTimeMillis(); // Get current time
            int delta = (int) (time2 - time1); // Calculate how long it's been since last update

            updateJump(delta);
            risingLava(delta);
            update(delta); // Call update with our delta
            //applyGravity(delta);

            time1 = time2; // Update our time variables.

            handler.postDelayed(this, 10);
        }
    };

    float jumpImpulse;

    ArrayList<Boolean> intersect = new ArrayList<Boolean>();
    float left, top, right, bot;
    int absoluteLeft, absoluteTop, absoluteRight, absoluteBot, newTop;
    boolean hittingLeft = false;
    boolean hittingRight = false;

    float velocityX = 4.0f;
    float velocityY = 0.0f;
    float gravity = 0.5f;

    public void startJump() {
        if(isGrounded) {
            gravity = 0.5f;
            velocityY = -12;
            isGrounded = false;
            playerObj.top -= 5;
            playerObj.bottom -=5;
        }
    }

    public void updateJump(float delta)
    {
        if (!isGrounded) {
            velocityY += gravity;
            playerObj.top += velocityY;

            mCam.translate(0, velocityY, 0.f);
            // newTop += velocityY;
            playerTop += velocityY;
            playerBot += velocityY;
            playerObj.bottom += velocityY;
        }


    }
    float playerTop, playerBot;
    boolean moveCam = false;

    boolean canMove = false;

    public void update(float delta)
    {
        isGrounded = false;
        hittingLeft = false;
        hittingRight = false;
        for (int i = 0; i < objects.size(); i++)
        {
            float w = 0.5f * (objects.get(i).width() + playerObj.width());
            float h = 0.5f * (objects.get(i).height() + playerObj.height());
            float dx = objects.get(i).centerX() - playerObj.centerX();
            float dy = objects.get(i).centerY() - playerObj.centerY();

            if (Math.abs(dx) <= w && Math.abs(dy) <= h)
            {
                    /* collision! */
                float wy = w * dy;
                float hx = h * dx;

                if (wy > hx) {
                    if (wy > -hx) {
                            /* collision at the top */
                        isGrounded = true;
                        playerTop = objects.get(i).top - (width/25);
                        playerBot = objects.get(i).top;
                        moveCam = false;
                        playerObj.set(playerObj.left, objects.get(i).top - (width/25), playerObj.right, objects.get(i).top);
                    }
                    else{
                        hittingLeft = true;
                            /* on the left */
                    }
                }
                else {
                    if (wy > -hx) {
                        hittingRight = true;
                            /* on the right */
                    }
                    else {
                        gameOver();
                        // gravity = 0;
                            /* at the bottom */
                    }
                }
            }

            left = objects.get(i).left;
            top = objects.get(i).top;
            right = objects.get(i).right;
            bot = objects.get(i).bottom;

            if (playerObj.bottom >= absoluteTop)
            {
                isGrounded = true;
                playerTop = player.getTop();
                playerBot = player.getBot();
                playerObj.set(playerObj.left, player.getTop(), playerObj.right, player.getBot());
            }

            if (playerObj.intersects(lava.left, lava.top, lava.right, lava.bottom))
            {
                gameOver();
            }

            for (int j = 0; j < objects.size(); j++)
            {
                if (j == i)
                {

                }

                else if (objects.get(j).intersects(left, top, right, bot))
                {
                    intersect.set(i, true);
                }
            }



            if (objects.get(i).intersects(absoluteLeft, absoluteTop, absoluteRight, absoluteBot))
            {
                intersect.set(i, true);
            }

            if (intersect.get(i) == true)
            {
                objects.get(i).offset(0,0);
            }

            else{
                objects.get(i).offset(0f, delta * .2f);} // drop speed. Needs to be increased. Alot
        }

        invalidate(); // Tells our view to redraw itself, since our position changed.
    }

    private float touchX;

    boolean isDown = false;

    private Handler pHandler = new Handler();
    private Runnable pMovement = new Runnable() {
        @Override
        public void run() {
            long time1 = System.currentTimeMillis();
            long time2;

            try {
                Thread.sleep(15);
            }
            catch (InterruptedException e){}

            if (isDown) {
                time2 = System.currentTimeMillis(); // Get current time
                int delta = (int) (time2 - time1); // Calculate how long it's been since last update
                //    update(delta); // Call update with our delta

                if (hittingLeft == false) {
                    if (touchX < (width / 2)) {
                        playerObj.offset(-1 * delta * 0.5f, 0);
                        //  mCam.translate(0, -10, 0.f);
                        //  newTop -= 10;
                    }
                }

                if (hittingRight == false) {
                    if (touchX > (width / 2)) {
                        playerObj.offset(1 * delta * 0.5f, 0);
                        //    mCam.translate(0, 10, 0.f);
                        //    newTop += 10;
                    }
                }

                if (playerObj.right > width)
                {
                    playerObj.set(0, playerTop, width/30, playerBot);
                }

                else if (playerObj.left < 0)
                {
                    playerObj.set(width - (width/30), playerTop, width, playerBot);
                }


                time1 = time2; // Update our time variables.
            }

            pHandler.postDelayed(this, 10);
        }
    };

    boolean isGrounded = true;

    private SparseArray<PointF> mActivePointers  = new SparseArray<PointF>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                isDown = true;
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);

                touchX = f.x;
                isDown = true;
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                        touchX = point.x;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                isDown = false;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                break;
            }
        }

        return true;
    }
}