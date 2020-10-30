package com.orient.test.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

@RestController
public class CRUD {
	
	@RequestMapping("/dbOrient")
	public String test() {
		// Connect db
		OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig()); //local connection
		ODatabaseSession db = orient.open("surveyTest", "admin", "admin"); //select the db
		
		CRUD.createSchema(db);
		CRUD.testData(db);
		CRUD.executeQuery2(db);
		
		//CRUD.executeAQuery(db);
		//CRUD.executeAnotherQuery(db);
		
		
		db.close();
		orient.close();
		 
		return "Successful test";
	}
	
	private static void testData(ODatabaseSession db) {
		 
	    OVertex survey1= createSurvey(db, "Lineal", 3,"Survey1");
	    OVertex q1= createQuestion(db, "How are you?",1, "Multiple option");
	    OVertex q2= createQuestion(db, "How is life?",1, "Multiple selection");
	    OVertex q3= createQuestion(db, "How is your dog?",1, "Rating");
	    
	    OVertex survey2= createSurvey(db, "Derivada", 2,"Survey2");
	    OVertex q4= createQuestion(db, "Are you tired?",1, "Multiple option");
	    
	    OEdge isMadeOf1= survey1.addEdge(q1, "isMadeOf");
	    OEdge isMadeOf2= survey1.addEdge(q2, "isMadeOf");
	    OEdge isMadeOf3= survey1.addEdge(q3, "isMadeOf");
	    
	    OEdge isMadeOf4= survey2.addEdge(q4, "isMadeOf");
	    OEdge isMadeOf5= survey2.addEdge(q2, "isMadeOf");
	    
	    
	    isMadeOf1.save();
	    isMadeOf2.save();
	    isMadeOf3.save();
	    isMadeOf4.save();
	    isMadeOf5.save();
	    
	}
	
	private static void createSchema(ODatabaseSession db) {
		OClass survey = db.getClass("Survey");
		OClass question = db.getClass("Question");
		OClass answer = db.getClass("Answer");
		OClass answerArray = db.getClass("AnswerArray");
		
		//Vertex
		//If Survey does not exist
		if (survey == null) {
			survey = db.createVertexClass("Survey");
		}

		if (survey.getProperty("type") == null) {
			survey.createProperty("numQuestions", OType.INTEGER);
			survey.createProperty("type", OType.STRING);
			survey.createProperty("name", OType.STRING);
			//survey.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}
		
		//If Question does not exist
		if (question == null) {
			question = db.createVertexClass("Question");
		}
		
		if (question.getProperty("text") == null) {
			question.createProperty("text", OType.STRING);
			question.createProperty("numAnswers", OType.INTEGER);
			question.createProperty("type", OType.STRING);
			//question.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}
		
		//If Answer does not exist
		if (answer == null) {
			answer = db.createVertexClass("Answer");
		}
		
		if (answer.getProperty("name") == null) {
			answer.createProperty("answerText", OType.INTEGER);
			answer.createProperty("value", OType.INTEGER);
			//survey.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}
		
		if (answerArray == null) {
			answerArray = db.createVertexClass("AnswerArray");
		}
		
		if (answerArray.getProperty("name") == null) {
			answerArray.createProperty("answerText", OType.INTEGER);
			answerArray.createProperty("value", OType.INTEGER);
			//survey.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}
		
		
		//Edges
		
		if (db.getClass("needsA") == null) {
			db.createEdgeClass("needsA");
		}
		
		if (db.getClass("isMadeOf") == null) {
			db.createEdgeClass("isMadeOf");
		}
		
		if (db.getClass("hasNext") == null) {
			db.createEdgeClass("hasNext");
		}
		
		if (db.getClass("providesValue") == null) {
			db.createEdgeClass("providesValue");
		}
		
		
	}

	private static OVertex createSurvey (ODatabaseSession db, String type, int  numQuestions, String name) {
		OVertex result = db.newVertex("Survey");
		result.setProperty("type", type);
	    result.setProperty("numQuestions", numQuestions);
	    result.setProperty("name",name);
	    result.save();
		return result;
	}
	private static OVertex createQuestion (ODatabaseSession db, String text, int  numAnswers,String type) {
		OVertex result = db.newVertex("Question");
		result.setProperty("type", type);
	    result.setProperty("numAnswers", numAnswers);
	    result.setProperty("text", text);
	    result.save();
		return result;
	}
	
	private static void executeAQuery(ODatabaseSession db) {
	    //String queryB = "SELECT expand(out('FriendOf').out('FriendOf')) from Person where name = ?";
	    String query = "SELECT expand(out('isMadeOf').out('isMadeOf')) from Survey where name = ?";
	    OResultSet rs = db.query(query, "Survey1");

	    while (rs.hasNext()) {
	      OResult item = rs.next();
	      System.out.println("Survey 1 has the following question: " + item.getProperty("text"));
	    }

	    rs.close(); //REMEMBER TO ALWAYS CLOSE THE RESULT SET!!!
	}
	
	private static void executeQuery2(ODatabaseSession db) {
		String query ="MATCH {Class: Survey, as: s, where: (name=? AND type='Derivada')}-isMadeOf-{Class: Question, as: q} RETURN q.text as question";
		OResultSet rs = db.query(query,"Survey2");

	    while (rs.hasNext()) {
	      OResult item = rs.next();
	      System.out.println("Survey 2 has questions: " + item.getProperty("question"));
	    }

	    rs.close();
		
	}
	
	
	
	
	
	

}
