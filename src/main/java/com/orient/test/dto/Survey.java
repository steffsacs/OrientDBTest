package com.orient.test.dto;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OVertex;

public class Survey{
	private final String survey = "Survey";
	private int numQuestions;
	private String type;
	private String name;
	
	public Survey(ODatabaseSession db) {
		if (db.getClass(survey) == null) {
			db.createVertexClass("Survey");
			initSurvey(db);
		}
		
	}
	
	private void initSurvey(ODatabaseSession db) {
		OClass s=db.getClass(survey);
		if (s.getProperty("type") == null) {
			s.createProperty("numQuestions", OType.INTEGER);
			s.createProperty("type", OType.STRING);
			s.createProperty("name", OType.STRING);
			//survey.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}
		
	}
	
	
	// Static ? Non static
	public OVertex createSurvey (ODatabaseSession db) {
		OVertex result = db.newVertex(survey);
		result.setProperty("type", type);
	    result.setProperty("numQuestions", numQuestions);
	    result.setProperty("name",name);
	    result.save();
		return result;
	}


	public int getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


		

	
	

}
