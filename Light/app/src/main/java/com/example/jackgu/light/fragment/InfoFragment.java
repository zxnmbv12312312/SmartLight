package com.example.jackgu.light.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.jackgu.light.HelpActivity;
import com.example.jackgu.light.R;
//import com.example.jackgu.light.activity.AboutUsActivity;
import com.example.jackgu.light.others.FragmentHelper;
import com.example.jackgu.light.view.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InfoFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private TitleView mTitle;

    private LinearLayout listInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mParent = view;
        listInfo = (LinearLayout) view.findViewById(R.id.list_info);
        mTitle = (TitleView) mParent.findViewById(R.id.info_title);
        mTitle.setTitle(R.string.title_info);
        TextView aboutus = (TextView)view.findViewById(R.id.about_us_text);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentIndicator.setIndicator(4);
                AboutUsFragment detailFragment = new AboutUsFragment();
                getFragmentManager().beginTransaction().addToBackStack("aboutus").replace(R.id.fragment_container, detailFragment, "aboutus").commit();
            }
        });
        TextView sharefacebook = (TextView)view.findViewById(R.id.share_facebook_text);

        sharefacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                Map<String,ResolveInfo> mAppInfo = getShareList("","",getActivity());
                intent.setPackage(mAppInfo.get(getString(R.string.Facebook)).activityInfo.packageName);
                intent.setType("text/plain"); //"image/*"
                intent.putExtra(Intent.EXTRA_SUBJECT,"share light");
                intent.putExtra(Intent.EXTRA_TEXT, "I find this wonderful App, please download it!");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "选择分享类型"));
            }
        });

        TextView sharelike = (TextView)view.findViewById(R.id.share_like_text);
        sharelike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView helpus = (TextView)view.findViewById(R.id.help_us_text);
        helpus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "test@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        TextView comment = (TextView)view.findViewById(R.id.comment_text);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private Map<String,ResolveInfo> getShareList(final String title, final String content, final Activity mActivity)
    {
        final Map<String,ResolveInfo> appInfo = new HashMap<String, ResolveInfo>();
        List<ResolveInfo> appList = getShareTargets(mActivity);
        String[]items = null;
        if(appList.size()>0)
        {
            for(int i = 0; i < appList.size(); i++)
            {
                ResolveInfo tmp_ri = (ResolveInfo)appList.get(i);
                ApplicationInfo apinfo = tmp_ri.activityInfo.applicationInfo;
                String tmp_appName = apinfo.loadLabel(mActivity.getPackageManager()).toString();
                if(tmp_appName.equals(getString(R.string.Facebook)))
                {
                    appInfo.put(tmp_appName, tmp_ri);
                }//imgIds = {R.drawable.e_address_book, R.drawable.e_weibo, R.drawable.e_weixin};
            }

        }
        return appInfo;
    }

    private List<ResolveInfo> getShareTargets(Activity activity)
    {
        Intent intent=new Intent(Intent.ACTION_SEND,null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pm = activity.getPackageManager();
        return pm.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
/*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //String str = adapter.getItem(position);
        ShowFragmentByItem(position);
        l.setVisibility(View.INVISIBLE);
    }*/

    private void ShowFragmentByItem(int position)
    {
        FragmentTransaction transaction =  getFragmentManager().beginTransaction();

        switch (position){
            case 0:
//                Intent intent = new Intent(mActivity, AboutUsActivity.class);
//                startActivity(intent);
//                break;
                AboutUsFragment detailFragment = new AboutUsFragment();
                transaction.addToBackStack(null).replace(R.id.detail_fragment, detailFragment, "detail");
                transaction.commit();

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
