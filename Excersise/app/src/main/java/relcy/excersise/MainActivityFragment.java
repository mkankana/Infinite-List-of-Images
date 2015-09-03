package relcy.excersise;


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

    View view;
    GridView gridView;
    ProgressBar progressBar;
    int pageCount = 1;
    GridAdapter adapter;
    ArrayList<DataModel> imageDataList = new ArrayList<>();
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
        view = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) view.findViewById(R.id.grid_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        FetchData task = new FetchData();
        task.execute();
        adapter = new GridAdapter(imageDataList, getActivity());
        gridView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    Log.d("List data over", "Load more");
                    if (!isDirty) {
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

    private class FetchData extends AsyncTask<String, Void, ArrayList<DataModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<DataModel> doInBackground(String... strings) {

            HashMap<String, String> params = new HashMap<>();
            params.put("feature", "popular");
            params.put("sort", "created_at");
            params.put("rpp", "20");
            params.put("image_size", "3");
            params.put("include_store", "store_download");
            params.put("include_states", "voted");
            params.put("page", String.valueOf(pageCount));


            return ParseJson.getObjects(RestServiceManager.getJsonData(params));
        }

        @Override
        protected void onPostExecute(ArrayList<DataModel> dataModels) {
            super.onPostExecute(dataModels);
            imageDataList.addAll(dataModels);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            isDirty = false;
        }
    }

}
