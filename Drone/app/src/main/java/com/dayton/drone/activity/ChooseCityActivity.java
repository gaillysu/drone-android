package com.dayton.drone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MySearchResultAdapter;
import com.dayton.drone.adapter.SortAdapter;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.model.SortModel;
import com.dayton.drone.model.WorldClock;
import com.dayton.drone.view.CharacterParser;
import com.dayton.drone.view.PinyinComparator;
import com.dayton.drone.view.SideBar;

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
    private WorldClockDatabaseHelper worldClockDatabase;
    //    private ChooseCityAdapter cityAdapter;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private List<SortModel> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_city_layout);
        ButterKnife.bind(this);

        worldClockDatabase = getModel().getWorldClockDatabaseHelper();
        worldClockDataList = worldClockDatabase.getAll();
        pinyinComparator = new PinyinComparator();
        characterParser = CharacterParser.getInstance();
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                selectAddCity(position);
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
        back(isChooseCity);
    }

    public void selectAddCity(int position){
        isChooseCity = true;
        WorldClock worldClock = worldClockDataList.get(position);
        Intent intent = getIntent();
        intent.putExtra("isChooseFlag", isChooseCity);
        intent.putExtra("worldClock", worldClock.getTimeZoneName());
        setResult(0, intent);
        finish();
    }

    private void back(boolean flag) {
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

    @OnClick(R.id.world_clock_open_search)
    public void startSearchCity() {
        searchLinearLayout.setVisibility(View.GONE);
        editSearchContent.setVisibility(View.VISIBLE);
        showSearchResultListView.setVisibility(View.VISIBLE);
        userSearchCityEdit.setFocusable(true);
        searchResult = new ArrayList<>();
        InputMethodManager imm = (InputMethodManager) ChooseCityActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        showSearchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectAddCity(position);
            }
        });
    }

    private void search() {

        String userInputSearchCityName = userSearchCityEdit.getText().toString();
        for (SortModel bean : SourceDateList) {
            if (bean.getName().contains(userInputSearchCityName)) {
                searchResult.add(bean);
            }
        }
        if (searchResult.size() > 0) {
            showSearchResultListView.setAdapter(new MySearchResultAdapter(ChooseCityActivity.this, searchResult));
        } else {
            Toast.makeText(this, getString(R.string.no_search_result), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.world_clock_search_city)
    public void startSearchCityBt() {
        back(false);
    }

}
