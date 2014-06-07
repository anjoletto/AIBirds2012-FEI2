/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.server.commands;

import org.json.simple.JSONObject;

public class MouseWheelCommand implements Command<Object> {
	private int delta;
	
	/**
	 * Simulate a scroll of the mouse wheel
	 * 
	 * @param delta the direction to scroll (-1 = up, 1 = down)
	 */
	public MouseWheelCommand(int delta) {
		this.delta = delta;
	}
	
	@Override
	public String getCommandName() {
		return "mousewheel";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject o = new JSONObject();
		o.put("delta", delta);
		return o;
	}
	
	@Override
	public Object gotResponse(JSONObject data) {
		return new Object();
	}
}
