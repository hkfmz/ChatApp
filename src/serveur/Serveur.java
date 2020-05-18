package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Serveur implements Runnable {
	private int port;
	private ServerSocket socket;
	private ArrayList<Socket> clients;
	private ArrayList<ClientThread> clientThreads;
	public ObservableList<String> serveurConnect;
	public ObservableList<String> nomClients;
	public Vector<Compte> Comptes = GestionCompte.comptes;

	public Serveur(int portNumber) throws IOException {
		this.port = portNumber;
		serveurConnect = FXCollections.observableArrayList();
		nomClients = FXCollections.observableArrayList();
		clients = new ArrayList<Socket>();
		clientThreads = new ArrayList<ClientThread>();
		socket = new ServerSocket(portNumber);

	}

	public void startServer() {
		try {
			socket = new ServerSocket(this.port);
			serveurConnect = FXCollections.observableArrayList();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						serveurConnect.add("Liste des clients ");
						Vector<Compte> comptes = new Vector<Compte>();
						comptes = GestionCompte.afficher();
						System.out.println("je suis server");
						if (comptes.isEmpty()) {
							serveurConnect.add("Liste vide ");
						} else {
							for (int i = 0; i < comptes.size(); i++) {
								serveurConnect.add(comptes.get(i).getLogin());
							}
						}

					}
				});

				final Socket clientSocket = socket.accept();
				clients.add(clientSocket);
				ClientThread clientThreadHolderClass = new ClientThread(clientSocket, this);
				Thread clientThread = new Thread(clientThreadHolderClass);
				clientThreads.add(clientThreadHolderClass);
				clientThread.setDaemon(true);
				clientThread.start();
				InterfaceServeur.threads.add(clientThread);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clientDisconnected(ClientThread client) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				serveurConnect.add(client.getNomClient() + " / " + client.getClientSocket().getRemoteSocketAddress()
						+ " déconnecté");

				Compte c = new Compte(client.getNomClient(), client.getPassClient());
				Comptes.remove(c);

				clients.remove(clientThreads.indexOf(client));
				System.out.println("erreur" + clientThreads.indexOf(client));
				clientThreads.remove(clientThreads.indexOf(client));
			}
		});

	}

	public void ecrireBroadcast(String input) {
		for (ClientThread clientThread : clientThreads) {
			clientThread.ecrireAuServeur(input);
		}
	}
	
	public void ecrireUnSocket(String input, ClientThread clientThread) {
			clientThread.ecrireAuServeur(input);
	}

}
