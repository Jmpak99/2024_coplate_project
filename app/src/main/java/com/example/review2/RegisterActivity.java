package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private EditText idText, passwordText, emailText, phoneText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupBottomNavigation();

        idText = findViewById(R.id.idText);
        passwordText = findViewById(R.id.passwordText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String userPassWord = passwordText.getText().toString();
                String userEmail = emailText.getText().toString();
                String userPhone = phoneText.getText().toString();

                registerUser(userID, userPassWord, userEmail, userPhone);
            }
        });
    }

    private void registerUser(String userID, String userPassWord, String userEmail, String userPhone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://restarea31.mycafe24.com/db/UserRegister.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        if(response.equals("회원가입이 성공적으로 완료되었습니다.")) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("userPassWord", userPassWord);
                params.put("userEmail", userEmail);
                params.put("userPhone", userPhone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
