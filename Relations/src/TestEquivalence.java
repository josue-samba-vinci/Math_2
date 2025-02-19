import java.lang.reflect.Field;
import java.util.Scanner;

public class TestEquivalence {
	private final static Scanner scanner = new Scanner(System.in);
	private static final Class cl = Equivalence.class;
	private static Field fieldSsJac;
	private static Field fieldRep;
	private static Field fieldVersion;

	private static final String[] NOMS_METHODE = { "Equivalence(Ensemble)","contient(Couple c)",
			"ajouter(Couple)", "Equivalence(Relation)", 
			"classe", "enlever(Couple c)", "nbreClasses", "quotient" };

	public static void main(String[] args) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		fieldSsJac = cl.getDeclaredField("sousJac");
		fieldSsJac.setAccessible(true);
		fieldRep = cl.getDeclaredField("tabRep");
		fieldRep.setAccessible(true);
		fieldVersion = cl.getDeclaredField("numVersion");
		fieldVersion.setAccessible(true);
		System.out.println("**************************************");
		System.out.println("Programme Test pour la classe Equivalence");
		System.out.println("**************************************");
		int choix = 0;
		while (true) {
			for (int i = 0; i < NOMS_METHODE.length; i++) {
				System.out.println((i + 1) + " -> Tester la m�thode '"
						+ NOMS_METHODE[i] + "'");
			}

			choix = scanner.nextInt();
			boolean testOK;
			switch (choix) {
			case 1:
				testOK = testEquivalenceEns();
				break;
			case 2:
				testOK = testContientCouple();
				break;
			case 3:
				testOK = testAjouterCouple();
				break;
			case 4:
				testOK = testEquivalenceRel();
				break;
			case 5:
				testOK = testClasse();
				break;
			case 6:
				testOK = testEnleverCouple();
				break;
			case 7:
				testOK = testNbreClasses();
				break;
			case 8:
				testOK = testQuotient();
				break;
			default:
				return;
			}
			
			System.out.println() ;
			if (testOK)
				System.out.println("Le test de la m�thode "
						+ NOMS_METHODE[choix - 1] + " a r�ussi.");
			else
				System.out.println("Le test de la m�thode "
						+ NOMS_METHODE[choix - 1] + " a �chou�.");
			System.out.println() ;
			
		}
	}

	private static boolean testEquivalenceEns()
			throws IllegalArgumentException, IllegalAccessException {
		boolean testOK = true;
		Ensemble ens = null;
		try {
			new Equivalence(ens);
			testOK = false;
			System.out
					.println("Lde param�tre est null --> Il fallait lancer une IllegalArgumentException.");
		} catch (IllegalArgumentException e) {

		}

		ens = new Ensemble("3,7,8,13,16,23,34");
		Equivalence eq = new Equivalence(ens);
		Ensemble sousJac = (Ensemble) fieldSsJac.get(eq);
		if (sousJac == null) {
			testOK = false;
			System.out
					.println("L'ensemble sous-jacent n'a pas �t� initialis�.");
		} else if (sousJac == ens) {
			testOK = false;
			System.out
					.println("Vous avez copi� l'adresse m�moire de l'ensemble sous-jacent dans l'attribut.");
		} else if (!sousJac.equals(ens)) {
			testOK = false;
			System.out
					.println("L'ensemble sous-jacent ne contient pas les bons �l�ments.");
		}
		Elt[] tabRep = (Elt[]) fieldRep.get(eq);
		if (tabRep == null) {
			testOK = false;
			System.out
					.println("Vous n'avez pas initialis� la table des repr�sentants.");
		} else if (tabRep.length != Elt.MAXELT.val() + 1) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas la bonne dimension");
		} else {
			for (Elt e : ens) {
				if (!tabRep[e.val()].equals(e)) {
					testOK = false;
					System.out
							.println("La table des repr�sentants n'a pas �t� bien initialis�e.");
					break;
				}
			}
			if (testOK) {
				Ensemble ensComp = ens.clone();
				ensComp.complementer();
				for (Elt e : ensComp) {
					if (tabRep[e.val()] != null) {
						testOK = false;
						System.out
								.println("La table des repr�sentants n'a pas �t� bien initialis�e.");
						break;
					}
				}
			}

		}
		if (testOK) {
			ens = new Ensemble();
			eq = new Equivalence(ens);
			sousJac = (Ensemble) fieldSsJac.get(eq);
			if (sousJac == null) {
				testOK = false;
				System.out
						.println("L'ensemble sous-jacent n'a pas �t� initialis�.");
			} else if (sousJac == ens) {
				testOK = false;
				System.out
						.println("Vous avez copi� l'adresse m�moire de l'ensemble sous-jacent dans l'attribut.");
			} else if (!sousJac.equals(ens)) {
				testOK = false;
				System.out
						.println("L'ensemble sous-jacent ne contient pas les bons �l�ments.");
			}
			tabRep = (Elt[]) fieldRep.get(eq);
			if (tabRep == null) {
				testOK = false;
				System.out
						.println("Vous n'avez pas initialis� la table des repr�sentants.");
			} else if (tabRep.length != Elt.MAXELT.val() + 1) {
				testOK = false;
				System.out
						.println("La table des repr�sentants n'a pas la bonne dimension");
			} else {

				Ensemble ensComp = ens.clone();
				ensComp.complementer();
				for (Elt e : ensComp) {
					if (tabRep[e.val()] != null) {
						testOK = false;
						System.out
								.println("La table des repr�sentants n'a pas �t� bien initialis�e.");
						break;
					}
				}
			}
		}

		return testOK;
	}
	
	private static boolean testContientCouple()
			throws IllegalAccessException {

		boolean testOK = true;
		Equivalence eq = createDg1();
		try {
			eq.contient(null);
			testOK = false;
			System.out.println("On a appel� la m�thode avec un param�tre null.");
			System.out.println("-->Il fallait lancer une IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
		}
		
		
		Elt eD = new Elt(1);
		Elt eA = new Elt(2);
		Couple c = new Couple(eD, eA);
		int versionAv = fieldVersion.getInt(eq);
		try {	
			eq.contient(c);
			testOK = false;
			System.out.println("On a appel� la m�thode contient sur un couple dont l'�l�ment de d�part n'est pas dans l'ensmble sous-jacent.");
			System.out.println("-->Il fallait lancer une IllegalArgumentException.");
		} catch (IllegalArgumentException e){
			
		}
		eD = new Elt(4);
		eA = new Elt(7);
		c = new Couple(eD, eA);
		try {	
			eq.contient(c);
			testOK = false;
			System.out.println("On a appel� la m�thode contient sur un couple dont l'�l�ment d'arriv�e n'est pas dans l'ensmble sous-jacent.");
			System.out.println("-->Il fallait lancer une IllegalArgumentException.");
		} catch (IllegalArgumentException e){
			
		}
		eD = new Elt(18);
		eA = new Elt(21);
		c = new Couple(eD, eA);
		if (eq.contient(c)) {
			System.out
					.println("On a cr�� une �quivalence � partir de la relation dg1.");
			System.out.println("Elle ne devrait pas contenir le couple " + c
					+ ".");
			System.out.println("Votre m�thode dit qu'elle le contient.");
		}
		int versionAp = fieldVersion.getInt(eq);
		if (versionAv != versionAp) {
			testOK = false;
			System.out
					.println("La m�thode contient ne modifie pas la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer le num�ro de version.");
		}
		eD = new Elt(18);
		eA = new Elt(29);
		c = new Couple(eD, eA);
		if (!eq.contient(c)) {
			testOK = false;
			System.out
					.println("On a cr�� une �quivalence � partir de la relation dg1.");
			System.out
					.println("Elle devrait contenir le couple " + c + ".");
			System.out.println("Votre m�thode dit qu'elle ne le contient pas.");
		}
		return testOK;
	}

	private static boolean testAjouterCouple() throws IllegalArgumentException,
			IllegalAccessException {
		boolean testOK = true;
		Ensemble ens = new Ensemble("3,7,8,13,16,23,34,35,36,37,40");
		Ensemble copie = ens.clone();
		Equivalence eq = createEqEns(ens);
		Elt eD = new Elt(5);
		Elt eA = new Elt(13);
		Couple cAj = new Couple(eD, eA);
		int versionAvantAjout = fieldVersion.getInt(eq);
		try {
			eq.ajouter(null);
			testOK = false;
			System.out
					.println("Le couple pass� en param�tre est null.");
			System.out.println("Il fallait lancer une IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionApresAjout = fieldVersion.getInt(eq);
			if (versionAvantAjout != versionApresAjout) {
				testOK = false;
				System.out
						.println("On essay� d'ajouter un couple null.");
				System.out.println("L'�quivalence n'a donc pas �t� modidi�e.");
				System.out
						.println("Il ne fallait donc pas changer le num�ro de version.");
			}
		}
		try {
			eq.ajouter(cAj);
			testOK = false;
			System.out
					.println("Le point de d�part du couple n'appartient pas � l'ensemble sous-jacent de l'�quivalence");
			System.out.println("Il fallait lancer une IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionApresAjout = fieldVersion.getInt(eq);
			if (versionAvantAjout != versionApresAjout) {
				testOK = false;
				System.out
						.println("On essay� d'ajouter un couple dont l'origine n'appartient pas � l'ensemble sous-jacent de l'�quivalence.");
				System.out.println("L'�quivalence n'a donc pas �t� modidi�e.");
				System.out
						.println("Il ne fallait donc pas changer le num�ro de version.");
			}
		}
		eq = createEqEns(ens);
		eD = new Elt(3);
		eA = new Elt(17);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		try {
			eq.ajouter(cAj);
			testOK = false;
			System.out
					.println("Le point d'arriv�e du couple n'appartient pas � l'ensemble sous-jacent de l'�quivalence");
			System.out.println("Il fallait lancer une IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionApresAjout = fieldVersion.getInt(eq);
			if (versionAvantAjout != versionApresAjout) {
				testOK = false;
				System.out
						.println("On essay� d'ajouter un couple dont l'arriv�e n'appartient pas � l'ensemble sous-jacent de l'�quivalence.");
				System.out.println("L'�quivalence n'a donc pas �t� modidi�e.");
				System.out
						.println("Il ne fallait donc pas changer le num�ro de version.");
			}
		}
		eq = createEqEns(ens);
		eD = new Elt(3);
		eA = new Elt(16);

		cAj = new Couple(eD, eA);
		copie.enlever(eD);
		copie.enlever(eA);
		Ensemble classeDe16 = new Ensemble(eD);
		versionAvantAjout = fieldVersion.getInt(eq);
		eq.ajouter(cAj); // ajout de 3-->16
		int versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout == versionApresAjout) {
			testOK = false;
			System.out.println("Le num�ro de version n'a pas �t� mise � jour.");
		}
		classeDe16.ajouter(eA); // classeDe16 = {3,16}
		Ensemble sousJac = (Ensemble) fieldSsJac.get(eq);
		if (!sousJac.equals(ens)) {
			testOK = false;
			System.out.println("L'ensemble sous-jacent a �t� modifi�.");
		}
		Elt[] tabRep = (Elt[]) fieldRep.get(eq);
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
				break;
			}
		}
		Elt repEA = tabRep[eA.val()];
		if (!classeDe16.contient(repEA)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe16) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("1");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		if (!testOK)
			return testOK;
		eD = new Elt(34);
		eA = new Elt(16);
		copie.enlever(eD);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		eq.ajouter(cAj); // ajout de 34-->16
		versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout == versionApresAjout) {
			testOK = false;
			System.out.println("Le num�ro de version n'a pas �t� mis � jour.");
		}
		classeDe16.ajouter(eD);// classeDe16 = {3,16,34}
		if (!sousJac.equals(ens)) {
			testOK = false;
			System.out.println("L'ensemble sous-jacent a �t� modifi�.");
		}
		tabRep = (Elt[]) fieldRep.get(eq);
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("Des �l�ments ont chang� de repr�sentant alors qu'ils ne devaient pas.");
				break;
			}
		}
		repEA = tabRep[eA.val()];
		if (!classeDe16.contient(repEA)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe16) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("2");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		eD = new Elt(23);
		eA = new Elt(37);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		eq.ajouter(cAj);// ajout de 23-->37
		versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout == versionApresAjout) {
			testOK = false;
			System.out.println("Le num�ro de version n'a pas �t� mis � jour.");
		}
		Ensemble classeDe23 = new Ensemble(eD);
		classeDe23.ajouter(eA);// classe de 23 = {23,37}
		copie.enlever(eA);
		copie.enlever(eD);
		tabRep = (Elt[]) fieldRep.get(eq);
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("Des �l�ments ont chang� de repr�sentant alors qu'ils ne devaient pas.");
				break;
			}
		}
		repEA = tabRep[eA.val()];
		if (!classeDe23.contient(repEA)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe23) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("3");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		if (!testOK)
			return testOK;
		Elt rep16 = tabRep[16];
		if (!classeDe16.contient(rep16)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe16) {
				if (!rep16.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("4");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		if (!testOK)
			return testOK;
		eD = new Elt(7);
		eA = new Elt(37);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		eq.ajouter(cAj);// ajout de 7-->37
		versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout == versionApresAjout) {
			testOK = false;
			System.out.println("Le num�ro de version n'a pas �t� mis � jour.");
		}
		classeDe23.ajouter(eD); // classe de 23 = {7,23,37}
		copie.enlever(eA);
		copie.enlever(eD);
		tabRep = (Elt[]) fieldRep.get(eq);
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("Des �l�ments ont chang� de repr�sentant alors qu'ils ne devaient pas.");
				break;
			}
		}
		repEA = tabRep[eA.val()];
		if (!classeDe23.contient(repEA)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe23) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("5");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		if (!testOK)
			return testOK;
		rep16 = tabRep[16];
		if (!classeDe16.contient(rep16)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe16) {
				if (!rep16.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("6");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		if (!testOK)
			return testOK;
		eD = new Elt(13);
		eA = new Elt(37);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		eq.ajouter(cAj);// ajout de 13-->37
		versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout == versionApresAjout) {
			testOK = false;
			System.out.println("Le num�ro de version n'a pas �t� mis � jour.");
		}
		classeDe23.ajouter(eD); // classe de 23 = {7,13,23,37}
		copie.enlever(eD);
		tabRep = (Elt[]) fieldRep.get(eq);
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("Des �l�ments ont chang� de repr�sentant alors qu'ils ne devaient pas.");
				break;
			}
		}
		repEA = tabRep[eA.val()];
		if (!classeDe23.contient(repEA)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe23) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("7");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		if (!testOK)
			return testOK;
		rep16 = tabRep[16];
		if (!classeDe16.contient(rep16)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe16) {
				if (!rep16.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("8");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}

		eD = new Elt(23);
		eA = new Elt(16);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		;
		eq.ajouter(cAj);// ajout de 23-->16
		versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout == versionApresAjout) {
			testOK = false;
			System.out.println("Le num�ro de version n'a pas �t� mis � jour.");
		}
		classeDe16.ajouter(classeDe23); // classe de 16 = {3,7,13,16,23,34,37}
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("Des �l�ments ont chang� de repr�sentant alors qu'ils ne devaient pas.");
				break;
			}
		}
		repEA = tabRep[eA.val()];
		if (!classeDe16.contient(repEA)) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas �t� bien mise � jour.");
		} else {
			for (Elt e : classeDe16) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out.println("9");
					System.out
							.println("Des �l�ments devant appartenir � la m�me classe n'ont pas le m�me repr�sentant.");
					break;
				}
			}
		}
		eD = new Elt(37);
		eA = new Elt(13);
		cAj = new Couple(eD, eA);
		versionAvantAjout = fieldVersion.getInt(eq);
		eq.ajouter(cAj);// ajout de 23-->16
		versionApresAjout = fieldVersion.getInt(eq);
		if (versionAvantAjout != versionApresAjout) {
			testOK = false;
			System.out
					.println("On a essay� d'ajouter une fl�che qui se trouvait d�j� dans l'�quivalence.");
			System.out
					.println("Il ne fallait dont pas mettre � jour le num�ro de version.");
		}
		classeDe16.ajouter(classeDe23); // classe de 16 = {3,7,13,16,23,34,37}
		for (Elt e : copie) {
			if (!tabRep[e.val()].equals(e)) {
				testOK = false;
				System.out
						.println("Des �l�ments ont chang� de repr�sentant alors qu'ils ne devaient pas.");
				break;
			}
		}
		repEA = tabRep[eA.val()];
		if (!classeDe16.contient(repEA)) {
			testOK = false;
			System.out
					.println("On a essay� d'ajouter une fl�che qui se trouvait d�j� dans l'�quivalence.");
			System.out
					.println("La table des repr�sentants a �t� mofifi�e alors qu'elle ne devait pas l'�tre.");
		} else {
			for (Elt e : classeDe16) {
				if (!repEA.equals(tabRep[e.val()])) {
					testOK = false;
					System.out
							.println("On a essay� d'ajouter une fl�che qui se trouvait d�j� dans l'�quivalence.");
					System.out
							.println("La table des repr�sentants a �t� mofifi�e alors qu'elle ne devait pas l'�tre.");
					break;
				}
			}
		}
		return testOK;
	}

	private static boolean testEquivalenceRel() throws IllegalAccessException {
		boolean testOK = true;
		Relation rel = null;
		try {
			new Equivalence(rel);
			testOK = false;
			System.out
					.println("Lde param�tre est null --> Il fallait lancer une IllegalArgumentException.");
		} catch (IllegalArgumentException e) {

		}
		try {
			new Equivalence(Io.chargerRelation("re1.rel"));
			testOK = false;
			System.out
					.println("On a essay� de cr�er une �quivalence � partir d'une relation dont l'ensemble de d�part est diff�rent de l'enamble d'arriv�e.");
			System.out.println("Il fallait lancer une IllegalArgumentException.");
		} catch (IllegalArgumentException m) {

		}
		Ensemble[] quotient = { new Ensemble("{2}"), new Ensemble("{4}"),
				new Ensemble("{6,18,19,20,29}"),
				new Ensemble("8,10,21,31,33,35"), new Ensemble("12,14,22,23"),
				new Ensemble("{16}"), new Ensemble("{17}"),
				new Ensemble("{25,27}"), new Ensemble("{24,37,39}") };
		Relation dg1 = Io.chargerRelation("dg1.rel");
		Relation copie = dg1.clone();
		Equivalence eq = new Equivalence(dg1);

		if (!dg1.depart().equals(copie.depart())
				|| !dg1.arrivee().equals(copie.arrivee())) {
			System.out
					.println("L'ensemble de d�part ou d'arriv�e de la relation pass�e en param�tre a �t� modifi�e.");
			testOK = false;
		}
		for (Elt eD : dg1.depart()) {
			for (Elt eA : dg1.arrivee()) {

				if (dg1.contient(eD, eA) != copie.contient(eD, eA)) {
					testOK = false;
					System.out
							.println("La relation pass�e en param�tre au constructeur a �t� mofifi�e.");
					break;
				}
			}
		}
		Ensemble sousJac = (Ensemble) fieldSsJac.get(eq);
		if (sousJac == null) {
			testOK = false;
			System.out
					.println("L'ensemble sous-jacent n'a pas �t� initialis�.");
		} else if (!sousJac.equals(copie.depart())) {
			testOK = false;
			System.out
					.println("L'ensemble sous-jacent ne contient pas les bons �l�ments.");
		}
		Elt[] tabRep = (Elt[]) fieldRep.get(eq);
		if (tabRep == null) {
			testOK = false;
			System.out
					.println("Vous n'avez pas initialis� la table des repr�sentants.");
		} else if (tabRep.length != Elt.MAXELT.val() + 1) {
			testOK = false;
			System.out
					.println("La table des repr�sentants n'a pas la bonne dimension");
		} else {
			for (Ensemble cl : quotient) {
				Elt x = cl.unElement();
				Elt rep = tabRep[x.val()];
				if (!cl.contient(rep)) {
					testOK = false;
					System.out
							.println("On a cr�� une relation d'�quivalence � partir de dg1.");
					System.out.println("La classe d'�quivalence de " + x
							+ " devrait �tre " + cl + ".");
					System.out.println("Le repr�sentant de " + x
							+ " dans votre relation d'�quivalence est " + rep);
					System.out.println(rep + " n'appartient pas � " + cl + ".");
					break;
				}
				for (Elt e : cl) {
					if (!tabRep[e.val()].equals(rep)) {
						testOK = false;
						System.out
								.println("On a cr�� une relation d'�quivalence � partir de dg1.");
						System.out
								.println(x
										+ " "
										+ e
										+ " devraient �tre dans la m�me classe d'�quivalence et avoir le m�me repr�sentant.");
						System.out
								.println("Repr�sentant de " + x + " : " + rep);
						System.out.println("Repr�sentant de " + e + " : "
								+ tabRep[e.val()]);
						break;
					}
				}
				if (!testOK)
					break;
			}
			if (testOK) {
				EnsembleAbstrait ensComp = copie.depart();
				ensComp.complementer();
				for (Elt e : ensComp) {
					if (tabRep[e.val()] != null) {
						testOK = false;
						System.out
								.println("La table des repr�sentants n'a pas �t� bien initialis�e.");
						break;
					}
				}
			}

		}		
		return testOK;
	}



	private static boolean testClasse() throws IllegalAccessException {

		boolean testOK = true;
		Ensemble[] quotient = { new Ensemble("{2}"), new Ensemble("{4}"),
				new Ensemble("{6,18,19,20,29}"),
				new Ensemble("8,10,21,31,33,35"), new Ensemble("12,14,22,23"),
				new Ensemble("{16}"), new Ensemble("{17}"),
				new Ensemble("{25,27}"), new Ensemble("{24,37,39}") };
		Equivalence eq = createDg1();
		int versionAv = fieldVersion.getInt(eq);
		Elt e = new Elt(5);
		try {
			eq.classe(e);
			testOK = false;
			System.out
					.println(e
							+ " n'appartient pas � l'ensemble sous-jacent de l'�quivalence cr��e � partir de la relation dg1.");
			System.out.println("Il fallait lancre une IllegalArgumentException.");
		} catch (IllegalArgumentException m) {

		}
		int versionAp = fieldVersion.getInt(eq);
		if (versionAv != versionAp) {
			testOK = false;
			System.out
					.println("La m�thode classe ne modifie pas la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer le num�ro de version.");
		}
		for (Ensemble cl : quotient) {
			for (Elt eCl : cl) {
				EnsembleAbstrait classeECl = eq.classe(eCl);
				if (!classeECl.equals(cl)) {
					System.out
							.println("On a cr�� une relation d'�quivalence � partir de la relation dg1.");
					System.out.println("La classe de " + eCl
							+ " pour cette �quivalente devrait �tre " + cl
							+ ".");
					System.out
							.println("Votre m�thode a renvoy� : " + classeECl);
					testOK = false;
					break;
				}
			}
			if (!testOK)
				break;
		}
		versionAp = fieldVersion.getInt(eq);
		if (versionAv != versionAp) {
			testOK = false;
			System.out
					.println("La m�thode classe ne modifie pas la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer le num�ro de version.");
		}
		return testOK;
	}

	private static boolean testEnleverCouple() throws IllegalAccessException {

		boolean testOK = true;
		Equivalence eq = createDg1();
		int versionAv = fieldVersion.getInt(eq);
		try {
			eq.enlever(null);
			testOK = false;
			System.out
					.println("On a appel� la m�thode enlever avec un param�tre null.");
			System.out.println("Il fallait lancer de IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionAp = fieldVersion.getInt(eq);
			if (versionAp != versionAv) {
				testOK = false;
				System.out
						.println("On a appel� la m�thode enlever dans un cas o� une IllegalArgumentException est lanc�e.");
				System.out
						.println("Il ne fallait pas changer le num�ro de version.");
			}
		}
		Elt eD = new Elt(3);
		Elt eA = new Elt(8);
		Couple cEnl = new Couple(eD, eA);
		try {
			eq.enlever(cEnl);
			testOK = false;
			System.out
					.println("On essaie d'enlever un couple dont le point de d�part n'est pas dans l'ensemble sous-jacent.");
			System.out.println("Il fallait lancer de IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionAp = fieldVersion.getInt(eq);
			if (versionAp != versionAv) {
				testOK = false;
				System.out
						.println("On a appel� la m�thode enlever dans un cas o� une IllegalArgumentException est lanc�e.");
				System.out
						.println("Il ne fallait pas changer le num�ro de version.");
			}
		}
		versionAv = fieldVersion.getInt(eq);
		eD = new Elt(3);
		eA = new Elt(8);
		cEnl = new Couple(eD, eA);
		try {
			eq.enlever(cEnl);
			testOK = false;
			System.out
					.println("On essaie d'enlever un couple dont le point d'arriv�e n'est pas dans l'ensemble sous-jacent.");
			System.out.println("Il fallait lancer de IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionAp = fieldVersion.getInt(eq);
			if (versionAp != versionAv) {
				testOK = false;
				System.out
						.println("On a appel� la m�thode enlever dans un cas o� une IllegalArgumentException est lanc�e.");
				System.out
						.println("Il ne fallait pas changer le num�ro de version.");
			}
		}
		versionAv = fieldVersion.getInt(eq);
		eD = new Elt(20);
		eA = new Elt(19);
		cEnl = new Couple(eD, eA);
		try {
			eq.enlever(cEnl);
			testOK = false;
			System.out
					.println("On essaie d'enlever la fl�che (20,19) de la relation d'�quivalence cr�e � partir de la relation dg1.");
			System.out.println("Il fallait lancer de IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionAp = fieldVersion.getInt(eq);
			if (versionAp != versionAv) {
				testOK = false;
				System.out
						.println("On a appel� la m�thode enlever dans un cas o� une IllegalArgumentException est lanc�e.");
				System.out
						.println("Il ne fallait pas changer le num�ro de version.");
			}
		}
		versionAv = fieldVersion.getInt(eq);
		eD = new Elt(2);
		eA = new Elt(2);
		cEnl = new Couple(eD, eA);
		try {
			eq.enlever(cEnl);
			testOK = false;
			System.out
					.println("On essaie d'enlever la fl�che (2,2) de la relation d'�quivalence cr�e � partor de la relation dg1.");
			System.out.println("Il fallait lancer de IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionAp = fieldVersion.getInt(eq);
			if (versionAp != versionAv) {
				testOK = false;
				System.out
						.println("On a appel� la m�thode enlever dans un cas o� une IllegalArgumentException est lanc�e.");
				System.out
						.println("Il ne fallait pas changer le num�ro de version.");
			}
		}
		eD = new Elt(27);
		eA = new Elt(27);
		cEnl = new Couple(eD, eA);
		try {
			eq.enlever(cEnl);
			testOK = false;
			System.out
					.println("On essaie d'enlever la fl�che(27,27) de la relation d'�quivalence cr�e � partir de la relation dg1.");
			System.out.println("Il fallait lancer de IllegalArgumentException");
		} catch (IllegalArgumentException m) {
			int versionAp = fieldVersion.getInt(eq);
			if (versionAp != versionAv) {
				testOK = false;
				System.out
						.println("On a appel� la m�thode enlever dans un cas o� une IllegalArgumentException est lanc�e.");
				System.out
						.println("Il ne fallait pas changer le num�ro de version.");
			}
		}
		Elt[] repAv = (Elt[]) fieldRep.get(eq);
		Ensemble sousJacAv = (Ensemble) fieldSsJac.get(eq);
		versionAv = fieldVersion.getInt(eq);
		eD = new Elt(17);
		eA = new Elt(18);
		cEnl = new Couple(eD, eA);
		eq.enlever(cEnl);
		int versionAp = fieldVersion.getInt(eq);
		if (versionAp != versionAv) {
			testOK = false;
			System.out
					.println("On a appel� la m�thode enlever avec en param�tre un couple n'appartenant pas � la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer le num�ro de version.");
		}
		Ensemble sousJacAp = (Ensemble) fieldSsJac.get(eq);
		if (!sousJacAp.equals(sousJacAv)) {
			testOK = false;
			System.out
					.println("On a appel� la m�thode enlever avec en param�tre un couple n'appartenant pas � la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer l'ensemble sous-jacent.");
		}
		Elt[] repAp = (Elt[]) fieldRep.get(eq);
		if (repAv.length != repAp.length) {
			testOK = false;
			System.out
					.println("La m�thode enlever ne doit pas changer la taille de la table des repr�sentants.");
		} else {
			for (Elt e : sousJacAp) {
				if (!repAv[e.val()].equals(repAp[e.val()])) {
					testOK = false;
					System.out
							.println("On a appel� la m�thode enlever avec en param�tre un couple n'appartenant pas � la relation d'�quivalence.");
					System.out
							.println("Il ne fallait pas changer la table des repr�sentants.");
					break;
				}
			}
			if (testOK) {
				Ensemble compl = sousJacAv.clone();
				compl.complementer();
				for (Elt e : compl) {
					if (repAp[e.val()] != null) {
						testOK = false;
						System.out
								.println("On a appel� la m�thode enlever avec en param�tre un couple n'appartenant pas � la relation d'�quivalence.");
						System.out
								.println("Il ne fallait pas changer la table des repr�sentants.");
						break;
					}
				}
			}
		}
		repAv = (Elt[]) fieldRep.get(eq);
		sousJacAv = (Ensemble) fieldSsJac.get(eq);
		versionAv = fieldVersion.getInt(eq);
		eD = new Elt(25);
		eA = new Elt(27);
		cEnl = new Couple(eD, eA);
		eq.enlever(cEnl);
		versionAp = fieldVersion.getInt(eq);
		if (versionAv == versionAp) {
			testOK = false;
			System.out
					.println("On a enlev� de la relation d'�quivalence un couple qui pouvait l'�tre.");
			System.out
					.println("Il fallait mettre � jour le num�ro de version.");
		}
		sousJacAp = (Ensemble) fieldSsJac.get(eq);
		if (!sousJacAp.equals(sousJacAv)) {
			testOK = false;
			System.out
					.println("On a enlev� un couple de la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas modifier l'ensemble sous-jacent.");
		}
		repAp = (Elt[]) fieldRep.get(eq);
		if (repAv.length != repAp.length) {
			testOK = false;
			System.out
					.println("La m�thode enlever ne doit pas changer la taille de la table des repr�sentants.");
		} else {
			Ensemble ensemble = sousJacAp.clone();
			ensemble.enlever(eD);
			ensemble.enlever(eA);
			for (Elt e : ensemble) {
				if (!repAv[e.val()].equals(repAp[e.val()])) {
					testOK = false;
					System.out
							.println("On a enlev� le couple ("
									+ eD
									+ ","
									+ eA
									+ ") de la relation d'�quivalence cr��e � partir de la relation dg1.");
					System.out
							.println("Il ne fallait pas changer le repr�senant de l'�l�ment "
									+ e + ".");
					break;
				}
			}
			if (!repAp[eD.val()].equals(eD)) {
				testOK = false;
				System.out
						.println("On a enlev� le couple ("
								+ eD
								+ ","
								+ eA
								+ ") de la relation d'�quivalence cr��e � partir de la relation dg1.");
				System.out.println("Le nouveau repr�sentant de " + eD
						+ " devrait �tre " + eD);
				System.out.println("Dans votre table, le repr�sentant de " + eD
						+ " est " + repAp[eD.val()]);
			}
			if (!repAp[eA.val()].equals(eA)) {
				testOK = false;
				System.out
						.println("On a enlever le couple ("
								+ eD
								+ ","
								+ eA
								+ ") de la relation d'�quivalence cr��e � partir de la relation dg1.");
				System.out.println("Le nouveau repr�sentant de " + eA
						+ " devrait �tre " + eA);
				System.out.println("Dans votre table, le repr�sentant de " + eA
						+ " est " + repAp[eA.val()]);
			}
			if (testOK) {
				Ensemble compl = sousJacAv.clone();
				compl.complementer();
				for (Elt e : compl) {
					if (repAp[e.val()] != null) {
						testOK = false;
						System.out
								.println("On a enlev� le couple ("
										+ eD
										+ ","
										+ eA
										+ ") de la relation d'�quivalence cr��e � partir de la relation dg1.");
						System.out.println("Le repr�sentant de " + e
								+ " n'est plus null.");
						break;
					}
				}
			}
		}
		return testOK;
	}

	private static boolean testNbreClasses() throws IllegalAccessException {

		boolean testOK = true;
		Equivalence eq = createDg1();
		int versionAv = fieldVersion.getInt(eq);
		int nbreClasses = eq.nbreClasses();
		if (nbreClasses != 9) {
			testOK = false ;
			System.out
					.println("L'�quivalence cr��e � partir de la relation dg1 devrait y avoir 9 classes.");
			System.out.println("Votre m�thode a renvoy� : " + nbreClasses);
		}
		int versionAp = fieldVersion.getInt(eq);
		if (versionAv != versionAp) {
			testOK = false;
			System.out
					.println("La m�thode nbreClasses ne modifie pas la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer le num�ro de version.");
		}
		return testOK;
	}

	private static boolean testQuotient() throws IllegalAccessException {

		boolean testOK = true;
		Ensemble[] quotient = { new Ensemble("{2}"), new Ensemble("{4}"),
				new Ensemble("{6,18,19,20,29}"),
				new Ensemble("8,10,21,31,33,35"), new Ensemble("12,14,22,23"),
				new Ensemble("{16}"), new Ensemble("{17}"),
				new Ensemble("{25,27}"), new Ensemble("{24,37,39}") };
		Equivalence eq = createDg1();
		int versionAv = fieldVersion.getInt(eq);
		Ensemble sousJacAv = ((Ensemble) fieldSsJac.get(eq)).clone();
		EnsembleAbstrait[] quot = eq.quotient();
		if (quot.length != 9) {
			System.out
					.println("La relation d'�quivalence cr��e � partir poss�dent 9 classes.");
			System.out
					.println("Le tableau renvoy� par la m�thode quotient devrait dans ce cas �tre de taille 9");
			System.out
					.println("La taille du tableau renvoy� par votre m�thode est "
							+ quot.length);
			testOK = false;
		} else {
			for (EnsembleAbstrait qTrouve : quot) {
				boolean ok = false;
				for (Ensemble qAtt : quotient) {
					if (qTrouve.equals(qAtt)) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					System.out
							.println("On a demand� le quotient pour la relation d'�quivalence cr��e � partir de dg1.");
					System.out
							.println("Vous avez renvoy� comme classe "
									+ qTrouve
									+ " qui n'est pas une classe de cette �quivalence.");
					testOK = false;
					break;
				}
			}
			if (testOK) {
				for (Ensemble qAtt : quotient) {
					boolean ok = false;
					for (EnsembleAbstrait qTrouve : quot) {
						if (qTrouve.equals(qAtt)) {
							ok = true;
							break;
						}
					}
					if (!ok) {
						System.out
								.println("On a demand� le quotient pour la relation d'�quivalence cr��e � partir de dg1.");
						System.out
								.println("Votre tableau ne contient pas la classe "
										+ qAtt + ".");
						testOK = false;
						break;
					}
				}
			}
		}
		int versionAp = fieldVersion.getInt(eq);
		if (versionAv != versionAp) {
			testOK = false;
			System.out
					.println("La m�thode quotient ne modifie pas la relation d'�quivalence.");
			System.out
					.println("Il ne fallait pas changer le num�ro de version.");
		}
		Ensemble sousJacAp = ((Ensemble) fieldSsJac.get(eq)).clone();
		if (!sousJacAv.equals(sousJacAp)) {
			testOK = false;
			System.out
					.println("La m�thode quotient a modifi� l'ensemble sous-jacent de la relation.");
		}
		return testOK;
	}


	private static Equivalence createDg1(){
		Ensemble ens = new Ensemble("{2,4,6,8,10,12,14,16..25,27,29,31,33,35,37,39}");
		Equivalence eq = new Equivalence(ens);
		try {
			fieldSsJac.set(eq, ens.clone());
			Elt[] tabRep = new Elt[Elt.MAXELT.val()+1];
			tabRep[2] = new Elt(2);
			tabRep[4] = new Elt(4);
			tabRep[16] = new Elt(16);
			tabRep[17] = new Elt(17);
			tabRep[18] = new Elt(20);
			tabRep[19] = new Elt(20);
			tabRep[20] = new Elt(20);
			tabRep[29] = new Elt(20);
			tabRep[6] = new Elt(20);
			tabRep[25] = new Elt(25);
			tabRep[27] = new Elt(25);
			tabRep[8] = new Elt(10);
			tabRep[10] = new Elt(10);
			tabRep[21] = new Elt(10);
			tabRep[31] = new Elt(10);
			tabRep[33] = new Elt(10);
			tabRep[35] = new Elt(10);
			tabRep[12] = new Elt(22);
			tabRep[14] = new Elt(22);
			tabRep[22] = new Elt(22);
			tabRep[23] = new Elt(22);
			tabRep[24] = new Elt(24);
			tabRep[37] = new Elt(24);
			tabRep[39] = new Elt(24);
			fieldRep.set(eq, tabRep);
		} catch (IllegalArgumentException | IllegalAccessException e) {
	
		}
		return eq;
	}
	private static Equivalence createEqEns(Ensemble ens){
		Equivalence eq = new Equivalence(ens);
		try {
			fieldSsJac.set(eq, ens.clone());
			Elt[] tabRep = new Elt[Elt.MAXELT.val()+1];
			for (Elt e : ens) tabRep[e.val()] = e;
			fieldRep.set(eq, tabRep);
		} catch (IllegalArgumentException e) {


		} catch (IllegalAccessException e) {

		}
		return eq;

	}

}
