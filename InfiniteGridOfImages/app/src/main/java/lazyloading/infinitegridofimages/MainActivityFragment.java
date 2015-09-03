package lazyloading.infinitegridofimages;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView imageGrid;
    private ProgressBar progress_bar;
    ArrayList<ImageData> imageDataList = new ArrayList<>();
    GridAdapter adapter;
    int pageCount = 1;
    boolean isDirty = false;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        imageGrid = (GridView) view.findViewById(R.id.image_grid);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        FetchData task = new FetchData();
        task.execute();
        adapter = new GridAdapter(imageDataList, getActivity());
        imageGrid.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    Log.d("List data over", "Load more");
                    if(!isDirty) {
                        isDirty = true;
                        pageCount++;
                        FetchData task = new FetchData();
                        task.execute();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
    }

    private class FetchData extends AsyncTask<String, Void, ArrayList<ImageData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ImageData> doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("feature", "popular");
            params.put("sort", "created_at");
            params.put("rpp", "20");
            params.put("image_size", "3");
            params.put("include_store", "store_download");
            params.put("include_states", "voted");
            params.put("page",String.valueOf(pageCount));
            return ParseJson.parseJson(RestServiceManager.getData(params));
        }

        @Override
        protected void onPostExecute(ArrayList<ImageData> datas) {
            super.onPostExecute(datas);
            imageDataList.addAll(datas);
            adapter.notifyDataSetChanged();
            progress_bar.setVisibility(View.GONE);
            isDirty= false;

        }

    }
}
