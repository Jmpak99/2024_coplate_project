package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class InfoActivity extends BaseActivity {

    private static final String TAG = "InfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        setupBottomNavigation();

        ImageView myImageView = findViewById(R.id.food);
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });
        ImageView myImageView1 = findViewById(R.id.gohome1);
        myImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView myImageView2 = findViewById(R.id.goreview);
        myImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, ReviewMainActivity.class);
                startActivity(intent);
            }
        });



        // Intent로부터 휴게소 이름을 받아옵니다.
        Intent intent = getIntent();
        String restAreaName = intent.getStringExtra("restAreaName");

        getRestAreaInfoFromWebService(restAreaName);

    }

    private HashMap<String, RestAreaInfo> restAreaInfoMap = new HashMap<>();

    private void getRestAreaInfoFromWebService(String restAreaName) {
        try {
            String encodedRestAreaName = URLEncoder.encode(restAreaName, "UTF-8");
            String url = "https://restarea31.mycafe24.com/db/restareaList.php?raName=" + encodedRestAreaName;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.cancelAll(TAG);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                // 휴게소 정보를 JSON으로부터 파싱합니다.
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String raName = jsonObject.getString("raName");
                                    String raGasoline = jsonObject.getString("raGasoline");
                                    String raLPG = jsonObject.getString("raLPG");
                                    String raDiesel = jsonObject.getString("raDiesel");
                                    String raElectronic = jsonObject.getString("raElectronic");

                                    // RestAreaInfo 객체를 생성하고 HashMap에 저장합니다.
                                    RestAreaInfo restAreaInfo = new RestAreaInfo(raName, raGasoline, raLPG, raDiesel, raElectronic);
                                    restAreaInfoMap.put(raName, restAreaInfo);
                                }

                                // 선택된 휴게소의 정보를 화면에 표시합니다.
                                displayRestAreaInfo(restAreaName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleVolleyError(error);
                }
            });
            jsonArrayRequest.setTag(TAG);

            requestQueue.add(jsonArrayRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void displayRestAreaInfo(String restAreaName) {
        RestAreaInfo restAreaInfo = restAreaInfoMap.get(restAreaName);
        if (restAreaInfo != null) {
            TextView textView = findViewById(R.id.restname);
            textView.setText(restAreaInfo.getName());
            TextView gasolineView = findViewById(R.id.gasoline);
            gasolineView.setText("휘발유 가격: " + restAreaInfo.getGasoline());

            TextView lpgView = findViewById(R.id.lpg);
            lpgView.setText("LPG 가격: " + restAreaInfo.getLpg());

            TextView dieselView = findViewById(R.id.diesel);
            dieselView.setText("경유 가격: " + restAreaInfo.getDiesel());

            TextView electronicView = findViewById(R.id.electronic);
            electronicView.setText("전기차 충전소 유무: " + ("1".equals(restAreaInfo.getElectronic()) ? "O" : "X"));
        }
    }

    private class RestAreaInfo {
        private String name;
        private String gasoline;
        private String lpg;
        private String diesel;
        private String electronic;

        public RestAreaInfo(String name, String gasoline, String lpg, String diesel, String electronic) {
            this.name = name;
            this.gasoline = gasoline;
            this.lpg = lpg;
            this.diesel = diesel;
            this.electronic = electronic;
        }

        public String getName() {
            return name;
        }

        public String getGasoline() {
            return gasoline;
        }

        public String getLpg() {
            return lpg;
        }

        public String getDiesel() {
            return diesel;
        }

        public String getElectronic() {
            return electronic;
        }
    }

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
        Toast.makeText(InfoActivity.this, "데이터 로드 실패: " + message, Toast.LENGTH_LONG).show();
    }
}