package kik.modules;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

/**
 * @author Philip van Damme
 * Extracts ontology modules using the module extractor from the OWL API. 
 */
public class ModuleExtractor {
	/**
	 * 
	 * @param args ontology file path, seed signature file path, module type (TOP, BOT, STAR). 
	 */
	public static void main(String[] args) {
		String ontologyFilePath = args[0];
		String seedFilePath = args[1];
		String moduleType = args[2];

		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();

			System.out.println("OWL Module Extractor: loading ontology...");
			OWLOntology ont;

			ont = man.loadOntologyFromOntologyDocument(new File(ontologyFilePath));

			// Get signature concepts from file
			List<String> seedConcepts = Collections.emptyList();
			Set<OWLEntity> sig = new HashSet<OWLEntity>();
			seedConcepts = Files.readAllLines(Paths.get(seedFilePath), StandardCharsets.UTF_8);
			for (String concept : seedConcepts) {
				OWLClass annotatedClass = dataFactory.getOWLClass(IRI.create(concept));
				sig.add(annotatedClass);
			}

			System.out.println("OWL Module Extractor: " + sig.size() + " classes in seed signature.");

			// Extract locality-based module
			System.out.println("OWL Module Extractor: creating module...");
			ModuleType type = null;
			if (moduleType.equals("STAR")) {
				type = ModuleType.STAR;
			} else if (moduleType.equals("BOT")) {
				type = ModuleType.BOT;
			} else if (moduleType.equals("TOP")) {
				type = ModuleType.TOP;
			}

			SyntacticLocalityModuleExtractor sme = new SyntacticLocalityModuleExtractor(man, ont, type);

			IRI moduleIRI;
			if (ont.getOntologyID().getOntologyIRI() == null) {
				moduleIRI = IRI.create(
						"http://www.semanticweb.org/ontologies/untitled-ontology-module-" + moduleType.toLowerCase());
			} else {
				moduleIRI = IRI.create(ont.getOntologyID().getOntologyIRI() + "module" + moduleType.toLowerCase());
			}

			String filePathModule = ontologyFilePath.replace(".owl", "_module_" + moduleType.toLowerCase() + ".owl");
			File fileModule = new File(filePathModule);

			OWLOntology mod = sme.extractAsOntology(sig, moduleIRI);
			man.saveOntology(mod, IRI.create(fileModule));

			System.out.println("OWL Module Extractor: module saved to " + fileModule);

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
