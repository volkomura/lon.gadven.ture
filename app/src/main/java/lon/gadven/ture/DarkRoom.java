package lon.gadven.ture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DarkRoom extends SurfaceView implements Runnable {
    private Thread mThread = null;
    private volatile boolean mRunning;

    private Path mPath;
    private Context mContext;
    private FlashlightCone mFlashlightCone;
    private Paint mWinnerPaint;
    private RectF mBoundingRect;
    private Bitmap mBitmap;
    private int mBitmapX;
    private int mBitmapY;
    private int mViewWidth;
    private int mViewHeight;
    private SurfaceHolder mSurfaceHolder;
    private List<Integer> gemArr;
    private int randomGem;
    Canvas canvas;


    public DarkRoom(Context context) {
        this(context, null);
    }

    public DarkRoom(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mWinnerPaint = new Paint();
        gemArr = new ArrayList<>();
        mWinnerPaint.setColor(Color.RED);
        mWinnerPaint.setAntiAlias(true);
        mPath = new Path();
        gemArr.add(R.drawable.gem_1);
        gemArr.add(R.drawable.gem_2);
        gemArr.add(R.drawable.gem_3);
        gemArr.add(R.drawable.gem_4);
        gemArr.add(R.drawable.gem_5);
        gemArr.add(R.drawable.gem_6);
        gemArr.add(R.drawable.gem_7);
        gemArr.add(R.drawable.gem_8);
        Random random = new Random();
        randomGem = gemArr.get(random.nextInt(8));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;
        mFlashlightCone = new FlashlightCone(mViewWidth, mViewHeight);
        mWinnerPaint.setTextSize((float) mViewHeight / 5);

        mBitmap = BitmapFactory.decodeResource(
                mContext.getResources(), randomGem);
        setBitmapPos();
    }

    @Override
    public void run() {
        while (mRunning) {
            if (mSurfaceHolder.getSurface().isValid()) {

                int x = mFlashlightCone.getmX();
                int y = mFlashlightCone.getmY();
                int radius = mFlashlightCone.getmRadius();

                canvas = mSurfaceHolder.lockCanvas();
                canvas.save();
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, null);
                mPath.addCircle(x, y, radius, Path.Direction.CCW);

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    canvas.clipPath(mPath, Region.Op.DIFFERENCE);
                } else {
                    canvas.clipOutPath(mPath);
                }

                canvas.drawColor(Color.BLACK);

                if (x > mBoundingRect.left && x < mBoundingRect.right
                        && y > mBoundingRect.top && y < mBoundingRect.bottom) {

                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, null);
                    gameWin();


                }
                mPath.rewind();
                canvas.restore();
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void gameWin() {
        Intent i = new Intent(getContext(), GamePuzzle.class);
        getContext().startActivity(i);
    }


    private void updateFrame(int newX, int newY) {
        mFlashlightCone.move(newX, newY);
    }

    private void setBitmapPos() {
        mBitmapX = (int) Math.floor(
                Math.random() * (mViewWidth - mBitmap.getWidth()));
        mBitmapY = (int) Math.floor(
                Math.random() * (mViewHeight - mBitmap.getHeight()));
        mBoundingRect = new RectF(mBitmapX, mBitmapY,
                mBitmapX + mBitmap.getWidth(),
                mBitmapY + mBitmap.getHeight());
    }

    public void pause() {
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        }
    }

    public void resume() {
        mRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBitmapPos();
                updateFrame((int) x, (int) y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                updateFrame((int) x, (int) y);
                invalidate();

                break;
            default:
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
