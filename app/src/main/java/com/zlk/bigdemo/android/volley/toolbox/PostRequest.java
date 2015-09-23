package com.zlk.bigdemo.android.volley.toolbox;

import com.zlk.bigdemo.android.volley.AuthFailureError;
import com.zlk.bigdemo.android.volley.NetworkResponse;
import com.zlk.bigdemo.android.volley.Request;
import com.zlk.bigdemo.android.volley.Response;
import com.zlk.bigdemo.android.volley.Response.ErrorListener;
import com.zlk.bigdemo.android.volley.Response.Listener;

import java.util.Map;

public abstract class PostRequest<T> extends Request<T> {
	
	private Listener<T> mListener;

	public PostRequest(String url, Listener<T> listener,
			ErrorListener errorListener) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return super.getParams();
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
}
