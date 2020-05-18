package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Client implements Runnable {

	private Socket clientSocket;
	private BufferedReader serveurMessageClient;
	private PrintWriter clientMessageServeur;
	private String nom;
	private String password;
	public ObservableList<String> chatConnect;

	public Client(String adresse, int port, String name, String password, int exist)
			throws UnknownHostException, IOException {

		clientSocket = new Socket(adresse, port);
		serveurMessageClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		clientMessageServeur = new PrintWriter(clientSocket.getOutputStream(), true);
		chatConnect = FXCollections.observableArrayList();

		this.nom = name;
		this.password = password;
		if (exist == 0) {
			clientMessageServeur.println(name + "_" + password + "_new");
		} else {
			clientMessageServeur.println(name + "_" + password + "_old");
		}

	}

	public void ecrireAuServeur(String input) {
		clientMessageServeur.println(nom + " : " + input);
	}

	public void run() {

		while (true) {
			try {

				final String inputFromServer = serveurMessageClient.readLine();
				Platform.runLater(new Runnable() {
					public void run() {
						chatConnect.add(inputFromServer);
					}
				});

			} catch (SocketException e) {
				Platform.runLater(new Runnable() {
					public void run() {
						chatConnect.add("Error in server");
					}

				});
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String connecte() throws IOException {
		String rep = serveurMessageClient.readLine();
		if (rep.equals("valide")) {
			return "valide";

		} else if (rep.equals("not exist")) {
			return "not exist";
		} else {
			return "erreur password";
		}

	}

	public boolean creation_compte() throws IOException {
		String rep = serveurMessageClient.readLine();
		if (rep.equals("compte creer")) {
			return true;
		}
		System.out.println("client false");
		return false;
	}

	public void deconnect() {
		clientMessageServeur.println(nom + "_disconnect");
	}

}
