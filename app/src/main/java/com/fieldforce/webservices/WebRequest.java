package com.fieldforce.webservices;import android.util.Log;import com.android.volley.AuthFailureError;import com.android.volley.DefaultRetryPolicy;import com.android.volley.NetworkResponse;import com.android.volley.Response;import com.android.volley.ServerError;import com.android.volley.VolleyError;import com.android.volley.toolbox.HttpHeaderParser;import com.android.volley.toolbox.JsonObjectRequest;import com.fieldforce.interfaces.ApiServiceCaller;import com.google.gson.Gson;import org.json.JSONObject;import java.util.HashMap;import java.util.Map;public class WebRequest {    public static String TAG = "WebRequest";    public static JsonObjectRequest callPostMethod(JSONObject jsonObject, int request_type, String url,                                                   final String label, final ApiServiceCaller caller, final String token) {        if (ApiConstants.LOG_STATUS == 0) {            Log.i("Api_Calling", "" + url);            Log.i("JSONObject", "" + jsonObject);            Log.i("TOKENN", "" + token);        }        JsonObjectRequest jsonObjReq = new JsonObjectRequest(request_type, url, jsonObject,                new Response.Listener<JSONObject>() {                    @Override                    public void onResponse(JSONObject response) {                        try {                            if (ApiConstants.LOG_STATUS == 0)                                Log.i(TAG, response.toString());                            Gson gson = new Gson();                            JsonResponse jsonResponse = gson.fromJson(response.toString(), JsonResponse.class);                            caller.onAsyncSuccess(jsonResponse, label);                        } catch (Exception e) {//                            e.printStackTrace();                            caller.onAsyncCompletelyFail("Failededddddd", label);                        }                    }                }, new Response.ErrorListener() {            @Override            public void onErrorResponse(VolleyError error) {                NetworkResponse response = error.networkResponse;                if (error instanceof ServerError && response != null) {                    try {                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));//                        VolleyLog.d(TAG, "Error: " + res);                        Gson gson = new Gson();                        JsonResponse jsonResponse = gson.fromJson(res, JsonResponse.class);                        caller.onAsyncSuccess(jsonResponse, label);                    } catch (Exception je) {                        caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", label, response);                    }                } else                    caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", label, response);            }        }) {            @Override            public Map<String, String> getHeaders() throws AuthFailureError {                HashMap<String, String> params = new HashMap<>();                params.put("Content-Type", "application/json; charset=utf-8");                params.put("Accept", "application/json");                params.put("Authorization", token);                return params;            }        };        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        return jsonObjReq;    }    public static JsonObjectRequest callPostMethod1(int request_type, JSONObject jsonObject, final String url,                                                    final ApiServiceCaller caller, final String token) {        if (ApiConstants.LOG_STATUS == 0) {            Log.i("Api_Calling", "" + url);            Log.i("JSONObject", "" + jsonObject);            Log.i("TOKENN", "" + token);        }        JsonObjectRequest jsonObjReq = new JsonObjectRequest(request_type, url, jsonObject,                new Response.Listener<JSONObject>() {                    @Override                    public void onResponse(JSONObject response) {                        try {                            if (ApiConstants.LOG_STATUS == 0)                                Log.i(TAG, response.toString());                            Gson gson = new Gson();                            JsonResponse jsonResponse = gson.fromJson(response.toString(), JsonResponse.class);                            caller.onAsyncSuccess(jsonResponse, url);                        } catch (Exception e) {//                            e.printStackTrace();                            caller.onAsyncCompletelyFail("Failed", url);                        }                    }                }, new Response.ErrorListener() {            @Override            public void onErrorResponse(VolleyError error) {                NetworkResponse response = error.networkResponse;                if (error instanceof ServerError && response != null) {                    try {                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));//                        VolleyLog.d(TAG, "Error: " + res);                        Gson gson = new Gson();                        JsonResponse jsonResponse = gson.fromJson(res, JsonResponse.class);                        caller.onAsyncSuccess(jsonResponse, url);                    } catch (Exception je) {                        caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", url, response);                    }                } else                    caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", url, response);            }        }) {            @Override            public Map<String, String> getHeaders() throws AuthFailureError {                HashMap<String, String> params = new HashMap<>();                params.put("Content-Type", "application/json; charset=utf-8");                params.put("Accept", "application/json");                params.put("Authorization", token);                return params;            }        };        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        return jsonObjReq;    }}