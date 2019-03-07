package com.rit.intelligrow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
// https://stackoverflow.com/questions/43055661/reading-csv-file-in-android-app
// https://stackoverflow.com/questions/9544737/read-file-from-assets
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private FusedLocationProviderClient mFusedLocationClient;

    private EditText statesList;
    private EditText districtsList;
    private EditText cropsList;
    private EditText soilList;
    private EditText area;

    private ImageButton getLocation;
    private Button submit;

    private final String [] crops = {"Arecanut", "Other Kharif pulses", "Rice", "Banana", "Cashewnut",
                                     "Coconut ", "Dry ginger", "Sugarcane", "Sweet potato", "Tapioca",
                                     "Black pepper", "Dry chillies", "other oilseeds", "Turmeric", "Maize",
                                     "Moong(Green Gram)", "Urad", "Arhar/Tur", "Groundnut", "Sunflower",
                                     "Bajra", "Castor seed", "Cotton(lint)", "Horse-gram", "Jowar", "Ragi",
                                     "Tobacco", "Gram", "Wheat", "Masoor", "Sesamum", "Linseed", "Safflower",
                                     "Onion", "Small millets", "Coriander", "Potato", "Other  Rabi pulses",
                                     "Soyabean", "Rapeseed &Mustard", "Mesta", "Niger seed", "Sannhamp",
                                     "Garlic", "Oilseeds total", "Jute", "Peas & beans (Pulses)", "Barley",
                                     "Khesari", "Other Cereals & Millets", "Moth", "Cardamom"};

    private final Map< String, HashMap< String, Integer[] > > locationDetails =
            new HashMap<String, HashMap< String, Integer[] > >()
            {{
                put("Andhra Pradesh", new HashMap<String, Integer[]>()
                                    {{
                                        put("Anantapur", new Integer[]{0, 1, 2, 3, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36});
                                        put("East Godavari", new Integer[]{0, 2, 3, 4, 5, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 40});
                                        put("Chittoor", new Integer[]{1, 2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 35, 36, 37});
                                        put("Kadapa", new Integer[]{1, 2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35});
                                        put("Visakhapatanam", new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 36, 37, 41});
                                        put("Guntur", new Integer[]{2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 26, 27, 30, 33, 34, 39});
                                        put("Krishna", new Integer[]{2, 3, 4, 5, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 26, 27, 30, 33, 39});
                                        put("Kurnool", new Integer[]{2, 3, 5, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 28, 30, 32, 33, 34, 35, 39});
                                        put("Prakasam", new Integer[]{2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 35, 39});
                                        put("Spsr Nellore", new Integer[]{2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33});
                                        put("Srikakulam", new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 30, 33, 36, 40});
                                        put("Vizianagaram", new Integer[]{2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 40, 41});
                                        put("West Godavari", new Integer[]{2, 3, 4, 5, 7, 9, 11, 13, 14, 15, 16, 17, 18, 19, 22, 23, 24, 26, 27, 30, 34});
                                    }});
                put("Assam", new HashMap<String, Integer[]>()
                                    {{
                                        put("Barpeta", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Bongaigaon", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Cachar", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Darrang", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Dhemaji", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Dhubri", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Dibrugarh", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 28, 30, 33, 36, 39, 40, 45});
                                        put("Dima Hasao", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 28, 30, 33, 36, 39, 40, 41, 45});
                                        put("Goalpara", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Golaghat", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 33, 34, 36, 39, 40, 45});
                                        put("Hailakandi", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 22, 26, 27, 30, 31, 33, 34, 36, 39, 45});
                                        put("Jorhat", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 33, 34, 36, 39, 45});
                                        put("Kamrup", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Karbi Anglong", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Karimganj", new Integer[]{0, 2, 3, 5, 7, 8, 11, 13, 14, 17, 22, 26, 27, 30, 31, 33, 36, 39, 40, 45});
                                        put("Kokrajhar", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Lakhimpur", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Marigaon", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Nagaon", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Nalbari", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Sivasagar", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 27, 28, 30, 31, 33, 34, 36, 39, 40, 45});
                                        put("Sonitpur", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 33, 34, 36, 39, 40, 41, 45});
                                        put("Tinsukia", new Integer[]{0, 2, 3, 5, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 28, 30, 33, 34, 36, 39, 40, 41, 45});
                                    }});
                put("Karnataka", new HashMap<String, Integer[]>()
                                    {{
                                        put("Bangalore Rural", new Integer[]{0, 1, 2, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 21, 23, 25, 27, 30, 33, 34, 35, 36, 37, 39, 41, 43});
                                        put("Bellary", new Integer[]{0, 1, 2, 5, 7, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 37, 39, 41, 42});
                                        put("Bengaluru Urban", new Integer[]{0, 1, 2, 5, 6, 7, 10, 11, 14, 15, 16, 17, 18, 19, 21, 23, 25, 27, 30, 33, 34, 35, 36, 37, 39, 41, 43});
                                        put("Chamarajanagar", new Integer[]{0, 1, 2, 5, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 35, 36, 37, 41, 43, 51});
                                        put("Chikmagalur", new Integer[]{0, 1, 2, 5, 6, 7, 8, 10, 11, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 35, 36, 37, 39, 41, 51});
                                        put("Chitradurga", new Integer[]{0, 1, 2, 5, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 37, 39, 41});
                                        put("Dakshin Kannad", new Integer[]{0, 2, 5, 6, 7, 8, 9, 10, 11, 13, 15, 16, 23, 51});
                                        put("Davangere", new Integer[]{0, 1, 2, 5, 6, 7, 10, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 37, 39, 41, 43});
                                        put("Dharwad", new Integer[]{0, 1, 2, 5, 6, 7, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 43});
                                        put("Hassan", new Integer[]{0, 1, 2, 5, 6, 7, 8, 10, 11, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 35, 36, 37, 39, 41, 43, 51});
                                        put("Haveri", new Integer[]{0, 1, 2, 5, 6, 7, 11, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 25, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Kodagu", new Integer[]{0, 2, 5, 6, 8, 9, 10, 11, 13, 14, 18, 26, 51});
                                        put("Mandya", new Integer[]{0, 1, 2, 5, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 30, 33, 36, 37, 41});
                                        put("Mysore", new Integer[]{0, 1, 2, 5, 6, 7, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 33, 34, 35, 36, 37, 41, 43, 51});
                                        put("Shimoga", new Integer[]{0, 1, 2, 5, 6, 7, 10, 11, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 30, 31, 35, 37, 41, 51});
                                        put("Tumkur", new Integer[]{0, 1, 2, 5, 6, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 31, 33, 34, 35, 36, 37, 39, 41, 43});
                                        put("Udupi", new Integer[]{0, 2, 5, 6, 7, 8, 9, 10, 11, 13, 15, 16, 18, 23, 30});
                                        put("Uttar Kannad", new Integer[]{0, 2, 5, 6, 7, 8, 10, 11, 13, 14, 15, 16, 17, 18, 19, 22, 23, 24, 25, 27, 33, 37, 38, 42, 51});
                                        put("Bagalkot", new Integer[]{1, 2, 5, 7, 11, 13, 14, 15, 17, 18, 19, 20, 22, 23, 24, 27, 28, 30, 31, 32, 33, 35, 37, 38, 41, 42});
                                        put("Belgaum", new Integer[]{1, 2, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43});
                                        put("Bidar", new Integer[]{1, 2, 5, 6, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43});
                                        put("Gadag", new Integer[]{1, 2, 5, 7, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35, 37, 39, 41, 43});
                                        put("Gulbarga", new Integer[]{1, 2, 5, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 27, 28, 30, 31, 32, 33, 34, 35, 36, 38, 40, 41, 43});
                                        put("Kolar", new Integer[]{1, 2, 5, 11, 14, 17, 18, 21, 23, 25, 27, 30, 33, 34, 35, 36, 39, 41});
                                        put("Koppal", new Integer[]{1, 2, 5, 7, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 27, 28, 30, 31, 32, 33, 34, 35, 41});
                                        put("Raichur", new Integer[]{2, 5, 7, 11, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 27, 28, 30, 31, 32, 33});
                                        put("Bijapur", new Integer[]{20, 22, 31, 32, 43});
                                    }});
                put("Kerala", new HashMap<String, Integer[]>()
                                    {{
                                        put("Alappuzha", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 30});
                                        put("Ernakulam", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 30});
                                        put("Idukki", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 25, 43, 51});
                                        put("Kannur", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 30});
                                        put("Kasaragod", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 26, 30});
                                        put("Kollam", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 30});
                                        put("Kottayam", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 51});
                                        put("Kozhikode", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 51});
                                        put("Malappuram", new Integer[]{0, 2, 3, 4, 5, 6, 8, 9, 10, 13, 30, 51});
                                        put("Palakkad", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 18, 24, 25, 30, 51});
                                        put("Pathanamthitta", new Integer[]{0, 2, 3, 4, 5, 6, 7, 9, 10, 13, 51});
                                        put("Thiruvananthapuram", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13});
                                        put("Thrissur", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 30});
                                        put("Wayanad", new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 24, 30, 51});
                                    }});
                put("Meghalaya", new HashMap<String, Integer[]>()
                                    {{
                                        put("East Garo Hills", new Integer[]{0, 2, 3, 6, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 34, 36, 38, 39, 40, 45});
                                        put("East Jaintia Hills", new Integer[]{0, 2, 3, 6, 7, 8, 9, 11, 13, 14, 30, 36, 38, 39});
                                        put("East Khasi Hills", new Integer[]{0, 2, 3, 6, 8, 9, 11, 13, 14, 30, 34, 36, 38, 39});
                                        put("Ri Bhoi", new Integer[]{0, 2, 3, 6, 8, 9, 11, 13, 14, 34, 36, 38, 39});
                                        put("South Garo Hills", new Integer[]{0, 2, 3, 6, 7, 8, 9, 11, 13, 14, 17, 22, 26, 27, 30, 34, 36, 38, 39, 40, 45});
                                        put("West Garo Hills", new Integer[]{0, 2, 3, 6, 7, 8, 9, 11, 13, 14, 17, 21, 22, 26, 27, 28, 30, 31, 34, 36, 38, 39, 40, 45});
                                        put("West Khasi Hills", new Integer[]{0, 2, 3, 6, 7, 8, 9, 11, 13, 14, 26, 30, 34, 36, 38, 39});
                                    }});
                put("Bihar", new HashMap<String, Integer[]>()
                                    {{
                                        put("Banka", new Integer[]{1, 2, 7, 14, 15, 16, 17, 23, 27, 28, 29, 31, 37, 39, 42, 46, 47, 48});
                                        put("Begusarai", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 23, 24, 25, 26, 27, 28, 29, 30, 31, 34, 37, 39, 40, 42, 46, 47, 48});
                                        put("Bhagalpur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 20, 21, 23, 24, 26, 27, 28, 29, 30, 31, 34, 39, 40, 42, 46, 47, 48});
                                        put("Bhojpur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 20, 24, 25, 27, 28, 29, 30, 31, 34, 39, 46, 47, 48});
                                        put("Buxar", new Integer[]{1, 2, 7, 14, 15, 17, 19, 20, 24, 27, 28, 29, 31, 37, 39, 46, 47, 48});
                                        put("Gaya", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 23, 25, 27, 28, 29, 30, 31, 34, 39, 42, 45, 46, 47, 48});
                                        put("Gopalganj", new Integer[]{1, 2, 7, 14, 15, 16, 17, 25, 26, 28, 29, 34, 39, 46});
                                        put("Jamui", new Integer[]{1, 2, 7, 14, 15, 16, 17, 23, 27, 28, 29, 31, 39, 46, 47, 48});
                                        put("Jehanabad", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 23, 25, 27, 28, 29, 30, 31, 39, 42, 46, 47, 48});
                                        put("Khagaria", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 23, 24, 25, 27, 28, 29, 30, 31, 34, 37, 39, 40, 42, 46, 47, 48});
                                        put("Kishanganj", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 23, 25, 26, 27, 28, 29, 30, 31, 39, 40, 45, 46, 48});
                                        put("Madhepura", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 23, 25, 28, 29, 30, 31, 34, 39, 40, 42, 45, 46, 48});
                                        put("Madhubani", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 23, 25, 27, 28, 29, 30, 31, 37, 39, 40, 42, 45, 46, 47, 48});
                                        put("Munger", new Integer[]{1, 2, 7, 14, 15, 16, 17, 23, 27, 28, 29, 31, 34, 39, 42, 46, 47, 48});
                                        put("Nalanda", new Integer[]{1, 2, 7, 14, 15, 17, 18, 19, 25, 27, 28, 29, 31, 37, 39, 46, 47, 48});
                                        put("Nawada", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 23, 24, 25, 27, 28, 29, 30, 31, 34, 39, 42, 46, 47, 48});
                                        put("Pashchim Champaran", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 25, 27, 28, 29, 30, 31, 37, 39, 40, 42, 46, 47, 48});
                                        put("Patna", new Integer[]{1, 2, 7, 14, 15, 17, 19, 24, 25, 27, 28, 29, 31, 34, 39, 46, 47, 48});
                                        put("Rohtas", new Integer[]{1, 2, 7, 14, 15, 17, 18, 23, 27, 28, 29, 30, 31, 39, 46, 47, 48});
                                        put("Samastipur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 21, 25, 26, 27, 28, 29, 30, 31, 32, 34, 37, 39, 40, 42, 45, 46, 47, 48});
                                        put("Siwan", new Integer[]{1, 2, 7, 14, 15, 16, 17, 20, 25, 26, 27, 28, 29, 30, 31, 34, 37, 39, 40, 42, 46, 47});
                                        put("Supaul", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 23, 24, 25, 28, 29, 30, 31, 34, 39, 40, 42, 45, 46, 48});
                                        put("Vaishali", new Integer[]{1, 2, 7, 14, 15, 16, 17, 19, 25, 26, 27, 28, 29, 30, 31, 34, 37, 39, 40, 45, 46, 47, 48});
                                        put("Araria", new Integer[]{2, 14, 15, 16, 17, 19, 23, 25, 27, 28, 29, 30, 31, 34, 39, 40, 45, 46, 48});
                                        put("Darbhanga", new Integer[]{2, 7, 14, 15, 16, 17, 19, 25, 27, 28, 29, 30, 31, 39, 46, 47, 48});
                                        put("Kaimur (Bhabua)", new Integer[]{2, 7, 14, 15, 16, 17, 27, 28, 29, 31, 39, 46, 47, 48});
                                        put("Katihar", new Integer[]{2, 7, 14, 15, 16, 17, 19, 23, 26, 27, 28, 29, 30, 31, 34, 37, 39, 40, 45, 46, 47, 48});
                                        put("Lakhisarai", new Integer[]{2, 14, 15, 17, 27, 28, 29, 31, 39, 46, 48});
                                        put("Muzaffarpur", new Integer[]{2, 7, 14, 15, 16, 17, 19, 25, 26, 27, 28, 29, 30, 31, 32, 34, 37, 39, 40, 46, 47, 48});
                                        put("Purbi Champaran", new Integer[]{2, 7, 14, 15, 16, 17, 19, 28, 29, 30, 31, 34, 37, 39, 46, 47, 48});
                                        put("Purnia", new Integer[]{2, 7, 14, 15, 16, 17, 19, 23, 27, 28, 29, 30, 31, 39, 40, 45, 46, 48});
                                        put("Saharsa", new Integer[]{2, 7, 14, 15, 16, 19, 23, 25, 28, 29, 30, 31, 39, 40, 42, 45, 46, 47, 48});
                                        put("Saran", new Integer[]{2, 7, 14, 15, 16, 17, 19, 25, 27, 28, 29, 31, 34, 37, 39, 40, 42, 46, 47, 48});
                                        put("Sheikhpura", new Integer[]{2, 7, 14, 15, 17, 27, 28, 29, 31, 39, 46, 48});
                                        put("Sheohar", new Integer[]{2, 7, 14, 15, 17, 19, 25, 26, 28, 29, 30, 31, 34, 37, 39, 46, 47, 48});
                                        put("Sitamarhi", new Integer[]{2, 7, 14, 15, 17, 25, 28, 29, 30, 31, 37, 39, 46, 47, 48});
                                        put("Aurangabad", new Integer[]{14, 17, 18, 19, 22, 23, 24, 25, 27, 28, 29, 31, 37, 38, 39, 41, 46, 47, 48});
                                    }});
                put("Chhattisgarh", new HashMap<String, Integer[]>()
                                    {{
                                        put("Bastar", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 46, 48});
                                        put("Bilaspur", new Integer[]{1, 8, 19, 24, 31, 32, 33, 35, 37, 41, 42, 48});
                                        put("Dantewada", new Integer[]{1, 2, 3, 6, 8, 11, 13, 14, 15, 16, 17, 18, 19, 23, 24, 25, 27, 28, 30, 34, 35, 36, 37, 38, 39, 40, 41, 42});
                                        put("Dhamtari", new Integer[]{1, 2, 3, 7, 8, 11, 14, 15, 16, 17, 18, 19, 23, 24, 25, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 39, 40, 42, 43, 46, 48});
                                        put("Durg", new Integer[]{1, 2, 3, 7, 11, 13, 14, 15, 16, 17, 19, 23, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 38, 39, 42, 46, 47, 48});
                                        put("Janjgir-Champa", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 21, 23, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 42, 43, 46, 48});
                                        put("Jashpur", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 20, 23, 24, 25, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 46, 47, 48});
                                        put("Kanker", new Integer[]{1, 2, 3, 6, 8, 11, 13, 14, 15, 16, 17, 19, 23, 24, 25, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 46, 48});
                                        put("Korba", new Integer[]{1, 2, 3, 8, 11, 13, 14, 15, 16, 17, 18, 20, 23, 24, 27, 28, 29, 30, 31, 33, 34, 36, 37, 38, 39, 40, 41, 42, 43, 46, 47, 48});
                                        put("Korea", new Integer[]{1, 2, 6, 8, 11, 13, 14, 15, 16, 17, 18, 23, 24, 25, 27, 28, 29, 30, 31, 33, 34, 35, 36, 38, 39, 40, 41, 42, 43, 46, 47, 48});
                                        put("Mahasamund", new Integer[]{1, 2, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 23, 24, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 46, 48});
                                        put("Raigarh", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 23, 24, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 46, 48});
                                        put("Raipur", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 42, 46, 48});
                                        put("Rajnandgaon", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 19, 23, 24, 25, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 46, 47, 48});
                                        put("Surguja", new Integer[]{1, 2, 3, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 23, 24, 25, 26, 27, 28, 29, 30, 31, 33, 34, 35, 36, 38, 39, 40, 41, 42, 43, 46, 47, 48});
                                        put("Kabirdham", new Integer[]{2, 3, 7, 8, 11, 14, 15, 16, 17, 18, 20, 21, 22, 24, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 46, 48});
                                    }});
                put("Goa", new HashMap<String, Integer[]>()
                                    {{
                                        put("North Goa", new Integer[]{1, 18, 25, 37});
                                        put("South Goa", new Integer[]{1, 2, 18, 25, 37});
                                    }});
                put("Madhya Pradesh", new HashMap<String, Integer[]>()
                                    {{
                                        put("Balaghat", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 19, 21, 24, 25, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Barwani", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 33, 34, 35, 36, 38, 42, 43});
                                        put("Betul", new Integer[]{1, 2, 6, 7, 8, 11, 14, 16, 17, 18, 20, 22, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Bhind", new Integer[]{1, 2, 7, 8, 11, 16, 17, 20, 24, 27, 28, 30, 33, 36, 37, 38, 39, 42, 43, 47});
                                        put("Bhopal", new Integer[]{1, 2, 6, 7, 11, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 42, 43});
                                        put("Chhatarpur", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 42, 43, 47});
                                        put("Chhindwara", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Damoh", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 43, 47});
                                        put("Dhar", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 31, 33, 34, 35, 36, 38, 39, 42, 43, 47});
                                        put("Gwalior", new Integer[]{1, 2, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 42, 43, 47});
                                        put("Hoshangabad", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 24, 27, 28, 30, 33, 34, 35, 36, 37, 38, 39, 43, 47});
                                        put("Indore", new Integer[]{1, 2, 6, 7, 8, 11, 14, 16, 17, 18, 22, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 43});
                                        put("Jabalpur", new Integer[]{1, 2, 6, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Jhabua", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 33, 34, 36, 38, 39, 42, 43, 47});
                                        put("Katni", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 24, 25, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Khandwa", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 42, 43});
                                        put("Khargone", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 31, 33, 34, 35, 36, 38, 39, 42, 43});
                                        put("Mandla", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Mandsaur", new Integer[]{1, 8, 11, 14, 16, 17, 18, 20, 22, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 43, 47});
                                        put("Narsinghpur", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 19, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Neemuch", new Integer[]{1, 2, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 35, 38, 39, 42, 43, 47});
                                        put("Panna", new Integer[]{1, 2, 6, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Rajgarh", new Integer[]{1, 2, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 33, 35, 36, 38, 39, 42, 43, 47});
                                        put("Ratlam", new Integer[]{1, 2, 6, 8, 11, 13, 14, 16, 17, 18, 22, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 42, 43, 47});
                                        put("Rewa", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Sagar", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Satna", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 21, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 42, 43, 47});
                                        put("Seoni", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 19, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43});
                                        put("Shahdol", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Shivpuri", new Integer[]{1, 2, 6, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 42, 43, 47});
                                        put("Sidhi", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 38, 39, 41, 42, 43, 47});
                                        put("Tikamgarh", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 42, 43, 47});
                                        put("Umaria", new Integer[]{1, 2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 47});
                                        put("Datia", new Integer[]{2, 6, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 33, 35, 36, 38, 39, 42, 43, 47});
                                        put("Dewas", new Integer[]{2, 6, 7, 11, 14, 16, 17, 18, 22, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 42, 43, 47});
                                        put("Dindori", new Integer[]{2, 8, 11, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 34, 35, 36, 38, 39, 41, 42, 43, 47});
                                        put("Guna", new Integer[]{2, 6, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 33, 35, 36, 38, 39, 43, 47});
                                        put("Harda", new Integer[]{2, 6, 7, 8, 11, 14, 16, 17, 18, 22, 24, 27, 28, 30, 31, 33, 34, 35, 36, 38, 43, 47});
                                        put("Morena", new Integer[]{2, 7, 8, 11, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 36, 38, 39, 43, 47});
                                        put("Raisen", new Integer[]{2, 6, 7, 8, 11, 13, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 34, 35, 36, 38, 39, 43, 47});
                                        put("Sehore", new Integer[]{2, 6, 7, 8, 11, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 42, 43, 47});
                                        put("Shajapur", new Integer[]{2, 6, 7, 11, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 43, 47});
                                        put("Sheopur", new Integer[]{2, 7, 14, 16, 17, 18, 20, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 43, 47});
                                        put("Vidisha", new Integer[]{2, 6, 7, 8, 11, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 34, 36, 37, 38, 39, 42, 43, 47});
                                        put("Ujjain", new Integer[]{6, 7, 8, 11, 14, 16, 17, 18, 24, 27, 28, 30, 31, 33, 35, 36, 38, 39, 43, 47});
                                    }});
                put("Maharashtra", new HashMap<String, Integer[]>()
                                    {{
                                        put("Ahmednagar", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 27, 28, 30, 31, 32, 37, 38, 41});
                                        put("Akola", new Integer[]{1, 14, 15, 16, 17, 18, 19, 20, 22, 24, 27, 28, 30, 32, 38});
                                        put("Amravati", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 30, 32, 37, 38});
                                        put("Beed", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 31, 32, 37, 38, 41});
                                        put("Buldhana", new Integer[]{1, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 32, 37, 38});
                                        put("Chandrapur", new Integer[]{1, 2, 14, 15, 16, 17, 22, 24, 27, 28, 30, 31, 37, 38});
                                        put("Dhule", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 27, 28, 30, 37, 38});
                                        put("Gadchiroli", new Integer[]{1, 2, 14, 17, 24, 27, 28, 30, 31, 37, 38});
                                        put("Gondia", new Integer[]{1, 2, 7, 14, 15, 16, 17, 27, 28, 30, 31, 37});
                                        put("Hingoli", new Integer[]{1, 7, 14, 15, 16, 17, 18, 21, 22, 24, 27, 28, 30, 32, 37, 38, 41});
                                        put("Jalgaon", new Integer[]{1, 7, 14, 15, 16, 17, 18, 19, 20, 22, 24, 27, 28, 30, 32, 37, 38});
                                        put("Jalna", new Integer[]{1, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 32, 37, 38, 41});
                                        put("Kolhapur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 21, 24, 25, 27, 28, 37, 38, 41});
                                        put("Latur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 31, 32, 37, 38, 41});
                                        put("Nagpur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 30, 31, 37, 38});
                                        put("Nanded", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 30, 32, 37, 38, 41});
                                        put("Nandurbar", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 32, 37, 38});
                                        put("Nashik", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 27, 28, 30, 32, 37, 38, 41});
                                        put("Osmanabad", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 31, 32, 37, 38, 41});
                                        put("Parbhani", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 31, 32, 37, 38, 41});
                                        put("Pune", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 24, 25, 27, 28, 30, 32, 37, 38, 41});
                                        put("Raigad", new Integer[]{1, 2, 15, 16, 17, 18, 25, 37, 41});
                                        put("Ratnagiri", new Integer[]{1, 2, 15, 17, 25, 30, 37, 41});
                                        put("Sangli", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 22, 24, 27, 28, 30, 32, 37, 38, 41});
                                        put("Satara", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 27, 28, 30, 32, 37, 38, 41});
                                        put("Sindhudurg", new Integer[]{1, 2, 14, 15, 16, 18, 25, 30, 37, 41});
                                        put("Solapur", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 28, 30, 31, 32, 37, 38, 41});
                                        put("Thane", new Integer[]{1, 2, 15, 16, 17, 25, 27, 30, 37, 41});
                                        put("Washim", new Integer[]{1, 7, 14, 15, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 32, 38});
                                        put("Bhandara", new Integer[]{2, 7, 15, 16, 17, 18, 27, 28, 30, 31, 37, 38});
                                        put("Yavatmal", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 21, 22, 24, 27, 28, 30, 38});
                                        put("Wardha", new Integer[]{7, 14, 15, 16, 17, 18, 22, 24, 27, 28, 32, 38});
                                        put("Aurangabad", new Integer[]{14, 17, 18, 19, 22, 23, 24, 25, 27, 28, 29, 31, 37, 38, 39, 41, 46, 47, 48});
                                    }});
                put("Nagaland", new HashMap<String, Integer[]>()
                                    {{
                                        put("Kohima", new Integer[]{1, 2, 7, 14, 15, 17, 18, 19, 20, 21, 24, 27, 28, 30, 31, 34, 36, 37, 38, 39, 45, 46, 47});
                                        put("Mokokchung", new Integer[]{1, 2, 7, 14, 15, 17, 18, 19, 20, 21, 27, 28, 30, 31, 34, 36, 37, 38, 39, 46, 47});
                                        put("Mon", new Integer[]{1, 2, 7, 14, 15, 17, 18, 19, 20, 21, 24, 27, 28, 30, 31, 34, 36, 37, 38, 39, 45, 46, 47});
                                        put("Phek", new Integer[]{1, 2, 14, 15, 17, 18, 19, 20, 22, 24, 27, 28, 30, 31, 34, 36, 37, 38, 39, 46, 47});
                                        put("Tuensang", new Integer[]{1, 2, 14, 15, 17, 18, 19, 20, 21, 27, 28, 30, 31, 34, 36, 37, 38, 39, 46, 47});
                                        put("Wokha", new Integer[]{1, 2, 7, 14, 15, 17, 18, 19, 20, 21, 24, 27, 28, 30, 31, 34, 36, 37, 38, 39, 45, 46, 47});
                                        put("Zunheboto", new Integer[]{1, 2, 7, 14, 15, 17, 18, 19, 20, 24, 27, 28, 30, 31, 34, 36, 37, 38, 39, 46, 47});
                                        put("Dimapur", new Integer[]{2, 7, 14, 15, 17, 18, 19, 20, 21, 22, 27, 28, 30, 31, 36, 38, 39, 45, 46, 47});
                                    }});
                put("Sikkim", new HashMap<String, Integer[]>()
                                    {{
                                        put("East District", new Integer[]{1, 2, 14, 16, 28, 34, 38, 39, 47});
                                        put("North District", new Integer[]{1, 2, 14, 28, 34, 38, 39, 47});
                                        put("South District", new Integer[]{1, 2, 14, 16, 28, 34, 38, 39, 47});
                                        put("West District", new Integer[]{1, 2, 14, 16, 28, 34, 38, 39, 47});
                                    }});
                put("Telangana", new HashMap<String, Integer[]>()
                                    {{
                                        put("Adilabad", new Integer[]{1, 2, 3, 4, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 28, 30, 31, 32, 33, 35, 37, 39});
                                        put("Karimnagar", new Integer[]{1, 2, 3, 6, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 30, 33, 35, 37, 39});
                                        put("Medak", new Integer[]{1, 2, 3, 6, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 35, 36, 39, 40});
                                        put("Khammam", new Integer[]{2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 26, 27, 30, 33, 39, 40});
                                        put("Mahbubnagar", new Integer[]{2, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 33, 34});
                                        put("Nalgonda", new Integer[]{2, 3, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 30, 33, 35});
                                        put("Nizamabad", new Integer[]{2, 3, 7, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 33, 35, 37, 39});
                                        put("Rangareddi", new Integer[]{2, 3, 5, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 30, 32, 33, 35, 36});
                                        put("Warangal", new Integer[]{2, 3, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 30, 33, 35});
                                    }});
                put("Tripura", new HashMap<String, Integer[]>()
                                    {{
                                        put("Dhalai", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 22, 27, 28, 29, 30, 37, 39, 40, 45, 46});
                                        put("North Tripura", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 22, 27, 28, 29, 30, 37, 39, 40, 45, 46});
                                        put("South Tripura", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 22, 27, 28, 29, 30, 37, 39, 40, 45, 46});
                                        put("West Tripura", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 22, 28, 29, 30, 37, 39, 40, 45, 46});
                                    }});
                put("Uttarakhand", new HashMap<String, Integer[]>()
                                    {{
                                        put("Almora", new Integer[]{1, 2, 14, 16, 17, 23, 25, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Bageshwar", new Integer[]{1, 2, 14, 16, 23, 25, 27, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Chamoli", new Integer[]{1, 2, 12, 14, 16, 17, 23, 25, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Champawat", new Integer[]{1, 2, 14, 16, 23, 25, 27, 28, 29, 30, 33, 34, 36, 37, 38, 39, 46, 47, 49});
                                        put("Dehradun", new Integer[]{1, 2, 7, 12, 13, 14, 15, 16, 17, 18, 19, 23, 25, 27, 28, 29, 30, 33, 34, 36, 37, 39, 43, 46, 47, 49, 50});
                                        put("Nainital", new Integer[]{1, 2, 7, 13, 14, 15, 16, 17, 18, 23, 25, 27, 28, 29, 30, 33, 34, 36, 37, 38, 39, 43, 46, 47, 50});
                                        put("Pauri Garhwal", new Integer[]{1, 2, 13, 14, 16, 17, 23, 25, 27, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Pithoragarh", new Integer[]{1, 2, 14, 16, 23, 25, 26, 27, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Rudra Prayag", new Integer[]{1, 2, 14, 16, 17, 23, 25, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47});
                                        put("Tehri Garhwal", new Integer[]{1, 2, 12, 14, 16, 17, 23, 25, 27, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Uttar Kashi", new Integer[]{1, 2, 14, 16, 17, 23, 25, 27, 28, 29, 30, 33, 34, 36, 38, 39, 43, 46, 47, 49});
                                        put("Haridwar", new Integer[]{2, 12, 13, 14, 15, 16, 17, 18, 20, 26, 27, 28, 29, 30, 33, 36, 37, 39, 46, 47});
                                        put("Udam Singh Nagar", new Integer[]{2, 7, 12, 13, 14, 15, 16, 17, 19, 27, 28, 29, 31, 36, 37, 38, 39, 46, 47});
                                    }});
                put("West Bengal", new HashMap<String, Integer[]>()
                                    {{
                                        put("Bardhaman", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 23, 27, 28, 29, 30, 31, 32, 36, 38, 39, 40, 42, 45, 46, 47, 48});
                                        put("Birbhum", new Integer[]{1, 2, 7, 14, 15, 16, 18, 23, 27, 28, 29, 30, 31, 32, 36, 38, 39, 40, 42, 45, 46, 47, 48});
                                        put("Darjeeling", new Integer[]{1, 2, 14, 15, 16, 17, 18, 25, 28, 30, 31, 34, 36, 38, 39, 41, 45, 46});
                                        put("Medinipur West", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 19, 21, 23, 27, 28, 29, 30, 31, 32, 36, 39, 41, 42, 45, 46, 48});
                                        put("Purulia", new Integer[]{1, 2, 7, 14, 15, 16, 17, 18, 20, 23, 24, 25, 27, 28, 29, 30, 31, 34, 36, 38, 39, 40, 41, 42, 46, 48});
                                        put("24 Paraganas North", new Integer[]{2, 7, 14, 15, 16, 18, 19, 27, 28, 29, 30, 31, 36, 39, 40, 45, 46, 48});
                                        put("24 Paraganas South", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 22, 27, 28, 29, 30, 36, 39, 45, 46, 48});
                                        put("Bankura", new Integer[]{2, 7, 14, 15, 16, 18, 23, 27, 28, 29, 30, 31, 34, 36, 39, 42, 45, 46});
                                        put("Coochbehar", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 26, 28, 29, 30, 31, 34, 36, 39, 40, 41, 45, 48});
                                        put("Dinajpur Dakshin", new Integer[]{2, 14, 15, 16, 27, 28, 29, 31, 36, 39, 40, 45, 46, 48});
                                        put("Dinajpur Uttar", new Integer[]{2, 7, 14, 15, 16, 17, 18, 23, 25, 27, 28, 29, 30, 31, 36, 39, 40, 41, 45, 46, 47});
                                        put("Hooghly", new Integer[]{2, 7, 14, 15, 16, 18, 27, 28, 29, 30, 36, 39, 40, 42, 45, 46});
                                        put("Howrah", new Integer[]{2, 7, 15, 18, 28, 29, 30, 36, 39, 45, 46, 48});
                                        put("Jalpaiguri", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 26, 27, 28, 29, 30, 31, 34, 36, 39, 40, 41, 45, 46, 47, 48});
                                        put("Maldah", new Integer[]{2, 7, 14, 15, 16, 17, 18, 23, 26, 27, 28, 29, 30, 31, 36, 39, 40, 45, 46, 47, 48});
                                        put("Medinipur East", new Integer[]{2, 15, 16, 18, 28, 29, 30, 36, 39, 42, 45, 48});
                                        put("Murshidabad", new Integer[]{2, 7, 14, 15, 16, 17, 18, 27, 28, 29, 30, 31, 36, 39, 40, 45, 46, 47, 48});
                                        put("Nadia", new Integer[]{2, 7, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 36, 39, 40, 45, 46, 47, 48});
                                    }});
                put("Arunachal Pradesh", new HashMap<String, Integer[]>()
                                    {{
                                        put("Changlang", new Integer[]{2, 6, 7, 11, 13, 14, 28, 34, 36, 44});
                                        put("Dibang Valley", new Integer[]{2, 6, 11, 14, 34, 36, 44});
                                        put("East Kameng", new Integer[]{2, 6, 11, 14, 28, 34, 36, 44});
                                        put("East Siang", new Integer[]{2, 6, 7, 11, 13, 14, 28, 34, 36, 44});
                                        put("Lohit", new Integer[]{2, 6, 7, 11, 13, 14, 28, 34, 36, 44});
                                        put("Lower Subansiri", new Integer[]{2, 6, 7, 11, 14, 34, 36, 44});
                                        put("Papum Pare", new Integer[]{2, 6, 7, 11, 14, 28, 34, 36, 44});
                                        put("Tawang", new Integer[]{2, 14, 28, 34, 36, 44});
                                        put("Tirap", new Integer[]{2, 6, 11, 14, 28, 34, 36, 44});
                                        put("Upper Siang", new Integer[]{2, 7, 11, 14, 34, 36, 44});
                                        put("Upper Subansiri", new Integer[]{2, 6, 7, 11, 14, 34, 36, 44});
                                        put("West Kameng", new Integer[]{2, 6, 11, 14, 28, 34, 36, 44});
                                        put("West Siang", new Integer[]{2, 6, 7, 11, 13, 14, 34, 36, 44});
                                    }});
                put("Dadra and Nagar Haveli", new HashMap<String, Integer[]>()
                                    {{
                                        put("Dadra And Nagar Haveli", new Integer[]{2, 14, 24, 25, 28, 34});
                                    }});
                put("Himachal Pradesh", new HashMap<String, Integer[]>()
                                    {{
                                        put("Hamirpur", new Integer[]{2, 7, 11, 13, 14, 16, 17, 20, 24, 26, 27, 28, 29, 30, 31, 34, 35, 36, 39, 43, 46, 47});
                                    }});
                put("Uttar Pradesh", new HashMap<String, Integer[]>()
                                    {{
                                        put("Hamirpur", new Integer[]{2, 7, 11, 13, 14, 16, 17, 20, 24, 26, 27, 28, 29, 30, 31, 34, 35, 36, 39, 43, 46, 47});
                                        put("Agra", new Integer[]{2, 7, 14, 15, 16, 17, 20, 22, 24, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Aligarh", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 22, 26, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Allahabad", new Integer[]{2, 7, 14, 15, 16, 17, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Ambedkar Nagar", new Integer[]{2, 7, 14, 15, 16, 17, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Amroha", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Auraiya", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Azamgarh", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Baghpat", new Integer[]{2, 7, 13, 14, 15, 16, 17, 20, 22, 24, 26, 27, 28, 29, 33, 36, 39, 46, 47});
                                        put("Bahraich", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Ballia", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 33, 34, 36, 39, 46, 47});
                                        put("Banda", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Barabanki", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 34, 36, 39, 46, 47});
                                        put("Bareilly", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Basti", new Integer[]{2, 7, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Bijnor", new Integer[]{2, 7, 14, 15, 16, 17, 18, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Budaun", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 22, 24, 26, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Bulandshahr", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 22, 24, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Chandauli", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Chitrakoot", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Deoria", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Etah", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 22, 26, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Etawah", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Faizabad", new Integer[]{2, 7, 14, 15, 16, 17, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Farrukhabad", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Fatehpur", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Firozabad", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 22, 24, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Gautam Buddha Nagar", new Integer[]{2, 7, 14, 15, 16, 17, 20, 28, 29, 33, 36, 39, 46, 47});
                                        put("Ghaziabad", new Integer[]{2, 7, 13, 14, 15, 16, 17, 20, 24, 28, 29, 33, 36, 39, 46, 47});
                                        put("Ghazipur", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Gonda", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 26, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Gorakhpur", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 33, 34, 36, 39, 46, 47});
                                        put("Hardoi", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 38, 39, 46, 47});
                                        put("Hathras", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 22, 24, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Jalaun", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Jaunpur", new Integer[]{2, 7, 14, 15, 16, 17, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Jhansi", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Kannauj", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Kanpur Dehat", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 36, 38, 39, 42, 46, 47});
                                        put("Kanpur Nagar", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Kaushambi", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Kheri", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Kushi Nagar", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Lalitpur", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Lucknow", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 33, 34, 36, 39, 46, 47});
                                        put("Maharajganj", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Mahoba", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 38, 39, 42, 46, 47});
                                        put("Mainpuri", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 33, 36, 39, 42, 46, 47});
                                        put("Mathura", new Integer[]{2, 7, 14, 15, 16, 17, 20, 22, 24, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Mau", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 33, 34, 36, 39, 46, 47});
                                        put("Meerut", new Integer[]{2, 7, 13, 14, 15, 16, 17, 20, 22, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Mirzapur", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Moradabad", new Integer[]{2, 7, 13, 14, 15, 16, 17, 20, 24, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Muzaffarnagar", new Integer[]{2, 7, 14, 15, 16, 17, 22, 27, 28, 29, 33, 36, 39, 42, 46, 47});
                                        put("Pilibhit", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Rae Bareli", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Rampur", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 28, 29, 30, 33, 36, 38, 39, 46});
                                        put("Saharanpur", new Integer[]{2, 7, 13, 14, 15, 16, 18, 20, 22, 27, 28, 29, 30, 33, 36, 39, 46, 47});
                                        put("Sant Kabeer Nagar", new Integer[]{2, 7, 13, 14, 16, 17, 18, 20, 27, 28, 29, 30, 31, 33, 34, 36, 39, 46, 47});
                                        put("Sant Ravidas Nagar", new Integer[]{2, 7, 14, 15, 16, 17, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Shahjahanpur", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 38, 39, 46, 47});
                                        put("Shravasti", new Integer[]{2, 7, 13, 14, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Siddharth Nagar", new Integer[]{2, 7, 14, 16, 17, 18, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Sitapur", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Sonbhadra", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Sultanpur", new Integer[]{2, 7, 14, 15, 16, 17, 18, 19, 20, 24, 27, 28, 29, 30, 31, 33, 34, 36, 39, 42, 46, 47});
                                        put("Unnao", new Integer[]{2, 7, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26, 27, 28, 29, 30, 31, 33, 36, 39, 46, 47});
                                        put("Varanasi", new Integer[]{2, 7, 14, 15, 16, 17, 18, 20, 24, 27, 28, 29, 30, 31, 33, 36, 39, 42, 46, 47});
                                        put("Pratapgarh", new Integer[]{26, 46});
                                    }});
                put("Jammu and Kashmir", new HashMap<String, Integer[]>()
                                    {{
                                        put("Anantnag", new Integer[]{2, 11, 14, 39});
                                        put("Badgam", new Integer[]{2, 11, 14, 28, 33, 39, 43});
                                        put("Baramulla", new Integer[]{2, 14, 28, 39});
                                        put("Doda", new Integer[]{2, 14, 28, 31, 33, 39, 43, 47});
                                        put("Jammu", new Integer[]{2, 14, 20, 28, 30, 31, 39, 42, 43, 47});
                                        put("Kathua", new Integer[]{2, 6, 11, 13, 14, 20, 26, 28, 30, 31, 39, 42, 43, 47});
                                        put("Kupwara", new Integer[]{2, 14});
                                        put("Poonch", new Integer[]{2, 11, 14, 28, 39, 43});
                                        put("Pulwama", new Integer[]{2, 11, 14, 28, 31, 33, 39});
                                        put("Rajauri", new Integer[]{2, 11, 14, 20, 28, 39, 43});
                                        put("Srinagar", new Integer[]{2, 11, 14, 39, 43});
                                        put("Udhampur", new Integer[]{2, 6, 7, 11, 13, 14, 20, 28, 30, 39, 42, 43, 47});
                                        put("Kargil", new Integer[]{28});
                                        put("Leh Ladakh", new Integer[]{28, 39, 47});
                                    }});
                put("Odisha", new HashMap<String, Integer[]>()
                                    {{
                                        put("Anugul", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Balangir", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Baleshwar", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39, 45});
                                        put("Bargarh", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Bhadrak", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39, 45});
                                        put("Boudh", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Cuttack", new Integer[]{2, 7, 14, 15, 16, 18, 23, 30, 36, 39, 45});
                                        put("Deogarh", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39});
                                        put("Dhenkanal", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Gajapati", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 30, 36, 39});
                                        put("Ganjam", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Jagatsinghapur", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 30, 36, 39, 45});
                                        put("Jajapur", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 36, 39, 45});
                                        put("Jharsuguda", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39});
                                        put("Kalahandi", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 39});
                                        put("Kandhamal", new Integer[]{2, 14, 15, 16, 18, 23, 25, 30, 36, 39});
                                        put("Kendrapara", new Integer[]{2, 7, 15, 16, 18, 23, 28, 30, 36, 39, 45});
                                        put("Kendujhar", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39, 45});
                                        put("Khordha", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Koraput", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Malkangiri", new Integer[]{2, 14, 15, 16, 18, 23, 25, 30, 39});
                                        put("Mayurbhanj", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39});
                                        put("Nabarangpur", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 36});
                                        put("Nayagarh", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 30, 36, 39});
                                        put("Nuapada", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Puri", new Integer[]{2, 7, 15, 16, 18, 23, 25, 30, 36, 39});
                                        put("Rayagada", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 30, 36, 39});
                                        put("Sambalpur", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                        put("Sonepur", new Integer[]{2, 7, 14, 15, 16, 18, 23, 28, 30, 36, 39});
                                        put("Sundargarh", new Integer[]{2, 7, 14, 15, 16, 18, 23, 25, 28, 30, 36, 39});
                                    }});
                put("Puducherry", new HashMap<String, Integer[]>()
                                    {{
                                        put("Karaikal", new Integer[]{2, 3, 4, 5, 15, 16, 18, 22, 25});
                                        put("Pondicherry", new Integer[]{2, 3, 4, 5, 7, 9, 11, 15, 16, 18, 20, 22, 25, 30, 33, 34, 37});
                                        put("Yanam", new Integer[]{2, 5, 15, 16, 35});
                                    }});
                put("Punjab", new HashMap<String, Integer[]>()
                                {{
                                    put("Amritsar", new Integer[]{2, 7, 14, 15, 16, 17, 27, 28, 30, 39, 46});
                                    put("Bathinda", new Integer[]{2, 14, 15, 20, 22, 27, 28, 39, 47});
                                    put("Faridkot", new Integer[]{2, 15, 22, 28});
                                    put("Fatehgarh Sahib", new Integer[]{2, 7, 14, 28, 39, 47});
                                    put("Firozepur", new Integer[]{2, 7, 15, 22, 28, 30});
                                    put("Gurdaspur", new Integer[]{2, 7, 14, 16, 28, 29, 30, 39, 46});
                                    put("Hoshiarpur", new Integer[]{2, 7, 14, 16, 18, 28, 29, 30, 39, 46});
                                    put("Jalandhar", new Integer[]{2, 7, 14, 16, 17, 28, 30, 39, 46});
                                    put("Kapurthala", new Integer[]{2, 7, 14, 28, 39, 46});
                                    put("Ludhiana", new Integer[]{2, 7, 14, 15, 17, 22, 28, 39, 46, 47});
                                    put("Mansa", new Integer[]{2, 15, 20, 22, 27, 28, 39, 47});
                                    put("Moga", new Integer[]{2, 15, 17, 22, 27, 28, 39});
                                    put("Muktsar", new Integer[]{2, 15, 22, 27, 28, 30, 39, 47});
                                    put("Nawanshahr", new Integer[]{2, 7, 14, 28, 39, 46});
                                    put("Patiala", new Integer[]{2, 7, 14, 27, 28, 39, 46, 47});
                                    put("Rupnagar", new Integer[]{2, 7, 14, 17, 27, 28, 29, 30, 39});
                                    put("Sangrur", new Integer[]{2, 7, 14, 15, 17, 20, 22, 27, 28, 39, 46, 47});
                                }});
                put("Tamil Nadu", new HashMap<String, Integer[]>()
                                {{
                                    put("Coimbatore", new Integer[]{2, 3, 4, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 33, 35, 39, 51});
                                    put("Cuddalore", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 33, 34, 35});
                                    put("Dharmapuri", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 33, 34, 35, 39, 43, 51});
                                    put("Dindigul", new Integer[]{2, 3, 4, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 33, 34, 35, 36, 43, 51});
                                    put("Erode", new Integer[]{2, 3, 4, 6, 7, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25, 26, 33, 34, 35, 36, 39, 43, 51});
                                    put("Kanchipuram", new Integer[]{2, 3, 4, 7, 8, 9, 11, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 34});
                                    put("Kanniyakumari", new Integer[]{2, 3, 4, 6, 9, 10, 16, 17, 18, 51});
                                    put("Karur", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 33, 34, 35});
                                    put("Madurai", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 33, 34, 35});
                                    put("Nagapattinam", new Integer[]{2, 3, 4, 7, 9, 11, 14, 15, 16, 18, 22, 26, 33});
                                    put("Namakkal", new Integer[]{2, 3, 4, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 33, 34, 35, 39, 43});
                                    put("Perambalur", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 33, 34, 35});
                                    put("Pudukkottai", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 33, 34});
                                    put("Ramanathapuram", new Integer[]{2, 3, 4, 7, 11, 13, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 33, 35});
                                    put("Salem", new Integer[]{2, 3, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 33, 34, 35, 39, 51});
                                    put("Sivaganga", new Integer[]{2, 3, 4, 7, 8, 9, 11, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 27, 33, 34, 35});
                                    put("Thanjavur", new Integer[]{2, 3, 4, 6, 7, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 25, 33});
                                    put("The Nilgiris", new Integer[]{2, 3, 6, 7, 9, 10, 11, 13, 18, 22, 24, 36, 43, 51});
                                    put("Theni", new Integer[]{2, 3, 4, 7, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 33, 34, 35, 51});
                                    put("Thiruvallur", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 15, 16, 17, 18, 19, 20, 23, 24, 25, 33});
                                    put("Thiruvarur", new Integer[]{2, 3, 4, 7, 9, 11, 14, 15, 16, 18, 22});
                                    put("Tiruchirappalli", new Integer[]{2, 3, 4, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 33, 34, 35});
                                    put("Tirunelveli", new Integer[]{2, 3, 4, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 33, 34, 35, 51});
                                    put("Tiruvannamalai", new Integer[]{2, 3, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 33, 34, 35});
                                    put("Tuticorin", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 33, 34, 35});
                                    put("Vellore", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 33, 34, 35});
                                    put("Villupuram", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 33, 34, 35});
                                    put("Virudhunagar", new Integer[]{2, 3, 4, 7, 8, 9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 33, 34, 35, 51});
                                }});
            }};

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("apy10.csv")));

            // do reading, usually loop until end of file reading
            String mLine;
            /*while ((mLine = reader.readLine()) != null) {
                //process line
            }*/
            Log.d("","CSV FILE READ:\n\n\n\n"+reader.readLine());
        } catch (IOException e) {
            //log the exception
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        statesList = (EditText) findViewById(R.id.state);

        statesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(MainActivity.this, "Search", "States",
                        null, initStatesData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        statesList.setText(searchable.getTitle());
                        districtsList.setText("");
                        cropsList.setText("");
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        districtsList = (EditText) findViewById(R.id.district);

        districtsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(MainActivity.this, "Search", "Districts",
                        null, initDistrictsData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        districtsList.setText(searchable.getTitle());
                        cropsList.setText("");
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });


        cropsList = (EditText) findViewById(R.id.crop);

        cropsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(MainActivity.this, "Search", "Crops",
                        null, initCropsData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        cropsList.setText(searchable.getTitle());
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        mLatitudeLabel = "latitude";
        mLongitudeLabel = "longitude";
        // mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        // mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // getLocation = findViewById(R.id.locateButton);

        area = findViewById(R.id.area);


        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Results.class);
                Bundle b = new Bundle();
                // TODO check before sending bundle

                b.putStringArray("details",
                        new String[] {
                                statesList.getText().toString(),
                                districtsList.getText().toString(),
                                cropsList.getText().toString(),
                                area.getText().toString()
                        });
                intent.putExtras(b);
                startActivity(intent);

                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();

                                double latitude  = mLastLocation.getLatitude();
                                double longitude = mLastLocation.getLongitude();

                                mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                        mLatitudeLabel,
                                        latitude));
                                mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                        mLongitudeLabel,
                                        longitude));


                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                                    /*


                                    locationsList.setText(addresses.get(0).getLocality());




                                     */

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.w(TAG, "getLastLocation:exception", task.getException());

                            }
                        }
                    });
    }

    private ArrayList<SearchModel> initStatesData() {
        ArrayList<SearchModel> states= new ArrayList<>();
        for ( String state : locationDetails.keySet() ) {
            states.add(new SearchModel(state));
        }
        return states;
    }

    private ArrayList<SearchModel> initDistrictsData() {
        String state = statesList.getText().toString();

        ArrayList<SearchModel> districts= new ArrayList<>();
        if( !state.equals("") ) {
            for (String district : locationDetails.get(state).keySet()) {
                districts.add(new SearchModel(district));
            }
        }
        return districts;
    }

    private ArrayList<SearchModel> initCropsData() {
        ArrayList<SearchModel> cropsList= new ArrayList<>();
        String state = statesList.getText().toString();
        String district = districtsList.getText().toString();

        // Log.d("", "ERROR" + state + " " + district);

        if( !state.equals("") && !district.equals("") ) {
            for (Integer cropIndex : locationDetails.get(state).get(district)) {
                cropsList.add(new SearchModel(crops[cropIndex]));
            }
        }
        return cropsList;
    }

    private ArrayList<SearchModel> initSoilData() {
        ArrayList<SearchModel> cities= new ArrayList<>();

        cities.add(new SearchModel("Black"));
        cities.add(new SearchModel("Red"));
        cities.add(new SearchModel("Alluvial"));

        return cities;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
