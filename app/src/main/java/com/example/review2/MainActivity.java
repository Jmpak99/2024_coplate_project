package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;

    private ArrayAdapter<CharSequence> routeAdapter;
    private Spinner routeSpinner;
    private ArrayAdapter<CharSequence> facilitiesAdapter;
    private Spinner facilitiesSpinner;
    private ArrayAdapter<String> listViewAdapter;
    private ListView listView;
    private ArrayList<String> listViewData;
    private EditText searchEditText;

    private String raRoute = "";
    private String raFacilities = "";
    private String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 엣지 투 엣지 모드를 활성화합니다.
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);
        setupBottomNavigation();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String restAreaName = listViewData.get(position).split("\n")[0].split(":")[1].trim();

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("restAreaName", restAreaName);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI 요소 초기화
        routeSpinner = findViewById(R.id.RouteSpinner);
        facilitiesSpinner = findViewById(R.id.FacilitesSpinner);
        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.editTextText);

        // 어댑터 초기화
        routeAdapter = ArrayAdapter.createFromResource(this, R.array.route, android.R.layout.simple_spinner_item);
        routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeSpinner.setAdapter(routeAdapter);

        facilitiesAdapter = ArrayAdapter.createFromResource(this, R.array.facilities, android.R.layout.simple_spinner_item);
        facilitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facilitiesSpinner.setAdapter(facilitiesAdapter);

        listViewData = new ArrayList<>();
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listViewData);
        listView.setAdapter(listViewAdapter);

        // 스피너 선택 이벤트 처리
        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                raRoute = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Selected route: " + raRoute);
                loadListViewData(raRoute, raFacilities, searchText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택 안되었을 때 처리
            }
        });

        facilitiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                raFacilities = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Selected facility: " + raFacilities);
                loadListViewData(raRoute, raFacilities, searchText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택 안되었을 때 처리
            }
        });

        // EditText 변경 이벤트 처리
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 전에 처리할 로직
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText = s.toString();
                Log.d(TAG, "Search text: " + searchText);
                loadListViewData(raRoute, raFacilities, searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 후에 처리할 로직
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.556, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국 수도");

        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));
    }

    // 선택된 데이터를 기반으로 리스트뷰에 데이터 로드
    private void loadListViewData(String selectedRoute, String selectedFacility, String searchText) {
        String url = "https://restarea31.mycafe24.com/db/restareaList.php";
        boolean hasQueryParam = false;

        if (!"-".equals(selectedRoute)) {
            url += "?route=" + selectedRoute;
            hasQueryParam = true;
        }

        if (!"-".equals(selectedFacility)) {
            if (hasQueryParam) {
                url += "&facility=" + selectedFacility;
            } else {
                url += "?facility=" + selectedFacility;
                hasQueryParam = true;
            }
        }

        if (!searchText.isEmpty()) {
            if (hasQueryParam) {
                url += "&search=" + searchText;
            } else {
                url += "?search=" + searchText;
            }
        }

        Log.d(TAG, "Request URL: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Response: " + response.toString()); // 응답 로그 추가
                        listViewData.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // 필요한 데이터 필드들을 가져와서 리스트에 추가
                                String raName = jsonObject.getString("raName");
                                String raPhone = jsonObject.getString("raPhone");
                                String raFacilities = jsonObject.getString("raFacilities");
                                listViewData.add("휴게소명 : " + raName + "\n전화번호 : " + raPhone + "\n편의시설 : " + raFacilities);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listViewAdapter.notifyDataSetChanged();
                        Log.d(TAG, "ListView data loaded: " + listViewData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    // Volley 에러 처리
    private void handleVolleyError(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        String message = "Unknown error";
        if (networkResponse != null) {
            String result = "";
            try {
                result = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message = "Status Code: " + networkResponse.statusCode + "\n" + result;
        } else if (error.getCause() != null) {
            message = error.getCause().getMessage();
        } else if (error.getMessage() != null) {
            message = error.getMessage();
        }
        Log.e(TAG, "Volley error: " + message);
        Toast.makeText(MainActivity.this, "데이터 로드 실패: " + message, Toast.LENGTH_LONG).show();
    }



}
