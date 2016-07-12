package com.dayton.drone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MySearchResultAdapter;
import com.dayton.drone.adapter.SortAdapter;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.model.SortModel;
import com.dayton.drone.model.WorldClock;
import com.dayton.drone.view.PinyinComparator;
import com.dayton.drone.view.SideBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ChooseCityActivity extends BaseActivity {

    @Bind(R.id.choose_activity_city_list)
    ListView cityListView;
    @Bind(R.id.choose_activity_list_index_sidebar)
    SideBar sideBar;
    @Bind(R.id.world_clock_open_search)
    LinearLayout searchLinearLayout;
    @Bind(R.id.world_clock_user_search_city)
    EditText userSearchCityEdit;
    @Bind(R.id.world_clock_city_edit_ll)
    LinearLayout editSearchContent;
    @Bind(R.id.choose_activity_search_list_view)
    ListView showSearchResultListView;

    private boolean isChooseCity = false;
    private List<WorldClock> worldClockDataList;
    //    private ChooseCityAdapter cityAdapter;
    private SortAdapter adapter;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private List<SortModel> searchResult;
    private MySearchResultAdapter searchAdapter;
    private  InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_city_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }

        ButterKnife.bind(this);
        WorldClockDatabaseHelper worldClockDatabase = getModel().getWorldClockDatabaseHelper();
        worldClockDataList = worldClockDatabase.getAll();
        pinyinComparator = new PinyinComparator();
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                isChooseCity = true;
                WorldClock worldClock = worldClockDataList.get(position);
                Intent intent = getIntent();
                intent.putExtra("isChooseFlag", isChooseCity);
                intent.putExtra("worldClock", worldClock.getTimeZoneName());
                setResult(0, intent);
                finish();
            }
        });

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    cityListView.setSelection(position);
                }

            }
        });

        SourceDateList = filledData(worldClockDataList);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        cityListView.setAdapter(adapter);
    }

    @OnClick(R.id.choose_activity_cancel_bt)
    public void cancelClick() {
        isChooseCity = false;
        back(false);
    }


    private void back(boolean flag) {
        if(imm != null){
            imm.hideSoftInputFromWindow(userSearchCityEdit.getWindowToken(),0);
        }
        Intent intent = getIntent();
        intent.putExtra("isChooseFlag", flag);
        setResult(0, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private List<SortModel> filledData(List<WorldClock> worldClockList) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < worldClockList.size(); i++) {
            SortModel sortModel = new SortModel();

            sortModel.setName(worldClockList.get(i).getTimeZoneTitle().split(",")[0]);
            sortModel.setTimeZoneName(worldClockList.get(i).getTimeZoneName());
            String pinyin = worldClockList.get(i).getTimeZoneCategory();
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


    private TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchResult.clear();
            search();
            searchAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @OnClick(R.id.world_clock_open_search)
    public void startSearchCity() {
        searchLinearLayout.setVisibility(View.GONE);
        editSearchContent.setVisibility(View.VISIBLE);
        showSearchResultListView.setVisibility(View.VISIBLE);
        searchResult = new ArrayList<>();
        userSearchCityEdit.requestFocus();
        //打开软键盘

              imm  = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        userSearchCityEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode()
                        == KeyEvent.KEYCODE_ENTER)) {
                    searchResult.clear();
                    search();
                    return true;
                }
                return false;
            }
        });

        userSearchCityEdit.addTextChangedListener(myTextWatcher);

        showSearchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                isChooseCity = true;
                SortModel  sortModel = searchResult.get(position);
                Intent intent = getIntent();
                intent.putExtra("isChooseFlag", isChooseCity);
                intent.putExtra("worldClock", sortModel.getTimeZoneName());
                setResult(0, intent);
                finish();
            }
        });
    }


    private void search() {
        String userInputSearchCityName = userSearchCityEdit.getText().toString();
        if (!userInputSearchCityName.isEmpty()) {
            for (SortModel bean : SourceDateList) {
                if (bean.getName().toLowerCase().contains(userInputSearchCityName.toLowerCase() )) {
                    searchResult.add(bean);
                }
            }
            if (searchResult.size() > 0) {
                Collections.sort(searchResult, pinyinComparator);
                searchAdapter = new MySearchResultAdapter(ChooseCityActivity.this, searchResult);
                showSearchResultListView.setAdapter(searchAdapter);
            }
        }
    }

    @OnClick(R.id.world_clock_search_city)
    public void startSearchCityBt() {
        back(false);
    }

}
