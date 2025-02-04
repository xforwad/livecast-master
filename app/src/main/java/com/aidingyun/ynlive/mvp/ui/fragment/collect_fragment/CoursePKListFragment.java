package com.aidingyun.ynlive.mvp.ui.fragment.collect_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.app.utils.UpdateVersionUtils;
import com.aidingyun.ynlive.mvp.contract.Global;
import com.aidingyun.ynlive.mvp.model.entity.CollectLiveroomModel;
import com.aidingyun.ynlive.mvp.ui.adapter.collect.PKRecycleItemAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vector.update_app.HttpManager;

import org.json.JSONObject;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 个人收藏——直播间
 */
public class CoursePKListFragment extends Fragment {
    private RelativeLayout network_fail;

    RecyclerView recycler_view;
    SmartRefreshLayout refreshLayout;
    PKRecycleItemAdapter pkRecycleItemAdapter;

    private UpdateVersionUtils updateVersionUtils = null;
    CollectLiveroomModel collectLiveroomModel;
    private int currentPage = 1;

    public static CoursePKListFragment newInstance() {
        Bundle args = new Bundle();
//        args.putString("hostid", hostid);
        CoursePKListFragment fragment = new CoursePKListFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_rv_refresh_loadmore, container, false);
        updateVersionUtils = new UpdateVersionUtils();
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(recycler_view.getContext(), 1, GridLayoutManager.VERTICAL, false));

//        network_fail = view.findViewById(R.id.network_fail);//网络连接失败布局

        refreshLayout = view.findViewById(R.id.refreshLayout);

        // 下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                getCollectionLiveRooms();
            }
        });

        // 上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (collectLiveroomModel.getTotalPage() > 1 && collectLiveroomModel.getTotalPage() > currentPage) {
                    currentPage ++;
                    getCollectionLiveRooms();
                } else  {
                    if (refreshLayout.getState() == RefreshState.Loading) {
                        refreshLayout.finishLoadMore();
                    }
                    Toast.makeText(getActivity(), "已经是最后一页", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState); //获取参数
        Bundle arguments = getArguments(); //改变值
//        mTv.setText(arguments.getString("tag"));
        getCollectionLiveRooms();
    }


    @SuppressLint("LongLogTag")
    private void getCollectionLiveRooms() {
        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        reqBody.put("op", "room");
        updateVersionUtils.postByName(Global.GET_COLLECT_SERVICE_NAME, reqBody, new HttpManager.Callback() {
            @Override
            public void onResponse(String result) {

                Log.e("CoursePKListFragment","result++++++++++++++++"+result);

                if (refreshLayout.getState() == RefreshState.Refreshing) {
                    refreshLayout.finishRefresh();
                }
                if (refreshLayout.getState() == RefreshState.Loading) {
                    refreshLayout.finishLoadMore();
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("success")){
                        Gson gson = new Gson();
                        collectLiveroomModel = gson.fromJson(result,CollectLiveroomModel.class);
                        pkRecycleItemAdapter = new PKRecycleItemAdapter(getActivity(),collectLiveroomModel.getList());
                        recycler_view.setAdapter(pkRecycleItemAdapter);
                    }else{
                        if (currentPage > 1) currentPage --;
                        ToastUtils.showError(getActivity(),jsonObject.getString("msg"));
                    }
                }catch (Exception e){
                    if (currentPage > 1) currentPage --;
                    e.getMessage();
                }

            }

            @Override
            public void onError(String error) {

                if (refreshLayout.getState() == RefreshState.Refreshing) {
                    refreshLayout.finishRefresh();
                }
                if (refreshLayout.getState() == RefreshState.Loading) {
                    refreshLayout.finishLoadMore();
                }

                if (currentPage > 1) currentPage --;

                ToastUtils.showError(getActivity(), error);
                Log.e("CoursePKListFragment","error++++++++++++++++"+error);
            }
        });

    }

}
