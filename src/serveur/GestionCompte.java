package serveur;

import java.util.Vector;

public class GestionCompte {
	static Vector<Compte> comptes = new Vector<Compte>();

	public String ouvrirCompte(String login, String password) {
		for (int i = 0; i < comptes.size(); i++) {
			if (comptes.get(i).getLogin().equals(login)) {
				if (comptes.get(i).getPassword().equals(password)) {
					return "valide";
				} else {
					return "erreur password";
				}
			}
		}
		return "not exist";
	}

	public boolean creation_compte(String login, String password) {
		for (int i = 0; i < comptes.size(); i++) {
			if (comptes.get(i).getLogin().equals(login)) {
				return false;

			}
		}
		Compte compte = new Compte(login, password);
		return comptes.add(compte);
	}

	public static Vector<Compte> afficher() {
		return comptes;
	}

	/*
	 * pas encours public Compte verifier(String login,String password) { for(int
	 * i=1;i<comptes.size();i++){ if(comptes.get(i).getLogin()==login) {
	 * if(comptes.get(i).getPassword()==password) { return comptes.get(i); } }
	 * 
	 * } return null; }
	 */

	public static boolean fermerCompte(String login) {
		for (int i = 1; i < comptes.size(); i++) {
			if (comptes.get(i).getLogin() == login) {
				Compte compte = new Compte(comptes.get(i).getLogin(), comptes.get(i).getPassword());
				comptes.remove(compte);
				return true;
			}
		}
		return false;

	}

}
