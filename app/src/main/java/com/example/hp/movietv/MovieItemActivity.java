package com.example.hp.movietv;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieItemActivity extends AppCompatActivity {
    RecyclerView castRecyclerView;
    RecyclerView crewRecyclerView;
    RecyclerView similarRecyclerView;
    Bundle bundle;
    CastRecyclerAdapter castRecyclerAdapter;
    CrewRecyclerAdapter crewRecyclerAdapter;
    MovieRecyclerAdapter similarRecyclerAdapter;
    ArrayList<Credits.Cast> castArrayList;
    ArrayList<Credits.Crew> crewArrayList;
    ArrayList<Movie> similarArrayList;
    ImageView expandImageView;
    TextView overViewContent;
    TextView taglineContent;
    TextView runtime;
    TextView releaseDate;
    TextView spokenLang;
    TextView productionCountry;
    TextView title;
    TextView genres;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    FrameLayout frameLayout;
    //    ImageView youTubeThumbnailView;
//    YouTubePlayerSupportFragment frag;
//    ImageButton playButton;
    ImageView poster;
    String videoKey;
    ProgressBar progressBar;
    NestedScrollView contentMovieItem;
    TextView viewAllReview;
    RecyclerView reviewRecyclerView;
    ReviewRecyclerAdapter reviewRecyclerAdapter;
    ArrayList<Reviews.Result> reviewArrayList;
    boolean isExpanded=false;
    TextView toolbarTitle;
    FavOpenHelper openHelper;
    boolean genreClicked=false;
    YouTubePlayer youTubePlayer=null;
    boolean isFullscreen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.movie_item_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Movies");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        castRecyclerView=findViewById(R.id.movie_item_cast_recycler);
        crewRecyclerView=findViewById(R.id.movie_item_crew_recycler);
        similarRecyclerView=findViewById(R.id.movie_item_similar_recycler);
        castArrayList=new ArrayList<>();
        crewArrayList=new ArrayList<>();
        similarArrayList=new ArrayList<>();
        openHelper=FavOpenHelper.getInstance(this);
        castRecyclerAdapter=new CastRecyclerAdapter(this, castArrayList, new CastRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(MovieItemActivity.this,CelebsDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.CELEB_ID,castArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        crewRecyclerAdapter=new CrewRecyclerAdapter(this, crewArrayList, new CrewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(MovieItemActivity.this,CelebsDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.CELEB_ID,crewArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        similarRecyclerAdapter=new MovieRecyclerAdapter(this, new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Intent intent=new Intent(MovieItemActivity.this,MovieItemActivity.class);
                intent.putExtra(Constants.MOVIE_ID,similarArrayList.get(position).id);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClicked(int position) {

            }
        }, similarArrayList);
        viewAllReview=findViewById(R.id.viewAllReviewRecycler);
        viewAllReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MovieItemActivity.this,ReviewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        reviewArrayList=new ArrayList<>();
        reviewRecyclerView=findViewById(R.id.reviewRecyclerView);
        reviewRecyclerAdapter=new ReviewRecyclerAdapter(this, reviewArrayList, new ReviewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCilck(int position) {

            }
        });
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        reviewRecyclerView.setAdapter(reviewRecyclerAdapter);
        contentMovieItem=findViewById(R.id.contentMovieItem);
        progressBar=findViewById(R.id.movieItemProgressBsr);
//        playButton=findViewById(R.id.playButton);
        poster=findViewById(R.id.movie_item_poster);
//        youTubeThumbnailView=findViewById(R.id.movie_item_trailerVideo);
        overViewContent=findViewById(R.id.movie_item_overview_content);
        taglineContent=findViewById(R.id.movie_item_tagline_content);
        runtime=findViewById(R.id.runtime_content);
        releaseDate=findViewById(R.id.movie_item_release_date);
        spokenLang=findViewById(R.id.movie_item_spokenLanguages);
        productionCountry=findViewById(R.id.movie_item_production_countries);
        title=findViewById(R.id.movie_item_title);
        genres=findViewById(R.id.movie_item_genre);
        castRecyclerView.setAdapter(castRecyclerAdapter);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        crewRecyclerView.setAdapter(crewRecyclerAdapter);
        crewRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        similarRecyclerView.setAdapter(similarRecyclerAdapter);
        similarRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(videoKey.length()!=0) {
//                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(Constants.YOUTUBE_BASE_URL+videoKey));
//                    startActivity(intent);
//                }
//            }
//        });
        expandImageView=findViewById(R.id.expand);
        expandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExpanded){
                    overViewContent.setMaxLines(Integer.MAX_VALUE);
                    isExpanded=true;
                    expandImageView.setBackgroundResource(R.drawable.ic_collapse);
                }
                else{
                    overViewContent.setMaxLines(3);
                    isExpanded=false;
                    expandImageView.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });
        genres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!genreClicked){
                    genres.setMaxLines(Integer.MAX_VALUE);
                    genreClicked=true;
                }
                else {
                    genres.setMaxLines(1);
                    genreClicked=false;
                }
            }
        });
        frameLayout=findViewById(R.id.movie_item_trailerVideo);
//        frag=(YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.movie_item_trailerVideo);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.movie_item_trailerVideo,youTubePlayerFragment).commit();
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            findDataFromBundle();
        }
    }

    private void findDataFromBundle() {
        contentMovieItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if(bundle.containsKey(Constants.MOVIE_ID));
        final int movieId=bundle.getInt(Constants.MOVIE_ID);
        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.TMDB_BASE_URL).build();
        MovieAPI movieAPI=retrofit.create(MovieAPI.class);
        String movieIdString= Integer.toString(movieId);
        Call<Movie> call=movieAPI.getMovieDetails(movieIdString);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                final Movie movie = response.body();
                toolbarTitle.setText(movie.title);
                if(movie.videos.results.size()!=0) {
                    videoKey = movie.videos.results.get(0).key;
                    youTubePlayerFragment.initialize(AppConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
                            if (!wasRestored) {
                                if(videoKey!=null) {
                                    youTubePlayer=player;
                                    player.cueVideo(videoKey);
                                    player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                        @Override
                                        public void onFullscreen(boolean b) {
                                            isFullscreen=b;
                                        }
                                    });
                                    player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                                        @Override
                                        public void onPlaying() {

                                        }

                                        @Override
                                        public void onPaused() {

                                        }

                                        @Override
                                        public void onStopped() {

                                        }

                                        @Override
                                        public void onBuffering(boolean b) {
                                            player.setFullscreen(true);
                                        }

                                        @Override
                                        public void onSeekTo(int i) {

                                        }
                                    });


                                }
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                            String errorMessage = error.toString();
                            Toast.makeText(MovieItemActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            Log.d("errorMessage:", errorMessage);

                        }
                    });
//                    Picasso.get().load("http://img.youtube.com/vi/" + videoKey + "/mqdefault.jpg").resize(2000, 900).into(youTubeThumbnailView);
                }
                else {
//                    playButton.setVisibility(View.GONE);
                    frameLayout.setBackgroundResource(R.drawable.no_video_availaible);

                }
                castArrayList.clear();
                ArrayList<Credits.Cast> casts=movie.credits.cast;
                for(int i=0;i<casts.size();i++){
                    if(casts.get(i).profile_path!=null){
                        castArrayList.add(casts.get(i));
                    }
                }
//                castArrayList.addAll(casts);
                castRecyclerAdapter.notifyDataSetChanged();
                crewArrayList.clear();
                ArrayList<Credits.Crew> crews=movie.credits.crew;
                for(int i=0;i<crews.size();i++){
                    if(crews.get(i).profile_path!=null){
                        crewArrayList.add(crews.get(i));
                    }
                }
//                crewArrayList.addAll(crews);
                crewRecyclerAdapter.notifyDataSetChanged();
                overViewContent.setText(movie.overview);
                taglineContent.setText(movie.tagline);
                title.setText(movie.title);
                runtime.setText(movie.runtime + "mins");
                releaseDate.setText("Release Date: "+movie.release_date);
                ArrayList<Movie> similar=movie.similar.results;
                similarArrayList.clear();
                similarArrayList.addAll(similar);
                similarRecyclerAdapter.notifyDataSetChanged();
                ArrayList<SpokenLanguage> spokenLanguages = movie.spoken_languages;
                spokenLang.setText("");
                for (int i = 0; i < spokenLanguages.size(); i++) {
                    String string = spokenLang.getText().toString();
                    if (string.length() == 0) {
                        spokenLang.setText("Spoken Languages: " + spokenLanguages.get(i).name);
                    } else {
                        spokenLang.setText(string + ", " + spokenLanguages.get(i).name);
                    }

                }
                ArrayList<ProductionCountry> productionCountries = movie.production_countries;
                productionCountry.setText("");
                for (int i = 0; i < productionCountries.size(); i++) {
                    String string = productionCountry.getText().toString();
                    if (string.length() == 0) {
                        productionCountry.setText("Production Countries: " + productionCountries.get(i).name);
                    } else {
                        productionCountry.setText(string + ", " + productionCountries.get(i).name);
                    }

                }
                ArrayList<Genre> genreArrayList = movie.genres;
                genres.setText("");
                for (int i = 0; i < genreArrayList.size(); i++) {
                    String string = genres.getText().toString();
                    if (string.length() == 0) {
                        genres.setText(genreArrayList.get(i).name);
                    } else {
                        genres.setText(string + ", " + genreArrayList.get(i).name);
                    }

                }
                ArrayList<Reviews.Result> reviews=movie.reviews.results;
                reviewArrayList.clear();
                reviewArrayList.addAll(reviews);
                reviewRecyclerAdapter.notifyDataSetChanged();
                Picasso.get().load(Constants.IMAGE_BASE_URL+"w185/"+movie.poster_path).resize(340,500).into(poster);
                contentMovieItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("ResponseMovieOpen",t.getMessage());
                Snackbar.make(contentMovieItem,"Network Error",Snackbar.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        if(youTubePlayer!=null&&isFullscreen){
            youTubePlayer.setFullscreen(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isFullscreen=false;

        }
        else {
            super.onBackPressed();
        }
    }

}
