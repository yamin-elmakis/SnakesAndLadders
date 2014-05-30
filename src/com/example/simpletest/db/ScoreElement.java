package com.example.simpletest.db;

import android.widget.BaseAdapter;

public class ScoreElement {

	private String name, difficulty, date ;
	private int rowId, score;
	private BaseAdapter adapter;
	
	public ScoreElement(String name, String difficulty, String date, int rowId,
			int score) {
		super();
		this.name = name;
		this.difficulty = difficulty;
		this.date = date;
		this.rowId = rowId;
		this.score = score;
	}
	
	public ScoreElement(String name, String difficulty, String date, int score) {
		super();
		this.name = name;
		this.difficulty = difficulty;
		this.date = date;
		this.rowId = 1;
		this.score = score;
	}
	
	protected void notifyDataChanged() {
	    adapter.notifyDataSetChanged();
	}
	
	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
