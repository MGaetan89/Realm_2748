package io.realm.realm_2748;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gaetan on 11/05/2016.
 */
public class Stat extends RealmObject {
	@PrimaryKey
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
