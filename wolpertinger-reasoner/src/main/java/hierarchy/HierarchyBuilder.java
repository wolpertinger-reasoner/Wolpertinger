package hierarchy;

import java.util.HashMap;
import java.util.HashSet;

import org.semanticweb.owlapi.model.OWLClass;

public class HierarchyBuilder {
	public static Hierarchy<OWLClass> buildHierarchy(HashMap<OWLClass, HashSet<OWLClass>> superClassHierarchy,
													 HashMap<OWLClass, OWLClass> classRepresentative,
													 OWLClass thing, OWLClass nothing,
													 HashSet<OWLClass> equalsToTopClasses, HashSet<OWLClass> equalsToBottomClasses) {
		HashMap<OWLClass, HierarchyNode<OWLClass>> representativeNodes = new HashMap<OWLClass, HierarchyNode<OWLClass>> ();
		for (OWLClass cl : superClassHierarchy.keySet()) {
			HierarchyNode<OWLClass> node = new HierarchyNode<OWLClass>(cl);
			representativeNodes.put(cl, node);
		}
		for (OWLClass represented : classRepresentative.keySet()) {
			OWLClass representative = classRepresentative.get(represented);
			representativeNodes.get(representative).m_equivalentElements.add(represented);
			representativeNodes.put(represented, representativeNodes.get(representative));
		}
		for (OWLClass cl : superClassHierarchy.keySet()) {
			HierarchyNode<OWLClass> node = representativeNodes.get(cl);
			for (OWLClass superClass : superClassHierarchy.get(cl)) {
				node.m_parentNodes.add(representativeNodes.get(superClass));
				representativeNodes.get(superClass).m_childNodes.add(node);
			}
		}
		HierarchyNode<OWLClass> topHierarchyNode = new HierarchyNode<OWLClass> (thing);
		for (OWLClass topEquivalentClass : equalsToTopClasses) {
			topHierarchyNode.m_equivalentElements.add(topEquivalentClass);
		}
		HierarchyNode<OWLClass> botHierarchyNode = new HierarchyNode<OWLClass> (nothing);
		for (OWLClass botEquivalentClass : equalsToBottomClasses) {
			botHierarchyNode.m_equivalentElements.add(botEquivalentClass);
		}
		for (OWLClass cl : superClassHierarchy.keySet()) {
			HierarchyNode<OWLClass> classNode = representativeNodes.get(cl);
			if(classNode.m_childNodes.isEmpty()) {
				classNode.m_childNodes.add(botHierarchyNode);
				botHierarchyNode.m_parentNodes.add(classNode);
			}
			if(classNode.m_parentNodes.isEmpty()) {
				classNode.m_parentNodes.add(topHierarchyNode);
				topHierarchyNode.m_childNodes.add(classNode);
			}
		}
		Hierarchy<OWLClass> hierarchy = new Hierarchy<OWLClass> (topHierarchyNode, botHierarchyNode);
		hierarchy.m_nodesByElements.putAll(representativeNodes);
		return hierarchy;
	}

}
