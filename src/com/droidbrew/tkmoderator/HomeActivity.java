package com.droidbrew.tkmoderator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.droidbrew.tkmoderator.adapter.ModeratorAdapter;
import com.droidbrew.travelkeeper.model.entity.Place;
import com.droidbrew.travelkeeper.model.manager.AppPlaceManager;

public class HomeActivity extends Activity {

	private ModeratorAdapter adapter;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		List<Place> list = new ArrayList<Place>();

		preferences = this.getSharedPreferences("TKMod", MODE_PRIVATE);
		if (!preferences.contains("last")) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong("last", 0);
			editor.commit();
		}

		ListView lv = (ListView) findViewById(R.id.lv_moderator);
		adapter = new ModeratorAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						HomeActivity.this);
				dialog.setTitle("Delete ?");
				dialog.setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Delete del = new Delete();
								del.execute(adapter.getItem(arg2));
							}
						});
				dialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.show();
			}

		});
		GetList gl = new GetList();
		gl.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong("last", new Date().getTime());
			editor.commit();
			System.exit(0);
			break;
		case R.id.action_update:
			GetList gl = new GetList();
			gl.execute();
			break;
		}
		return true;
	}

	class GetList extends AsyncTask<Void, Void, Void> {

		List<Place> list = new ArrayList<Place>();
		boolean allOk = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			AppPlaceManager pm = new AppPlaceManager();
			try {
				list = pm.getPlaces(preferences.getLong("last", 0));
			} catch (Exception e) {
				Log.e(HomeActivity.class.getName(), e.getMessage(), e);
				allOk = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter.setData(list);
			adapter.notifyDataSetChanged();

		}
	}

	class Delete extends AsyncTask<Place, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Place... params) {
			AppPlaceManager pm = new AppPlaceManager();
			try {
				pm.deletePlace(params[0]);
			} catch (Exception e) {
				Log.e(HomeActivity.class.getName(), e.getMessage(), e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			GetList gl = new GetList();
			gl.execute();
		}
	}
}
