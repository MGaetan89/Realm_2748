package io.realm.realm_2748;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RealmChangeListener<Stat> {
	private Realm realm = null;
	private TextView result = null;
	private Stat stat = null;

	@Override
	public void onChange(Stat stat) {
		this.result.append(String.format(
				"onChange: [loaded: %s, valid: %s, count: %s]\n", stat.isLoaded(), stat.isValid(), this.realm.where(Stat.class).count()
		));
	}

	@Override
	public void onClick(View view) {
		this.result.setText(R.string.waiting);

		this.realm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				realm.delete(Stat.class);
			}
		});

		this.result.postDelayed(new Runnable() {
			@Override
			public void run() {
				realm.executeTransaction(new Realm.Transaction() {
					@Override
					public void execute(Realm realm) {
						Stat stat = new Stat();
						stat.setName("Realm");

						realm.copyToRealmOrUpdate(stat);
					}
				});
			}
		}, 2000L);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		RealmConfiguration configuration = new RealmConfiguration.Builder(this)
				.build();
		Realm.deleteRealm(configuration);

		this.realm = Realm.getInstance(configuration);
		this.result = (TextView) this.findViewById(R.id.result);

		this.stat = this.realm.where(Stat.class).findFirstAsync();
		this.stat.addChangeListener(this);

		View triggerRequest = this.findViewById(R.id.trigger_request);

		if (triggerRequest != null) {
			triggerRequest.setOnClickListener(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (this.stat != null) {
			this.stat.removeChangeListeners();
		}

		this.realm.close();
	}
}
