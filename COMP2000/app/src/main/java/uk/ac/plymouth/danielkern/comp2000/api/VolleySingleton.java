package uk.ac.plymouth.danielkern.comp2000.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class VolleySingleton {
    private static Context context;
    private RequestQueue requestQueue;
    private static VolleySingleton instance;
    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static void getUser(VolleySingleton volleySingleton, String username, Response.Listener<org.json.JSONObject> callback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, String.format(Locale.getDefault(), "http://10.240.72.69/comp2000/coursework/read_user/10944460/%s", username), null, callback, errorCallback);
        volleySingleton.getRequestQueue().add(request);
    }

    public static void getAllUsers(VolleySingleton volleySingleton, Response.Listener<org.json.JSONObject> callback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://10.240.72.69/comp2000/coursework/read_all_users/10944460", null, callback, errorCallback);
        volleySingleton.getRequestQueue().add(request);
    }

    public static void setUserPassword(VolleySingleton volleySingleton, String username, String oldPassword, String newPassword, Response.Listener<org.json.JSONObject> callback, Response.ErrorListener errorListener) {
        getUser(volleySingleton, username, response -> {
            JSONObject user = response.optJSONObject("user");
            if (user == null) {
                errorListener.onErrorResponse(new VolleyError("User not found"));
                return;
            }
            String password = user.optString("password");
            if (!password.equals(oldPassword)) {
                errorListener.onErrorResponse(new VolleyError("Incorrect password"));
                return;
            }
            try {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, String.format(Locale.getDefault(), "http://10.240.72.69/comp2000/coursework/update_user/10944460/%s", username), user.put("password", newPassword), callback, errorListener);
                volleySingleton.getRequestQueue().add(request);
            } catch (JSONException e) {
                errorListener.onErrorResponse(new VolleyError(e));
            }
        }, errorListener);
    }
}
