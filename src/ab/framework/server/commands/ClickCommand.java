/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.server.commands;

import org.json.simple.JSONObject;

public class ClickCommand implements Command<Object> {
	private int x, y;
	
	public ClickCommand(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String getCommandName() {
		return "click";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject o = new JSONObject();
		o.put("x", x);
		o.put("y", y);
		return o;
	}
	
	@Override
	public Object gotResponse(JSONObject data) {
		return new Object();
	}
}
