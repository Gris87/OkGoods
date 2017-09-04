package ru.okmarket.okgoods.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.util.AppLog;


@SuppressWarnings("PublicConstructor")
public class GlowView extends View
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "GlowView";
    // endregion



    // region Handler messages
    private static final int ANIMATION_MESSAGE = 1;
    private static final int LONGPRESS_MESSAGE = 2;
    // endregion



    // region Handler parameters
    private static final int   ANIMATION_START_DELAY    = 300;
    private static final int   ANIMATION_DELAY          = 40;
    private static final float ANIMATION_INCREASE_SPEED = 2.0f;
    private static final float ANIMATION_DECREASE_SPEED = 0.3f;

    private static final int   LONGPRESS_DELAY          = 1000;
    // endregion
    // endregion



    // region Attributes
    private Paint               mPaint               = null;
    private ProcessHandler      mHandler             = null;
    private float               mCenterX             = 0;
    private float               mRadius              = 0;
    private int[]               mColors              = null;
    private float[]             mStops               = null;
    private Vibrator            mVibrator            = null;
    private OnClickListener     mOnClickListener     = null;
    private OnLongClickListener mOnLongClickListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "GlowView{" +
                "mPaint="                 + mPaint                   +
                ", mHandler="             + mHandler                 +
                ", mCenterX="             + mCenterX                 +
                ", mRadius="              + mRadius                  +
                ", mColors="              + Arrays.toString(mColors) +
                ", mStops="               + Arrays.toString(mStops)  +
                ", mOnClickListener="     + mOnClickListener         +
                ", mOnLongClickListener=" + mOnLongClickListener     +
                '}';
    }

    public GlowView(Context context)
    {
        super(context);

        init();
    }

    public GlowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    public GlowView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    @SuppressWarnings("deprecation")
    private void init()
    {
        mPaint               = new Paint();
        mHandler             = null;
        mCenterX             = -1;
        mRadius              = -1;
        mColors              = new int[] { getResources().getColor(R.color.highlightItemColor), Color.TRANSPARENT, Color.TRANSPARENT };
        mStops               = new float[] { 1.0f, 0.9f, 0.0f };
        mVibrator            = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mOnClickListener     = null;
        mOnLongClickListener = null;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener)
    {
        mOnClickListener = listener;
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener listener)
    {
        mOnLongClickListener = listener;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        if (
            mCenterX >= 0
            &&
            mRadius >= 0
           )
        {
            mPaint.setShader(new RadialGradient(mCenterX, mRadius, mRadius, mColors, mStops, Shader.TileMode.MIRROR));

            canvas.drawCircle(mCenterX, mRadius, mRadius, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                mCenterX = event.getX();
                mRadius  = getHeight() * 2.0f;

                invalidate();



                if (mHandler == null)
                {
                    mHandler = ProcessHandler.newInstance(this);
                }
                else
                {
                    mHandler.removeMessages(ANIMATION_MESSAGE);
                    mHandler.removeMessages(LONGPRESS_MESSAGE);

                    mHandler.setIncreasing(true);
                }

                mHandler.sendEmptyMessageDelayed(ANIMATION_MESSAGE, ANIMATION_START_DELAY);
                mHandler.sendEmptyMessageDelayed(LONGPRESS_MESSAGE, LONGPRESS_DELAY);

                return true;
            }
            // break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            {
                if (mHandler != null)
                {
                    mHandler.removeMessages(ANIMATION_MESSAGE);
                    mHandler.removeMessages(LONGPRESS_MESSAGE);

                    mHandler.setIncreasing(false);
                    mHandler.sendEmptyMessage(ANIMATION_MESSAGE);



                    if (mOnClickListener != null && event.getAction() == MotionEvent.ACTION_UP)
                    {
                        mOnClickListener.onClick(this);
                    }
                }

                return true;
            }
            // break;

            default:
            {
                // Nothing
            }
            break;
        }

        return super.onTouchEvent(event);
    }



    private static final class ProcessHandler extends Handler
    {
        @SuppressWarnings("FieldNotUsedInToString")
        private GlowView mGlowView   = null;
        private boolean  mIncreasing = false;



        @SuppressWarnings("MethodReturnAlwaysConstant")
        @Override
        public String toString()
        {
            return "ProcessHandler{" +
                    "mIncreasing=" + mIncreasing +
                    '}';
        }

        private ProcessHandler(GlowView view)
        {
            mGlowView   = view;
            mIncreasing = true;
        }

        public static ProcessHandler newInstance(GlowView view)
        {
            return new ProcessHandler(view);
        }

        @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case ANIMATION_MESSAGE:
                {
                    if (mIncreasing)
                    {
                        if (mGlowView.mRadius < mGlowView.getWidth() * 2.0f)
                        {
                            mGlowView.mRadius *= ANIMATION_INCREASE_SPEED;
                            mGlowView.invalidate();
                        }
                    }
                    else
                    {
                        mGlowView.mRadius *= ANIMATION_DECREASE_SPEED;

                        if (mGlowView.mRadius < mGlowView.getHeight() * 2.0f)
                        {
                            mGlowView.mCenterX = -1;
                            mGlowView.mRadius  = -1;

                            mGlowView.mHandler = null;
                            mGlowView.invalidate();

                            return;
                        }

                        mGlowView.invalidate();
                    }

                    sendEmptyMessageDelayed(ANIMATION_MESSAGE, ANIMATION_DELAY);
                }
                break;

                case LONGPRESS_MESSAGE:
                {
                    if (mGlowView.mOnLongClickListener != null)
                    {
                        removeMessages(ANIMATION_MESSAGE);

                        mGlowView.mCenterX = -1;
                        mGlowView.mRadius  = -1;

                        mGlowView.mHandler = null;
                        mGlowView.invalidate();



                        //noinspection deprecation
                        mGlowView.mVibrator.vibrate(100);



                        mGlowView.mOnLongClickListener.onLongClick(mGlowView);
                    }
                }
                break;

                default:
                {
                    AppLog.wtf(TAG, "Unknown message type: " + msg.what);
                }
            }
        }

        @SuppressWarnings("WeakerAccess")
        public void setIncreasing(boolean increasing)
        {
            mIncreasing = increasing;
        }
    }
}
