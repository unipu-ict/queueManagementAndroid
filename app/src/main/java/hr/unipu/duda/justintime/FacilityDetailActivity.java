package hr.unipu.duda.justintime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import hr.unipu.duda.justintime.model.Facility;

public class FacilityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detail);
        final Facility facility = new Facility();

        final TextView facilityNameTextView = (TextView) findViewById(R.id.facilityNameTextView);
        final TextView facilityAddressTextView = (TextView) findViewById(R.id.facilityAddressTextView);
        final TextView facilityTelephoneTextView = (TextView) findViewById(R.id.facilityTelephoneTextView);
        final TextView facilityMailTextView = (TextView) findViewById(R.id.facilityMailTextView);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://justin-time.herokuapp.com/facility/" +getIntent().getStringExtra("id");

        //čitanje podataka o ustanovi
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSON Response", response.toString());
                try {
                    facility.setId(response.getString("id"));
                    facility.setName(response.getString("name"));
                    facility.setAddress(response.getString("address"));
                    facility.setTelephone(response.getString("telephone"));
                    facility.setMail(response.getString("mail"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacilityDetailActivity.this, "Greška prilikom dohvaćanja podataka", Toast.LENGTH_SHORT).show();
                Log.d("JSON Error", "onErrorResponse: "+error.getMessage());
            }
        });

        queue.add(request);

        //prikaz dohvaćenih podataka o ustanovi
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                setTitle(facility.getName());
                facilityNameTextView.setText(facility.getName());
                facilityAddressTextView.setText("Adresa: " + facility.getAddress());
                facilityTelephoneTextView.setText("Telefon: " + facility.getTelephone());
                facilityMailTextView.setText("Mail: " + facility.getMail());
            }
        });
    }

}
