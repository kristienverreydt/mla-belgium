/**
 * 
 */
package com.ibm.mla.mapping.concept;

/** 
 * @author Georg Temnitschka
 */

public class BodyPart extends ConceptWithDescriptors<BodyPart> {

	public BodyPart(String lemma) {
		super(lemma);
	}
//logically, BodyPart belongs in a separate Anatomy project; prepares for implementation of body parts hierarchy
//	private Set<BodyPart> containedBodyParts = null;
//	private BodyPart containingBodyPart      = null;

//	public BodyPart(String lemma, Set<BodyPart> containedBodyParts) {
//		this.lemma = (lemma == null) ? "" : lemma;
//		this.containedBodyParts = containedBodyParts;
//		if (containedBodyParts != null)
//			for (BodyPart bp : containedBodyParts)
//				if (bp != null)
//					bp.setContainingBodyPart(this);
//	}
//
//	public Set<BodyPart> getContainedBodyParts() {
//		return containedBodyParts;
//	}
//
//	public BodyPart getContainingBodyPart() {
//		return containingBodyPart;
//	}
//
//	protected void setContainingBodyPart(BodyPart containingBodyPart) {
//		this.containingBodyPart = containingBodyPart;
//	}
//
//	public void addContainedBodyPart(BodyPart bp) {
//		if (bp == null) return;
//		if (this.containedBodyParts == null) this.containedBodyParts = new CopyOnWriteArraySet<BodyPart>();
//		this.containedBodyParts.add(bp);
//		bp.setContainingBodyPart(this);
//	}

//	
//	public boolean isSameOrMoreSpecific(BodyPart arg) {
//// returns true if the two body parts are the same or this body part is contained by the other
//		if (arg == null) return true; // no body part at all is most unspecific
//		if (arg == this) return true;
//		String argLemma = arg.getLemma(); // need many times, anyway
//		if (argLemma == null) return true; // how should this happen? stay defensive, anyway - same as no body part
//// traverse list of body parts of arg up to the root, compare to this body part
//		BodyPart t1 = this;
//		while (t1 != null) {
//			if (argLemma.equals(t1.getLemma())) return true;
//			t1 = t1.getContainingBodyPart();
//		}
//		return false;
//	}
//
//	@Override
//	public String toString() {
//		return lemma;
//	}
//
//	@Override
//	public int hashCode() {
//		return lemma.hashCode();
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		BodyPart other = (BodyPart) obj;
//		if (this.lemma == null)
//			return false; // should never happen, but stay defensive
//		return this.lemma.equals(other.lemma);
//	}
}