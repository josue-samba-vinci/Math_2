public class Ens2 extends EnsembleAbstrait {

	private Elt[] elements; // contient les elements de l'ensemble. Il ne peut pas y avoir de doublon.
	private int cardinal;

	public Ens2() {
		//TODO
		elements = new Elt[MAX];
		cardinal=0;
	}



	public boolean estVide() {
		//TODO
		if (cardinal == 0)
			return true;
		else
			return false;
	}


	// renvoie un element de l'ensemble s'il n'est pas vide
	// lance une MathException si l'ensemble est vide
	public Elt unElement() {
		//TODO
		if (estVide())
			throw new MathException();
		else return elements[0];
	}

	// renvoie true ssi e appartient � l'ensemble courant
	// lance une IllegalArgumentException en cas de param�tre invalide
	public boolean contient(Elt e) {
		//TODO
		int position = 0;
		if (e == null)
			throw new IllegalArgumentException();
		for (int i = 0; i < cardinal; i++) {
			if (elements[i].equals(e))
				position = i;
				return true;
		}
		return false;
	}

	// ajoute e (�ventuellement) � l'ensemble courant
	//AJOUTE SI IL NE LE CONTIENT PAS CAR ON NE PEUT PAS AVOIR DEUX FOIS LE MEME ELEMENT DANS UN TABLEAU
	// lance une IllegalArgumentException en cas de param�tre invalide
	public void ajouter(Elt e) {
		//TODO
		if (e==null)
			throw new IllegalArgumentException();
		if (!contient(e)){
			elements[e.val()] = e;
			cardinal--;
		}
	}

	// enl�ve e (�ventuellement) de l'ensemble courant
	// lance une IllegalArgumentException en cas de param�tre invalide
	public void enlever(Elt e) {
		//TODO
		
	}

	// renvoie le cardinal de l'ensemble courant
	public int cardinal() {
		//TODO
		return cardinal;
	}

	// this <- compl�m. de this
	public void complementer() {
		//TODO;
		
	}

	// renvoie une cha�ne de caract�re d�crivant this en extension
	public String toString() {
		//TODO
		return null ;
	}

}
