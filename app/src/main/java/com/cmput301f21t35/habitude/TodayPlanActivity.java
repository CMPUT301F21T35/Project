package com.cmput301f21t35.habitude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TodayPlanActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ListView today_habitList;
    ArrayAdapter<Habit> today_habitAdapter;
    ArrayList<Habit> today_habitDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_plan);

        NavigationBarView navigationBarView = findViewById(R.id.navigation_today);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_today);

        today_habitList = findViewById(R.id.today_plan_list);
        today_habitDataList = new ArrayList<>();
        today_habitAdapter = new HabitList(this,today_habitDataList);
        today_habitList.setAdapter(today_habitAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                today_habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitName = doc.getId();
                    String habitDate = (String) doc.getData().get("Date");
                    String habitReason = (String) doc.getData().get("Habit Reason");
                    stringToDate(habitDate);
                    Date current_date = Calendar.getInstance().getTime();

                    if (doc.getData().get("Plan") != null) {
                        String[] WeekPlan = doc.getData().get("Plan").toString().split(",", 0);
                        ArrayList<String> habitWeekday = new ArrayList<>();
                        Collections.addAll(habitWeekday, WeekPlan);
                        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
                        Habit todayHabit = new Habit(habitName,habitReason,habitDate,habitWeekday);

                        if (stringToDate(habitDate).before(current_date)) {
                            for (int i = 0; i < todayHabit.getPlan().size(); i++) {
                                if (weekday_name.equals(todayHabit.getPlan().get(i))) {
                                    today_habitDataList.add(todayHabit);
                                    today_habitAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }

                if (today_habitDataList.size() == 0) {
                    TextView today_no_habit = findViewById(R.id.today_no_habits);
                    today_no_habit.setVisibility(View.VISIBLE);
                    today_no_habit.setText("No habits planned for today!");
                }
            }

        });

    }

    public static Date stringToDate(String habitDate){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date date = null;
        try {
            date = format.parse(habitDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_today):
                return true;
            case (R.id.action_habits):
                Intent intent_all_habits = new Intent(this,MainActivity.class);
                intent_all_habits.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_all_habits.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_all_habits);
                return true;
            case (R.id.action_profile):
                return true;
            case (R.id.action_following):
                return true;
        }
        return false;
    }
}