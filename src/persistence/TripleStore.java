package persistence;

import java.io.File;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import de.derivo.sparqldlapi.QueryEngine;
import model.MyFactory;

public class TripleStore {
	// the manager
	private OWLOntologyManager manager;
	// the ontology
	private OWLOntology ontology;
	// merger
	private OWLOntologyMerger merger;
	// Factory
	private MyFactory factory;
	// The reasoner based on the ontology.
	private OWLReasoner reasoner;
	// Sparql-dl query engine
	private QueryEngine qengine;

	public TripleStore() {
		// Create the manager
		manager = OWLManager.createOWLOntologyManager();
	}
	
	public TripleStore(String strFileName){
		this();
		createOntologyFromRDF(strFileName);
	}

	public MyFactory createFactory(OWLOntology ontology) {
		return factory = new MyFactory(ontology);
	}

	public OWLReasoner createReasoner(OWLOntology ontology) {
		return reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
	}

	public QueryEngine createQueryEngine(OWLOntologyManager manager, OWLReasoner reasoner) {
		return qengine = QueryEngine.create(manager, reasoner, true);
	}

	public void createOntologyFromRDF(String strFileName) {
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(strFileName));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createFactory(ontology);
		createReasoner(ontology);
		createQueryEngine(manager, reasoner);
	}

	public void createOntologyFromRDFs(String strFileNames[]) {
		OWLOntology[] ontologies = new OWLOntology[strFileNames.length];
		for (int i = 0; i < strFileNames.length; i++) {
			try {
				ontologies[i] = manager.loadOntologyFromOntologyDocument(new File(strFileNames[i]));
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		merger = new OWLOntologyMerger(manager);
		IRI mergedOntologyIRI = IRI.create(ontologies[0].getOntologyID().toString());
		try {
			ontology = merger.createMergedOntology(manager, mergedOntologyIRI);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createFactory(ontology);
		createReasoner(ontology);
		createQueryEngine(manager, reasoner);
	}

	public OWLOntologyManager manager() {
		return manager;
	}

	public OWLOntology ontology() {
		return ontology;
	}

	public MyFactory factory() {
		return factory;
	}

	public OWLReasoner reasoner() {
		return reasoner;
	}

	public QueryEngine qengine() {
		return qengine;
	}

	public void save() {
		for (OWLOntology ont : manager.getOntologies()) {
			try {
				manager.saveOntology(ont);
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void saveIn3Boxes(String directory) {
		// save TBox, ABox, RBox to separate files.
		OWLOntology ontlgTbox = null, ontlgAbox = null, ontlgRbox = null;
		try {
			ontlgTbox = manager.createOntology(ontology.getTBoxAxioms(true));
			ontlgAbox = manager.createOntology(ontology.getABoxAxioms(true));
			ontlgRbox = manager.createOntology(ontology.getRBoxAxioms(true));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			manager.saveOntology(ontlgTbox, IRI.create(new File(directory + "/Tbox.owl")));
			manager.saveOntology(ontlgAbox, IRI.create(new File(directory + "/Abox.owl")));
			manager.saveOntology(ontlgRbox, IRI.create(new File(directory + "/Rbox.owl")));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
