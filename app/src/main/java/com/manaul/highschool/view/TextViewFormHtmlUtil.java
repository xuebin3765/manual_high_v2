package com.manaul.highschool.view;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class TextViewFormHtmlUtil extends AsyncTask<Void, Void, Void> {
	
	private String content;
	private TextView textView;
	private Context context;
	HttpGet req;
	String htmlContent;
	HttpClient httpClient = new DefaultHttpClient();
	
	public TextViewFormHtmlUtil(String content, TextView textView , Context context) {
		this.content = content;
		this.textView = textView;
		this.context = context;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (htmlContent != null) {
			htmlContent = htmlContent.replaceAll("<img src=\"/", "<img src=\"");
			htmlContent = htmlContent.replaceAll("(<p[^>]*>)*(<span[^>]*>)*(<img)", "<img ");
			htmlContent = htmlContent.replaceAll("(</img>)(<br/>)*(<br>)*(</span>)*(</p>)*", "</img>");
			htmlContent = htmlContent.replaceAll("(<p[^>]*>)*", "");
			htmlContent = htmlContent.replaceAll("</p>", "<br/>");
		}
		try {
			MImageGetter mg = new MImageGetter(textView, context);
			textView.setText(Html.fromHtml(htmlContent, mg, new MTagHandler()));
		} catch (Throwable e) {
			if (e != null) {
				e.printStackTrace();
			}
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			htmlContent = content;
		} catch (Exception e) {
			return null;
		}
		return null;
	}

}