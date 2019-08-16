package com.efsalokyay.sozlukuygulamasivolley;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<Kelimeler> kelimelerArrayListe;
    private KelimeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        recyclerView = findViewById(R.id.main_recylerview);

        toolbar.setTitle("Sözlük Uygulaması-Volley");
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tumKelimeler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_ara);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.e("onQueryTextSubmit", s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.e("onQueryTextChange", s);
        kelimeAra(s);
        return false;
    }

    public void tumKelimeler() {

        String url = "http://kasimadalan.pe.hu/sozluk/tum_kelimeler.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                kelimelerArrayListe = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kelimeler = jsonObject.getJSONArray("kelimeler");

                    for (int i = 0; i < kelimeler.length(); i++) {

                        JSONObject k = kelimeler.getJSONObject(i);

                        int kelime_id = k.getInt("kelime_id");
                        String ingilizce = k.getString("ingilizce");
                        String turkce = k.getString("turkce");

                        Kelimeler kelime = new Kelimeler(kelime_id, ingilizce, turkce);
                        kelimelerArrayListe.add(kelime);
                    }

                    adapter = new KelimeAdapter(MainActivity.this, kelimelerArrayListe);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

    public void kelimeAra(final String arananKelime) {

        String url = "http://kasimadalan.pe.hu/sozluk/kelime_ara.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                kelimelerArrayListe = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kelimeler = jsonObject.getJSONArray("kelimeler");

                    for (int i = 0; i < kelimeler.length(); i++) {

                        JSONObject k = kelimeler.getJSONObject(i);

                        int kelime_id = k.getInt("kelime_id");
                        String ingilizce = k.getString("ingilizce");
                        String turkce = k.getString("turkce");

                        Kelimeler kelime = new Kelimeler(kelime_id, ingilizce, turkce);
                        kelimelerArrayListe.add(kelime);
                    }

                    adapter = new KelimeAdapter(MainActivity.this, kelimelerArrayListe);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("ingilizce", arananKelime);

                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(request);
    }
}
