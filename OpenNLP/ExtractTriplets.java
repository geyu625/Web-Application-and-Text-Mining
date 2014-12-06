import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import opennlp.tools.parser.Parse;

//extract triplets from a single sentence using OpenNLP
public class ExtractTriplets {

	public static String[] findInfo(Parse[] parses, Set<String> verbsCandidates) {
		String subject = ExtractTriplets.findSubject(parses);
		String verb = ExtractTriplets.findVerb(parses);
		String[] objects = ExtractTriplets.findObject(parses);
		String[] reviews = {"",""};
		// check if
		if (subject == null || subject.equals("I") || subject.equals("We")) {
			return reviews;
		}
		else if (objects == null || objects[0] == null) {
			return reviews;
		}
		else if (objects[1].equals("ADJP")) {
			reviews[0] = objects[0];
			return reviews;
		}
		else if (verb == null) {
			return reviews;
		}
		else if ((objects[1].equals("NP") || objects[1].equals("PP")) && (verbsCandidates.contains(verb))){
			reviews[1] = verb + " " + objects[0];
			return reviews;
		}
		else return reviews;	
	}
	
	public static String findTriplets(Parse[] parses) {
		String subject = ExtractTriplets.findSubject(parses);
		String verb = ExtractTriplets.findVerb(parses);
		String[] objects = ExtractTriplets.findObject(parses);
		String result;
		if (objects != null) {
			result = subject + " " + verb + " " + objects[0] + ". ";
		}
		else {
			result = subject + " " + verb + " " + "null" + ". ";
		}
		return result;
	}
	
	// recursively to find deepestVP in the parse tree
	public static Parse findDeepestVP(Parse[] parses) {
		Parse p = null;
		if (ExtractTriplets.hasVP(parses) == null) {
			p = parses[0].getParent();
			return p;
		}
		else {
			p = ExtractTriplets.hasVP(parses);
			return findDeepestVP(p.getChildren());
		}
	}
	
	public static Parse hasVP(Parse[] parses) {
		if (parses[0].getType().equals("TOP")) {
			parses = parses[0].getChildren();
		}
		if (parses[0].getType().equals("S")) {
			parses = parses[0].getChildren();
		}
		for (Parse p : parses) {
			if(p.getType().equals("VP"))
				return p;
		}
		return null;
	}
	
	public static String findVerb(Parse[] parses) {
		String verb = null;
		Parse deepVP = ExtractTriplets.findDeepestVP(parses);
		if (deepVP == null) {
			return null;
		}
		for (Parse child : deepVP.getChildren()) {
				if (child.getType().equals("VB") || child.getType().equals("VBP") || child.getType().equals("VBG")|| child.getType().equals("VBD") || child.getType().equals("VBN")) {
					verb = child.getCoveredText();
				}
		}
		return verb;
	}
	
	public static String findSubject(Parse[] topparse) {
		Queue<Parse> queue = new LinkedList<Parse>();
		queue.add(topparse[0]);
		Parse temp = topparse[0];
		String subject = null;
		while (!(temp == null)) {
			if (temp.getType().equals("NP")) {
				for (Parse child : temp.getChildren()) {
					if (child.getType().equals("NN") || child.getType().equals("NNS") ||  child.getType().equals("NNP") || child.getType().equals("NNPS") || child.getType().equals("NP") || child.getType().equals("PRP")) {
						subject = child.getCoveredText();
						break;
					}
				}
				break;
			}
			// add temp children (from left to right) to queue
			for (Parse p : temp.getChildren()) {
				queue.add(p);
			}
			
			// in case of queue is empty before doing remove
			if (queue.size() == 0 ) {
				return null;
			}
			// dequeue a node from queue and assign its value to temp
			temp = queue.remove();
		}
		return subject;
	}
	

	public static String[] findObject(Parse[] topparse) {
		Queue<Parse> queue = new LinkedList<Parse>();
		Parse deepestVP = ExtractTriplets.findDeepestVP(topparse);
		if (deepestVP == null) {
			return null;
		}
		queue.add(deepestVP);
		Parse temp = deepestVP;
		String[] objects= new String[2];
		while (!(temp == null)) {
			if (temp.getType().equals("NP")) {
				for (Parse child : temp.getChildren()) {
					if (child.getType().equals("NN") || child.getType().equals("NNS") ||  child.getType().equals("NNP") || child.getType().equals("NNPS") || child.getType().equals("NP") || child.getType().equals("PRP")) {
						objects[0] = child.getCoveredText();
						objects[1] = "NP";
						break;
					}
				}
				break;
			}
			
			if (temp.getType().equals("PP")) {
				for (Parse child : temp.getChildren()) {
					if (child.getType().equals("NN") || child.getType().equals("NNS") ||  child.getType().equals("NNP") || child.getType().equals("NNPS") || child.getType().equals("PP")) {
						objects[0] = child.getCoveredText();
						objects[1] = "PP";
						break;
					}
				}
				break;
			}
			
			if (temp.getType().equals("ADJP")) {
				for (Parse child : temp.getChildren()) {
					if (child.getType().equals("JJ") || child.getType().equals("JJR") ||  child.getType().equals("JJS") || child.getType().equals("ADJP")) {
						objects[0] = child.getCoveredText();
						objects[1] = "ADJP";
						break;
					}
				}
				break;
			}
			
			// add temp children (from left to right) to queue
			for (Parse p : temp.getChildren()) {
				queue.add(p);
			}
			
			// in case of queue is empty before doing remove!
			if (queue.size() == 0 ) {
				return null;
			}
			
			// dequeue a node from queue and assign its value to temp
			temp = queue.remove();
		}
		return objects;
	}
}