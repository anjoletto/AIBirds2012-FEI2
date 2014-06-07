/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ab.framework.server.commands.Command;

public class Server extends WebSocketServer {
	private Long id = 0L;
	private HashMap<Long, Result<?>> results = new HashMap<Long, Result<?>>();
	
	private class Result<T> {
		public Command<T> command;
		public SynchronousQueue<Object> queue = new SynchronousQueue<Object>();
	}
	
	public Server(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public Server(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		onOpen();
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		onClose();
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		JSONArray j = (JSONArray) JSONValue.parse(message);
		Long id = (Long) j.get(0);
		JSONObject data = (JSONObject) j.get(1);

		Result<?> result = results.get(id);
		
		if (result != null) {
			results.remove(id);
			try {
				result.queue.put(result.command.gotResponse(data));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@SuppressWarnings("unchecked")
	public <T> T send(Command<T> command) {
		JSONArray a = new JSONArray();
		a.add(id);
		a.add(command.getCommandName());
		a.add(command.getJSON());

		Result<T> result = new Result<T>();
		result.command = command;
		results.put(id, result);
		
		for (WebSocket conn : connections()) {	
			conn.send(a.toJSONString());
		}

		id++;
		
		try {
			return (T)result.queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void onOpen() {
	}

	public void onClose() {
	}
	
	public void waitForClients(int numClients) {
		while (connections().size() < numClients) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
}