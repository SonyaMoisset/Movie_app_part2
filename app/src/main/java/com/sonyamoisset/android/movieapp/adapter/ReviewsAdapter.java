package com.sonyamoisset.android.movieapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonyamoisset.android.movieapp.R;
import com.sonyamoisset.android.movieapp.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {


    private final List<Review> reviewList;

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView movieReviewAuthor;
        private final TextView movieReviewContent;

        private ReviewViewHolder(View view) {
            super(view);

            movieReviewAuthor = view.findViewById(R.id.movie_review_author);
            movieReviewContent = view.findViewById(R.id.movie_review_content);
        }
    }

    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_list_items, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewViewHolder holder, int position) {
        String reviewAuthor = reviewList.get(position).getAuthor();
        String reviewContent = reviewList.get(position).getContent();

        holder.movieReviewAuthor.setText(reviewAuthor);
        holder.movieReviewContent.setText(reviewContent);
    }

    @Override
    public int getItemCount() {
        return (reviewList == null) ? 0 : reviewList.size();
    }
}
