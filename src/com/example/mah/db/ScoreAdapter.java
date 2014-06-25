package com.example.mah.db;

import java.util.List;
import com.example.mah.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScoreAdapter extends ArrayAdapter<ScoreElement>{

	private Context context;
	private List<ScoreElement> scoreList;
	private int layout;
	private DBAdapter db;

	public ScoreAdapter(Context context, int resource, List<ScoreElement> scoreList, int layout) {
		super(context, resource, scoreList);
		this.context = context;
		this.scoreList = scoreList;
		this.layout= layout;
		db = new DBAdapter(context);
		for (ScoreElement scoreElement : scoreList) {
			scoreElement.setAdapter(this);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
		}
		ScoreElement scoreElement = scoreList.get(position);
		if (scoreElement != null){
			TextView name = (TextView) convertView.findViewById(R.id.tvName);
			TextView diff = (TextView) convertView.findViewById(R.id.tvDifficulty);
			TextView date = (TextView) convertView.findViewById(R.id.tvDate);
			TextView score = (TextView) convertView.findViewById(R.id.tvScore);
			if (name != null)
				name.setText(scoreElement.getName());
			if (diff != null)
				diff.setText(scoreElement.getDifficulty());
			if (date != null)
				date.setText(scoreElement.getDate());
			if (score != null)
				score.setText(scoreElement.getScore() + "");
		}
		return convertView;
	}
}
