package test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryResult;

import model.Phrase;
import persistence.TripleStore;

public class TestTripleStore implements ITestCase {

	private TripleStore tstore;
	@Override
	public boolean prepare() {
		tstore = new TripleStore("model/GameDialogueModeltest.owl");
		return true;
	}

	@Override
	public boolean test() {
/*		for (Method m: Phrase.class.getDeclaredMethods()){
			System.out.println(m.getName());
		}
		for (Field f: Phrase.class.getDeclaredFields()){
			System.out.println(f.getName());
		}*/
		//tstore.saveIn3Boxes("triplestore");
		//System.out.println(tstore.query("SELECT ?c WHERE { Class(?c) }").toString());
		//System.out.println(tstore.query("SELECT ?i WHERE { Individual(?i) }").toString());
		//System.out.println(tstore.query("SELECT ?p WHERE { Transitive(?p) }").toString());
		//System.out.println(tstore.query("SELECT ?a ?b  WHERE { DirectSubClassOf(?a, ?b) }").toString());
		//System.out.println(tstore.query("SELECT ?a ?b ?c  WHERE { PropertyValue(?a, ?b, ?c) }").toString());
		//System.out.println(tstore.query("SELECT ?a ?b  WHERE { ComplementOf(?a, ?b) }").toString());
		//System.out.println(tstore.query("SELECT ?a ?b ?c  WHERE { Annotation(?a, ?b, ?c) }").toString());
		QueryResult res = tstore.query(
					"PREFIX ont: <http://www.co-ode.org/ontologies/ont.owl#>"+
					" PREFIX agl: <http://www.semanticweb.org/serene/ontologies/2015/IRIT/Project/AgileDoctor#>" +
					" SELECT ?x  WHERE { Type(?ms, ont:TopPhrase), PropertyValue(?ms, ont:expression, ?x), PropertyValue(?ms, ont:hasActor, agl:Vidal) }");
	/*	QueryResult res = tstore.query(
				"PREFIX ont: <http://www.co-ode.org/ontologies/ont.owl#>"+
				" PREFIX agl: <http://www.semanticweb.org/serene/ontologies/2015/IRIT/Project/AgileDoctor#>" +
				" SELECT ?ms WHERE { Type(?ms, ont:testclass) }");
		*/
		//System.out.println(res.toString());
	
		
		
		for (int i=0;i<res.size();i++) {
			
			QueryBinding b = res.get(i);
			for (QueryArgument a : b.getBoundArgs()) {
					QueryArgument a1 = b.get(a);
					System.out.println(a1.getValue());
			}
			
		}

		
		//System.out.println(tstore.query("PREFIX AgileDoctor: <http://www.semanticweb.org/serene/ontologies/2015/IRIT/Project/AgileDoctor#>"
			//	+ " SELECT ?ms WHERE { Type(?ms, ont:MicroSequence) }").toString());
		
		return true;
	}

}
