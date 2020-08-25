package net.ekhdemni.model.models;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import tn.core.util.DateUtils;


public class MyXmlParser extends DefaultHandler {

	private Article currentArticle = new Article();
	private Category currentCategory = new Category();
	private List<Article> articleList = new ArrayList<Article>();
	private List<Category> categoriesList = new ArrayList<Category>();

	private int articlesAdded = 0;
	private static final int ARTICLES_LIMIT = 15;
	boolean firstPostFound = false;
	boolean readCategories = true; // for blog categories
	StringBuffer chars = new StringBuffer();


	public List<Article> getArticleList() {
		return articleList;
	}
	public List<Category> getCategoriesList() {
		return categoriesList;
	}


	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		chars = new StringBuffer();

		if (localName.equalsIgnoreCase("entry") && !firstPostFound){
			firstPostFound = true;
			readCategories = false;
		}
		if (localName.equalsIgnoreCase("link") && (attributes.getValue("rel").equals("alternate")) && firstPostFound){
			String href = attributes.getValue("href");
			currentArticle.setUrl(href);
		}
		if (localName.equalsIgnoreCase("link") && firstPostFound){ // && (attributes.getValue("rel")!="alternate")
			String href = attributes.getValue("href");
			currentArticle.addLink(href);
		}
		if (localName.trim().equals("thumbnail")) {
			String src = attributes.getValue("url");
			currentArticle.setImg(src);
		}

		if (localName.equalsIgnoreCase("category")){ //  && readCategories
			String title = attributes.getValue("term");
			currentCategory.setTitle(title);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (localName.equalsIgnoreCase("id")){
			//currentArticle.setId(chars.toString());
		} else if (localName.equalsIgnoreCase("title")){
			currentArticle.setTitle(chars.toString());
		} else if (localName.equalsIgnoreCase("content")){
			currentArticle.setContent(chars.toString());
		} else if (localName.equalsIgnoreCase("published")){
			try {
				currentArticle.setPublished(DateUtils.prettyDate(chars.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (localName.equalsIgnoreCase("updated")){
			currentArticle.setUpdated(chars.toString());
		}else if (localName.equalsIgnoreCase("category") && !readCategories) {
			currentArticle.addCategory(currentCategory.getTitle());
		}

		if (localName.equalsIgnoreCase("entry")) {
			articleList.add(currentArticle);
			currentArticle = new Article();
			articlesAdded++;
			/*if (articlesAdded < ARTICLES_LIMIT)
			{
				throw new SAXException();
			}*/
		}
		if (localName.equalsIgnoreCase("category")) {
			boolean addit = true;
			for(Category c : categoriesList)
				if(c.getTitle().equals(currentCategory.getTitle())) addit = false;
			if (addit) categoriesList.add(currentCategory);
			currentCategory = new Category();
		}

	}

	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}
}