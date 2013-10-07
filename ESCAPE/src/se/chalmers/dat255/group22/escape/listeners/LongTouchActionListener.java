package se.chalmers.dat255.group22.escape.listeners;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This onTouchListener checks the duration of a touch down press in order to
 * handle long touch presses. <br>
 * 
 * We found this implementation online and decided to use it instead of
 * implementing our own.
 * 
 * @author found online
 * 
 */
public abstract class LongTouchActionListener implements OnTouchListener {

	/**
	 * Implement these methods in classes that extend this
	 */
	public abstract void onClick(View v);

	public abstract void onLongTouchAction(View v);

	/**
	 * The time before we count the current touch as a long touch
	 */
	public static final long LONG_TOUCH_TIME = 500;

	/**
	 * The interval before calling another action when the users finger is held
	 * down
	 */
	public static final long LONG_TOUCH_ACTION_INTERVAL = 100;

	/**
	 * The time the user first put their finger down
	 */
	private long mTouchDownTime;

	/**
	 * The coordinates of the first touch
	 */
	private float mTouchDownX;
	private float mTouchDownY;

	/**
	 * The amount the users finger has to move in DIPs before we cancel the
	 * touch event
	 */
	public static final int TOUCH_MOVE_LIMIT_DP = 50;

	/**
	 * TOUCH_MOVE_LIMIT_DP converted to pixels, and squared
	 */
	private float mTouchMoveLimitPxSq;

	/**
	 * Is the current touch event a long touch event
	 */
	private boolean mIsLongTouch;

	/**
	 * Is the current touch event a simple quick tap (click)
	 */
	private boolean mIsClick;

	/**
	 * Handlers to post UI events
	 */
	private LongTouchHandler mHandler;

	/**
	 * Reference to the long-touched view
	 */
	private View mLongTouchView;

	/**
	 * Constructor
	 */

	public LongTouchActionListener(Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		mTouchMoveLimitPxSq = scale * scale * TOUCH_MOVE_LIMIT_DP
				* TOUCH_MOVE_LIMIT_DP;

		mHandler = new LongTouchHandler();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		final int action = event.getAction();

		switch (action) {

		case MotionEvent.ACTION_DOWN:
			// down event
			mIsLongTouch = false;
			mIsClick = true;

			mTouchDownX = event.getX();
			mTouchDownY = event.getY();
			mTouchDownTime = event.getEventTime();

			mLongTouchView = v;

			// post a runnable
			mHandler.sendEmptyMessageDelayed(
					LongTouchHandler.MESSAGE_LONG_TOUCH_WAIT, LONG_TOUCH_TIME);
			break;

		case MotionEvent.ACTION_MOVE:
			// check to see if the user has moved their
			// finger too far
			if (mIsClick || mIsLongTouch) {
				final float xDist = (event.getX() - mTouchDownX);
				final float yDist = (event.getY() - mTouchDownY);
				final float distanceSq = (xDist * xDist) + (yDist * yDist);

				if (distanceSq > mTouchMoveLimitPxSq) {
					// cancel the current operation
					mHandler.removeMessages(LongTouchHandler.MESSAGE_LONG_TOUCH_WAIT);
					mHandler.removeMessages(LongTouchHandler.MESSAGE_LONG_TOUCH_ACTION);

					mIsClick = false;
					mIsLongTouch = false;
				}
			}
			break;

		case MotionEvent.ACTION_CANCEL:
			mIsClick = false;
		case MotionEvent.ACTION_UP:
			// cancel any message
			mHandler.removeMessages(LongTouchHandler.MESSAGE_LONG_TOUCH_WAIT);
			mHandler.removeMessages(LongTouchHandler.MESSAGE_LONG_TOUCH_ACTION);

			long elapsedTime = event.getEventTime() - mTouchDownTime;
			if (mIsClick && elapsedTime < LONG_TOUCH_TIME) {
				onClick(v);
			}
			break;

		}

		// we did not consume the event, pass it on
		// to the button
		return false;
	}

	/**
	 * Handler to run actions on UI thread
	 */
	private class LongTouchHandler extends Handler {
		public static final int MESSAGE_LONG_TOUCH_WAIT = 1;
		public static final int MESSAGE_LONG_TOUCH_ACTION = 2;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_LONG_TOUCH_WAIT:
				mIsLongTouch = true;
				mIsClick = false;

				// flow into next case
			case MESSAGE_LONG_TOUCH_ACTION:
				if (!mIsLongTouch)
					return;

				onLongTouchAction(mLongTouchView); // call users function

				// wait for a bit then update
				takeNapThenUpdate();

				break;
			}
		}

		private void takeNapThenUpdate() {
			sendEmptyMessageDelayed(MESSAGE_LONG_TOUCH_ACTION,
					LONG_TOUCH_ACTION_INTERVAL);
		}
	};
}
