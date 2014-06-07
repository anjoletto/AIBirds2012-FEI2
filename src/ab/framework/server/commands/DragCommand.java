/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.server.commands;

import org.json.simple.JSONObject;

public class DragCommand implements Command<Object> {
	private int x, y, dx, dy;
	
	public DragCommand(int x, int y, int dx, int dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public String getCommandName() {
		return "drag";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject o = new JSONObject();
		o.put("x", x);
		o.put("y", y);
		o.put("dx", dx);
		o.put("dy", dy);
		return o;
	}
	
	@Override
	public Object gotResponse(JSONObject data) {
		return new Object();
	}
}
