package hillfly.wifichat.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hillfly.wifichat.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ListView
 * 
 * @date 
 * @change hillfly
 * @version 1.0
 */
public class MultiListView extends ListView implements OnScrollListener {

    /**  */
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";

    /**  */
    private final static int RATIO = 3;

    private final static int RELEASE_TO_REFRESH = 0;
    private final static int PULL_TO_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;

    /**  */
    private final static int ENDINT_LOADING = 1;
    /**  */
    private final static int ENDINT_MANUAL_LOAD_DONE = 2;
    /**  */
    private final static int ENDINT_AUTO_LOAD_DONE = 3;

    /**
     * 0:RELEASE_TO_REFRESH;
     * <p>
     * 1:PULL_To_REFRESH;
     * <p>
     * 2:REFRESHING;
     * <p>
     * 3:DONE;
     * <p>
     * 4:LOADING
     */
    private int mHeadState;

    /**
     * 0: ;
     * <p>
     * 1:
     */
    private int mEndState;

    // ================================= Flag
    // ================================

    /**  */
    private boolean mCanLoadMore = false;

    /**  */
    private boolean mCanRefresh = false;

    /**  */
    private boolean mIsAutoLoadMore = true;

    /**  */
    private boolean mIsMoveToFirstItemAfterRefresh = false;

    public boolean isCanLoadMore() {
        return mCanLoadMore;
    }

    public void setCanLoadMore(boolean pCanLoadMore) {
        mCanLoadMore = pCanLoadMore;
        if (mCanLoadMore && getFooterViewsCount() == 0) {
            addFooterView();
        }
    }

    public boolean isCanRefresh() {
        return mCanRefresh;
    }

    public void setCanRefresh(boolean pCanRefresh) {
        mCanRefresh = pCanRefresh;
    }

    public boolean isAutoLoadMore() {
        return mIsAutoLoadMore;
    }

    public void setAutoLoadMore(boolean pIsAutoLoadMore) {
        mIsAutoLoadMore = pIsAutoLoadMore;
    }

    public boolean isMoveToFirstItemAfterRefresh() {
        return mIsMoveToFirstItemAfterRefresh;
    }

    public void setMoveToFirstItemAfterRefresh(boolean pIsMoveToFirstItemAfterRefresh) {
        mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
    }

    // ============================================================================

    private LayoutInflater mInflater;

    private LinearLayout mHeadView;
    private TextView mTipsTextView;
    private TextView mLastUpdatedTextView;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;

    private View mEndRootView;
    private ProgressBar mEndLoadProgressBar;
    private TextView mEndLoadTipsTextView;

    /** headView*/
    private RotateAnimation mArrowAnim;
    /** headView */
    private RotateAnimation mArrowReverseAnim;

    /**  */
    private boolean mIsRecored;

    private int mHeadViewWidth;
    private int mHeadViewHeight;

    private int mStartY;
    private boolean mIsBack;

    private int mFirstItemIndex;
    private int mLastItemIndex;
    private int mCount;
    private boolean mEnoughCount; // 

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    public MultiListView(Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);
        init(pContext);
    }

    public MultiListView(Context pContext) {
        super(pContext);
        init(pContext);
    }

    public MultiListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
        super(pContext, pAttrs, pDefStyle);
        init(pContext);
    }

    /**
     * 
     * 
     * @param pContext
     * @date 
     * @change hillfly
     * @version 1.0
     */
    private void init(Context pContext) {
        setCacheColorHint(pContext.getResources().getColor(R.color.transparent));
        mInflater = LayoutInflater.from(pContext);

        addHeadView();

        setOnScrollListener(this);

        initPullImageAnimation(0);
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    private void addHeadView() {
        mHeadView = (LinearLayout) mInflater.inflate(R.layout.listview_multilistview_head, null);

        mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
        mArrowImageView.setMinimumWidth(70);
        mArrowImageView.setMinimumHeight(50);
        mProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
        mTipsTextView = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
        mLastUpdatedTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadView);
        mHeadViewHeight = mHeadView.getMeasuredHeight();
        mHeadViewWidth = mHeadView.getMeasuredWidth();

        mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
        mHeadView.invalidate();

        addHeaderView(mHeadView, null, false);

        mHeadState = DONE;
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    private void addFooterView() {
        mEndRootView = mInflater.inflate(R.layout.listview_multilistview_foot, null);
        mEndRootView.setVisibility(View.VISIBLE);
        mEndLoadProgressBar = (ProgressBar) mEndRootView
                .findViewById(R.id.pull_to_refresh_progress);
        mEndLoadTipsTextView = (TextView) mEndRootView.findViewById(R.id.load_more);
        mEndRootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCanLoadMore) {
                    if (mCanRefresh) {
                        // 
                        if (mEndState != ENDINT_LOADING && mHeadState != REFRESHING) {
                            mEndState = ENDINT_LOADING;
                            onLoadMore();
                        }
                    }
                    else if (mEndState != ENDINT_LOADING) {
                        // 
                        mEndState = ENDINT_LOADING;
                        onLoadMore();
                    }
                }
            }
        });

        addFooterView(mEndRootView);

        if (mIsAutoLoadMore) {
            mEndState = ENDINT_AUTO_LOAD_DONE;
        }
        else {
            mEndState = ENDINT_MANUAL_LOAD_DONE;
        }
    }

    /**
     * 
     * 
     * @param pAnimDuration
     *            
     * @date 
     * @change hillfly
     */
    private void initPullImageAnimation(final int pAnimDuration) {

        int _Duration;

        if (pAnimDuration > 0) {
            _Duration = pAnimDuration;
        }
        else {
            _Duration = 250;
        }

        Interpolator _Interpolator = new LinearInterpolator();

        mArrowAnim = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mArrowAnim.setInterpolator(_Interpolator);
        mArrowAnim.setDuration(_Duration);
        mArrowAnim.setFillAfter(true);

        mArrowReverseAnim = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mArrowReverseAnim.setInterpolator(_Interpolator);
        mArrowReverseAnim.setDuration(_Duration);
        mArrowReverseAnim.setFillAfter(true);
    }

    /**
     * 
     * 
     * @param pChild
     * @date 
     * @change hillfly
     */
    private void measureView(View pChild) {
        ViewGroup.LayoutParams p = pChild.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;

        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }
        else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        pChild.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 
     */
    @Override
    public void onScroll(AbsListView pView, int pFirstVisibleItem, int pVisibleItemCount,
            int pTotalItemCount) {
        mFirstItemIndex = pFirstVisibleItem;
        mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
        mCount = pTotalItemCount - 2;
        if (pTotalItemCount > pVisibleItemCount) {
            mEnoughCount = true;
        }
        else {
            mEnoughCount = false;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView pView, int pScrollState) {
        if (mCanLoadMore) { //
            if (mLastItemIndex == mCount && pScrollState == SCROLL_STATE_IDLE) {
                // SCROLL_STATE_IDLE=0，
                if (mEndState != ENDINT_LOADING) {
                    if (mIsAutoLoadMore) { // 
                        if (mCanRefresh) {
                            // 
                            if (mHeadState != REFRESHING) {
                                // FootView
                                mEndState = ENDINT_LOADING;
                                onLoadMore();
                                changeEndViewByState();
                            }
                        }
                        else {
                            // 
                            mEndState = ENDINT_LOADING;
                            onLoadMore();
                            changeEndViewByState();
                        }
                    }
                    else {
                        // 
                        mEndState = ENDINT_MANUAL_LOAD_DONE;
                        changeEndViewByState();
                    }
                }
            }
        }
        else if (mEndRootView != null && mEndRootView.getVisibility() == VISIBLE) {
            // 
            System.out.println("this.removeFooterView(endRootView);...");
            mEndRootView.setVisibility(View.GONE);
            this.removeFooterView(mEndRootView);
        }
    }

    /**
     *
     * 
     * @date 
     * @change hillfly
     */
    private void changeEndViewByState() {
        if (mCanLoadMore) {
            // 
            switch (mEndState) {
                case ENDINT_LOADING: // 

                    // 
                    if (mEndLoadTipsTextView.getText()
                            .equals(R.string.lv_refresh_doing_end_refresh)) {
                        break;
                    }
                    mEndLoadTipsTextView.setText(R.string.lv_refresh_doing_end_refresh);
                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
                    mEndLoadProgressBar.setVisibility(View.VISIBLE);
                    break;
                case ENDINT_MANUAL_LOAD_DONE: // 

                    // 
                    mEndLoadTipsTextView.setText(R.string.lv_refresh_end_click_load_more);
                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
                    mEndLoadProgressBar.setVisibility(View.GONE);

                    mEndRootView.setVisibility(View.VISIBLE);
                    break;
                case ENDINT_AUTO_LOAD_DONE: // 

                    // 
                    mEndLoadTipsTextView.setText(R.string.lv_refresh_end_load_more);
                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
                    mEndLoadProgressBar.setVisibility(View.GONE);

                    mEndRootView.setVisibility(View.VISIBLE);
                    break;
                default:
                    // 

                    // if (enoughCount) {
                    // endRootView.setVisibility(View.VISIBLE);
                    // } else {
                    // endRootView.setVisibility(View.GONE);
                    // }
                    break;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (mCanRefresh) {
            if (mCanLoadMore && mEndState == ENDINT_LOADING) {
                // 
                return super.onTouchEvent(event);
            }

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (mFirstItemIndex == 0 && !mIsRecored) {
                        mIsRecored = true;
                        mStartY = (int) event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:

                    if (mHeadState != REFRESHING && mHeadState != LOADING) {
                        if (mHeadState == DONE) {

                        }
                        if (mHeadState == PULL_TO_REFRESH) {
                            mHeadState = DONE;
                            changeHeaderViewByState();
                        }
                        if (mHeadState == RELEASE_TO_REFRESH) {
                            mHeadState = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }

                    mIsRecored = false;
                    mIsBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();

                    if (!mIsRecored && mFirstItemIndex == 0) {
                        mIsRecored = true;
                        mStartY = tempY;
                    }

                    if (mHeadState != REFRESHING && mIsRecored && mHeadState != LOADING) {

                        //
                        //
                        //
                        if (mHeadState == RELEASE_TO_REFRESH) {

                            setSelection(0);

                            //
                            if (((tempY - mStartY) / RATIO < mHeadViewHeight)
                                    && (tempY - mStartY) > 0) {
                                mHeadState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                            //
                            else if (tempY - mStartY <= 0) {
                                mHeadState = DONE;
                                changeHeaderViewByState();
                            }
                            //
                        }
                        //
                        if (mHeadState == PULL_TO_REFRESH) {

                            setSelection(0);

                            //
                            if ((tempY - mStartY) / RATIO >= mHeadViewHeight) {
                                mHeadState = RELEASE_TO_REFRESH;
                                mIsBack = true;
                                changeHeaderViewByState();
                            }
                            else if (tempY - mStartY <= 0) {
                                mHeadState = DONE;
                                changeHeaderViewByState();
                            }
                        }

                        if (mHeadState == DONE) {
                            if (tempY - mStartY > 0) {
                                mHeadState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        if (mHeadState == PULL_TO_REFRESH) {
                            mHeadView.setPadding(0, -1 * mHeadViewHeight + (tempY - mStartY)
                                    / RATIO, 0, 0);

                        }

                        if (mHeadState == RELEASE_TO_REFRESH) {
                            mHeadView.setPadding(0, (tempY - mStartY) / RATIO - mHeadViewHeight, 0,
                                    0);
                        }
                    }
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     *
     * 
     * @date 
     * @change hillfly
     */
    private void changeHeaderViewByState() {
        switch (mHeadState) {
            case RELEASE_TO_REFRESH:
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mTipsTextView.setVisibility(View.VISIBLE);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mArrowAnim);
                // 
                mTipsTextView.setText(R.string.lv_refresh_release_refresh);

                break;
            case PULL_TO_REFRESH:
                mProgressBar.setVisibility(View.GONE);
                mTipsTextView.setVisibility(View.VISIBLE);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.VISIBLE);
                // 
                if (mIsBack) {
                    mIsBack = false;
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mArrowReverseAnim);
                    // 
                    mTipsTextView.setText(R.string.lv_refresh_pull_to_refresh);
                }
                else {
                    // 
                    mTipsTextView.setText(R.string.lv_refresh_pull_to_refresh);
                }
                break;

            case REFRESHING:
                mHeadView.setPadding(0, 0, 0, 0);

                mProgressBar.setVisibility(View.VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.GONE);

                // 
                mTipsTextView.setText(R.string.lv_refresh_doing_head_refresh);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
            case DONE:
                mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

                mProgressBar.setVisibility(View.GONE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setImageResource(R.drawable.arrow);

                // 
                mTipsTextView.setText(R.string.lv_refresh_pull_to_refresh);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
        if (pRefreshListener != null) {
            mRefreshListener = pRefreshListener;
            mCanRefresh = true;
        }
    }

    public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
        if (pLoadMoreListener != null) {
            mLoadMoreListener = pLoadMoreListener;
            mCanLoadMore = true;
            if (mCanLoadMore && getFooterViewsCount() == 0) {
                addFooterView();
            }
        }
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    private void onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    /**
     * 
     * 
     * @date 2015年2月14日 上午11:17
     * @change hillfly
     */
    public void onRefreshComplete() {
        // 
        if (mIsMoveToFirstItemAfterRefresh)
            setSelection(0);

        mHeadState = DONE;
        // 
        mLastUpdatedTextView.setText(getResources().getString(R.string.lv_refresh_refresh_lasttime)
                + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
        changeHeaderViewByState();
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    private void onLoadMore() {
        if (mLoadMoreListener != null) {
            // 
            mEndLoadTipsTextView.setText(R.string.lv_refresh_doing_end_refresh);
            mEndLoadTipsTextView.setVisibility(View.VISIBLE);
            mEndLoadProgressBar.setVisibility(View.VISIBLE);

            mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * 
     * 
     * @date 
     * @change hillfly
     */
    public void onLoadMoreComplete() {
        if (mIsAutoLoadMore) {
            mEndState = ENDINT_AUTO_LOAD_DONE;
        }
        else {
            mEndState = ENDINT_MANUAL_LOAD_DONE;
        }
        changeEndViewByState();
    }

    /**
     * 
     * 
     * @param adapter
     * @date 
     * @change hillfly
     */
    public void setAdapter(BaseAdapter adapter) {
        // 
        mLastUpdatedTextView.setText(getResources().getString(R.string.lv_refresh_refresh_lasttime)
                + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
        super.setAdapter(adapter);
    }

}
