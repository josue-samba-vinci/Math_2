public class Ens1 extends EnsembleAbstrait {

	private boolean[] tabB; // e appartient � l'ensemble courant ssi tabE[e.val()] est � true.
	private int cardinal;

	public Ens1() {
		//TODO
		tabB = new boolean[Elt.MAXELT.val() + 1];
		cardinal = 0;
	}

	public boolean estVide() {
		//TODO
		if (cardinal == 0)
			return true;
		else
			return false;
	}

	public Elt unElement() {
		//TODO
		if (estVide())
			throw new MathException();
		else for (int i = 1; i <= Elt.MAXELT.val(); i++) {
			if (tabB[i] == true)
				return new Elt(i);
		}
		throw new InternalError();
	}

	public boolean contient(Elt e) {
		//TODO
		if (e == null)
			throw new IllegalArgumentException();
		return tabB[e.val()];
	}

	public void ajouter(Elt e) {
		//TODO
		if (e == null)
			throw new IllegalArgumentException();
		if (!contient(e)) {
			tabB[e.val()] = true;
			cardinal++;
		}
	}


	public void enlever(Elt e) {
		//TODO
		if (e == null)
			throw new IllegalArgumentException();
		if (contient(e)) {
			tabB[e.val()] = false;
			cardinal--;
		}
	}


	public int cardinal() {
		//TODO
		return cardinal;
	}
		/*cardinal = 0;
			for (int i = 0; i < tabB.length; i++) {
				if (tabB[i]==true) {
					cardinal++;
				}
			}
		return cardinal ;
	}*/

	public void complementer() {
		//TODO
		for (int i = 0; i < tabB.length; i++) {
			if (tabB[i] == true) {
				tabB[i] = false;
				//cardinal--;
			} else if (tabB[i] == false) {
				tabB[i] = true;
				//cardinal++;
			}
		}
		cardinal = Elt.MAXELT.val() - cardinal;
		//LE CARDINAL EST EGALE A LA LA LONGUEUR DE LA TABLE - LE NOMBRE D'ELEMENTS CONTENUS DANS L'ENSEMBLE
	}

	public String toString() {
		// TODO
		if (estVide()) return "{}";
		String ensemble = "{";
		for (int i = 0; i < tabB.length; i++) {
			if (tabB[i]) {
				//SI L'ELEMENT A LA POSITION I EXISTE
				ensemble += i + ",";
			}
		}
		ensemble += "}";
		return ensemble;
	}
}
