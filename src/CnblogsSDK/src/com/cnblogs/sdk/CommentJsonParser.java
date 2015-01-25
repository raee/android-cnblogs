package com.cnblogs.sdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cnblogs.sdk.model.Comment;

public class CommentJsonParser extends JsonParser<Comment> {

	@Override
	public List<Comment> parser(String json) {
		try {
			JSONArray arr = getJsonData(json);
			List<Comment> comments = new ArrayList<Comment>();
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				Comment comment = new Comment();
				comment.setId(obj.getString("id"));
				comment.setAuthor(obj.getString("comment"));
				comment.setAuthorUrl(obj.getString("author_url"));
				comment.setCommentDate(obj.getString("comment_time"));
				comment.setContent(obj.getString("content"));
				comments.add(comment);
			}
			return comments;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
