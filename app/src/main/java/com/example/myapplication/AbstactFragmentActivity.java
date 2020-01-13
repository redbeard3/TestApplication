package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.*;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstactFragmentActivity extends FragmentActivity {

	public abstract Fragment createFragment();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);

		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (null == fragment) {
			fragment = createFragment();
			manager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}

	public static Intent invoke(Context context, Class<?> clazz, Map<String, Serializable> params) {
		Intent intent = new Intent(context, clazz);
		for (Map.Entry<String, Serializable> entry : params.entrySet()) {
			intent.putExtra(entry.getKey(), entry.getValue());
		}
		return intent;
	}

}
