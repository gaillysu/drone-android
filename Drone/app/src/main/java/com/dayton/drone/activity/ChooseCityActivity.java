package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.ChooseCityAdapter;
import com.dayton.drone.adapter.MySearchResultAdapter;
import com.dayton.drone.utils.CheckEmailFormat;
import com.dayton.drone.view.PinyinComparator;
import com.dayton.drone.view.SideBar;
import com.dayton.drone.viewmodel.ChooseCityViewModel;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.worldclock.City;

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
    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;

    private List<ChooseCityViewModel> chooseCityViewModelsList;
    private ChooseCityAdapter adapter;
    private PinyinComparator pinyinComparator;
    private List<ChooseCityViewModel> searchResultViewModelsList;
    private MySearchResultAdapter searchAdapter;
    private List<City> mCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_city_layout);
        chooseCityViewModelsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }

        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView titleTv = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        titleTv.setText(getString(R.string.choose_activity_title_choose_city_tv));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCities = getModel().getWorldClockDatabaseHelp().getAll();
        for (City city : mCities) {
            chooseCityViewModelsList.add(new ChooseCityViewModel(city));
        }
        pinyinComparator = new PinyinComparator();
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                addWorldClock(chooseCityViewModelsList.get(position).getCityId());
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

        Collections.sort(chooseCityViewModelsList, pinyinComparator);
        adapter = new ChooseCityAdapter(this, chooseCityViewModelsList);
        cityListView.setAdapter(adapter);
        searchResultViewModelsList = new ArrayList<>();
        searchAdapter = new MySearchResultAdapter(ChooseCityActivity.this, searchResultViewModelsList);
    }

    private TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchResultViewModelsList.clear();
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
        userSearchCityEdit.requestFocus();
        CheckEmailFormat.openInputMethod(ChooseCityActivity.this);
        userSearchCityEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode()
                        == KeyEvent.KEYCODE_ENTER)) {
                    searchResultViewModelsList.clear();
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
                addWorldClock(searchResultViewModelsList.get(position).getCityId());

            }
        });
    }

    private void search() {
        String userInputSearchCityName = userSearchCityEdit.getText().toString();
        if (!userInputSearchCityName.isEmpty()) {
            for (ChooseCityViewModel chooseCityViewModel : chooseCityViewModelsList) {

                if (chooseCityViewModel.getDisplayName().toLowerCase().contains(userInputSearchCityName.toLowerCase())) {
                    searchResultViewModelsList.add(chooseCityViewModel);
                }
            }
            if (searchResultViewModelsList.size() > 0) {
                Collections.sort(searchResultViewModelsList, pinyinComparator);

                showSearchResultListView.setAdapter(searchAdapter);
            }
        }
    }

    public void addWorldClock(int cityId) {
        int count = 0;
        for (City city : mCities) {
            chooseCityViewModelsList.add(new ChooseCityViewModel(city));
            if (city.isSelected()) {
                count++;
            }
        }
        if (count < 5) {
            City city = getModel().getWorldClockDatabaseHelp().get(cityId);
            if (city != null) {
                city.setSelected(true);
                getModel().getWorldClockDatabaseHelp().update(city);
                Intent intent = getIntent();
                intent.putExtra(getString(R.string.is_choose_flag), true);
                setResult(0, intent);
                finish();
            } else {
                Log.w("Karl", "There is something terribly wrong");
            }
        } else {
            Toast.makeText(ChooseCityActivity.this, getResources().getString(R.string.add_city_toast_text)
                    , Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.world_clock_search_city)
    public void startSearchCityBt() {
        finish();
    }

}
