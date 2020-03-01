package servicesProg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import bri.Service;
import bri.ServiceRegistry;

public class ServiceArrêt implements Service {
	
	private Socket client;
	
	public ServiceArrêt(Socket sock) {
		this.client = sock;
	}
	
	@Override
	public void run() {
		
	}

}
