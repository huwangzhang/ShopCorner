package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwz.shopcorner.R;
import com.hwz.shopcorner.bean.Campaign;
import com.hwz.shopcorner.bean.HomeCampaign;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by huwang on 2017/6/19.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {


    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;

    private onHomeCategoryListener mCategoryListener;

    private Context mContext;
    private List<HomeCampaign> mDatas;

    public HomeCategoryAdapter(List<HomeCampaign> datas, Context context) {
        mDatas = datas;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {


        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_R) {

            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2, viewGroup, false));
        }

        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HomeCampaign category = mDatas.get(i);
        viewHolder.textTitle.setText(category.getTitle());
        Picasso.with(mContext).load(category.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Picasso.with(mContext).load(category.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Picasso.with(mContext).load(category.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else return VIEW_TYPE_L;


    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);


            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());
            if (mCategoryListener != null) {
                    anim(v);
            }
        }

        private void anim(final View v) {

            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    HomeCampaign campaign = mDatas.get(getLayoutPosition());

                    switch (v.getId()) {

                        case R.id.imgview_big:
                            mCategoryListener.onClick(v, campaign.getCpOne());
                            break;

                        case R.id.imgview_small_top:
                            mCategoryListener.onClick(v, campaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mCategoryListener.onClick(v, campaign.getCpThree());
                            break;

                    }

                }
            });
            animator.start();
        }
    }

    public interface onHomeCategoryListener {
        void onClick(View view, Campaign campaign);
    }

    public void setCategoryListener(onHomeCategoryListener listener) {
        this.mCategoryListener = listener;
    }
}
