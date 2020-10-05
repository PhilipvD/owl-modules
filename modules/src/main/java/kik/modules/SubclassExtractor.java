/**
 * 
 */
package kik.modules;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

/**
 * @author Philip van Damme 
 * Extracts all asserted subclasses from a given class (URI) of an ontology
 */
public class SubclassExtractor {

	/**
	 * @param args ontology OWL file path, URI from which the subclasses should be
	 *             returned
	 */
	public static void main(String[] args) {

		// Get arguments
		String ontoloyFilePath = args[0];
		String uri = args[1];

		try {
			// Create manager and data factory, load ontology
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLDataFactory df = OWLManager.getOWLDataFactory();

			System.out.println("OWL Extractor: loading ontology... " + ontoloyFilePath);
			OWLOntology ont;

			ont = man.loadOntologyFromOntologyDocument(new File(ontoloyFilePath));

			// Initiate reasoner
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
			OWLReasoner reasoner = reasonerFactory.createReasoner(ont, config);

			// Classify the ontology.
			reasoner.precomputeInferences();
			System.out.println("Using reasoner: " + reasoner);

			// Get subclasses
			OWLClass owlClass = df.getOWLClass(IRI.create(uri));
			NodeSet<OWLClass> superClasses = reasoner.getSubClasses(owlClass, false);
			System.out.println("\nSubclasses (URI):");
			for (Node<OWLClass> c : superClasses) {
				if (!c.getRepresentativeElement().isOWLThing() && !c.getRepresentativeElement().isOWLNothing())
					System.out.println(c.getRepresentativeElement().toStringID());
			}

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

}
